package com.example.ccuda.ui_Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.RecipeDTO;
import com.example.ccuda.data.RecipeItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Chat.ChatRoomActivity;
import com.example.ccuda.ui_Recipe.RecipeFragment;
import com.example.ccuda.ui_Recipe.RecipeitemAdapter;
import com.example.ccuda.ui_Recipe.RegiItemsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductItemFragment extends Fragment {
    private ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    private ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    private ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;

    private CouponData data;
    public ArrayList<ItemData> currentItem = new ArrayList<>();
    TextView Productname, Store, Price, Date, Seller, Star, Otheritems;
    ImageView Photo;
    Button btn_message;
    Context context;
    RecyclerView recyclerView;

    String seller_id;
    String coupon_id;
    String seller_nicname;
    String star;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itemref;

    //관련상품리스트
    private ArrayList<RegiItemsModel> mrgArrayList;
    private RecipeitemAdapter mrgAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment1_home_product_item, container, false);

        String photo = getArguments().getString("photo");
        String productname = getArguments().getString("productname");
        String store = getArguments().getString("store");
        int price = getArguments().getInt("price");
        String date = getArguments().getString("validity");
        context = getActivity();
        coupon_id = getArguments().getString("coupon_id");
        seller_id = getArguments().getString("seller_id");
        seller_nicname = getArguments().getString("seller_nicname");
        star = getArguments().getString("seller_score");


        Photo = (ImageView) view.findViewById(R.id.photo);
        Productname = (TextView) view.findViewById(R.id.itemname2);
        Store = (TextView) view.findViewById(R.id.store2);
        Price = (TextView) view.findViewById(R.id.price2);
        Date = (TextView) view.findViewById(R.id.validity2);
        Seller = (TextView) view.findViewById(R.id.sellerID2);
        Star = (TextView) view.findViewById(R.id.star2);
        btn_message = view.findViewById(R.id.btn_message);
        recyclerView = view.findViewById(R.id.reco_recyclerview);

        Glide.with(this).load(photo).into(Photo);
        Productname.setText(productname);
        Store.setText(store);
        Price.setText(price + " 원");
        Date.setText(date);
        Seller.setText(seller_nicname);
        Star.setText(star);

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: need seller_id, coupon_id
                // 자신이 올린 게시물이 아닌 경우 메세지보내기 가능

                if(!seller_id.equals(Long.toString(SaveSharedPreference.getId(context)))){
                        open_chatroom(coupon_id, seller_id);
                }else{
                    Toast.makeText(context, "회원님이 작성한 글입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //관련상품목록
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mrgArrayList = new ArrayList<RegiItemsModel>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        itemref = firebaseDatabase.getReference().child("Recipe");

        itemref.orderByChild("items/"+productname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mrgArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //새 데이터(값 : ChatData객체) 가져오기
                    RecipeDTO recipeDTO = ds.getValue(RecipeDTO.class);
                    String[] items = recipeDTO.getItemname().split(", ");
                    for(int j=0; j<items.length; j++){
                        String[] item = items[j].split(" - ");
                        if(!item[2].equals(productname) && !checkisthere(item[2], mrgArrayList)) {
                            mrgArrayList.add(new RegiItemsModel(item[1], item[3], item[2], Integer.parseInt(item[0])));
                        }
                    }
                }
                mrgAdapter = new RecipeitemAdapter(mrgArrayList);
                recyclerView.setAdapter(mrgAdapter);
                mrgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //임시
        //mrgArrayList.add(new RegiItemsModel("편의점", "","상품명",0));
        //mrgArrayList.add(new RegiItemsModel("편의점명", "","상품명",0));
        //mrgAdapter = new RecipeitemAdapter(mrgArrayList);
        //recyclerView.setAdapter(mrgAdapter);
        //mrgAdapter.notifyDataSetChanged();

        return view;
    }

    void open_chatroom(String coupon_id, String seller_id){
        String buyer_id = Long.toString(SaveSharedPreference.getId(context));
        String roomnum = coupon_id + seller_id + buyer_id;

        // 채팅방으로 이동
        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra("roomnum",roomnum);
        intent.putExtra("seller_id",seller_id);
        intent.putExtra("buyer_id",buyer_id);
        intent.putExtra("coupon_id",coupon_id);
        startActivity(intent);
    }

    /*
    void find_seller(){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("couponlist");
                    int length = jsonArray.length();

                    for(int i=0; i<length; i++){
                        CouponData couponData = new CouponData();
                        JSONObject object = jsonArray.getJSONObject(i);

                        couponData.setCoupon_id(Integer.parseInt(object.getString("coupon_id")));
                        couponData.setPrice(Integer.parseInt(object.getString("price")));       //쿠폰 가격
                        couponData.setExpiration_date(object.getString("expiration_date")); // 쿠폰 유효기간 "Y-m-d" 형식
                        couponData.setContent(object.getString("content")); // 글 내용
                        String storename = object.getString("storename");
                        storename = storename.toLowerCase();
                        couponData.setStorename(storename);
                        couponData.setPlustype(object.getString("category"));
                        couponData.setSeller_id(Long.parseLong(object.getString("seller_id"))); // 판매자 확인용 id
                        couponData.setPost_date(object.getString("post_date")); // "Y-m-d H:i:s" 형식
                        couponData.setSeller_name(object.getString("seller_nicname")); // 판매자 닉네임
                        couponData.setSeller_score(object.getString("seller_score")); // 판매자 평점

                        String item_id = object.getString("item_id");
                        System.out.println("itemssss"+item_id);
                        if(item_id.equals(coupon_id)){
                            seller_id=object.getString("seller_id");
                            star=object.getString("seller_score");

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

     */
    public boolean checkisthere(String itemname, ArrayList<RegiItemsModel> mrgArrayList){
        for(int i=0; i<mrgArrayList.size(); i++){
            if(mrgArrayList.get(i).getItemname().equals(itemname))
                return true;
        }
        return false;
    }
}

