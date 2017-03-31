package org.ia.transporter.domain;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/25 0025.
 */
@Table(name = "group")
public class Group implements Serializable {

    @Column(name = "id", isId = true, autoGen = true) private int id;

    @Column(name = "groupName") private String groupName;

    private List<Client> clientList = new ArrayList<>();

    private int onLineCount;

    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(int id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public int getOnLineCount() {
        return onLineCount;
    }

    public void setOnLineCount(int onLineCount) {
        this.onLineCount = onLineCount;
    }
}
