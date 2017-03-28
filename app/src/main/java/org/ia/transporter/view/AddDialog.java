package org.ia.transporter.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;
import org.ia.transporter.R;
import org.ia.transporter.adapter.GroupSpinnerAdapter;
import org.ia.transporter.app.MyApplication;
import org.ia.transporter.domain.Client;
import org.ia.transporter.domain.Group;
import org.ia.transporter.domain.TransMessage;
import org.ia.transporter.events.MsgSendEvent;
import org.ia.transporter.utils.Constants;
import org.ia.transporter.utils.DBUtil;
import org.ia.transporter.utils.DateUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class AddDialog extends DialogFragment {

    @ViewInject(R.id.et_ip)         private EditText et_ip;
    @ViewInject(R.id.s_group)       private Spinner s_group;

    private Client targetClient;
    private List<Group> groupList;
    private GroupSpinnerAdapter adapter;

    public Client getTargetClient() {
        targetClient.setIp(et_ip.getText().toString());
        targetClient.setGroupName(((Group)s_group.getSelectedItem()).getGroupName());
        targetClient.setGroupId(((Group)s_group.getSelectedItem()).getId());
        return targetClient;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_add, container);
        x.view().inject(this, view);

        initData();
        initView();

        return view;
    }

    private void initData() {
        try {
            groupList = DBUtil.db.findAll(Group.class);
            adapter = new GroupSpinnerAdapter(groupList, getActivity());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        s_group.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        targetClient = new Client();
        super.onResume();
    }

    @Event(R.id.tv_confirm)
    private void onConfirmClicked(View view) {

        // TODO: 2017/3/28 0028 重复添加判断
        
        TransMessage tMsg = new TransMessage();
        tMsg.setCode(Constants.TYPE_ADD_REQ);
        tMsg.setFromClient(MyApplication.me);
        tMsg.setFromIp(MyApplication.me.getIp());
        tMsg.setToIP(et_ip.getText().toString());
        tMsg.setToClient(getTargetClient());
        tMsg.setOpDate(DateUtil.toMonthDay(new Date()));
        tMsg.setOpTime(DateUtil.toHourMinString(new Date()));
        tMsg.setMessage("好友请求");
        EventBus.getDefault().post(new MsgSendEvent(tMsg));
        dismiss();
    }

    @Event(R.id.tv_cancel)
    private void onCancelClicked(View view) {
        dismiss();
    }

}
