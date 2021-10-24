package com.example.ccuda;

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
import com.example.ccuda.db.PostRequest;
import com.example.ccuda.data.UserData;

import org.json.JSONObject;

public class CupponFragment extends Fragment {
    private Context context;
    UserData userData;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        context = container.getContext();
        userData = UserData.getInstance();
        return inflater.inflate(R.layout.cuppon, container, false);
    }

    // 판매 쿠폰 포스팅 db 저장
    protected void posting(int item_id, int price, String expiration_date,
                           String content, String coupon_image){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success=="success"){
                        // 포스팅 성공
                        Log.d("success","posting success");
                        Toast.makeText(context,"판매글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(success == "NoItem"){
                            // 해당 물품에 대한 편의점 데이터 부존재
                            Log.d("success","postring fail: there no item information in DB");
                        }
                        else if(success == "fail")
                            Log.d("success", "query fail");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PostRequest postRequest = new PostRequest("posting", userData.getUserid(), item_id, price, expiration_date, content, coupon_image, 0, responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    // 게시글 삭제 db 저장
    // 작성자: seller_id, 삭제할 쿠폰: coupon_id
    protected void deletepost(long seller_id, int coupon_id){
        // 작성자: seller_id, 삭제할 쿠폰: coupon_id
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success=="success"){
                        // 포스팅 성공
                        Log.d("success","delete success");
                        Toast.makeText(context,"해당 판매글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PostRequest postRequest = new PostRequest("deletepost", seller_id, 0, 0, "", "", "", coupon_id, responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
