package com.example.ccuda.ui_Home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.db.PostRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ArrayList<CouponData> mArrayList = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    HomeActivity activity;
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        activity=(HomeActivity)getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        activity=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ListView listView;
        View v=inflater.inflate(R.layout.fragment1_home, container, false);
        listView=(ListView) v.findViewById(R.id.listView);
        Adapter adapter=new Adapter();

        listView.setAdapter(adapter);

        adapter.addItem("물건1", R.drawable.add, "gs");
        adapter.addItem("물건2", R.drawable.add, "gs");
        adapter.addItem("물건3", R.drawable.add, "gs");
        adapter.addItem("물건4", R.drawable.add, "gs");
        adapter.addItem("물건5", R.drawable.add, "gs");

        FloatingActionButton addCuppon= (FloatingActionButton) v.findViewById(R.id.add_article);
        addCuppon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new UploadCoupon()).commit();

            }
        });

        return v;
    }


    // 판매 쿠폰 리스트 db 불러오기
    protected void load_couponlist(Context context){
        mArrayList.clear();
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
                        couponData.setSeller_id(Long.parseLong(object.getString("seller_id"))); // 판매자 확인용 id

                        if(object.getString("isdeal")=="1") // 거래 완료 여부
                            couponData.setIsdeal(true);
                        else
                            couponData.setIsdeal(false);

                        couponData.setPost_date(object.getString("post_date")); // "Y-m-d H:i:s" 형식
                        couponData.setSeller_name(object.getString("seller_name")); // 판매자 닉네임
                        couponData.setSeller_score(object.getString("seller_score")); // 판매자 평점
                        couponData.setItem_name(object.getString("item_name")); // 물품명
                        couponData.setCategory(object.getString("category")); // 물품 카테고리
                        couponData.setPlustype(object.getString("plustype"));
                        couponData.setStorename(object.getString("storename"));
                        couponData.setPrice(Integer.parseInt(object.getString("price"))); // 판매자가 등록한 쿠폰가격
                        couponData.setExpiration_date(object.getString("expiration_date")); // 쿠폰 유효기간 "Y-m-d" 형식
                        couponData.setContent(object.getString("content")); // 글 내용
                        couponData.setImage(BitmapConverter.StringToBitmap(object.getString("image"))); // 상품 이미지 (!= 쿠폰 이미지)
                        couponData.setCouponimage(BitmapConverter.StringToBitmap(object.getString("original"))); // 쿠폰 이미지 (!= 상품 이미지)

                        mArrayList.add(couponData);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CouponpageRequest couponpageRequest = new CouponpageRequest("couponlist", responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(couponpageRequest);
    }




    // 판매 쿠폰 포스팅 db 저장
    protected void posting(Context context, int item_id, int price, String expiration_date,
                           String content, String coupon_image){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success=="success"){
                        // 포스팅 성공
                        Log.d("success","posting success");
                        Toast.makeText(context,"판매글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(success == "NoItem"){
                            // 해당 물품에 대한 편의점 데이터 부존재
                            Log.d("success","postring fail: there no item information in DB");
                        }
                        else if(success == "fail")
                            Log.d("success", "query fail");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PostRequest postRequest = new PostRequest("posting", SaveSharedPreference.getId(context), item_id, price, expiration_date, content, coupon_image, 0, responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    // 게시글 삭제 db 저장
    protected void deletepost(Context context, int coupon_id){
        // 작성자: seller_id, 삭제할 쿠폰: coupon_id
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success=="success"){
                        // 포스팅 성공
                        Log.d("success","delete success");
                        int i;
                        for(i=0; i<mArrayList.size(); i++){
                            if(mArrayList.get(i).getCoupon_id() == coupon_id){
                                mArrayList.remove(i);
                                Toast.makeText(context,"해당 판매글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PostRequest postRequest = new PostRequest("deletepost",SaveSharedPreference.getId(context), 0, 0, "", "", "", coupon_id, responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}