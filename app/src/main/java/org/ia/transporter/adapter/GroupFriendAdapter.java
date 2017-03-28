package org.ia.transporter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ia.transporter.R;
import org.ia.transporter.domain.Client;
import org.ia.transporter.domain.Group;
import org.ia.transporter.utils.Base64Util;

import java.util.List;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class GroupFriendAdapter extends BaseExpandableListAdapter {

    private List<Group> groupList;
    private Context context;

    public GroupFriendAdapter() {
    }

    public GroupFriendAdapter(List<Group> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupList.get(i).getClientList().size();
    }

    @Override
    public Group getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Client getChild(int groupPosition, int clientPosition) {
        return groupList.get(groupPosition).getClientList().get(clientPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int clientPosition) {
        return clientPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        View view = null;
        GroupHolder groupholder = null;
        if (convertView != null) {
            view = convertView;
            groupholder = (GroupHolder) view.getTag();
        } else {
            view = View.inflate(context, R.layout.item_group, null);
            groupholder = new GroupHolder();
            groupholder.tv_group_name = (TextView) view.findViewById(R.id.tv_group_name);
            groupholder.tv_onLine_count = (TextView) view.findViewById(R.id.tv_onLine_count);
            view.setTag(groupholder);
        }
        groupholder.tv_group_name.setText(groupList.get(groupPosition).getGroupName());
        groupholder.tv_onLine_count.setText(groupList.get(groupPosition).getOnLineCount() + "/" + groupList.get(groupPosition).getClientList().size());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int clientPosition, boolean b, View convertView, ViewGroup viewGroup) {
        Client client = groupList.get(groupPosition).getClientList().get(clientPosition);
        View view = null;
        ChildHolder childholder = null;
        if (convertView != null) {
            view = convertView;
            childholder = (ChildHolder) view.getTag();
        } else {
            view = View.inflate(context, R.layout.item_client, null);
            childholder = new ChildHolder();
            childholder.tv_client_name = (TextView) view.findViewById(R.id.tv_client_name);
            childholder.tv_client_ip = (TextView) view.findViewById(R.id.tv_client_ip);
            childholder.iv_client_photo = (ImageView) view.findViewById(R.id.iv_client_photo);
            view.setTag(childholder);
        }
        if (client.getPhoto() != null && client.getPhoto().length() > 0) {
            childholder.iv_client_photo.setImageBitmap(Base64Util.stringtoBitmap(client.getPhoto()));
        }
        childholder.tv_client_name.setText(groupList.get(groupPosition).getClientList().get(clientPosition).getName());
        childholder.tv_client_ip.setText(groupList.get(groupPosition).getClientList().get(clientPosition).getIp());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    static class GroupHolder{
        TextView tv_group_name;
        TextView tv_onLine_count;
    }

    static class ChildHolder{
        ImageView iv_client_photo;
        TextView tv_client_name;
        TextView tv_client_ip;
    }



}
