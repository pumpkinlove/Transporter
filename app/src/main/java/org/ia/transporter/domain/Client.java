package org.ia.transporter.domain;

import org.ia.transporter.utils.Constants;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

@Table(name = "client")
public class Client implements Serializable {

    @Column(name = "id", isId = true, autoGen = true)   private int id;

    @Column(name = "name")              private String name;

    @Column(name = "ip")                private String ip;

    @Column(name = "groupId")           private int groupId;

    @Column(name = "groupName")         private String groupName;

    @Column(name = "photo")             private String photo;       //将图片Base64编码

                                        private int state;

    public Client() {
    }

    public Client(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
