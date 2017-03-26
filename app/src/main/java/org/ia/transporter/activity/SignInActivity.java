package org.ia.transporter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import org.ia.transporter.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);      //全屏显示
        super.onCreate(savedInstanceState);

        x.view().inject(this);          //xutils 第三方框架， 初始化 界面

        initData();                     //重写BaseActivity的抽象方法， 这个页面这里没有任何操作，所以方法体是空的
        initView();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onResume() {

        goToMain();

        super.onResume();
    }

    /** 起一个线程， sleep两秒之后 跳转到主界面 */
    private void goToMain() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));     //跳转到主界面
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
