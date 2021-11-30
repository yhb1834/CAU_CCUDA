package com.example.ccuda.SideMenu;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.ui_Cart.CartItemModel;
import com.example.ccuda.ui_Cart.ItemView;

import java.util.ArrayList;
import java.util.List;

public class AdapterForGetcoupon extends BaseAdapter {
    private ArrayList<CouponData> listViewItemList=new ArrayList<>();
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView itemImage;
        TextView prodName;
        TextView prodVali;
        TextView prodPrice;
        TextView prodStore;

        final  Context context= parent.getContext();
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.product_list, parent, false);
        }

        itemImage=(ImageView) convertView.findViewById(R.id.icon);
        prodName=(TextView) convertView.findViewById(R.id.name);
        prodPrice=(TextView) convertView.findViewById(R.id.price1);
        prodVali=(TextView) convertView.findViewById(R.id.validity1);
        prodStore=(TextView) convertView.findViewById(R.id.store);

        Glide.with(context).load(listViewItemList.get(position).getImageurl())
                .into(itemImage);
        prodName.setText(listViewItemList.get(position).getItem_name());
        prodPrice.setText("  "+listViewItemList.get(position).getPrice() + " 원");
        prodVali.setText(listViewItemList.get(position).getExpiration_date());
        prodStore.setText(listViewItemList.get(position).getStorename());

        return convertView;
    }

    public void setArrayList(ArrayList<CouponData> inputList){
        listViewItemList=inputList;
        System.out.println("잘 왔나 보자");
        System.out.println(listViewItemList);
    }
}
