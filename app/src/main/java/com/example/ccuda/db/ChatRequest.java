package com.example.ccuda.db;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class ChatRequest extends StringRequest {
    final static private String URL = String.format("%s/chatpage.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public ChatRequest(String option, long user_id, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("user_id", user_id+"");
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}