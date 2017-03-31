package org.ia.transporter.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ia.transporter.R;
import org.ia.transporter.activity.GroupManageActivity;
import org.ia.transporter.utils.Base64Util;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import static org.ia.transporter.app.MyApplication.me;
/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @ViewInject(R.id.iv_me_photo)       private ImageView iv_me_photo;
    @ViewInject(R.id.tv_me_name)        private TextView tv_me_name;
    @ViewInject(R.id.tv_me_ip)          private TextView tv_me_ip;
    @ViewInject(R.id.tv_head_middle)    private TextView tv_head_middle;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        x.view().inject(this, v);

        initView();
        return v;
    }

    private void initView() {
        tv_head_middle.setText("设置");
        iv_me_photo.setImageBitmap(Base64Util.stringtoBitmap(me.getPhoto()));
        tv_me_name.setText(me.getName());
        tv_me_ip.setText(me.getIp());
    }

    @Event(R.id.ll_group_manage)
    private void onGroupManageClicked(View view) {
        startActivity(new Intent(getContext(), GroupManageActivity.class));
    }

}
