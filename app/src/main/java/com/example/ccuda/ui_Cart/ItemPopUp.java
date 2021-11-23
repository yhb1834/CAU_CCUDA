package com.example.ccuda.ui_Cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.CartRequest;

public class ItemPopUp extends Activity {
    ImageView popupImage;
    TextView name;
    TextView conv;
    Button cancelButton;
    Button deleteButton;
    boolean isClicked=false;

    String prodImage;
    String prodName;
    String prodConv;
    int prodId;
    String clickedWhere;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cart_item_popup);
        Intent intent=getIntent();
        popupImage=findViewById(R.id.popup_image);
        name=findViewById(R.id.popup_prod);
        conv=findViewById(R.id.popup_conv);
        cancelButton=findViewById(R.id.popup_cancel_button);
        deleteButton=findViewById(R.id.popup_delete_button);
        prodImage=intent.getStringExtra("prodImage");
        prodName=intent.getStringExtra("prodName");
        prodConv=intent.getStringExtra("prodConv");
        prodId=intent.getIntExtra("prodId",-1);
        clickedWhere=intent.getStringExtra("clickedWhere");

        Glide.with(this).load(prodImage).into(popupImage);
        name.setText(prodName);
        conv.setText(prodConv);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromcart(prodId, prodConv);
                Intent intent=new Intent();
                if (clickedWhere=="MAIN_CART"){
                    intent=new Intent(getApplicationContext(), addToCart.class);
                    intent.putExtra("result", 0);
                }
                if(clickedWhere=="ALL_CART"){
                    intent=new Intent(getApplicationContext(), AllCartFragment.class);
                    intent.putExtra("result",1);

                }
                setResult(RESULT_OK, intent);
                try{
                    startActivity(intent);
                }catch (Exception e){

                }

                finish(); // 팝업 닫기

            }
        });

    }

    public void onClickCancel(View v){

        finish(); // 팝업 닫기
    }

    /*
    public void onClickDelete(View v){
        //CartFragment fragment=new CartFragment();
        //fragment.removeFromcart(prodId, prodConv);
        //finish(); // 팝업 닫기
    }
    */


    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    protected void removeFromcart(int item_id, String storename){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    // 성공
                    Toast.makeText(getApplicationContext(),"장바구니에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CartRequest cartRequest = new CartRequest("removeFromcart", SaveSharedPreference.getId(getApplicationContext()), item_id,storename, responsListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(cartRequest);
    }
}
