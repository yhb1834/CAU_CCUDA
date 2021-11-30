package com.example.ccuda.db;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class PostRequest extends StringRequest {
    final static private String URL = String.format("%s/posting.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public PostRequest(String option, long seller_id, String storename,String category,int item_id, int price, String expiration_date,
                       String content, String coupon_image, String filename,int coupon_id, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("seller_id", seller_id+"");
        parameters.put("storename", storename);
        parameters.put("category", category);
        parameters.put("item_id", item_id+"");
        parameters.put("price", price+"");
        parameters.put("expiration_date", expiration_date);
        parameters.put("content", content);
        parameters.put("coupon_image", coupon_image);
        parameters.put("filename",filename);
        parameters.put("coupon_id", coupon_id+"");
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
