// 회원가입 값 요청
package com.example.ccuda.db;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class SignupRequest extends StringRequest {
    final static private String URL = String.format("%s/signup.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public SignupRequest(String UserEmail, String UserPwd, String PwdConfirmed,String UserName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("UserEmail", UserEmail);
        parameters.put("UserPwd", UserPwd);
        parameters.put("PwdConfirmed", PwdConfirmed);
        parameters.put("UserName", UserName);
    }
    public Map<String,String> getParams(){
        return parameters;
    }
}
