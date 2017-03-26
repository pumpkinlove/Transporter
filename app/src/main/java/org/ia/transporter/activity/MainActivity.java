package org.ia.transporter.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import org.ia.transporter.R;
import org.ia.transporter.adapter.MyFragmentAdapter;
import org.ia.transporter.fragment.ChatsFragment;
import org.ia.transporter.fragment.FriendsFragment;
import org.ia.transporter.fragment.HomeFragment;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.tl_main)    private TabLayout tl_main;
    @ViewInject(R.id.vp_main)    private ViewPager vp_main;

    private MyFragmentAdapter adapter;
    private List<Fragment> fragmentList;

    private ChatsFragment chatsFragment;
    private FriendsFragment friendsFragment;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        x.view().inject(this);

        initData();
        initView();
        initTabLayout();
    }

    @Override
    protected void initData() {
        fragmentList = new ArrayList<>();
        chatsFragment = new ChatsFragment();
        friendsFragment = new FriendsFragment();
        homeFragment = new HomeFragment();

        fragmentList.add(chatsFragment);
        fragmentList.add(friendsFragment);
        fragmentList.add(homeFragment);

        adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
    }

    @Override
    protected void initView() {
        vp_main.setAdapter(adapter);
        ViewPager.OnPageChangeListener l = new TabLayout.TabLayoutOnPageChangeListener(tl_main);
        vp_main.addOnPageChangeListener(l);
        tl_main.setupWithViewPager(vp_main);
        vp_main.setOffscreenPageLimit(20);
        tl_main.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.gray_dark));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
    }

    private void initTabLayout() {

        List<Drawable> drawableList = new ArrayList<>();
        Drawable tab_drawable = getResources().getDrawable(R.drawable.tab1_n);
        drawableList.add(tab_drawable);
        tab_drawable = getResources().getDrawable(R.drawable.tab2_n);
        drawableList.add(tab_drawable);
        tab_drawable = getResources().getDrawable(R.drawable.tab3_n);
        drawableList.add(tab_drawable);

        for (int i=0; i< tl_main.getTabCount(); i++) {
            final TabLayout.Tab tab = tl_main.getTabAt(i);
            if (tab != null) {
                tab.setIcon(drawableList.get(i));
                if (tab.isSelected()) {
                    tab.getIcon().setTint(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        //屏蔽返回键
    }
}
