package com.example.ccuda.db;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ccuda.Config;

import java.util.HashMap;
import java.util.Map;

public class ReportRequest extends StringRequest {
    final static private String URL = String.format("%s/report.php", Config.SERVER_URL);
    private Map<String, String> parameters;

    public ReportRequest(String option,String reporter_id, String cheater_name, String content, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("reporter_id", reporter_id);
        parameters.put("cheater_name", cheater_name);
        parameters.put("content", content);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
