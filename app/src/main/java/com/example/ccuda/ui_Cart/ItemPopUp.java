package com.example.ccuda.ui_Cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;

public class ItemPopUp extends Activity {
    ImageView popupImage;
    TextView name;
    TextView conv;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cart_item_popup);
        Intent intent=getIntent();
        popupImage=findViewById(R.id.popup_image);
        name=findViewById(R.id.popup_prod);
        conv=findViewById(R.id.popup_conv);
        String prodImage=intent.getStringExtra("prodImage");
        String prodName=intent.getStringExtra("prodName");
        String prodConv=intent.getStringExtra("prodConv");

        Glide.with(this).load(prodImage).into(popupImage);
        name.setText(prodName);
        conv.setText(prodConv);
        //popupImage.setImage
    }

    public void onClickCancel(View v){

        finish(); // 팝업 닫기
    }

    public void onClickDelete(View v){
        finish(); // 팝업 닫기
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
