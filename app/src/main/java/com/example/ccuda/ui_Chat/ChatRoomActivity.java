package com.example.ccuda.ui_Chat;

import static android.view.View.*;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ccuda.BuildConfig;
import com.example.ccuda.R;
import com.example.ccuda.data.ChatData;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.ChatRequest;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.db.DealManager;
import com.example.ccuda.login_ui.LoginActivity;
import com.example.ccuda.ui_Home.HomeActivity;
import com.example.ccuda.ui_Recipe.MultiImageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    Button finish;

    EditText et;
    ListView listView;

    ArrayList<ChatData.Comment> messageItems=new ArrayList<>();
    ChatAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatref;
    String chatRoomUid;
    String seller_id;
    String buyer_id;
    String coupon_id;
    Uri imageuri;
    String imageurl;


    String[] items = {"","????????? ?????????", "??????CU"};
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ratingBar = findViewById(R.id.ratingBar);
        finish = findViewById(R.id.finish);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                float score = ratingBar.getRating();
                System.out.println(score);
            }
        });

        finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingBar.getRating() >= 0.5){
                    clickfinish();
                }else {
                    Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et = findViewById(R.id.et);
        listView=findViewById(R.id.listView);
        adapter=new ChatAdapter(messageItems,getLayoutInflater());
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        chatRoomUid=intent.getExtras().getString("roomnum");
        seller_id = intent.getExtras().getString("seller_id");
        buyer_id = intent.getExtras().getString("buyer_id");
        coupon_id = intent.getExtras().getString("coupon_id");
        imageurl = null;

        // ???????????? ??????????????? ??????
        finish = findViewById(R.id.finish);
        ratingBar = findViewById(R.id.ratingBar);
        setbuttonenable();

        //Firebase DB?????? ??????
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatref = firebaseDatabase.getReference();

        chatref.child("chatrooms").child(chatRoomUid).child("comments").addChildEventListener(new ChildEventListener() {
            //?????? ????????? ?????? ??? ValueListener??? ????????? ?????? ???????????? ???????????? ?????? ?????? ???
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //?????? ????????? ?????????(??? : ChatData??????) ????????????
                ChatData.Comment chatData= dataSnapshot.getValue(ChatData.Comment.class);

                //????????? ???????????? ???????????? ???????????? ?????? ArrayList??? ??????
                messageItems.add(chatData);

                //??????????????? ??????
                adapter.notifyDataSetChanged();
                listView.setSelection(messageItems.size()-1); //??????????????? ????????? ????????? ????????? ?????? ??????
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Spinner openApp = (Spinner) findViewById(R.id.otherApp);
        openApp.setAdapter(arrayAdapter);
        openApp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view,int position, long id){
                //Toast.makeText(getApplicationContext(),items[position] , Toast.LENGTH_LONG).show();
                PackageManager pm = getApplicationContext().getPackageManager();
                switch (position) {
                    case 1:
                        String packageName = "com.gsr.gs25";
                        try {
                            pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA);
                            Intent intent = pm.getLaunchIntentForPackage(packageName);
                            startActivity(intent);
                        }catch (PackageManager.NameNotFoundException e){
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id="+packageName));
                            startActivity(intent);
                        }

                        break;
                    case 2:
                        packageName = "com.bgfcu.membership"; ///com.gsretail.android.thepop.activity.splash.SplashActivity";
                        try {
                            pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA);
                            Intent intent = pm.getLaunchIntentForPackage(packageName);
                            startActivity(intent);
                        }catch (PackageManager.NameNotFoundException e){
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id="+packageName));
                            startActivity(intent);
                        }
                        break;
                }

            }
            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }

        });

        // ???????????? ???????????? ??????
        ImageButton btn_getImage = findViewById(R.id.imageButton);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });

        //?????? ??????
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ChatData.Comment data = (ChatData.Comment) adapter.getItem(position);
                if(data.type.equals("image")){
                    String fileName = coupon_id + seller_id + buyer_id + data.date;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this)
                            .setTitle("????????? ????????????")
                            .setMessage("?????? ???????????? ?????????????????? ?????????????????????????")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    imagedownload(fileName);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    imageuri = data.getData();
                    saveData();
                }
                else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(this,"?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    void saveData(){
        //Firebase storage??? ????????? ???????????? ?????? ????????? ?????????(????????? ????????????)
        SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
        String date = sdf.format(new Date());
        String fileName= chatRoomUid+date+".png";

        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference imgRef= firebaseStorage.getReference("chatImages/"+fileName);


        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this)
                .setTitle("???????????? ??????")
                .setMessage("?????? ???????????? ?????????????????????????")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        UploadTask uploadTask= imgRef.putFile(imageuri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //????????? ???????????? ?????????????????????...
                                //????????? firebase storage??? ????????? ?????? ???????????? URL??? ????????????
                                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //??????????????? firebase??? ???????????? ???????????? ??????
                                        //???????????? ?????? ???????????? ??????(URL)??? ???????????? ????????????
                                        imageurl = uri.toString();
                                        openchat();
                                        ChatData.Comment comment = new ChatData.Comment();
                                        comment.user_id = String.valueOf(SaveSharedPreference.getId(ChatRoomActivity.this));
                                        Calendar calendar= Calendar.getInstance();
                                        comment.timestamp = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

                                        comment.msg = imageurl;
                                        comment.type = "image";
                                        comment.date = date;

                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        chatref = firebaseDatabase.getReference();
                                        chatref.child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment);
                                    }
                                });

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imageurl=null;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /*
    ArrayList<Uri> uriList = new ArrayList<>();
    MultiImageAdapter adapter2;


    // ???????????? ??????????????? ????????? ??? ???????????? ?????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // ?????? ???????????? ???????????? ?????? ??????
            //Toast.makeText(getApplicationContext(), "???????????? ???????????? ???????????????.", Toast.LENGTH_LONG).show();
        }
        else{   // ???????????? ???????????? ????????? ??????
            if(data.getClipData() == null){     // ???????????? ????????? ????????? ??????
                //Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                adapter2 = new MultiImageAdapter(uriList, getApplicationContext());
                listView.setAdapter((ListAdapter) adapter2);
                listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // ???????????? ????????? ????????? ??????
                ClipData clipData = data.getClipData();
                //Log.e("clipData", String.valueOf(clipData.getItemCount()));

        finish = findViewById(R.id.finish);
        finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              //
            }
        });

                if(clipData.getItemCount() > 10){   // ????????? ???????????? 11??? ????????? ??????
                    Toast.makeText(getApplicationContext(), "????????? 10????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                }
                else{   // ????????? ???????????? 1??? ?????? 10??? ????????? ??????
                    //Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // ????????? ??????????????? uri??? ????????????.
                        try {
                            uriList.add(imageUri);  //uri??? list??? ?????????.

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    listView.setAdapter(adapter2);
                    listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));     // ?????????????????? ?????? ????????? ??????
                }
            }
        }
    }
*/


    public void clickSend(View view){
        if(!et.getText().toString().equals("")){
            openchat();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
            String date = sdf.format(new Date());
            ChatData.Comment comment = new ChatData.Comment();
            comment.user_id = String.valueOf(SaveSharedPreference.getId(this));
            Calendar calendar= Calendar.getInstance();
            comment.timestamp = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

            comment.msg = et.getText().toString();
            comment.type = "text";
            comment.date = date;

            firebaseDatabase = FirebaseDatabase.getInstance();
            chatref = firebaseDatabase.getReference();
            chatref.child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment);

            et.setText("");
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    private void setbuttonenable(){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msgisdeal = jsonObject.getString("msgisdeal");
                    String postisdeal = jsonObject.getString("postisdeal");

                    if(buyer_id.equals(Long.toString(SaveSharedPreference.getId(ChatRoomActivity.this)))){
                        if(postisdeal.equals("0")){     // ?????????
                            finish.setEnabled(false);
                        }
                        else{
                            if(msgisdeal.equals("0")){  // ??????????????? ????????????
                                finish.setEnabled(false);
                                et.setHint("????????????");
                                et.setClickable(false);
                                et.setFocusable(false);
                            }
                            else if(msgisdeal.equals("1")){  // ?????? ???
                                finish.setEnabled(true);
                            }
                            else{ // ?????? ???
                                finish.setEnabled(false);
                                et.setHint("????????????");
                                et.setClickable(false);
                                et.setFocusable(false);
                            }
                        }

                    }
                    else{
                        if(postisdeal.equals("0"))      // ?????????
                            finish.setEnabled(true);
                        else {                            // ????????????
                            finish.setEnabled(false);
                            et.setHint("????????????");
                            et.setClickable(false);
                            et.setFocusable(false);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DealManager dealManager = new DealManager("isdealdone", seller_id, buyer_id, coupon_id,"",responsListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(dealManager);
    }

    private void clickfinish(){
        float star = ratingBar.getRating();
        String option;
        if(seller_id.equals(Long.toString(SaveSharedPreference.getId(this)))){
            option = "dealdone";
        }else{
            option = "eval_seller";
        }

        if(star == 0.0){
            Toast.makeText(this,"????????? ??????????????????.",Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this)
                .setTitle("?????? ??????")
                .setMessage("??????: "+star+"\n"+"???????????? ??? ??????????????? ?????? ????????? ?????????????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Response.Listener<String> responsListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Toast.makeText(ChatRoomActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                    finish.setEnabled(false);
                                    et.setHint("????????????");
                                    et.setClickable(false);
                                    et.setFocusable(false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        DealManager dealManager = new DealManager(option, seller_id, buyer_id, coupon_id,Float.toString(star),responsListener);
                        RequestQueue queue = Volley.newRequestQueue(ChatRoomActivity.this);
                        queue.add(dealManager);
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "*??????????????? ???????????? ??? ????????????.", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openchat(){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        };
        CouponpageRequest couponpageRequest = new CouponpageRequest("openchat",buyer_id,seller_id,coupon_id,responsListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(couponpageRequest);
    }

    private void imagedownload(String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        //??????????????? ????????? ???????????? ?????? ?????????
        StorageReference pathReference = storageReference.child("chatImages/" + fileName + ".png");

        //????????? ?????? ????????? ????????????

        File rootPath = makeDir();

        final File localFile = new File(rootPath,fileName+".png");
        pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(localFile));
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public File makeDir() {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(); //????????? ?????????
        String directoryName = "PPLUSONE";
        final File myDir = new File(root + "/" + directoryName);
        if (!myDir.exists()) {
            boolean wasSuccessful = myDir.mkdir();
            if (!wasSuccessful) {
                System.out.println("file: was not successful.");
            } else {
                System.out.println("file: ????????? ??????????????????." + root + "/" + directoryName);
            }
        } else {
            System.out.println("file: " + root + "/" + directoryName +"?????? ????????????");
        }

        return myDir;
    }

}
