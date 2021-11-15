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
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import android.widget.Button;
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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.ChatData;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.db.BitmapConverter;
import com.example.ccuda.db.CartRequest;
import com.example.ccuda.db.PostRequest;
import com.example.ccuda.db.SaveJsoupRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class UploadCoupon extends Fragment {
    FragmentTransaction transaction;

    private String URL="https://pyony.com/search/";
    public static final String WEB_DRIVER_PATH="/Users/iseung-yeon/Desktop/chromedriver";
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    TextView convName; // 편의점명
    public ArrayList<String> cuReturn=new ArrayList<String>();
    public ArrayList<String> gsReturn=new ArrayList<String>();
    public ArrayList<String> sevenReturn=new ArrayList<String>();
    public static ArrayList<String> test=new ArrayList<String>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference itemRef;
    ArrayList<ItemData> cuItem = new ArrayList<>();
    ArrayList<ItemData> gs25Item = new ArrayList<>();
    ArrayList<ItemData> sevenItem = new ArrayList<>();
    String[] conv={"GS25", "SEVEN11", "CU"};
    ImageView uploadPhoto;
    Uri mImageCaptureUri;
    String limitPrice;

    SearchableSpinner searchView;
    EditText editText;
    EditText editText2;
    Button uploadButton;

    Bitmap finalImage; // 내가 올릴 품목 사진
    String finalConv; // 파는 품목이 어떤 편의점 물건인지
    String finalProduct; // 내가 올릴 품목 이름
    String finalPrice; // 내가 올린 판매 금액
    String finalDate;


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
                    //saveProductList(cuReturn,"cu");
                    gsReturn=getProductList("gs25","https://pyony.com/brands/gs25/");
                    //saveProductList(gsReturn,"gs25");
                    sevenReturn=getProductList("seven","https://pyony.com/brands/seven/");
                    //saveProductList(sevenReturn,"seven");
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
        editText2=v.findViewById(R.id.proddate);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, conv);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), conv[position], Toast.LENGTH_SHORT).show();
                String convN="gs25";
                String URL="https://pyony.com/brands/gs25/";
                //load_item();
                //runThread(convN,URL);
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

        load_item();



        uploadButton=v.findViewById(R.id.uploadbutton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalConv= (String) spinner1.getSelectedItem();
                finalProduct= (String) searchView.getSelectedItem();
                finalPrice=editText.getText().toString();
                finalDate=editText2.getText().toString();

                //if (finalImage==null){
                //    Toast.makeText(getContext(),"사진을 업로드해주세요.", Toast.LENGTH_SHORT).show();
                //}
                if(finalConv=="" || finalConv==null){
                    Toast.makeText(getContext(),"편의점을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(finalProduct=="" || finalProduct==null){
                    Toast.makeText(getContext(),"품목을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(finalPrice.length()==0 || finalPrice=="" || finalPrice==null){
                    Toast.makeText(getContext(),"가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(finalPrice)>Integer.parseInt(limitPrice)){
                    Toast.makeText(getContext(), "가격은 최대 "+limitPrice+"원까지 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                else if(finalDate.length()==0 || finalDate=="" || finalDate==null){
                    Toast.makeText(getContext(),"유효기간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    BitmapDrawable drawable=(BitmapDrawable)uploadPhoto.getDrawable();
                    finalImage= drawable.getBitmap();

                    int item_id = getitem_id(finalConv,finalProduct);
                    Response.Listener<String> responsListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if(success){
                                    // 포스팅 성공
                                    Log.d("success","posting success");
                                    //Toast.makeText(context,"판매글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    System.out.println("picture: "+finalImage);
                                    Toast.makeText(getContext(), finalConv+" "+finalProduct+" "+finalPrice, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Log.d("success", "query fail");
                                }
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new HomeFragment()).commit();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    PostRequest postRequest = new PostRequest("posting", SaveSharedPreference.getId(getActivity()), finalConv,"",item_id, Integer.parseInt(finalPrice), finalDate, "", BitmapConverter.BitmapToString(finalImage), 0, responsListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(postRequest);
                }
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


    public ArrayList<String> getProductPicture(String convName, String url) throws IOException {
        Document doc=Jsoup.connect(url).get();
        Elements link=doc.select("a.page-link");
        ArrayList<String> result=new ArrayList<String>();

        String lastSize=link.get(link.size()-1).attr("href").replace("?page=","");
        int lastPage=Integer.parseInt(lastSize);
        for(int i=1;i<=lastPage;i++){
            String URL=url+"?page="+Integer.toString(i);
            Document docPage=Jsoup.connect(URL).get();
            Elements elements=docPage.select("div.card-body img");
            for(Element element:elements){
                String imageURL=element.getElementsByAttribute("src").attr("src");
                result.add(imageURL);
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
    public void saveProductList(ArrayList<String> returnlist, String storename){

        firebaseDatabase= FirebaseDatabase.getInstance();
        itemRef= firebaseDatabase.getReference().child("item").child(storename);

        for(int i=0; i< returnlist.size(); i++){
            ItemData itemData = new ItemData();
            itemData.setItemid(i);
            itemData.setItemname(returnlist.get(i).split(" ")[0]);
            itemData.setPlustype(returnlist.get(i).split(" ")[1]);
            itemData.setStorename(storename);
            int price2 = Integer.parseInt((returnlist.get(i).split(" ")[2]).replaceAll("[^0-9]",""));
            itemData.setItemprice2(price2);
            itemData.setItemprice(calculateprice(itemData.getPlustype(),price2));
            itemData.setImage("");
            itemData.setCategory("");

            itemRef.child(i+"").setValue(itemData);
        }

    }

    private int calculateprice(String plus,int price2){
        int price;
        String plustype0 = plus.substring(0,1);
        String plustype1 = plus.substring(2);


        if(!plustype0.equals("1")){
            price = (price2 * (Integer.parseInt(plustype0) + Integer.parseInt(plustype1))) / Integer.parseInt(plustype0);
            price = (price + 5) / 10 * 10;
        }
        else {
            if(plustype1.equals(1))
                price = price2 * 2;
            else
                price = price2;
        }
        return price;
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
            ArrayList<String> prodList=new ArrayList<>();
            ArrayList<String> prodPrice=new ArrayList<>();
            Handler mHandler=new Handler();
            Elements elements=new Elements();
            @Override
            public void run() {
                try {
                    if(convName == "cu"){
                        for(int i=0; i<cuItem.size(); i++){
                            prodList.add(cuItem.get(i).getItemname());
                            prodPrice.add(Integer.toString(cuItem.get(i).getItemprice2()));
                        }
                    }
                    else if(convName == "seven"){
                        for(int i=0; i<sevenItem.size(); i++){
                            prodList.add(sevenItem.get(i).getItemname());
                            prodPrice.add(Integer.toString(sevenItem.get(i).getItemprice2()));
                        }
                    }
                    if(convName == "gs25"){
                        for(int i=0; i<gs25Item.size(); i++){
                            prodList.add(gs25Item.get(i).getItemname());
                            prodPrice.add(Integer.toString(gs25Item.get(i).getItemprice2()));
                        }
                    }
                    //prodList = getProductName(convName, URL); //getProductList(convName, URL);
                    // prodPrice = getProductPrice(convName, URL);
                    Thread.sleep(1000);

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
                                    limitPrice=prodPrice.get(position);
                                    editText.setHint(limitPrice+"원 보다 적게 입력해주세요.");

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

    public void load_item(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        itemRef= firebaseDatabase.getReference();

        itemRef.child("item").child("cu").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                cuItem.add(itemData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        itemRef.child("item").child("gs25").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                gs25Item.add(itemData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        itemRef.child("item").child("seven").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                sevenItem.add(itemData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    protected int getitem_id(String storename,String itemname){
        int itemid=0;
        if(storename == "CU"){
            for(int i=0; i<cuItem.size(); i++){
                if(cuItem.get(i).getItemname() == itemname){
                    itemid = cuItem.get(i).getItemid();
                    break;
                }
            }
        }
        else if(storename == "GS25"){
            for(int i=0; i<gs25Item.size(); i++){
                if(gs25Item.get(i).getItemname() == itemname){
                    itemid = gs25Item.get(i).getItemid();
                    break;
                }
            }
        }
        else{
            for(int i=0; i<sevenItem.size(); i++){
                if(sevenItem.get(i).getItemname() == itemname){
                    itemid = sevenItem.get(i).getItemid();
                    break;
                }
            }
        }
        return itemid;
    }
}