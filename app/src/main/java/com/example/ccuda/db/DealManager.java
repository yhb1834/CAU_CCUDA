package com.example.ccuda.db;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class DealManager extends StringRequest {
    final static private String URL = String.format("%s/dealmanager.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public DealManager(String option,String seller_id, String buyer_id, String coupon_id, String star, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("seller_id", seller_id);
        parameters.put("buyer_id", buyer_id);
        parameters.put("coupon_id", coupon_id);
        parameters.put("star", star);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
