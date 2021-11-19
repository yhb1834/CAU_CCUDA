package com.example.ccuda.ui_Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.ChatData;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.ChatRequest;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.ui_Chat.ChatRoomActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductItemFragment extends Fragment {
    private CouponData data;
    TextView Productname, Store, Price, Date, Seller, Star, Otheritems;
    ImageView Photo;
    Button btn_message;
    Context context;

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
        context = getActivity();

        Photo = (ImageView) view.findViewById(R.id.photo);
        Productname = (TextView) view.findViewById(R.id.itemname2);
        Store = (TextView) view.findViewById(R.id.store2);
        Price = (TextView) view.findViewById(R.id.price2);
        Date = (TextView) view.findViewById(R.id.validity2);
        Seller = (TextView) view.findViewById(R.id.sellerID2);
        Star = (TextView) view.findViewById(R.id.star2);
        btn_message = view.findViewById(R.id.btn_message);


        //Photo.setImageResource(StringToBitmap(photo));
        Productname.setText(productname);
        Store.setText(store);
        Price.setInputType(price);
        //Date.setText(date);
        Seller.setText(seller);
        //Star.setText(score);

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: need seller_id, coupon_id

                String seller_id = "";
                String coupon_id = "";
                // 자신이 올린 게시물이 아닌 경우 메세지보내기 가능
                if(!seller_id.equals(Long.toString(SaveSharedPreference.getId(context)))){
                    if(seller_id != "")
                        open_chatroom(coupon_id, seller_id);
                }
            }
        });

        return view;
    }

    void open_chatroom(String coupon_id, String seller_id){
        String buyer_id = Long.toString(SaveSharedPreference.getId(context));
        String roomnum = coupon_id + seller_id + buyer_id;

        //채팅방으로 이동
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                intent.putExtra("roomnum",roomnum);
                startActivity(intent);
            }
        };
        CouponpageRequest couponpageRequest = new CouponpageRequest("openchat",buyer_id,seller_id,coupon_id,responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(couponpageRequest);

    }

}