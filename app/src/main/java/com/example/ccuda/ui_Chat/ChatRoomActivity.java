package com.example.ccuda.ui_Chat;

import static android.view.View.*;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.ChatData;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Home.HomeActivity;
import com.example.ccuda.ui_Recipe.MultiImageAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    Uri imageuri;
    String imageurl;



    String[] items = {"","나만의 냉장고", "포켓CU"};
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
                      showdialog();
                  }else {
                      Toast.makeText(getApplicationContext(), "별점을 입력하세요", Toast.LENGTH_SHORT).show();
                  }
              }
        });

        et = findViewById(R.id.et);
        listView=findViewById(R.id.listView);
        adapter=new ChatAdapter(messageItems,getLayoutInflater());
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        chatRoomUid=intent.getExtras().getString("roomnum");
        imageurl = null;

        //Firebase DB관리 객체
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatref = firebaseDatabase.getReference();

        chatref.child("chatrooms").child(chatRoomUid).child("comments").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //새로 추가된 데이터(값 : ChatData객체) 가져오기
                ChatData.Comment chatData= dataSnapshot.getValue(ChatData.Comment.class);

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                messageItems.add(chatData);

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged();
                listView.setSelection(messageItems.size()-1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
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

        // 앨범으로 이동하는 버튼
        ImageButton btn_getImage = findViewById(R.id.imageButton);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
    }

    void showdialog(){
        android.app.AlertDialog.Builder msgBuilder = new android.app.AlertDialog.Builder(this)
                .setTitle("구매 완료")
                .setMessage("별점 " + ratingBar.getRating() + "\n구매를 완료하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //score 저장 및 구매완료 전환, 홈에서 삭제
                        //finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "신고하기를 이용하세요", Toast.LENGTH_SHORT).show();
                    }
                });
        android.app.AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();


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
                    Toast.makeText(this,"사진 선택 취소", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    void saveData(){
        //Firebase storage에 이미지 저장하기 위해 파일명 만들기(날짜를 기반으로)
        SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
        String fileName= chatRoomUid+sdf.format(new Date())+".png";

        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference imgRef= firebaseStorage.getReference("chatImages/"+fileName);


        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this)
                .setTitle("이미지를 전송")
                .setMessage("해당 이미지를 전송하시겠습니까?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        UploadTask uploadTask= imgRef.putFile(imageuri);
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
                                        imageurl = uri.toString();

                                        ChatData.Comment comment = new ChatData.Comment();
                                        comment.user_id = String.valueOf(SaveSharedPreference.getId(ChatRoomActivity.this));
                                        Calendar calendar= Calendar.getInstance();
                                        comment.timestamp = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

                                        comment.msg = imageurl;
                                        comment.type = "image";

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


    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            //Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                //Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                adapter2 = new MultiImageAdapter(uriList, getApplicationContext());
                listView.setAdapter((ListAdapter) adapter2);
                listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                //Log.e("clipData", String.valueOf(clipData.getItemCount()));

        finish = findViewById(R.id.finish);
        finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              // TODO: 거래완료
            }
        });

                if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    //Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    listView.setAdapter(adapter2);
                    listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
    }
*/


    public void clickSend(View view){
        if(!et.getText().toString().equals("")){
            ChatData.Comment comment = new ChatData.Comment();
            comment.user_id = String.valueOf(SaveSharedPreference.getId(this));
            Calendar calendar= Calendar.getInstance();
            comment.timestamp = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

            comment.msg = et.getText().toString();
            comment.type = "text";

            firebaseDatabase = FirebaseDatabase.getInstance();
            chatref = firebaseDatabase.getReference();
            chatref.child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment);

            et.setText("");
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    /*class Listener implements RatingBar.OnRatingBarChangeListener
    {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            //float score = ratingBar.getRating();
            System.out.println(rating);
        }
    }*/

}

