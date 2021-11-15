package com.example.ccuda.ui_Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.ChatData;
import com.example.ccuda.data.SaveSharedPreference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    ArrayList<ChatData.Comment> chatData;
    LayoutInflater layoutInflater;

    public ChatAdapter(ArrayList<ChatData.Comment> chatData, LayoutInflater layoutInflater) {
        this.chatData = chatData;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return chatData.size();
    }

    @Override
    public Object getItem(int position) {
        return chatData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //현재 보여줄 번째의(position)의 데이터로 뷰를 생성
        ChatData.Comment Data = chatData.get(position);

        //재활용할 뷰는 사용하지 않음!!
        View itemView=null;

        //메세지가 내 메세지인지??
        if(Data.nicname.equals(SaveSharedPreference.getNicname(viewGroup.getContext()))){
            itemView= layoutInflater.inflate(R.layout.my_msg_box,viewGroup,false);
        }else{
            itemView= layoutInflater.inflate(R.layout.other_msg_box,viewGroup,false);
        }

        //만들어진 itemView에 값들 설정
        CircleImageView iv= itemView.findViewById(R.id.iv);
        TextView tvName= itemView.findViewById(R.id.tv_name);
        TextView tvMsg= itemView.findViewById(R.id.tv_msg);
        TextView tvTime= itemView.findViewById(R.id.tv_time);

        tvName.setText(Data.nicname);
        tvMsg.setText(Data.msg);
        tvTime.setText(Data.timestamp);

        //if(Data.imageurl !=null && Data.imageurl.equals("")==false){
        //    Glide.with(itemView).load(Data.imageurl).into(iv);
        //}

        return itemView;
    }
}