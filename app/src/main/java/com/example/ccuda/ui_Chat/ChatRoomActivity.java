package com.example.ccuda.ui_Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.ChatData;
import com.example.ccuda.data.SaveSharedPreference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity {
    ImageButton sendbtn;
    EditText et;
    ListView listView;

    ArrayList<ChatData.Comment> messageItems=new ArrayList<>();
    ChatAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatref;
    String destid;
    String coupon_id;
    String chatRoomUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        et=findViewById(R.id.et);
        listView=findViewById(R.id.listView);
        adapter=new ChatAdapter(messageItems,getLayoutInflater());
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        chatRoomUid=intent.getExtras().getString("roomnum");
        coupon_id = intent.getExtras().getString("coupon_id");
        destid = intent.getExtras().getString("destid");

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

    }
    public void clickSend(View view){
        if(!et.getText().toString().equals("")){
            ChatData.Comment comment = new ChatData.Comment();
            comment.user_id = String.valueOf(SaveSharedPreference.getId(this));
            comment.nicname = SaveSharedPreference.getNicname(this);
            comment.imageurl = SaveSharedPreference.getProfileimage(this);
            comment.msg = et.getText().toString();
            Calendar calendar= Calendar.getInstance();
            comment.timestamp = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
            firebaseDatabase = FirebaseDatabase.getInstance();
            chatref = firebaseDatabase.getReference();
            chatref.child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment);

            et.setText("");
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

}