package com.example.ccuda.ui_Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.annotation.Nullable;
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
import com.bumptech.glide.Glide;
import com.example.ccuda.MyPage;
import com.example.ccuda.R;
import com.example.ccuda.SideMenu.AppSettingsFragment;
import com.example.ccuda.SideMenu.GetcouponFragment;
import com.example.ccuda.SideMenu.NotifyFragment;
import com.example.ccuda.SideMenu.UploadarticlesFragment;
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.db.PostRequest;
import com.example.ccuda.ui_Cart.CartFragment;
import com.example.ccuda.ui_Cart.addToCart;
import com.example.ccuda.ui_Chat.ChatFragment;
import com.example.ccuda.ui_Home.HomeFragment;
import com.example.ccuda.ui_Recipe.OnBackPressedListener;
import com.example.ccuda.ui_Recipe.RecipeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    //profile image
    Uri imgUri;
    private ImageView profile;

    //private ListView listView;
    //private Adapter adapter;

    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment fragmentHome = new HomeFragment();
    private RecipeFragment fragmentRecipe = new RecipeFragment();
    private addToCart fragmentCart = new addToCart();
    private ChatFragment fragmentChat = new ChatFragment();

    OnBackPressedListener listener;

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
        //fragmentCart = new CartFragment();
        fragmentChat = new ChatFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, fragmentHome).commit();


        ivMenu=findViewById(R.id.menu);
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View header = navigationView.getHeaderView(0);
        TextView nav_nicname_text = (TextView) header.findViewById(R.id.nickname);
        TextView mypage = (TextView) header.findViewById(R.id.mypage);
        profile = (ImageView) header.findViewById(R.id.imageView);


        Glide.with(this).load(SaveSharedPreference.getProfileimage(this)).into(profile);
        nav_nicname_text.setText(SaveSharedPreference.getNicname(this));



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

        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast toast=Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_SHORT);
                //toast.show();
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new MyPage()).commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });

        nav_nicname_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

   public void setOnBackPressedListener(OnBackPressedListener listener){
        this.listener = listener;
   }

   @Override
   public void onBackPressed(){
        if(listener != null) {
            listener.onBackPressed();
        }else{
            super.onBackPressed();
        }
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    imgUri= data.getData();
                    Glide.with(this).load(imgUri).into(profile);
                    saveData();

                }
                else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(this,"사진 선택 취소", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    void saveData(){
        //Firebase storage에 이미지 저장하기 위해 파일명 만들기(날짜를 기반으로)
        SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
        String fileName= sdf.format(new Date())+".png";

        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference imgRef= firebaseStorage.getReference("profileImages/"+fileName);

        UploadTask uploadTask= imgRef.putFile(imgUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //이미지 업로드가 성공되었으므로...
                //곧바로 firebase storage의 이미지 파일 다운로드 URL을 얻어오기
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //파라미터로 firebase의 저장소에 저장되어 있는
                        //이미지에 대한 다운로드 주소(URL)을 문자열로 얻어오기
                        SaveSharedPreference.setProfileimage(HomeActivity.this, uri.toString());
                        Toast.makeText(HomeActivity.this, "프로필 저장 완료", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

