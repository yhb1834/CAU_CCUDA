package com.example.ccuda.ui_Cart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.ccuda.R;
import com.example.ccuda.ui_Home.Adapter;

import java.util.ArrayList;

public class AllCartFragment extends CartFragment {
    GridView cartItemList;
    AllCartListAdapter adapter;
    ArrayList<ItemParccelable> itemList=new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_cartlist, container, false);
        cartItemList=view.findViewById(R.id.cartItemGrid);
        adapter=new AllCartListAdapter();
        Bundle bundle=getArguments();
        if(bundle!=null){
            itemList=bundle.getParcelableArrayList("itemlist");
        }
        for(ItemParccelable e:itemList){
            System.out.print(e.getProdName());
        }
        for(int i=0;i<itemList.size();i++){
            adapter.addItem(new CartItemModel(itemList.get(i).getImgUrl(),itemList.get(i).getProdName(),itemList.get(i).getConvName(),itemList.get(i).getId()));
        }
        System.out.println("adapter length: "+adapter.getCount());
        cartItemList.setAdapter(adapter);
        cartItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position: "+position);
                CartItemModel item=new CartItemModel(itemList.get(position).getImgUrl(), itemList.get(position).getProdName(), itemList.get(position).getConvName(), itemList.get(position).getId());
                click_cart_item_in_all(getActivity(), item);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        load_MyCartList();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AllCartListAdapter adapterNew=new AllCartListAdapter();
                for(CartItemModel e:cartList){
                    adapterNew.addItem(e);
                }

                cartItemList.setAdapter(adapterNew);
                cartItemList.invalidateViews();
                cartItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("position: "+position);
                        CartItemModel item=cartList.get(position);
                        click_cart_item_in_all(getActivity(), item);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        }, 500);

    }




}
