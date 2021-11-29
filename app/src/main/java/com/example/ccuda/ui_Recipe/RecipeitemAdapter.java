package com.example.ccuda.ui_Recipe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.RecipeItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.ui_Chat.ChatRoomActivity;

import org.json.JSONObject;

import java.util.ArrayList;
class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.ItemViewHolder> {
    private ArrayList<RegiItemsModel> mList;
    private int position;
    public RecipeItemAdapter(ArrayList<RegiItemsModel> list){
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
            //this.image = (ImageView) itemView.findViewById(R.id.imageView);
            this.image = (ImageView) itemView.findViewById(R.id.image_view);
        }

        public void onBind(RegiItemsModel rg){
            name.setText(rg.getItemname());
            convname.setText(rg.getConvName());
            Glide.with(itemView).load(rg.getImageurl()).into(image);
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
                final RegiItemsModel regiItemsModel = mList.get(position);
                setPosition(position);
                showalert(v,regiItemsModel);
            }
        });
    }

    public void showalert(View v, RegiItemsModel regiItemsModel){
        android.app.AlertDialog.Builder msgBuilder = new android.app.AlertDialog.Builder(v.getContext())
                .setTitle("장바구니")
                .setMessage("장바구니에 담으시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                        Response.Listener<String> responsListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        // 성공
                                        Log.d("success","query success");
                                        Toast.makeText(v.getContext(),"장바구니 추가", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        // 실패
                                        Log.d("success","already in cart");
                                        Toast.makeText(v.getContext(),"같은 상품이 이미 장바구니에 있습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        CartRequest cartRequest = new CartRequest("addTocart", SaveSharedPreference.getId(v.getContext()), regiItemsModel.getItemid(), regiItemsModel.getConvName(), responsListener);
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                        queue.add(cartRequest);
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
