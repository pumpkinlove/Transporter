package org.ia.transporter.utils;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.ia.transporter.R;

import java.io.File;

/**
 * Created by Administrator on 2017/2/6 0006.
 * 常量
 */

public class Constants {

    /** 客户端状态  state */
    public static final int CLIENT_CONNECTED    = 1001;
    public static final int CLIENT_DISCONNECTED = 1002;

    /** socket 通信端口 */
    public static final int MSG_PORT  = 30000;
    public static final int FILE_PORT = 30001;

    /** 定义消息类型 */
    public static final int TYPE_CHAT           = 2001;         //聊天
    public static final int TYPE_FILE           = 2002;         //传文件
    public static final int TYPE_FILE_REQ       = 2003;         //请求传输文件
    public static final int TYPE_FILE_RSP       = 2004;         //反馈传输文件
    public static final int TYPE_ADD_REQ        = 2005;         //请求添加好友
    public static final int TYPE_ADD_RSP        = 2006;         //反馈添加好友
    public static final int TYPE_RECEIVE        = 2007;         //反馈收到消息

    /** 接收文件存放路径 */
    public static String SAVE_PATH              = Environment.getExternalStorageDirectory().getPath() + File.separator + "TransDownLoad";

    public static String DEFAULT_PHOTO;

    public static final int SOCKET_TIME_OUT     = 10000;
}
