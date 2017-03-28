package org.ia.transporter.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ia.transporter.R;
import org.ia.transporter.activity.ChatActivity;
import org.ia.transporter.activity.FriendRequestActivity;
import org.ia.transporter.adapter.SessionAdapter;
import org.ia.transporter.domain.TransMessage;
import org.ia.transporter.events.AddFriendEvent;
import org.ia.transporter.events.MsgArriveEvent;
import org.ia.transporter.listener.RecycleViewClickListener;
import org.ia.transporter.utils.Constants;
import org.ia.transporter.utils.DBUtil;
import org.ia.transporter.view.AddDialog;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    @ViewInject(R.id.rv_session)        private RecyclerView rv_session;
    @ViewInject(R.id.tv_head_middle)    private TextView tv_head_middle;

    private EventBus bus;
    private SessionAdapter adapter;
    private List<TransMessage> tMsgList;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);
        x.view().inject(this, v);

        initData();
        initView();
        return v;
    }

    private void initData() {
        bus = EventBus.getDefault();
        bus.register(this);

        tMsgList = new ArrayList<>();
        adapter = new SessionAdapter(tMsgList, getContext());
    }

    private void initView() {
        tv_head_middle.setText("消息");
        rv_session.setAdapter(adapter);
        rv_session.setLayoutManager(new LinearLayoutManager(getContext()));

        rv_session.addOnItemTouchListener(new RecycleViewClickListener(getContext(), rv_session, new RecycleViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TransMessage tMsg = tMsgList.get(position);
                switch (tMsg.getCode()) {
                    case Constants.TYPE_ADD_REQ :
                        Intent i = new Intent(getActivity(), FriendRequestActivity.class);
                        i.putExtra("tMsg", tMsg);
                        startActivity(i);
                        break;
                    case Constants.TYPE_CHAT :
                        Intent ic = new Intent(getActivity(), ChatActivity.class);
                        ic.putExtra("tMsg", tMsg);
                        startActivity(ic);
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgArrive(MsgArriveEvent e) {
        try {
            Log.e("onMsgArriveEvent","ChatsFragment");
            TransMessage tMsg = e.getTransMessage();
            DBUtil.db.save(tMsg);
            switch (tMsg.getCode()) {
                case Constants.TYPE_ADD_RSP :
                    if ("同意".equals(tMsg.getMessage())) {
                        bus.post(new AddFriendEvent(tMsg.getFromClient()));
                    } else if ("拒绝".equals(tMsg.getMessage())) {

                    }
                    break;
            }
            for (int i=0; i<tMsgList.size(); i++) {
                if (tMsgList.get(i).getFromIp().equals(tMsg.getFromIp())) {
                    tMsgList.remove(i);
                }
            }
            tMsgList.add(tMsg);
            adapter.notifyDataSetChanged();
        } catch (DbException e1) {
            e1.printStackTrace();
        }
    }

}
