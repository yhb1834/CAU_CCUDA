package com.example.ccuda.db;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class CartRequest extends StringRequest {
    final static private String URL = String.format("%s/cartpage.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public CartRequest(String option, long user_id, int item_id,String storename, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("user_id", user_id+"");
        parameters.put("item_id", item_id+"");
        parameters.put("storename", storename);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
