package org.ia.transporter.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.ia.transporter.R;
import org.ia.transporter.adapter.GroupSpinnerAdapter;
import org.ia.transporter.domain.Client;
import org.ia.transporter.domain.Group;
import org.ia.transporter.utils.Base64Util;
import org.ia.transporter.utils.DBUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_friend_request)
public class FriendRequestActivity extends BaseActivity {

    @ViewInject(R.id.tv_head_middle)    private TextView tv_head_middle;
    @ViewInject(R.id.s_group)           private Spinner s_group;
    @ViewInject(R.id.iv_req_photo)      private ImageView iv_req_photo;
    @ViewInject(R.id.tv_ip)             private TextView tv_ip;
    @ViewInject(R.id.tv_name)           private TextView tv_name;

    private List<Group> groupList;
    private GroupSpinnerAdapter adapter;
    private Client client;
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
        try {
            client = (Client) getIntent().getSerializableExtra("client");
            groupList = DBUtil.db.findAll(Group.class);
            adapter = new GroupSpinnerAdapter(groupList, this);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        tv_head_middle.setText("好友请求");
        s_group.setAdapter(adapter);
        s_group.setPopupBackgroundDrawable(getDrawable(R.mipmap.spinner_bg));
        tv_name.setText(client.getName());
        tv_ip.setText(client.getIp());
        iv_req_photo.setImageBitmap(Base64Util.stringtoBitmap(client.getPhoto()));
    }
}
