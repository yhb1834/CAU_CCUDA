package com.example.ccuda.ui_Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.ChatData;
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.db.ChatRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private ChatPeopleAdapter mChatPeopleAdapter;
    private ArrayList<PeopleItem> PeopleItems;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Context context;
    String destid;
    String roomnum;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = (View) inflater.inflate(R.layout.fragment1_chat, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        context = getContext();

        /* initiate adapt data */
        PeopleItems = new ArrayList<>();

        /* initiate adapter */
        mChatPeopleAdapter = new ChatPeopleAdapter();

        /* adapt data */
        load_chatlist(context);


        // Recycler view item click event 처리
        mChatPeopleAdapter.setOnItemClickListener(new ChatPeopleAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final PeopleItem item = PeopleItems.get(a_position);
                Intent intent = new Intent(getActivity(),ChatRoomActivity.class);
                intent.putExtra("roomnum",item.getRoomnum());
                startActivity(intent);
            }
        });

        return view;
    }

    protected void load_chatlist(Context context){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("chatlist");
                    int length = jsonArray.length();

                    for (int i = 0; i < length; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int coupon_id = Integer.parseInt(object.getString("coupon_id"));
                        String buyer_id = object.getString("buyer_id");
                        String buyer_nicname = object.getString("buyer_nicname");
                        String seller_id = object.getString("seller_id");
                        String seller_nicname = object.getString("seller_nicname");
                        if (!buyer_nicname.equals(SaveSharedPreference.getNicname(context))) {
                            // 판매자일때 챗상대방
                            roomnum = Integer.toString(coupon_id)+SaveSharedPreference.getId(context)+buyer_id;
                            destid = buyer_id;
                            double buyer_score = Double.parseDouble(object.getString("buyer_score"));
                            PeopleItems.add(new PeopleItem(R.drawable.person, buyer_nicname, "별점" + buyer_score + "점",buyer_id,coupon_id+"",roomnum));

                        } else {
                            // 구매자일때 챗상대방
                            roomnum = Integer.toString(coupon_id)+seller_id+SaveSharedPreference.getId(context);
                            destid = seller_id;
                            double seller_score = Double.parseDouble(object.getString("seller_score"));
                            PeopleItems.add(new PeopleItem(R.drawable.person, seller_nicname, "별점" + seller_score + "점",seller_id,coupon_id+"",roomnum));
                        }

                        //ChatData chatData = new ChatData();
                        //chatData.users.put(String.valueOf(SaveSharedPreference.getId(context)),true);
                        //chatData.users.put(destid,true);
                        //chatData.users.put(coupon_id+"",true);
                        //firebaseDatabase.getReference().child("chatrooms").child(roomnum).setValue(chatData); //(coupon_id + sellerid + destid)
                    }
                    System.out.println(PeopleItems);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //for(int i=1;i<2;i++){
                //    PeopleItems.add(new PeopleItem(R.drawable.person,i+"번","별점 "+i+"점","","","-1"));
                //}

                mChatPeopleAdapter.setChatPeopleList(PeopleItems);

                /* initiate recyclerview */
                mRecyclerView.setAdapter(mChatPeopleAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
            }
        };
        ChatRequest chatRequest = new ChatRequest("chatlist", SaveSharedPreference.getId(context),responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(chatRequest);
    }
}
