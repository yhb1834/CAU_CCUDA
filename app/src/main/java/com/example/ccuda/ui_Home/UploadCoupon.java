// https://pyony.com/search/?page=50&event_type=&category=&item=100&sort=&q=
// https://wonyong-jang.github.io/java/2020/07/01/Java-Selenium.html
// https://todaycode.tistory.com/5
// https://gdtbgl93.tistory.com/154
// https://devjun.tistory.com/226?category=999558
// http://daplus.net/java-android-os-networkonmainthreadexception를-수정하는-방법/
// https://mizzo-dev.tistory.com/entry/Mac-OS-환경에서-Selenium-Driver-Path-설정하기

package com.example.ccuda.ui_Home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.LoginRequest;
import com.example.ccuda.db.SaveItemRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

public class UploadCoupon extends Fragment {
    FragmentTransaction transaction;

    //private WebDriver driver=new ChromeDriver();
    private String URL="https://pyony.com/search/";
    public static final String WEB_DRIVER_PATH="/Users/iseung-yeon/Desktop/chromedriver";
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "add button listener", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    ArrayList<String> cuReturn=getProductList("cu","https://pyony.com/brands/cu/");
                    saveProductList(getActivity().getApplicationContext(),cuReturn,"cu");
                    ArrayList<String> gsReturn=getProductList("gs25","https://pyony.com/brands/gs25/");
                    saveProductList(getActivity().getApplicationContext(),gsReturn,"gs25");
                    ArrayList<String> sevenReturn=getProductList("seven","https://pyony.com/brands/seven/");
                    saveProductList(getActivity().getApplicationContext(),sevenReturn,"seven");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_uploadcoupon, container, false);

        return v;
    }

    public ArrayList<String> getProductList(String convName, String url) throws IOException {
        Document doc=Jsoup.connect(url).get();
        Elements link=doc.select("a.page-link");
        ArrayList<String> result=new ArrayList<String>();

        String lastSize=link.get(link.size()-1).attr("href").replace("?page=","");
        int lastPage=Integer.parseInt(lastSize);
        for(int i=1;i<=lastPage;i++){
            String URL=url+"?page="+Integer.toString(i);
            Document docPage=Jsoup.connect(URL).get();
            Elements elements=docPage.select("div.card-body.px-2.py-2");
            for(Element element:elements){
                String a=element.select("strong").text()+" "+element.select("span.badge.bg-"+convName).text()+" "+element.select("span.text-muted").text();
                //System.out.println(a);
                result.add(a);
            }
        }

        return result;
    }

    // 크롤링한 데이터 arraylist를 JSONarray로 변환 후 저장
    public void saveProductList(Context context, ArrayList<String> returnlist, String storename){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            for(int i=0; i< returnlist.size(); i++){
                jsonArray.put(returnlist.get(i));
            }
            jsonObject.put("store", storename);
            jsonObject.put("item", jsonArray);

            String json = jsonObject.toString();
            Response.Listener<String> responsListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String update_date = jsonObject.getString("date");
                        Log.d("update",update_date);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            SaveItemRequest saveItemRequest = new SaveItemRequest(json,responsListener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(saveItemRequest);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
