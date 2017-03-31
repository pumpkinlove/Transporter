package org.ia.transporter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.ia.transporter.R;
import org.ia.transporter.domain.Client;
import org.ia.transporter.domain.Group;
import org.ia.transporter.domain.GroupRefreshEvent;
import org.ia.transporter.listener.RecycleViewClickListener;
import org.ia.transporter.utils.DBUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by xu.nan on 2017/3/31.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {

    private List<Group> groupList;
    private Context context;

    public GroupAdapter(Context context) {
        this.context = context;
    }

    public GroupAdapter(List<Group> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupHolder(LayoutInflater.from(context).inflate(R.layout.item_group_manage, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupHolder holder, int position) {
        Group g = groupList.get(position);
        if (g.getId() == 1) {
            holder.tv_del_group.setVisibility(View.GONE);
        }
        holder.et_group_name.setText(g.getGroupName());

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class GroupHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.et_group_name)   private EditText et_group_name;
        @ViewInject(R.id.tv_mod_group)    private TextView tv_mod_group;
        @ViewInject(R.id.tv_save_group)   private TextView tv_save_group;
        @ViewInject(R.id.tv_del_group)    private TextView tv_del_group;

        public GroupHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }

        @Event(R.id.tv_mod_group)
        private void OnMod(View view) {
            tv_save_group.setVisibility(View.VISIBLE);
            tv_mod_group.setVisibility(View.GONE);
            et_group_name.setEnabled(true);
        }

        @Event(R.id.tv_save_group)
        private void OnSave(View view) {
            tv_save_group.setVisibility(View.GONE);
            tv_mod_group.setVisibility(View.VISIBLE);
            et_group_name.setEnabled(false);
            Group g = groupList.get(getPosition());
            g.setGroupName(et_group_name.getText().toString());
            try {
                DBUtil.db.update(g, "groupName");
                EventBus.getDefault().post(new GroupRefreshEvent());
            } catch (DbException e) {
                e.printStackTrace();
            }
        }


        @Event(R.id.tv_del_group)
        private void OnDel(View view) {
            Group g = groupList.get(getPosition());
            g.setGroupName(et_group_name.getText().toString());
            try {
                List<Client> clients = DBUtil.db.selector(Client.class).where("groupId", "==", g.getId()).findAll();
                for (Client c : clients) {
                    c.setGroupId(1);
                    c.setGroupName(groupList.get(0).getGroupName());
                    DBUtil.db.update(c, "groupId", "groupName");
                }
                DBUtil.db.delete(g);
                EventBus.getDefault().post(new GroupRefreshEvent());
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

}
