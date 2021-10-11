package com.example.ccuda;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = String.format("%s/login.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public LoginRequest(long id, String email, String name, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("id", id+"");
        parameters.put("email", email);
        parameters.put("name", name);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}