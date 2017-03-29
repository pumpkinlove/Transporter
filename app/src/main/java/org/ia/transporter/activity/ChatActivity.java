package org.ia.transporter.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ia.transporter.R;
import org.ia.transporter.adapter.ChatAdapter;
import org.ia.transporter.app.MyApplication;
import org.ia.transporter.domain.Client;
import org.ia.transporter.domain.TransMessage;
import org.ia.transporter.events.ChatEvent;
import org.ia.transporter.events.MsgArriveEvent;
import org.ia.transporter.events.MsgSendEvent;
import org.ia.transporter.events.SocketEvent;
import org.ia.transporter.events.ToastEvent;
import org.ia.transporter.utils.Constants;
import org.ia.transporter.utils.DBUtil;
import org.ia.transporter.utils.DateUtil;
import org.ia.transporter.utils.FileUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.ia.transporter.app.MyApplication.me;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    @ViewInject(R.id.tv_head_middle)    private TextView tv_head_middle;
    @ViewInject(R.id.tv_head_right)     private TextView tv_head_right;
    @ViewInject(R.id.tv_head_left)      private TextView tv_head_left;

    @ViewInject(R.id.rv_chat)           private RecyclerView rv_chat;

    @ViewInject(R.id.et_send_message)   private EditText et_send_message;

    private Client client;
    private ChatAdapter adapter;
    private List<TransMessage> messageList;
    private EventBus bus;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        x.view().inject(this);

        initData();
        initView();
        bus.register(this);
    }

    @Override
    protected void initData() {
        try {
            bus = EventBus.getDefault();
            client = (Client) getIntent().getSerializableExtra("client");
            messageList = DBUtil.db.selector(TransMessage.class)
                    .where("fromIp", "==", client.getIp())
                    .or("toIp", "==", client.getIp())
                    .where("code", "==", Constants.TYPE_CHAT)
                    .findAll();
            adapter = new ChatAdapter(messageList, this);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        tv_head_middle.setText(client.getName());
        tv_head_left.setVisibility(View.VISIBLE);
        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setText(" + ");

        rv_chat.setAdapter(adapter);
        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        et_send_message.setOnEditorActionListener(listener);
        if (messageList.size() > 0) {
            rv_chat.smoothScrollToPosition(messageList.size()-1);
        }
    }

    private EditText.OnEditorActionListener listener = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                try {
                    sendMessage(et_send_message.getText().toString());
                    et_send_message.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Event(R.id.tv_head_right)
    private void onChooseFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");      //设置类型，这里是任意类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a File to ic_launcher"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1 :
                if (resultCode == Activity.RESULT_OK) { //是否选择，没选择就不会继续
                    Uri uri = data.getData();           //得到uri，将uri转化成file的过程。
                    String path = FileUtil.getFileAbsolutePath(this, uri);
                    if (path != null) {
                        file = new File(path);
                    } else {
                        bus.post(new ToastEvent("文件路径为空"));
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendFile(file, client);
                        }
                    }).start();
                }
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatEvent(ChatEvent e) {
        try {
            messageList = DBUtil.db.selector(TransMessage.class)
                    .where("fromIp", "==", client.getIp())
                    .or("toIp", "==", client.getIp())
                    .where("code", "==", Constants.TYPE_CHAT)
                    .findAll();
            adapter.setMessageList(messageList);
            adapter.notifyDataSetChanged();
            if (messageList.size() > 0) {
                rv_chat.smoothScrollToPosition(messageList.size()-1);
            }

        } catch (DbException e1) {
            e1.printStackTrace();
        }
    }


    /** 发送消息 */
    private void sendMessage(String content) throws Exception {
        TransMessage t = new TransMessage();
        t.setCode(Constants.TYPE_CHAT);
        t.setMessage(content);
        t.setToClient(client);
        t.setFromClient(MyApplication.me);
        t.setToIP(client.getIp());
        t.setFromIp(MyApplication.me.getIp());
        t.setOpDate(DateUtil.toMonthDay(new Date()));
        t.setOpTime(DateUtil.toHourMinString(new Date()));
        t.setSelf(true);
        bus.post(new MsgSendEvent(t));
        messageList.add(t);
        adapter.notifyDataSetChanged();
        rv_chat.smoothScrollToPosition(messageList.size()-1);
    }

    /** 发送文件 */
    public void sendFile(File file, Client c) {
        try {
            /** 写文件名*/
            Socket name = new Socket(c.getIp(), Constants.FILE_PORT);
            OutputStream osName = name.getOutputStream();
            OutputStreamWriter oswName = new OutputStreamWriter(osName);
            BufferedWriter bwName = new BufferedWriter(oswName);
            bwName.write(file.getName());
            bwName.close();
            oswName.close();
            osName.close();
            name.close();

            /** 写文件内容*/
            Socket data = new Socket(c.getIp(), Constants.FILE_PORT);
            OutputStream osData = data.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            int size = -1;
            byte[] buffer = new byte[1024];
            while ((size = fis.read(buffer, 0, 1024)) != -1) {
                osData.write(buffer, 0, size);
            }
            osData.close();
            fis.close();
            data.close();

        } catch (Exception e) {
            bus.post(new ToastEvent("发送失败，请确认好友IP是否正确"));
        }
    }

}