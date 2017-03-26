package org.ia.transporter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ia.transporter.domain.TransMessage;
import org.ia.transporter.events.SocketEvent;
import org.ia.transporter.events.MsgArriveEvent;
import org.ia.transporter.events.MsgSendEvent;
import org.ia.transporter.events.ToastEvent;
import org.ia.transporter.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService extends Service {

    private ServerSocket fileServer;
    private ServerSocket msgServer;
    private EventBus bus;
    private Gson g = new Gson();


    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initData();

        startReceiveMsg();
    }

    private void initData() {
        try {
            bus = EventBus.getDefault();
            fileServer = new ServerSocket(Constants.FILE_PORT);
            msgServer = new ServerSocket(Constants.MSG_PORT);
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class MsgServerRunnable implements Runnable {

        Socket socket = null;

        public MsgServerRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String buffer = "";
            String line = null;
            InputStream input;
            OutputStream output;
            try {
                output = socket.getOutputStream();
                input = socket.getInputStream();
                BufferedReader bff = new BufferedReader(new InputStreamReader(input));
                output.write("收到".getBytes("utf8"));
                output.flush();
                socket.shutdownOutput();
                while ((line = bff.readLine()) != null) {
                    buffer = buffer + line;
                }
                output.close();
                bff.close();
                input.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /** 接收文件 */
    public void receiveFile(ServerSocket fileServerSocket) {
        try {

            /** 读文件名 */
            Socket name = fileServerSocket.accept();
            InputStream nameStream = name.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(nameStream);
            BufferedReader br = new BufferedReader(streamReader);
            String fileName = br.readLine();
            br.close();
            streamReader.close();
            nameStream.close();
            bus.post(new ToastEvent(name.getInetAddress().getHostAddress() + "正在向你传输文件" + fileName));
            name.close();

            /** 读文件内容 */
            Socket data = fileServerSocket.accept();
            InputStream dataStream = data.getInputStream();
            File dir = new File(Constants.SAVE_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(Constants.SAVE_PATH + File.separator + fileName, false);
            byte[] buffer = new byte[1024];
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                fos.write(buffer, 0 ,size);
            }
            fos.close();
            dataStream.close();
            bus.post(new ToastEvent(fileName + " 接收完成"));
            data.close();

        } catch(Exception e) {
            bus.post(new ToastEvent("接收文件失败" + e.getMessage()));
        }
    }

}
