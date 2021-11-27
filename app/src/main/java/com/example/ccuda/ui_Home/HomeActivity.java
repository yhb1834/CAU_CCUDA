package com.example.ccuda.ui_Home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ccuda.BuildConfig;
import com.example.ccuda.MyPage;
import com.example.ccuda.R;
import com.example.ccuda.SideMenu.AppSettingsFragment;
import com.example.ccuda.SideMenu.GetcouponFragment;
import com.example.ccuda.SideMenu.LikeRecipeFragment;
import com.example.ccuda.SideMenu.NotifyFragment;
import com.example.ccuda.SideMenu.UploadRecipeFragment;
import com.example.ccuda.SideMenu.UploadarticlesFragment;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.db.MypageRequest;
import com.example.ccuda.ui_Cart.addToCart;
import com.example.ccuda.ui_Chat.ChatFragment;
import com.example.ccuda.OnBackPressedListener;
import com.example.ccuda.ui_Recipe.RecipeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    public static ArrayList<ItemData> cuItem = new ArrayList<>();
    public static ArrayList<ItemData> gs25Item = new ArrayList<>();
    public static ArrayList<ItemData> sevenItem = new ArrayList<>();
    //profile image
    Uri imgUri;
    //firebase 객체
    FirebaseDatabase firebaseDatabase;
    DatabaseReference itemref;

    private ImageView profile;
    private String newnicname;
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

        //권한요청하기
        chck_permission();

        load_item();

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
                    case R.id.upload_recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new UploadRecipeFragment()).commit();
                        break;
                    case R.id.like_recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new LikeRecipeFragment()).commit();
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
                final EditText et = new EditText(HomeActivity.this);
                et.setSingleLine(true);
                LinearLayout container = new LinearLayout(HomeActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                et.setLayoutParams(params);

                container.addView(et);

                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                alert.setCancelable(false);
                alert.setTitle("닉네임 변경");
                alert.setMessage("변경할 닉네임을 입력해주세요.");
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(10); //글자수 제한
                et.setFilters(FilterArray);
                et.setHint("2~10글자 이내로 입력해주세요.");
                alert.setView(container);
                alert.setPositiveButton("확인", null);
                alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) { //취소 버튼을 클ㅣ
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        newnicname = et.getText().toString();
                        if(newnicname.length()>1){
                            Response.Listener<String> responsListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if(success){
                                            SaveSharedPreference.setNicname(HomeActivity.this,newnicname);
                                            nav_nicname_text.setText(newnicname);
                                            Toast.makeText(HomeActivity.this,"닉네임 변경",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                        else {
                                            Toast.makeText(HomeActivity.this,"이미 해당 닉네임이 존재합니다.",Toast.LENGTH_SHORT).show();
                                            et.setText("");
                                            et.setBackgroundResource(R.drawable.red_edit);
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            MypageRequest mypageRequest = new MypageRequest("rewrite", SaveSharedPreference.getId(HomeActivity.this), newnicname, responsListener);
                            RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
                            queue.add(mypageRequest);
                        }
                    }
                });


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
                        if(SaveSharedPreference.getPrefUserProfilefile(HomeActivity.this).length()!=0){
                            StorageReference desertRef = firebaseStorage.getReference("profileImages/"+SaveSharedPreference.getPrefUserProfilefile(HomeActivity.this));
                            desertRef.delete();
                        }
                        SaveSharedPreference.setProfileimage(HomeActivity.this, uri.toString());
                        SaveSharedPreference.setPrefUserProfilefile(HomeActivity.this,fileName);
                        saveprofile("profile",SaveSharedPreference.getProfileimage(HomeActivity.this));
                        saveprofile("filename",SaveSharedPreference.getPrefUserProfilefile(HomeActivity.this));
                    }
                });

            }
        });
    }


    void saveprofile(String option, String newinfo){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.d("profile","success");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        MypageRequest mypageRequest = new MypageRequest(option, SaveSharedPreference.getId(this), newinfo, responsListener);
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(mypageRequest);
    }

    private void load_item(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        itemref= firebaseDatabase.getReference();

        itemref.child("item").child("cu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemData itemData = snapshot.getValue(ItemData.class);
                    cuItem.add(itemData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        itemref.child("item").child("gs25").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemData itemData = snapshot.getValue(ItemData.class);
                    gs25Item.add(itemData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        itemref.child("item").child("seven").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemData itemData = snapshot.getValue(ItemData.class);
                    sevenItem.add(itemData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /*private void chck_permission(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED){ //DENIED
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},2); //1000
                return;
            }
        }
    }*/

    private void chck_permission(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED){ //DENIED
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},2); //1000
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //권한 거절시
                    denialDialog();
                }
                return;
            }
        }
    }

    public void denialDialog() {
        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("저장소 권한이 필요합니다. 환경 설정에서 저장소 권한을 허가해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent); //확인버튼:설정 창으로 이동
                    }
                })
                .create()
                .show();
    }
}

