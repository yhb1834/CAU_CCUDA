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
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.db.ChatRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private ChatPeopleAdapter mChatPeopleAdapter;
    private ArrayList<PeopleItem> PeopleItems;
    Context context;

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
                        String buyer_nicname = object.getString("buyer_nicname");
                        String seller_nicname = object.getString("seller_nicname");
                        if (!buyer_nicname.equals(SaveSharedPreference.getNicname(context))) {
                            // 판매자일때 챗상대방
                            double buyer_score = Double.parseDouble(object.getString("buyer_score"));
                            //PeopleItems.add(new PeopleItem(R.drawable.person, buyer_nicname, "별점" + buyer_score + "점"));
                        } else {
                            // 구매자일때 챗상대방
                            double seller_score = Double.parseDouble(object.getString("seller_score"));
                           // PeopleItems.add(new PeopleItem(R.drawable.person, seller_nicname, "별점" + seller_score + "점"));
                        }
                    }
                    System.out.println(PeopleItems);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for(int i=1;i<=10;i++){
                    PeopleItems.add(new PeopleItem(R.drawable.person,i+"번","별점 "+i+"점"));
                }
                PeopleItems.add(new PeopleItem(R.drawable.person,"11번","별점 12점"));

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