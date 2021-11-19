package com.example.ccuda.ui_Cart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;

public class ItemView extends LinearLayout {
    ImageView imageView;
    public ItemView(Context context){
        super(context);
        init(context);
    }
    public ItemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.gridview_item,this,true);
        imageView=findViewById(R.id.grid_view_item);
    }
    public void setImageView(String img){
        Glide.with(this).load(img).into(imageView);
    }
}
