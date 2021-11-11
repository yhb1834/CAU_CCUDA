package com.example.ccuda.ui_Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccuda.R;
import com.example.ccuda.data.PeopleItem;

import java.util.ArrayList;

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
        View view = (View) inflater.inflate(R.layout.fragment1_chat, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        /* initiate adapter */
        mChatPeopleAdapter = new ChatPeopleAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mChatPeopleAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));

        /* adapt data */
        PeopleItems = new ArrayList<>();
        for(int i=1;i<=10;i++){
            PeopleItems.add(new PeopleItem(R.drawable.person,i+"번","별점 "+i+"점"));
        }
        PeopleItems.add(new PeopleItem(R.drawable.person,"11번","별점 12점"));
        mChatPeopleAdapter.setChatPeopleList(PeopleItems);
        return view;
    }
}
