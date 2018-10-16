package com.snoopy.scancode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.snoopy.scancode.R;
import com.snoopy.scancode.entity.GoodItem;
import java.util.List;


/*
商品适配器
 */
public class GoodAdapter extends ArrayAdapter {


    private final int resourceId;

    //适配器构造函数，传入layout id和List
    public GoodAdapter(@NonNull Context context, int resource, List<GoodItem> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        GoodItem goodItem = (GoodItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView goodName = view.findViewById(R.id.good_name);
        goodName.setText(goodItem.getgName());

        TextView goodDetail = view.findViewById(R.id.good_detail);
        goodDetail.setText(goodItem.getGheight());

        TextView goodMoney = view.findViewById(R.id.good_money);
        goodMoney.setText(goodItem.getgMoney()+"");

        TextView goodCount = view.findViewById(R.id.good_count);
        goodCount.setText(goodItem.getgCount()+"");
        return view;
    }
}
