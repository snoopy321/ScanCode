package com.snoopy.scancode.result;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.snoopy.scancode.R;
import com.snoopy.scancode.dialog.ExitDialog;
import com.snoopy.scancode.dialog.ServiceDialog;
import com.snoopy.scancode.util.Constant;
import com.snoopy.scancode.util.ScreenUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
扫描出场码后，显示订单详情的页面，包括商品总价和总数，
门店，订单号，下单时间，和商品详细情况等
 */
public class ResultActivity extends AppCompatActivity {

    //详情列表ListView
    ListView resultList;
    //确认订单按钮
    Button btn_confirm;
    //已付款
    TextView money_paid;
    //商品总数
    TextView goods_count;
    //门店
    TextView store;
    //订单号
    TextView order_num;
    //付款时间
    TextView order_time;

    //解析json获取的商品详情
    public ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    //付款总额
    private String payMoney;
    //商品总数
    private int totalCount;
    //门店信息
    private String storeName;
    //订单号
    private String id;
    //付款时间
    private String paytime;

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
        showServiceDialog();
        initGoodDatas();

        //在展示详情页面的同时，展示出等待员工服务的dialog
    }

    //初始化控件
    private void initViews() {
        //scanResult = findViewById(R.id.scan_result);
        money_paid = findViewById(R.id.money_paid);
        goods_count = findViewById(R.id.goods_count);
        store = findViewById(R.id.store);
        order_num = findViewById(R.id.order_num);
        order_time = findViewById(R.id.order_time);

        resultList = findViewById(R.id.result_litView);
        btn_confirm = findViewById(R.id.btn_confirm);
        //点击确认订单按钮，显示退出详情界面dialog
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitDialog();
            }
        });
        getSupportActionBar().hide();
    }

    //初始化订单数据（测试数据，等待后台接口写入）
    private void initGoodDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String strContentString = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
        jsonJX(strContentString);
    }


    //解析json字串
    private void jsonJX(String data) {
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String resultCode = jsonObject.getString("success");
                Log.i("tbw", "success: " + resultCode);
                if (resultCode == "true") {
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");

                    //获取商品详情的array
                    JSONArray jsonArray = jsonObjectData.getJSONArray("detailMapList");
                    payMoney = jsonObjectData.getString("paymoney");
                    money_paid.setText(payMoney + "元");
                    totalCount = jsonArray.length();
                    goods_count.setText(totalCount + "件");
                    storeName = jsonObjectData.getString("storeName");
                    store.setText(storeName);
                    id = jsonObjectData.getString("id");
                    order_num.setText(id);
                    paytime = jsonObjectData.getString("paytime");
                    order_time.setText(paytime);
                    Log.i("tbw", "zxc: " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //获取每个商品的详情
                        jsonObject = jsonArray.getJSONObject(i);
                        Map<String, Object> map = new HashMap<String, Object>();
                        try {
                            String goodsName = jsonObject.getString("goodsName");
                            map.put("goodsName", goodsName);

                            String goods_standard = jsonObject.getString("goods_standard");
                            dealGoodsStandard(goods_standard);
                            map.put("goods_standard", goods_standard);

                            String price = jsonObject.getString("price");
                            map.put("price", price);

                            String goodsAmount = jsonObject.getString("goodsAmount");
                            map.put("goodsAmount", goodsAmount);

                            list.add(map);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    //对商品的重量信息进行处理
    private String dealGoodsStandard(String standard) {
        Log.i("tbw", "standard: " + standard);
        return null;
    }

    //Handler运行在主线程中(UI线程中)，  它与子线程可以通过Message对象来传递数据
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyBaseAdapter list_item = new MyBaseAdapter();
                    resultList.setAdapter(list_item);
                    break;
            }

        }
    };

    //适配器
    public class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.listitem_result, null);
                viewHolder.goodsName = view.findViewById(R.id.good_name);
                viewHolder.goods_standard = view.findViewById(R.id.good_detail);
                viewHolder.price = view.findViewById(R.id.good_money);
                viewHolder.goodsAmount = view.findViewById(R.id.good_count);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.goodsName.setText(list.get(i).get("goodsName").toString());
            viewHolder.goods_standard.setText(list.get(i).get("goods_standard").toString());
            viewHolder.price.setText(list.get(i).get("price").toString());
            viewHolder.goodsAmount.setText(list.get(i).get("goodsAmount").toString());
            return view;
        }
    }

    final static class ViewHolder {
        TextView goodsName;
        TextView goods_standard;
        TextView price;
        TextView goodsAmount;
    }

    //展示服务dialog
    void showServiceDialog() {
        Dialog dialog = new ServiceDialog(ResultActivity.this);
        dialog.create();
        setDialogWidthAndHeight(dialog);
        dialog.show();
    }

    //展示退出dialog
    void showExitDialog() {
        Dialog dialog = new ExitDialog(ResultActivity.this);
        dialog.create();
        setDialogWidthAndHeight(dialog);
        dialog.show();
    }

    //设置dialog的高宽
    void setDialogWidthAndHeight(Dialog dialog){
        Window window = dialog.getWindow();
        WindowManager.LayoutParams P = window.getAttributes();
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        P.width =(int) (width * 0.7);
        P.height = (int) (width * 0.7);
        window.setAttributes(P);
    }
}
