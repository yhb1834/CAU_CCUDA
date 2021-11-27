package com.example.ccuda.ui_Recipe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccuda.R;

import java.util.ArrayList;

public class RegiitemsAdapter extends RecyclerView.Adapter<RegiitemsAdapter.ItemViewHolder> {
    private ArrayList<RegiItemsModel> mList;
    private int position;
    public RegiitemsAdapter(ArrayList<RegiItemsModel> list){
        this.mList = list;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected TextView convname;
        protected ImageView image;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.text_view1);
            this.convname = itemView.findViewById(R.id.text_view2);
            this.image = itemView.findViewById(R.id.imageView);
        }

        public void onBind(RegiItemsModel rg){
            name.setText(rg.getItemname());
            convname.setText(rg.getConvName());
            System.out.println(rg.getImageurl());
            //image.setImageResource(rg.getImageurl());
        }
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);

        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(mList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPosition(position);
                showalert(v);
            }
        });
    }

    public void showalert(View v){
        android.app.AlertDialog.Builder msgBuilder = new android.app.AlertDialog.Builder(v.getContext())
                .setTitle("취소")
                .setMessage("취소하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        mList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mList.size());
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(, "안 끔", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    public int getPosition(int position){
        return position;
    }
    public void setPosition(int position){
        this.position = position;
    }

    public void removeItem(int position){
        //itemList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    public ArrayList<RegiItemsModel> getmList(){
        return mList;
    }

    public void setmList(ArrayList<RegiItemsModel> listData) {
        this.mList = listData;
    }
}
