package org.ia.transporter.events;

import org.ia.transporter.domain.TransMessage;

import java.net.Socket;

/**
 * Created by Administrator on 2017/2/12 0012.
 */

public class SocketEvent {
    private Socket socket;

    public SocketEvent() {
    }

    public SocketEvent(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
