package org.ia.transporter.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ia.transporter.R;
import org.ia.transporter.adapter.GroupAdapter;
import org.ia.transporter.domain.Group;
import org.ia.transporter.domain.GroupRefreshEvent;
import org.ia.transporter.utils.DBUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_group_manage)
public class GroupManageActivity extends BaseActivity {

    @ViewInject(R.id.rv_group)          RecyclerView rv_group;
    @ViewInject(R.id.tv_head_right)     TextView tv_head_right;
    @ViewInject(R.id.tv_head_middle)     TextView tv_head_middle;

    private List<Group> groupList;
    private GroupAdapter groupAdapter;
    private EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        bus = EventBus.getDefault();
        bus.register(this);
        groupAdapter = new GroupAdapter(this);
        refreshGroupList();
    }

    @Override
    protected void initView() {
        tv_head_middle.setText("分组管理");
        tv_head_right.setText("新增");
        tv_head_right.setVisibility(View.VISIBLE);

        rv_group.setAdapter(groupAdapter);
        rv_group.setLayoutManager(new LinearLayoutManager(this));
    }

    @Event(R.id.tv_head_right)
    private void onAddGroup(View view) {
        Group g = new Group("新增分组");
        try {
            DBUtil.db.save(g);
            refreshGroupList();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void refreshGroupList() {
        try {
            groupList = DBUtil.db.findAll(Group.class);
            if (groupList == null) {
                throw new Exception();
            }
            groupAdapter.setGroupList(groupList);
            groupAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            groupList = new ArrayList<>();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupRefreshEvent(GroupRefreshEvent e) {
        refreshGroupList();
    }

    @Override
    public void finish() {
        bus.post(new GroupRefreshEvent());
        super.finish();
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }
}
