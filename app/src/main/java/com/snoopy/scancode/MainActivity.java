package com.snoopy.scancode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.snoopy.scancode.helper.ScanGunHelper;
import com.snoopy.scancode.listener.HttpCallbackListener;
import com.snoopy.scancode.result.ResultActivity;
import com.snoopy.scancode.update.UpdateChecker;
import com.snoopy.scancode.util.Constant;
import com.snoopy.scancode.util.HttpUtil;
import com.snoopy.scancode.util.ToastUtils;

public class MainActivity extends AppCompatActivity implements ScanGunHelper.OnScanSuccessListener {

    //引导图
    private ImageView homeAppPage;
    //从服务器获取json字串
    private String jsonStr = null;
    public final String pre_url = "http://ydjkf.hydee.cn/scanbuy/order/detail?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewsAndParas();
    }

    //初始化组件以及参数
    void initViewsAndParas() {
        homeAppPage = findViewById(R.id.app_home_page);
        homeAppPage.bringToFront();
        homeAppPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQRCodeInfo("2018040864", "0FDA26ECB6E3E5F56C109522F96BB777");
            }
        });
        getSupportActionBar().hide();

        //设置沉浸式状态栏，，注意需要在android5.0版本及以上
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        UpdateChecker.checkForDialog(MainActivity.this);
    }

    //其实这类事件都是普通的按键事件通过dispatchKeyEvent就可以拦截
    //------------------------------获取磁条卡信息-------------------------------
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!event.getDevice().getName().equals("Virtual")) {
            //activity实现了扫码成功的回调这里的this就是这个listener
            //采用单例模式调用
            ScanGunHelper.getInstance().analysisKeyEvent(event, this);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onSuccess(String barcode) {
        //Toast.makeText(MainActivity.this, "barcode: " + barcode, Toast.LENGTH_LONG).show();
        if (barcode != null) {
            if (TextUtils.isEmpty(barcode)) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.scan_error), Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    /*
                    JSONObject jsonObject = new JSONObject(barcode);
                    String orderId = jsonObject.getString("orderId");
                    String userKey = jsonObject.getString("userKey");*/
                    String strStart = "userKey";
                    String strEnd = "orderId";
                    /* 找出指定的2个字符在 该字符串里面的 位置 */
                    int strStartIndex = barcode.indexOf(strStart);
                    int strEndIndex = barcode.indexOf(strEnd);

                    /* 开始截取 */
                    String userKey = barcode.substring(strStartIndex, strEndIndex).substring(strStart.length());
                    String orderId = barcode.substring(barcode.lastIndexOf(strEnd) + strEnd.length());
                    getQRCodeInfo(orderId, userKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //根据二维码包含的信息，获取json字串并传递给ResultActivity
    private void getQRCodeInfo(final String id, final String key) {
        String urlStr = pre_url + "orderId=" + id + "&userKey=" + key;
        Toast.makeText(MainActivity.this,"orderId: " + id + "  userKey: " + key,Toast.LENGTH_LONG).show();
        Log.i("tbw", "urlStr: " + urlStr);
        HttpUtil.getInstance().get(urlStr, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        jsonStr = response;
                        Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN, jsonStr);
                        bundle.putString("orderId", id);
                        resultIntent.putExtras(bundle);
                        startActivity(resultIntent);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                ToastUtils.show(MainActivity.this, getResources().getString(R.string.network_error));
            }
        });
    }

}
