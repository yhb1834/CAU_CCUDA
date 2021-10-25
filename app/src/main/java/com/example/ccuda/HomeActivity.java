package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CouponData;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.db.UserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG_JSON = "couponlist";
    private ArrayList<CouponData> mArrayList = new ArrayList<>();

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ChatFragment fragmentChat = new ChatFragment();
    private CupponFragment fragmentCuppon = new CupponFragment();
    private CartFragment fragmentCart = new CartFragment();


    private ListView listView;
    private Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());
        //bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());



        //FrameLayout contentFrame = findViewById((R.id.innerLayout));
        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater.inflate(R.layout.posthome, contentFrame, false);

        //changeView(0);

        adapter=new Adapter();

        listView=(ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        adapter.addItem("물건1", R.drawable.add, "gs");
        adapter.addItem("물건2", R.drawable.add, "gs");
        adapter.addItem("물건3", R.drawable.add, "gs");
        adapter.addItem("물건4", R.drawable.add, "gs");
        adapter.addItem("물건5", R.drawable.add, "gs");

    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.cuppon:
                    transaction.replace(R.id.innerLayout, fragmentCuppon).commitAllowingStateLoss();

                    break;
                case R.id.cart:
                    transaction.replace(R.id.innerLayout, fragmentCart).commitAllowingStateLoss();
                    break;
                case R.id.chat:
                    transaction.replace(R.id.innerLayout, fragmentChat).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    // 판매 쿠폰 리스트 db 불러오기
    protected void load_couponlist(){
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
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(couponpageRequest);
    }


    private void changeView(int index) {
        // LayoutInflater 초기화.
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout frame = (FrameLayout) findViewById(R.id.innerLayout) ;
        if (frame.getChildCount() > 0) {
            // FrameLayout에서 뷰 삭제.
            frame.removeViewAt(0);
        }

        // XML에 작성된 레이아웃을 View 객체로 변환.
        View view = null ;
        switch (index) {
            case 0 :
                view = inflater.inflate(R.layout.posthome, frame, false) ;
                break ;
            case 1 :
                view = inflater.inflate(R.layout.posthome, frame, false) ;
                break ;
            case 2 :
                view = inflater.inflate(R.layout.posthome, frame, false) ;
                break ;
        }

        // FrameLayout에 뷰 추가.
        if (view != null) {
            frame.addView(view) ;
        }
    }

}
