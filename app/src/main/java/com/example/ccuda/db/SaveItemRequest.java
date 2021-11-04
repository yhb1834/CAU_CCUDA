package com.example.ccuda.db;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SaveItemRequest extends StringRequest {
    final static private String URL = String.format("%s/savedata.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public SaveItemRequest(String content, Response.Listener<String> listener){
        super(Method.POST, URL, listener,null);
        parameters.put("content", content);
        parameters = new HashMap<>();
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
