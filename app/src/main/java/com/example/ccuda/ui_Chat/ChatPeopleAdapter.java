package com.example.ccuda.ui_Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccuda.R;
import com.example.ccuda.data.PeopleItem;

import java.util.ArrayList;

public class ChatPeopleAdapter extends RecyclerView.Adapter<ChatPeopleAdapter.ViewHolder> {

    private ArrayList<PeopleItem> PeopleItems;

    @NonNull
    @Override
    public ChatPeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_peoplelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatPeopleAdapter.ViewHolder holder, int position) {
        holder.onBind(PeopleItems.get(position));
    }


    public void setChatPeopleList(ArrayList<PeopleItem> list){
        this.PeopleItems = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return PeopleItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;
        TextView star;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            star = (TextView) itemView.findViewById(R.id.star);
        }

        void onBind(PeopleItem item){
            profile.setImageResource((item.getResourceId()));
            name.setText(item.getNicname());
            star.setText(item.getStar());
        }
    }
}