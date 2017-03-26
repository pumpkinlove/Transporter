package org.ia.transporter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ia.transporter.R;
import org.ia.transporter.domain.Group;

import java.util.List;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class GroupSpinnerAdapter extends BaseAdapter {

    private List<Group> groupList;
    private Context mContext;

    public GroupSpinnerAdapter(List<Group> groupList, Context mContext) {
        this.groupList = groupList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int i) {
        return groupList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater=LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_group_spinner, null);
        }

        Group g = groupList.get(i);
        TextView tv_group_name = (TextView) view.findViewById(R.id.tv_group_name);
        tv_group_name.setText(g.getGroupName());

        return view;
    }
}
