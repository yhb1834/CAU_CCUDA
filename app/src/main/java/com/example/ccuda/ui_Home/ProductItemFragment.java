package com.example.ccuda.ui_Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ccuda.R;
import com.example.ccuda.data.CouponData;

public class ProductItemFragment extends Fragment {
    private CouponData data;
    TextView Productname, Store, Price, Date, Seller, Star, Otheritems;
    ImageView Photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment1_home_product_item, container, false);

        String photo = getArguments().getString("photo");
        String productname = getArguments().getString("productname");
        String store = getArguments().getString("store");
        int price = getArguments().getInt("price");
        String seller = getArguments().getString("seller");

        Photo = (ImageView) view.findViewById(R.id.photo);
        Productname = (TextView) view.findViewById(R.id.itemname2);
        Store = (TextView) view.findViewById(R.id.store2);
        Price = (TextView) view.findViewById(R.id.price2);
        Date = (TextView) view.findViewById(R.id.validity2);
        Seller = (TextView) view.findViewById(R.id.sellerID2);
        Star = (TextView) view.findViewById(R.id.star2);


        //Photo.setImageResource(StringToBitmap(photo));
        Productname.setText(productname);
        Store.setText(store);
        Price.setInputType(price);
        //Date.setText(date);
        Seller.setText(seller);
        //Star.setText(score);

        return view;
    }

}