package com.snoopy.scancode.result;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.snoopy.scancode.R;
import com.snoopy.scancode.adapter.GoodAdapter;
import com.snoopy.scancode.dialog.ExitDialog;
import com.snoopy.scancode.dialog.ServiceDialog;
import com.snoopy.scancode.entity.GoodItem;
import com.snoopy.scancode.util.Constant;
import com.snoopy.scancode.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/*
扫描出场码后，显示订单详情的页面，包括商品总价和总数，
门店，订单号，下单时间，和商品详细情况等
 */
public class ResultActivity extends AppCompatActivity {

    //详情列表ListView
    ListView resultList;
    //确认订单按钮
    Button btn_confirm;
    //商品List
    private List<GoodItem> goodList = new ArrayList<GoodItem>();

    private static ResultActivity mInstance;
    /*
    获取静态实例
     */
    public static ResultActivity getInstance() {
        if (mInstance == null) {
            if (mInstance == null) {
                mInstance = new ResultActivity();
            }
        }
        return mInstance;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtil.resetDensity(ResultActivity.this);
        setContentView(R.layout.activity_result);
        initViews();
        getSupportActionBar().hide();
        initGoodDatas();

        //设置商品适配器
        GoodAdapter goodAdapter = new GoodAdapter(ResultActivity.this, R.layout.listitem_result,goodList);
        resultList.setAdapter(goodAdapter);

        //在展示详情页面的同时，展示出等待员工服务的dialog
        showServiceDialog();
        Intent intent = getIntent();
        String result = intent.getStringExtra(Constant.INTENT_EXTRA_KEY_QR_SCAN);
        //scanResult.setText(result);
        Toast.makeText(ResultActivity.this,result,Toast.LENGTH_LONG).show();
    }

    //初始化控件
    private void initViews() {
        //scanResult = findViewById(R.id.scan_result);
        resultList = findViewById(R.id.result_litView);
        btn_confirm = findViewById(R.id.btn_confirm);
        //点击确认订单按钮，显示退出详情界面dialog
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitDialog();
            }
        });
    }

    //初始化订单数据（测试数据，等待后台接口写入）
    private void initGoodDatas(){
        GoodItem goodItem = new GoodItem("999","10g/袋","1","2.5");
        for(int i=0;i<10;i++){
            goodList.add(goodItem);
        }
    }

    //展示服务dialog
    void showServiceDialog(){
        Dialog dialog = new ServiceDialog(ResultActivity.this);
        dialog.create();
        dialog.show();
    }

    //展示退出dialog
    void showExitDialog(){
        Dialog dialog = new ExitDialog(ResultActivity.this);
        dialog.create();
        dialog.show();
    }
}
