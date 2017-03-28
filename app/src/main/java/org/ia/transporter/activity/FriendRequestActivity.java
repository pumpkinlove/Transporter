package org.ia.transporter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.ia.transporter.R;
import org.ia.transporter.adapter.GroupSpinnerAdapter;
import org.ia.transporter.app.MyApplication;
import org.ia.transporter.domain.Client;
import org.ia.transporter.domain.Group;
import org.ia.transporter.domain.TransMessage;
import org.ia.transporter.events.AddFriendEvent;
import org.ia.transporter.events.MsgArriveEvent;
import org.ia.transporter.events.MsgSendEvent;
import org.ia.transporter.utils.Base64Util;
import org.ia.transporter.utils.Constants;
import org.ia.transporter.utils.DBUtil;
import org.ia.transporter.utils.DateUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_friend_request)
public class FriendRequestActivity extends BaseActivity {

    @ViewInject(R.id.tv_head_middle)    private TextView tv_head_middle;
    @ViewInject(R.id.s_group)           private Spinner s_group;
    @ViewInject(R.id.iv_req_photo)      private ImageView iv_req_photo;
    @ViewInject(R.id.tv_ip)             private TextView tv_ip;
    @ViewInject(R.id.tv_name)           private TextView tv_name;

    private List<Group> groupList;
    private GroupSpinnerAdapter adapter;
    private TransMessage message;
    private Client fromClient;
    private EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        x.view().inject(this);
        initData();
        initView();

    }

    @Override
    protected void initData() {
        try {
            bus = EventBus.getDefault();
            message = (TransMessage) getIntent().getSerializableExtra("tMsg");
            fromClient = message.getFromClient();
            groupList = DBUtil.db.findAll(Group.class);
            adapter = new GroupSpinnerAdapter(groupList, this);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        tv_head_middle.setText("好友请求");
        s_group.setAdapter(adapter);
        s_group.setPopupBackgroundDrawable(getDrawable(R.mipmap.spinner_bg));
        s_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromClient.setGroupId(groupList.get(i).getId());
                fromClient.setGroupName(groupList.get(i).getGroupName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tv_name.setText(fromClient.getName());
        tv_ip.setText(fromClient.getIp());
        iv_req_photo.setImageBitmap(Base64Util.stringtoBitmap(fromClient.getPhoto()));
    }

    @Event(R.id.tv_agree)
    private void onAgree(View view) {
        TransMessage tMsg = new TransMessage();
        tMsg.setFromIp(MyApplication.me.getIp());
        tMsg.setToIP(fromClient.getIp());
        tMsg.setToClient(fromClient);
        MyApplication.me.setGroupId(message.getToClient().getGroupId());
        MyApplication.me.setGroupName(message.getToClient().getGroupName());
        tMsg.setFromClient(MyApplication.me);
        tMsg.setCode(Constants.TYPE_ADD_RSP);
        tMsg.setMessage("同意");
        tMsg.setOpDate(DateUtil.toMonthDay(new Date()));
        tMsg.setOpTime(DateUtil.toHourMinString(new Date()));
        tMsg.setSelf(false);
        bus.post(new MsgSendEvent(tMsg));
        bus.post(new AddFriendEvent(fromClient));
        finish();
    }

    @Event(R.id.tv_refuse)
    private void onRefuse(View view) {
        TransMessage tMsg = new TransMessage();
        tMsg.setFromIp(MyApplication.me.getIp());
        tMsg.setToIP(fromClient.getIp());
        tMsg.setToClient(fromClient);
        MyApplication.me.setGroupId(message.getToClient().getGroupId());
        MyApplication.me.setGroupName(message.getToClient().getGroupName());
        tMsg.setFromClient(MyApplication.me);
        tMsg.setCode(Constants.TYPE_ADD_RSP);
        tMsg.setMessage("拒绝");
        tMsg.setOpDate(DateUtil.toMonthDay(new Date()));
        tMsg.setOpTime(DateUtil.toHourMinString(new Date()));
        tMsg.setSelf(false);
        bus.post(new MsgSendEvent(tMsg));
        finish();
    }

}
