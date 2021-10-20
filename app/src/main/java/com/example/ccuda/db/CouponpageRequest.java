package com.example.myapplication.db;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class CouponpageRequest extends StringRequest {
    final static private String URL = String.format("%s/couponpage.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public CouponpageRequest(String option, String coupon_id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("option",option);
        parameters.put("coupon_id",coupon_id);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
