package com.snoopy.scancode.dialog;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.snoopy.scancode.MainActivity;
import com.snoopy.scancode.R;
import com.snoopy.scancode.result.ResultActivity;
import java.util.List;


/*
进入详情页面时，显示将有服务员为您服务的弹框dialog
 */
public class ExitDialog extends Dialog {

    public Context context;
    TextView tv_timer;
    public ExitDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    public ExitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
    }

    protected ExitDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_exit,null);
        setContentView(view);
        tv_timer = findViewById(R.id.tv_timer);
        //点击边框外围不会取消dialog
        setCanceledOnTouchOutside(false);
        //点击back不会取消dialog
        setCancelable(false);
        new DownTimer().start();
        //等待员工前来点击"好的"按钮，取消dialog
        view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                turnToMainActivity();
            }
        });
    }


    //退出详情界面，到主界面
    void turnToMainActivity(){
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        //判断ResultActivity是否位于前台，防止重复返回MainActivity
        if(isForeground(context, "com.snoopy.scancode.result.ResultActivity")){
            context.startActivity(intent);
            ResultActivity.getInstance().finish();
        }
    }

    /**
     * 判断某个Activity 界面是否在前台
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean  isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }


    //倒计时的计时器
    class DownTimer extends CountDownTimer{

        //设置总时间3秒，，间隔1秒
        public DownTimer(){
            super(3000,1000);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            tv_timer.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            ExitDialog.this.dismiss();
            turnToMainActivity();
        }
    }
}
