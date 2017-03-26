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
    private View.OnClickListener clientClickListener;

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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_group, null);
        }
        view.setTag(R.layout.item_group, groupPosition);
        view.setTag(R.layout.item_client, -1);
        TextView tv_group_name = (TextView) view.findViewById(R.id.tv_group_name);
        TextView tv_onLine_count = (TextView) view.findViewById(R.id.tv_onLine_count);
        tv_group_name.setText(groupList.get(groupPosition).getGroupName());
        tv_onLine_count.setText(groupList.get(groupPosition).getOnLineCount() + "/" + groupList.get(groupPosition).getClientList().size());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int clientPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_client, null);
        }
        Client client = groupList.get(groupPosition).getClientList().get(clientPosition);
        view.setTag(R.layout.item_group, groupPosition);
        view.setTag(R.layout.item_client, clientPosition);
        TextView tv_client_name = (TextView) view.findViewById(R.id.tv_client_name);

        if (client.getPhoto() != null && client.getPhoto().length() > 0) {
            ImageView iv_client_photo = (ImageView) view.findViewById(R.id.iv_client_photo);
            iv_client_photo.setImageBitmap(Base64Util.stringtoBitmap(client.getPhoto()));
        }

        tv_client_name.setText(groupList.get(groupPosition).getClientList().get(clientPosition).getName());
        tv_client_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clientClickListener != null) {
                    clientClickListener.onClick(view);
                }
            }
        });
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

    public View.OnClickListener getClientClickListener() {
        return clientClickListener;
    }

    public void setClientClickListener(View.OnClickListener clientClickListener) {
        this.clientClickListener = clientClickListener;
    }
}
