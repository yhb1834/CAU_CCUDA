package com.example.ccuda.ui_Cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CartRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        context = getContext();
        return inflater.inflate(R.layout.fragment1_cart, container, false);
    }



    // 장바구니 삭제 정보 db저장
    protected void removeFromcart(int item_id, String storename){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    // 성공
                    Toast.makeText(context,"장바구니에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CartRequest cartRequest = new CartRequest("removeFromcart", SaveSharedPreference.getId(context), item_id,storename, responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartRequest);
    }

    public void click_cart_item(Context context,CartItemModel item){
        Intent intent=new Intent(context,ItemPopUp.class);
        intent.putExtra("prodImage", item.getImageUrl());
        intent.putExtra("prodName", item.getText1());
        intent.putExtra("prodConv", item.getText2());
        intent.putExtra("prodId", item.getItemid());
        intent.putExtra("clickedWhere","MAIN_CART");
        //startActivity(intent);
        startForResult_main.launch(intent);
    }
    public void click_cart_item_in_all(Context context,CartItemModel item){
        Intent intent=new Intent(context,ItemPopUp.class);
        intent.putExtra("prodImage", item.getImageUrl());
        intent.putExtra("prodName", item.getText1());
        intent.putExtra("prodConv", item.getText2());
        intent.putExtra("prodId", item.getItemid());
        intent.putExtra("clickedWhere","ALL_CART");
        //startActivity(intent);
        startForResult_cart.launch(intent);
    }

    ActivityResultLauncher<Intent> startForResult_main=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==getActivity().RESULT_OK){
                }
            }
    );
    ActivityResultLauncher<Intent> startForResult_cart=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==getActivity().RESULT_OK){
                }
            }
    );

}
