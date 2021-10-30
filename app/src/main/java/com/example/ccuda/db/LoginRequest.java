package com.example.ccuda.db;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = String.format("%s/login.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public LoginRequest(String option, long id, String email, String pwd, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("id", id+"");
        parameters.put("email", email);
        parameters.put("pwd", pwd);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}