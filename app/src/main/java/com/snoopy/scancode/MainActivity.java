package com.snoopy.scancode;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.snoopy.scancode.helper.ScanGunHelper;
import com.snoopy.scancode.result.ResultActivity;
import com.snoopy.scancode.util.Constant;
import com.snoopy.scancode.R;

public class MainActivity extends AppCompatActivity implements ScanGunHelper.OnScanSuccessListener{

    private TextView textView;

    //引导图
    private ImageView homeAppPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewsAndParas();
    }


    //初始化组件以及参数
    void initViewsAndParas(){
        homeAppPage = findViewById(R.id.app_home_page);
        homeAppPage.bringToFront();
        getSupportActionBar().hide();

        //设置沉浸式状态栏，，注意需要在android5.0版本及以上
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    //其实这类事件都是普通的按键事件通过dispatchKeyEvent就可以拦截
    //------------------------------获取磁条卡信息-------------------------------

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!event.getDevice().getName().equals("Virtual")) {
            //activity实现了扫码成功的回调这里的this就是这个listener（不懂得就百度下吧）
            //采用单例模式调用
            ScanGunHelper.getInstance().analysisKeyEvent(event, this);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onSuccess(String barcode) {

        if (barcode != null) {
            if (TextUtils.isEmpty(barcode)) {
                Toast.makeText(MainActivity.this,"扫描失败",Toast.LENGTH_LONG).show();
                return;
            }else{
                Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN, barcode);
                resultIntent.putExtras(bundle);
                startActivity(resultIntent);
            }

        }

    }

}
