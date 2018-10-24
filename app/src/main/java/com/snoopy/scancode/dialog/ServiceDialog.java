package com.snoopy.scancode.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.snoopy.scancode.R;


/*
进入详情页面时，显示将有服务员为您服务的弹框dialog
 */
public class ServiceDialog extends Dialog {

    public Context context;
    public ServiceDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    public ServiceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
    }

    protected ServiceDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_service,null);
        Window window = getWindow();
        window.getAttributes().gravity = Gravity.CENTER;
        //设置透明背景
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setContentView(view);
        //点击边框外围不会取消dialog
        setCanceledOnTouchOutside(false);
        //点击back键不会取消dialog
        setCancelable(false);

        //等待员工前来点击"好的"按钮，取消dialog
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
