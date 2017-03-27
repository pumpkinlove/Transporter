package org.ia.transporter.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.ia.transporter.R;

/**
 * Created by Administrator on 2017/1/19 0019.
 * 自定义一个抽象activity， 所有的activity都继承这个抽象类，方便统一管理
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void initData();

    protected abstract void initView();

    protected void initWindow() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

}
