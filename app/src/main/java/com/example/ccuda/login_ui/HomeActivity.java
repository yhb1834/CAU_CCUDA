package com.example.ccuda.login_ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.AppSettingsFragment;
import com.example.ccuda.CartFragment;
import com.example.ccuda.ChatFragment;
import com.example.ccuda.GetcouponFragment;
import com.example.ccuda.HomeFragment;
import com.example.ccuda.NotifyFragment;
import com.example.ccuda.R;
import com.example.ccuda.RecipeFragment;
import com.example.ccuda.UploadarticlesFragment;
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.db.PostRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {


    //private ListView listView;
    //private Adapter adapter;

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
        View header = navigationView.getHeaderView(0);
        TextView nav_nicname_text = (TextView) header.findViewById(R.id.nickname);
        TextView nav_email_text = (TextView) header.findViewById(R.id.user_id);

        nav_nicname_text.setText(SaveSharedPreference.getNicname(this));
        if(SaveSharedPreference.getEmail(this).length() == 0)
            nav_email_text.setText("kakao login user");
        else
            nav_email_text.setText(SaveSharedPreference.getEmail(this));




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



    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

}

