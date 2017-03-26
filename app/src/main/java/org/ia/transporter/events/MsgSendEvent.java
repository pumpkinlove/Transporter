package org.ia.transporter.events;

import org.ia.transporter.domain.TransMessage;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class MsgSendEvent {
    private TransMessage transMessage;

    public MsgSendEvent() {
    }

    public MsgSendEvent(TransMessage transMessage) {
        this.transMessage = transMessage;
    }

    public TransMessage getTransMessage() {
        return transMessage;
    }

    public void setTransMessage(TransMessage transMessage) {
        this.transMessage = transMessage;
    }
}
