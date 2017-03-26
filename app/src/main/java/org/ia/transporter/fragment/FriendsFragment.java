package org.ia.transporter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ia.transporter.R;
import org.ia.transporter.adapter.GroupFriendAdapter;
import org.ia.transporter.domain.Client;
import org.ia.transporter.domain.Group;
import org.ia.transporter.domain.TransMessage;
import org.ia.transporter.view.AddDialog;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import static org.ia.transporter.utils.DBUtil.db;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    @ViewInject(R.id.elv_friend)        private ExpandableListView elv_friend;
    @ViewInject(R.id.tv_head_right)     private TextView tv_head_right;
    @ViewInject(R.id.tv_head_middle)    private TextView tv_head_middle;

    private AddDialog addDialog;

    private List<Group> groupList;
    private GroupFriendAdapter adapter;
    private EventBus bus;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        x.view().inject(this, v);
        
        initData();
        initView();
        
        return v;
    }

    private void initData() {
        try {
            bus = EventBus.getDefault();
            bus.register(this);
            loadList();
            adapter = new GroupFriendAdapter(groupList, getContext());

        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        elv_friend.setAdapter(adapter);
        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setText("添加");
        tv_head_middle.setText("联系人");

        addDialog = new AddDialog();
        addDialog.setCancelable(false);
    }

    @Event(R.id.tv_head_right)
    private void onRightClick(View view) {
        addDialog.show(getActivity().getFragmentManager(), "ADD_FRIEND");
    }

    private void loadList() throws DbException {
        groupList = db.findAll(Group.class);
        List<Client> clientList;
        for (Group group: groupList) {
            clientList = db.selector(Client.class).where("groupId", "==" , group.getId()).findAll();
            if (clientList == null || clientList.size() < 1) {
                clientList = new ArrayList<>();
            }
            group.setClientList(clientList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TransMessage e) {
        Log.e("onMessageEvent","FriendsFragment");

    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }
}
