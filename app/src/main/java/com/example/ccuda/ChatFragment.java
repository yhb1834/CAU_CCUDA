package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccuda.data.PeopleItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private ChatPeopleAdapter mChatPeopleAdapter;
    private ArrayList<PeopleItem> PeopleItems;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment1_chat, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        /* initiate adapter */
        mChatPeopleAdapter = new ChatPeopleAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mChatPeopleAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));

        /* adapt data */
        PeopleItems = new ArrayList<PeopleItem>();
        for(int i=1;i<=10;i++){
            PeopleItems.add(new PeopleItem(R.drawable.person,i+"번","별점 "+i+"점"));
        }
        mChatPeopleAdapter.setChatPeopleList(PeopleItems);

        return view;
    }


}
