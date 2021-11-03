package com.example.ccuda.db;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class RecoverRequest extends StringRequest {
    final static private String URL = String.format("%s/recover_userinfo.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public RecoverRequest(String option, String email, String name, String recoverinfo, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("name", name);
        parameters.put("email", email);
        parameters.put("recoverinfo", recoverinfo);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
