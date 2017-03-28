package org.ia.transporter.app;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ia.transporter.R;
import org.ia.transporter.domain.Client;
import org.ia.transporter.domain.Group;
import org.ia.transporter.domain.TransMessage;
import org.ia.transporter.events.MsgArriveEvent;
import org.ia.transporter.events.MsgSendEvent;
import org.ia.transporter.events.SocketEvent;
import org.ia.transporter.events.ToastEvent;
import org.ia.transporter.utils.Base64Util;
import org.ia.transporter.utils.Constants;
import org.ia.transporter.utils.DBUtil;
import org.ia.transporter.utils.ImageUtil;
import org.ia.transporter.utils.NetUtil;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ia.transporter.utils.DBUtil.db;
/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class MyApplication extends Application {

    private SoundPool soundPool;
    private Map<Integer, Integer> soundMap;

    private ServerSocket fileServer;
    private ServerSocket msgServer;
    public static Client me;
    private EventBus bus;

    private List<Group> groupList;
    private Gson g = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);               //初始化Xutils
        DBUtil.initDB();
        init();
        startReceiveMsg();
    }

    private void init() {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.transport);
            Constants.DEFAULT_PHOTO = Base64Util.bitmaptoString(bitmap);
            bus = EventBus.getDefault();
            bus.register(this);
            fileServer = new ServerSocket(Constants.FILE_PORT);
            msgServer = new ServerSocket(Constants.MSG_PORT);

            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundMap = new HashMap<>();
            soundMap.put(1, soundPool.load(this, R.raw.notice, 1));

            initGroup();
            initMe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGroup() throws Exception {
        List<Client> c = db.findAll(Client.class);
        groupList = db.findAll(Group.class);
        if (groupList == null || groupList.size() < 1) {
            groupList = new ArrayList<>();
            Group g = new Group(1, "我的好友");
            if (db.saveBindingId(g)) {
                groupList.add(g);
            }
        }
    }

    private void initMe() {
        try {
            me = DBUtil.db.findFirst(Client.class);
            if (me == null ) {
                me = new Client();
                me.setId(1);
                me.setIp(NetUtil.getLocalIpAddress(this));
                me.setName("默认名称");
                me.setState(Constants.CLIENT_CONNECTED);
                me.setPhoto(Constants.DEFAULT_PHOTO);
                me.setGroupId(0);
                DBUtil.db.saveBindingId(me);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        bus.unregister(this);
        super.onTerminate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToastEvent(ToastEvent e) {
        Toast.makeText(this, e.getToast(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSocketeArrive(SocketEvent e) {
        Socket socket = e.getSocket();
        StringBuffer buffer = new StringBuffer();
        String line = null;
        InputStream input = null;
        BufferedReader bff = null;
        try {
            input = socket.getInputStream();
            bff = new BufferedReader(new InputStreamReader(input));
            while ((line = bff.readLine()) != null) {
                buffer.append(line);
            }
            TransMessage tMsg = g.fromJson(buffer.toString(), TransMessage.class);
            bus.post(new MsgArriveEvent(tMsg));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bff.close();
                input.close();
                socket.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgArriveEvent(MsgArriveEvent e) {
        ring();
        Log.e("onMsgArriveEvent","MyApplication");
        TransMessage tMsg = e.getTransMessage();
        switch (tMsg.getCode()) {
        }
    }

    private void ring() {
        soundPool.play(soundMap.get(1), Constants.LEFT_VOLUME, Constants.RIGHT_VOLUME, Constants.PRIORITY, Constants.LOOP, Constants.SOUND_RATE);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMsgSendEvent(MsgSendEvent e) {
        OutputStream ou = null;
        Socket socket = new Socket();
        try {
            TransMessage trans = e.getTransMessage();
            socket.connect(new InetSocketAddress(trans.getToIP(), Constants.MSG_PORT), Constants.SOCKET_TIME_OUT);
            ou = socket.getOutputStream();
            ou.write(g.toJson(trans).getBytes("utf8"));
            ou.flush();
        } catch (Exception e1) {
            bus.post(new ToastEvent("连接失败， 请确认Ip是否正确"));
        } finally {
            try {
                if (ou != null) {
                    ou.close();
                }
                socket.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void startReceiveMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = msgServer.accept();
                        bus.post(new SocketEvent(socket));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
