package org.ia.transporter.activity;

import android.os.Bundle;

import org.ia.transporter.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_friend_request)
public class FriendRequestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
        initView();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }
}
