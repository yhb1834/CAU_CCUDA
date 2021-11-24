package com.example.ccuda.ui_Cart;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class AllCartListAdapter extends BaseAdapter {
    ArrayList<CartItemModel> items=new ArrayList<>();
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public CartItemModel getCartItemModel(int position){ return items.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemIntId(int position){ return items.get(position).getItemid(); }

    public void addItem(CartItemModel item){
        items.add(item);
    }

    public void remove(CartItemModel item){
        items.remove(item);
    }

    public void removeAll(){ items.clear(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView view=null;
        if(convertView==null){
            view=new ItemView(parent.getContext());
        }
        else {
            view=(ItemView) convertView;
        }
        CartItemModel item=items.get(position);
        view.setImageView(item.getImageUrl());
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
