package com.example.ccuda.db;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class SaveJsoupRequest extends StringRequest {
    final static private String URL = String.format("%s/savedata.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public SaveJsoupRequest(String content, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("content", content);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
