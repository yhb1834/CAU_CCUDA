package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ccuda.SideMenu.AdapterForUploadarticles;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.db.MypageRequest;
import com.example.ccuda.ui_Cart.CartItemAdapter;
import com.example.ccuda.ui_Cart.CartItemModel;
import com.example.ccuda.ui_Cart.addToCart;
import com.example.ccuda.ui_Home.Adapter;
import com.example.ccuda.ui_Home.HomeActivity;
import com.example.ccuda.ui_Home.UploadCoupon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPage extends Fragment {
    private ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    private ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    private ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;
    private TextView mypagetv;
    private ImageView mypageiv;
    private TextView star;
    private ImageView[] imageViews=new ImageView[5];
    private ArrayList<CouponData> CouponArrayList=new ArrayList<>();
    Context context;

    Button upload;
    RecyclerView cartList;
    CartItemAdapter adapter;
    Button addCart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mypage, container, false);
        context = getActivity();
        mypagetv = view.findViewById(R.id.mypagetv);
        mypageiv = view.findViewById(R.id.mypageiv);
        upload=view.findViewById(R.id.mypage_coupon_upload);
        cartList=view.findViewById(R.id.cartList);
        star=view.findViewById(R.id.star);

        imageViews[0]=view.findViewById(R.id.my_coupon_image1);
        imageViews[1]=view.findViewById(R.id.my_coupon_image2);
        imageViews[2]=view.findViewById(R.id.my_coupon_image3);
        imageViews[3]=view.findViewById(R.id.my_coupon_image4);
        imageViews[4]=view.findViewById(R.id.my_coupon_image5);

        /*set userinfo*/
        String email;
        if(SaveSharedPreference.getEmail(context).length() == 0)
            email = "kakao login user";
        else
            email = SaveSharedPreference.getEmail(context);
        mypagetv.setText(SaveSharedPreference.getNicname(context)+"\n"+email);
        Glide.with(this).load(SaveSharedPreference.getProfileimage(context)).into(mypageiv);
        star.setText(Double.toString(SaveSharedPreference.getScore(context)));

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, new UploadCoupon());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        // 장바구니
        load_MyCartList();
        // 내 쿠폰
        load_couponlist(getContext());

        addCart=view.findViewById(R.id.add_to_cart_button);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, new addToCart());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }


    private void load_MyCartList(){ // 장바구니 목록 불러오
        ArrayList<CartItemModel> arr=new ArrayList<>();
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("mycartlist");
                    int length = jsonArray.length();

                    for(int i=0; i<length; i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        String item_id = object.getString("item_id");
                        String storename = object.getString("storename");

                        if(storename.equals("cu")){
                            for(int j=0; j<cuItem.size(); j++){
                                if(Integer.toString(cuItem.get(j).getItemid()).equals(item_id)){
                                    //adapter.addItem(cuItem.get(j).getItemname(),cuItem.get(j).getImage(),storename);
                                    arr.add(new CartItemModel(cuItem.get(j).getImage(), cuItem.get(j).getItemname(), cuItem.get(j).getStorename(), cuItem.get(j).getItemid()));
                                }
                            }
                        }else if(storename.equals("gs25")){
                            for(int j=0; j<gs25Item.size(); j++){
                                if(Integer.toString(gs25Item.get(j).getItemid()).equals(item_id)){
                                    //adapter.addItem(gs25Item.get(j).getItemname(),gs25Item.get(j).getImage(),storename);
                                    arr.add(new CartItemModel(gs25Item.get(j).getImage(), gs25Item.get(j).getItemname(), gs25Item.get(j).getStorename(), gs25Item.get(j).getItemid()));
                                }
                            }
                        }else {
                            for(int j=0; j<sevenItem.size(); j++){
                                if(Integer.toString(sevenItem.get(j).getItemid()).equals(item_id)){
                                    //adapter.addItem(sevenItem.get(j).getItemname(),sevenItem.get(j).getImage(),storename);
                                    arr.add(new CartItemModel(sevenItem.get(j).getImage(), sevenItem.get(j).getItemname(), sevenItem.get(j).getStorename(), sevenItem.get(j).getItemid()));
                                }
                            }
                        }
                    }
                    adapter=new CartItemAdapter(arr);
                    System.out.println("length"+arr.size());
                    cartList.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    cartList.setLayoutManager(layoutManager);
                    cartList.setAdapter(adapter);

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL); //밑줄
                    cartList.addItemDecoration(dividerItemDecoration);


                    //adapter.setOnClickListener(this::onItemClicked);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CartRequest cartRequest = new CartRequest("mycartlist", SaveSharedPreference.getId(context), 0,"",responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartRequest);
    }



    protected void load_couponlist(Context context){ // 내가 업로드 한 쿠폰 불러오기
        CouponArrayList.clear();
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("mypostlist");
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
                        String isdealdone = object.getString("isdeal");
                        if(isdealdone.equals("0")){
                            couponData.setIsdeal(false);
                        }else{
                            couponData.setIsdeal(true);
                        }
                        couponData.setSeller_name(object.getString("seller_nicname")); // 판매자 닉네임
                        couponData.setSeller_score(object.getString("seller_score")); // 판매자 평점

                        int item_id = Integer.parseInt(object.getString("item_id"));
                        if(storename.equals("cu")){
                            for(int j=0; j<cuItem.size(); j++ ){
                                if(cuItem.get(j).getItemid()==item_id){
                                    couponData.setItem_name(cuItem.get(j).getItemname());
                                    couponData.setCategory(cuItem.get(j).getCategory());
                                    couponData.setImage(cuItem.get(j).getImage());
                                    break;
                                }
                            }
                        }else if(storename.equals("gs25")){
                            for(int j=0; j<gs25Item.size(); j++ ){
                                if(gs25Item.get(j).getItemid()==item_id){
                                    couponData.setItem_name(gs25Item.get(j).getItemname());
                                    couponData.setCategory(gs25Item.get(j).getCategory());
                                    couponData.setPlustype(gs25Item.get(j).getPlustype());
                                    couponData.setImage(gs25Item.get(j).getImage());
                                    break;
                                }
                            }
                        }else {
                            for(int j=0; j<sevenItem.size(); j++ ){
                                if(sevenItem.get(j).getItemid()==item_id){
                                    couponData.setItem_name(sevenItem.get(j).getItemname());
                                    couponData.setCategory(sevenItem.get(j).getCategory());
                                    couponData.setPlustype(sevenItem.get(j).getPlustype());
                                    couponData.setImage(sevenItem.get(j).getImage());
                                    break;
                                }
                            }
                        }
                        CouponArrayList.add(couponData);
                        //adapter.addItem(couponData.getItem_name(), R.drawable.add, couponData.getStorename());

                    }
                    for(CouponData a:CouponArrayList){
                        System.out.println(a.getItem_name());
                    }




                    for(int i=0;i<5;i++){
                        imageViews[i].setImageResource(0);
                        imageViews[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }

                    int i;
                    if(CouponArrayList.size()<=5){
                        for (i=0;i<CouponArrayList.size();i++){
                            CouponData item=CouponArrayList.get(CouponArrayList.size()-1-i);
                            Glide.with(getContext()).load(CouponArrayList.get(CouponArrayList.size()-1-i).getImageurl())
                                    .into(imageViews[i]);
                            imageViews[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //click_cart_item(getContext(),item);
                                }
                            });
                        }
                    }else {
                        for (i=0;i<5;i++){
                            CouponData item=CouponArrayList.get(CouponArrayList.size()-1-i);
                            Glide.with(getContext()).load(CouponArrayList.get(CouponArrayList.size()-1-i).getImageurl())
                                    .into(imageViews[i]);
                            imageViews[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {  }
                            });
                        }
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        MypageRequest mypageRequest = new MypageRequest("mypostlist", SaveSharedPreference.getId(context),"",responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(mypageRequest);
    }

}
