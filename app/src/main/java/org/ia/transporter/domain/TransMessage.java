package org.ia.transporter.domain;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/25 0025.
 */
@Table(name = "message")
public class TransMessage implements Serializable {

    @Column(name = "id", isId = true, autoGen = true) private int id;
    @Column(name = "code")          private int code;
    @Column(name = "message")       private String message;
    @Column(name = "fromIp")        private String fromIp;
    @Column(name = "toIP")          private String toIP;
                                    private Client fromClient;
                                    private Client toClient;
    @Column(name = "isSelf")        private boolean isSelf;
    @Column(name = "opTime")        private Date opTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Client getFromClient() {
        return fromClient;
    }

    public void setFromClient(Client fromClient) {
        this.fromClient = fromClient;
    }

    public Client getToClient() {
        return toClient;
    }

    public void setToClient(Client toClient) {
        this.toClient = toClient;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public String getToIP() {
        return toIP;
    }

    public void setToIP(String toIP) {
        this.toIP = toIP;
    }
}
