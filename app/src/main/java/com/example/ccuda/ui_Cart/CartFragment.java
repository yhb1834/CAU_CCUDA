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

    // 편의점 아이템 db 불러오기
    protected void load_itemlist(){
        iArrayList.clear();
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("itemlist");
                    int length = jsonArray.length();

                    for(int i=0; i<length; i++){
                        ItemData itemData = new ItemData();

                        JSONObject object = jsonArray.getJSONObject(i);

                        itemData.setItemid(Integer.parseInt(object.getString("item_id")));
                        itemData.setItemname(object.getString("item_name"));
                        itemData.setCategory(object.getString("category"));
                        itemData.setPlustype(object.getString("plustype"));
                        itemData.setStorename(object.getString("storename"));
                        itemData.setItemprice(Integer.parseInt(object.getString("item_price")));    // 원가
                        itemData.setItemprice2(Integer.parseInt(object.getString("item_price2")));    // 개당 가격
                        itemData.setImage(BitmapConverter.StringToBitmap(object.getString("item_image")));

                        iArrayList.add(itemData);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CartRequest cartRequest = new CartRequest("itemlist",0, 0,"",responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartRequest);
    }

    // 장바구니 담기 db 저장
    protected void addTocart(int item_id){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        // 성공
                        Log.d("success","query success");
                        Toast.makeText(context,"해당 상품이 장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // 실패
                        Log.d("success","already in cart");
                        Toast.makeText(context,"같은 상품이 이미 장바구니에 있습니다.", Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CartRequest cartRequest = new CartRequest("addTocart", SaveSharedPreference.getId(context), item_id,"", responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartRequest);
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
