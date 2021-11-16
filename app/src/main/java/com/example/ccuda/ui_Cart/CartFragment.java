package com.example.ccuda.ui_Cart;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private ArrayList<ItemData> iArrayList = new ArrayList<>();
    private ArrayList<ItemData> mycartArrayList = new ArrayList<>();
    private Context context;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        context = getContext();
        return inflater.inflate(R.layout.fragment1_cart, container, false);
    }


    // 장바구니 리스트 불러오기
    protected void mycartlist(ArrayList<ItemData> iarrayList){
        mycartArrayList.clear();
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("itemlist");
                    int length = jsonArray.length();

                    for(int i=0; i<length; i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        int item_id = Integer.parseInt(object.getString("item_id"));

                        for(int j=0; i<iarrayList.size(); j++){
                            if(iarrayList.get(j).getItemid() == item_id){
                                mycartArrayList.add(iarrayList.get(j));
                                return;
                            }
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CartRequest cartRequest = new CartRequest("mycartlist", SaveSharedPreference.getId(context), 0,"",responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartRequest);
    }

    // 장바구니 삭제 정보 db저장
    protected void removeFromcart(int item_id){
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
        CartRequest cartRequest = new CartRequest("removeFromcart", SaveSharedPreference.getId(context), item_id,"", responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartRequest);
    }
}
