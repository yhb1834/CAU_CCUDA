// https://pyony.com/search/?page=50&event_type=&category=&item=100&sort=&q=
// https://wonyong-jang.github.io/java/2020/07/01/Java-Selenium.html
// https://todaycode.tistory.com/5
// https://gdtbgl93.tistory.com/154
// https://devjun.tistory.com/226?category=999558
// http://daplus.net/java-android-os-networkonmainthreadexception를-수정하는-방법/
// https://mizzo-dev.tistory.com/entry/Mac-OS-환경에서-Selenium-Driver-Path-설정하기
// https://kumgo1d.tistory.com/5
// https://developer.android.com/training/basics/intents/result?hl=ko -> 사진 업로드 API 써야할듯
package com.example.ccuda.ui_Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.db.SaveJsoupRequest;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class UploadCoupon extends Fragment {
    FragmentTransaction transaction;

    //private WebDriver driver=new ChromeDriver();
    private String URL="https://pyony.com/search/";
    public static final String WEB_DRIVER_PATH="/Users/iseung-yeon/Desktop/chromedriver";
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    TextView convName; // 편의점명
    TextView prod;
    TextView convInfo;
    public ArrayList<String> cuReturn=new ArrayList<String>();
    public ArrayList<String> gsReturn=new ArrayList<String>();
    public ArrayList<String> sevenReturn=new ArrayList<String>();
    public static ArrayList<String> test=new ArrayList<String>();

    String[] conv={"GS25", "SEVEN11", "CU"};
    ImageView uploadPhoto;
    Uri mImageCaptureUri;

    SearchableSpinner searchView;
    String[] hhhhh;
    RecyclerView recyclerView;
    EditText editText;



    private static final int PICK_FROM_ALBUM=1;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                }
            });


    ActivityResultLauncher<Intent> launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri data=result.getData().getData();
                        uploadPhoto.setImageURI(data);
                        System.out.println(data);
                    }
                }
            });



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    cuReturn=getProductList("cu","https://pyony.com/brands/cu/");
                    //saveProductList(getActivity().getApplicationContext(),cuReturn,"cu");
                    gsReturn=getProductList("gs25","https://pyony.com/brands/gs25/");
                    //saveProductList(getActivity().getApplicationContext(),gsReturn,"gs25");
                    sevenReturn=getProductList("seven","https://pyony.com/brands/seven/");
                    //saveProductList(getActivity().getApplicationContext(),sevenReturn,"seven");
                    Thread.sleep(1000);
                    //System.out.println("cuReturn: "+cuReturn);
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


        convName=v.findViewById(R.id.conv_name);
        Spinner spinner1=v.findViewById(R.id.spinner1);
        editText=v.findViewById(R.id.prodPrice);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, conv);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), conv[position], Toast.LENGTH_SHORT).show();
                String convN="gs25";
                String URL="https://pyony.com/brands/gs25/";
                switch (position){
                    case 0: // gs
                        convN="gs25";
                        URL="https://pyony.com/brands/gs25/";
                        break;
                    case 1: // seven
                        convN="seven";
                        URL="https://pyony.com/brands/seven/";
                        break;
                    case 2: // cu
                        convN="cu";
                        URL="https://pyony.com/brands/cu/";
                        break;
                }
                runThread(convN,URL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchView=v.findViewById(R.id.searchProd);


        uploadPhoto=(ImageView) v.findViewById(R.id.upload_photo);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener cameraListener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhoto();
                    }
                };

                DialogInterface.OnClickListener albumListener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbum();

                    }
                };

                DialogInterface.OnClickListener cancelListener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(getContext())
                        .setTitle("업로드 할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });



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


    public ArrayList<String> getProductName(String convName, String url) throws IOException {
        Document doc=Jsoup.connect(url).get();
        Elements link=doc.select("a.page-link");
        ArrayList<String> result=new ArrayList<String>();
        Elements elements=new Elements();

        String lastSize=link.get(link.size()-1).attr("href").replace("?page=","");
        int lastPage=Integer.parseInt(lastSize);
        for(int i=1;i<=lastPage;i++){
            String URL=url+"?page="+Integer.toString(i);
            Document docPage=Jsoup.connect(URL).get();
            elements=docPage.select("div.card-body.px-2.py-2 strong"); //div.card-body strong
            for(Element element:elements){
                result.add(element.text());
            }
        }

        return result;
    }

    public ArrayList<String> getProductPrice(String convName, String url) throws IOException {
        Document doc=Jsoup.connect(url).get();
        Elements link=doc.select("a.page-link");
        ArrayList<String> result=new ArrayList<String>();

        String lastSize=link.get(link.size()-1).attr("href").replace("?page=","");
        int lastPage=Integer.parseInt(lastSize);
        for(int i=1;i<=lastPage;i++){
            String URL=url+"?page="+Integer.toString(i);
            Document docPage=Jsoup.connect(URL).get();
            Elements elements=docPage.select("div.card-body.px-2.py-2 span.text-muted");
            for(Element element:elements){
                result.add(element.text());
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
                        Log.d("success", "volley success");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            SaveJsoupRequest saveJsoupRequest = new SaveJsoupRequest(json,responsListener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(saveJsoupRequest);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void doTakePhoto(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url="tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";

        //mImageCaptureUri= FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID+".fileprovider", new File(getContext().getFilesDir(),url));

//        mImageCaptureUri= FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID+".fileprovider", new File(getContext().getFilesDir(),url));


        //intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
        startActivity(intent);
    }

    public void doTakeAlbum(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        mGetContent.launch("image/*");

        launcher.launch(intent);


    }

    public void runThread(String convName, String URL){
        new Thread(new Runnable() {
            ArrayList<String> prodList;
            ArrayList<String> prodPrice;
            Handler mHandler=new Handler();
            Elements elements=new Elements();
            @Override
            public void run() {
                try {
                    Response.Listener<String> responsListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("storeitemlist");
                                int length = jsonArray.length();

                                for(int i=0; i<length; i++){
                                    ItemData itemData = new ItemData();

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    prodList.add(object.getString("item_name"));
                                    prodPrice.add(object.getString("item_price2"));    //개당 가격
                                    //String image = BitmapConverter.StringToBitmap(object.getString("item_image");


                                    //prodList = getProductName(convName, URL); //getProductList(convName, URL);
                                    //prodPrice = getProductPrice(convName, URL);
                                    Thread.sleep(1000);

                                    //elements=getProductName(convName,URL);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    CartRequest cartRequest = new CartRequest("storeitem",0, 0,convName,responsListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(cartRequest);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> arrayList=new ArrayAdapter<>(
                                    getContext(), android.R.layout.simple_spinner_dropdown_item, prodList
                            );

                            searchView.setAdapter(arrayList);

                            searchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position==0){
                                        Toast.makeText(getContext(), prodList.get(position), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String sNumber=parent.getItemAtPosition(position).toString();
                                        Toast.makeText(getContext(), sNumber,Toast.LENGTH_SHORT);
                                    }
                                    String limitPrice=prodPrice.get(position);
                                    editText.setHint(limitPrice+"보다 적게 입력해주세요.");

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });



                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}