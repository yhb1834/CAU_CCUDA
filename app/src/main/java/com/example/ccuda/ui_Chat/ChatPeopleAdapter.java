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

public class ChatPeopleAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<PeopleItem> PeopleItems;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment1_chat_peoplelist, parent, false);
        return new ViewHolder(view, onItemClickEventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    public interface OnItemClickEventListener {
        void onItemClick(View view, int position);
    }


    private OnItemClickEventListener onItemClickEventListener;

    public void setOnItemClickListener(OnItemClickEventListener listener) {
        onItemClickEventListener = listener;
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    ImageView profile;
    TextView name;
    TextView star;

    public ViewHolder(@NonNull View itemView, final ChatPeopleAdapter.OnItemClickEventListener itemClickListener) {
        super(itemView);

        profile = (ImageView) itemView.findViewById(R.id.profile);
        name = (TextView) itemView.findViewById(R.id.name);
        star = (TextView) itemView.findViewById(R.id.star);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(a_view, position);
                }
            }
        });
    }

    void onBind(PeopleItem item){
        profile.setImageResource((item.getResourceId()));
        name.setText(item.getNicname());
        star.setText(item.getStar());
    }
}