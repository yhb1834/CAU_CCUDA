package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.data.UserData;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.db.PostRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<CouponData> mArrayList = new ArrayList<>();
    UserData userData;



    private ListView listView;
    private Adapter adapter;

    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment fragmentHome = new HomeFragment();
    private RecipeFragment fragmentRecipe = new RecipeFragment();
    private CartFragment fragmentCart = new CartFragment();
    private ChatFragment fragmentChat = new ChatFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
            switch (item.getItemId()){
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, fragmentHome).commit();
                    return true;
                case R.id.recipe:
                    getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, fragmentRecipe).commit();
                    return true;
                case R.id.cart:
                    getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, fragmentCart).commit();
                    return true;
                case R.id.chat:
                    getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, fragmentChat).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();

        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());
        //bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        BottomNavigationView navView = findViewById((R.id.bottom_navigation));
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentHome = new HomeFragment();
        fragmentRecipe = new RecipeFragment();
        fragmentCart = new CartFragment();
        fragmentChat = new ChatFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, fragmentHome).commit();


        ivMenu=findViewById(R.id.menu);
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!= null){
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                switch(id){
                    case R.id.upload_article:
                        getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new UploadarticlesFragment()).commit();
                        break;
                    case R.id.get_coupon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new GetcouponFragment()).commit();
                        break;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new AppSettingsFragment()).commit();
                        break;
                    case R.id.notify:
                        getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new NotifyFragment()).commit();
                        break;
                }
                return true;
            }
        });
        //FrameLayout contentFrame = findViewById((R.id.innerLayout));
        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater.inflate(R.layout.posthome, contentFrame, false);

        //changeView(0);
        //adapter=new Adapter();

        //listView=(ListView) findViewById(R.id.list);
        //listView.setAdapter(adapter);

        //adapter.addItem("물건1", R.drawable.add, "gs");
        //adapter.addItem("물건2", R.drawable.add, "gs");
        //adapter.addItem("물건3", R.drawable.add, "gs");
        //adapter.addItem("물건4", R.drawable.add, "gs");
        //adapter.addItem("물건5", R.drawable.add, "gs");

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }



    // 판매 쿠폰 리스트 db 불러오기
    protected void load_couponlist(){
        mArrayList.clear();
        userData = UserData.getInstance();
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




    // 판매 쿠폰 포스팅 db 저장
    protected void posting(int item_id, int price, String expiration_date,
                           String content, String coupon_image){
        userData = UserData.getInstance();
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success=="success"){
                        // 포스팅 성공
                        Log.d("success","posting success");
                        Toast.makeText(getApplicationContext(),"판매글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
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
        PostRequest postRequest = new PostRequest("posting", userData.getUserid(), item_id, price, expiration_date, content, coupon_image, 0, responsListener);
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(postRequest);
    }

    // 게시글 삭제 db 저장
    protected void deletepost(int coupon_id){
        // 작성자: seller_id, 삭제할 쿠폰: coupon_id
        userData = UserData.getInstance();
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success=="success"){
                        // 포스팅 성공
                        Log.d("success","delete success");
                        Toast.makeText(getApplicationContext(),"해당 판매글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PostRequest postRequest = new PostRequest("deletepost",userData.getUserid(), 0, 0, "", "", "", coupon_id, responsListener);
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(postRequest);
    }

}

