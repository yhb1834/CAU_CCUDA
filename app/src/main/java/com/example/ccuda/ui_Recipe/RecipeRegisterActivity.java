package com.example.ccuda.ui_Recipe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.RecipeDTO;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Home.HomeActivity;
import com.example.ccuda.ui_Home.ProductItemFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class RecipeRegisterActivity extends AppCompatActivity {
    private static final String TAG = "MultiImageActivity";
    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase;

    // ??? ????????? ?????? ?????? ?????????
    private ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    private ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    private ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;

    DatabaseReference itemRef;

    ArrayList<Uri> uriList = new ArrayList<>();     // ???????????? uri??? ?????? ArrayList ??????
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> itemList = new ArrayList<>(); // ?????? ????????? ?????? ??????
    ArrayList<String> filenameList = new ArrayList<>();
    String itemname;
    String[] conv={"GS25", "SEVEN11", "CU"};
    String storename;   // ?????? ????????????
    String title;
    String content;

    RecyclerView recyclerView;  // ???????????? ????????? ??????????????????
    MultiImageAdapter adapter;  // ????????????????????? ???????????? ?????????
    EditText edit_title;
    EditText edit_Content;
    Button btn_register;
    Button additem_r;
    Spinner store_spinner;
    SearchableSpinner searchView;
    int item_id;
    String item_name;
    String image;


    private ArrayList<RegiItemsModel> mrgArrayList;
    private RegiitemsAdapter mrgAdapter;
    int dataCount = -1;


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
                        //uploadPhoto.setImageURI(data);
                        //System.out.println(data);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_register);
        edit_title = findViewById(R.id.edit_title);
        edit_Content = findViewById(R.id.edit_Content);
        btn_register = findViewById(R.id.btn_register);
        additem_r = findViewById(R.id.additem_r);
        store_spinner = findViewById(R.id.spinner_r);

        RecyclerView mrgRecyclerView = findViewById(R.id.additemresults);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mrgRecyclerView.setLayoutManager(mLinearLayoutManager);
        mrgArrayList = new ArrayList<RegiItemsModel>();

        // ?????????????????? ???????????? ??????
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // ???????????? ???????????? ??????
        Button btn_getImage = findViewById(R.id.add_photo);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });

        recyclerView = findViewById(R.id.imageRecyclerView);
        searchView= findViewById(R.id.item_spinner_r);


        //????????? ?????????
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conv);
        store_spinner.setAdapter(adapter);
        store_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        storename = "GS25";
                        break;
                    case 1:
                        storename = "SEVEN";
                        break;
                    case 2:
                        storename = "CU";
                        break;
                }
                runThread(storename);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            public void runThread(String convName){
                new Thread(new Runnable() {
                    ArrayList<String> prodList=new ArrayList<>();
                    Handler mHandler=new Handler();
                    Elements elements=new Elements();
                    @Override
                    public void run() {
                        try {
                            if(convName.equals("CU")){
                                for(int i=0; i<cuItem.size(); i++){
                                    prodList.add(cuItem.get(i).getItemname());
                                    //prodPrice.add(Integer.toString(cuItem.get(i).getItemprice2()));
                                }
                            }
                            else if(convName.equals("SEVEN")){
                                for(int i=0; i<sevenItem.size(); i++){
                                    prodList.add(sevenItem.get(i).getItemname());
                                    //prodPrice.add(Integer.toString(sevenItem.get(i).getItemprice2()));
                                }
                            }
                            else{
                                for(int i=0; i<gs25Item.size(); i++){
                                    prodList.add(gs25Item.get(i).getItemname());
                                    //prodPrice.add(Integer.toString(gs25Item.get(i).getItemprice2()));
                                }
                            }
                            //prodList = getProductName(convName, URL); //getProductList(convName, URL);
                            // prodPrice = getProductPrice(convName, URL);
                            Thread.sleep(1000);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<String> arrayList=new ArrayAdapter<>(
                                            getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, prodList
                                    );

                                    searchView.setAdapter(arrayList);

                                    searchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if(position==0){
                                                //Toast.makeText(getApplicationContext(), prodList.get(position), Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                String sNumber=parent.getItemAtPosition(position).toString();
                                                //Toast.makeText(getApplicationContext(), sNumber,Toast.LENGTH_SHORT).show();
                                            }

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

        });

        //????????? ?????? ??????
        additem_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name= (String) searchView.getSelectedItem();
                ProductItemFragment fragment = new ProductItemFragment();
                //System.out.println("?????? ??? ????????? ???" + store_spinner.getSelectedItem().toString() + "  " + item_id + searchView.getSelectedItem().toString());
                if(item_name != null){
                    if (store_spinner.getSelectedItem().toString().equals("CU")){
                        for(ItemData i:cuItem){
                            if(i.getItemname().equals(searchView.getSelectedItem().toString()) && !fragment.checkisthere(i.getItemname(), mrgArrayList)){
                                mrgArrayList.add(new RegiItemsModel(store_spinner.getSelectedItem().toString(), i.getImage(),searchView.getSelectedItem().toString(),i.getItemid()));
                            }
                        }
                    }
                    if (store_spinner.getSelectedItem().toString().equals("GS25")){
                        for(ItemData i:gs25Item){
                            if(i.getItemname().equals(searchView.getSelectedItem().toString()) && !fragment.checkisthere(i.getItemname(), mrgArrayList)){
                                mrgArrayList.add(new RegiItemsModel(store_spinner.getSelectedItem().toString(), i.getImage(),searchView.getSelectedItem().toString(),i.getItemid()));
                            }
                        }
                    }
                    if (store_spinner.getSelectedItem().toString().equals("SEVEN11")){
                        for(ItemData i:sevenItem){
                            if(i.getItemname().equals(searchView.getSelectedItem().toString()) && !fragment.checkisthere(i.getItemname(), mrgArrayList)){
                                mrgArrayList.add(new RegiItemsModel(store_spinner.getSelectedItem().toString(), i.getImage(),searchView.getSelectedItem().toString(),i.getItemid()));
                            }
                        }
                    }

                    //mrgArrayList.add(new RegiItemsModel(store_spinner.getSelectedItem().toString(), searchView.getSelectedItem().toString()));
                    //mrgAdapter.notifyDataSetChanged();

                    mrgAdapter = new RegiitemsAdapter(mrgArrayList);
                    mrgRecyclerView.setAdapter(mrgAdapter);

                    mrgAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_LONG).show();
                }
                System.out.println("?????? ????????? ?????????");
                for(RegiItemsModel e:mrgArrayList){
                    System.out.print(e.getItemname()+" ");
                }

            }
        });

        // ????????? ?????? ??????
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = edit_title.getText().toString();
                content = edit_Content.getText().toString();

                //TODO: need storename, itemList
                if(storename.equals("")){
                    Toast.makeText(getApplicationContext(),"???????????? ??????????????????",Toast.LENGTH_SHORT).show();
                }else{
                    showdialog();
                    //finish();
                }

            }
        });

    }

    void showdialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(this)
                .setTitle("????????? ??????")
                .setMessage("?????????????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        setContentView(R.layout.progress_cyclic);
                        clickregister(title, storename, content);
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(, "??? ???", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();


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
        Document doc= Jsoup.connect(url).get();
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

    protected int getitem_id(String storename1,String itemname1){
        int itemid=0;
        if(storename1.equals("CU")){
            for(int i=0; i<cuItem.size(); i++){
                if(cuItem.get(i).getItemname() == itemname1){
                    itemid = cuItem.get(i).getItemid();
                    break;
                }
            }
        }
        else if(storename1.equals("GS25")){
            for(int i=0; i<gs25Item.size(); i++){
                if(gs25Item.get(i).getItemname() == itemname1){
                    itemid = gs25Item.get(i).getItemid();
                    break;
                }
            }
        }
        else{
            for(int i=0; i<sevenItem.size(); i++){
                if(sevenItem.get(i).getItemname() == itemname1){
                    itemid = sevenItem.get(i).getItemid();
                    break;
                }
            }
        }
        return itemid;
    }

    // ???????????? ??????????????? ????????? ??? ???????????? ?????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // ?????? ???????????? ???????????? ?????? ??????
            //Toast.makeText(getApplicationContext(), "???????????? ???????????? ???????????????.", Toast.LENGTH_LONG).show();
        }
        else{   // ???????????? ???????????? ????????? ??????
            if(data.getClipData() == null){     // ???????????? ????????? ????????? ??????
                //Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // ???????????? ????????? ????????? ??????
                ClipData clipData = data.getClipData();
                //Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // ????????? ???????????? 11??? ????????? ??????
                    Toast.makeText(getApplicationContext(), "????????? 10????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                }
                else{   // ????????? ???????????? 1??? ?????? 10??? ????????? ??????
                    //Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // ????????? ??????????????? uri??? ????????????.
                        try {
                            uriList.add(imageUri);  //uri??? list??? ?????????.

                        } catch (Exception e) {
                            //Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    recyclerView.setAdapter(adapter);   // ????????????????????? ????????? ??????
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));     // ?????????????????? ?????? ????????? ??????
                }
            }
        }
    }



    /*private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    private ImageView imageview2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_register);
        imageview2 = (ImageView) findViewById(R.id.imageView2);
        imageview = (ImageView) findViewById(R.id.add_photo);
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageview2.setImageURI(selectedImageUri);
        }
    }*/

    private void clickregister(String title, String storename,String content){
        itemname = "";
        for(int i=0; i<mrgArrayList.size(); i++){
            RegiItemsModel model = mrgArrayList.get(i);
            itemList.add(model.getItemid()+" - "+model.getConvName()+" - "+model.getItemname()+" - "+model.getImageurl());
        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
        String fileName1=sdf.format(new Date())+"";
        for(int i =0; i<itemList.size(); i++){
            if(i==0){
                itemname = itemname+itemList.get(i);
            }
            else{
                itemname = itemname +", "+itemList.get(i);
            }
        }
        for(int j=0; j<uriList.size(); j++){
            String fileName = fileName1+"-"+j+".png";
            uploadimg(uriList.get(j),j, fileName, title, storename, content);
        }
    }

    // firebase??? ?????? ??? ????????? ??????
    private void uploadimg( Uri uri,int index,String fileName, String title, String storename,String content) {
        try{
            FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
            final StorageReference imgRef= firebaseStorage.getReference("recipeImages/"+fileName);

            UploadTask uploadTask= imgRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //????????? ???????????? ?????????????????????...
                    //????????? firebase storage??? ????????? ?????? ???????????? URL??? ????????????
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //??????????????? firebase??? ???????????? ???????????? ??????
                            //???????????? ?????? ???????????? ??????(URL)??? ???????????? ????????????
                            urlList.add(uri.toString());
                            filenameList.add(fileName);
                            if(index == uriList.size()-1){
                                saverecipe(title, content);
                            }
                        }
                    });
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void saverecipe(String title,String content){
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setTitle(title);
        recipeDTO.setContent(content);
        recipeDTO.setWriter_id(Long.toString(SaveSharedPreference.getId(this)));
        recipeDTO.setItemname(itemname);
        recipeDTO.setLike(0);

        for(int j=0; j<urlList.size(); j++){
            String url = urlList.get(j);
            String filename = filenameList.get(j);
            if(j==0){
                recipeDTO.setImage1(url,filename);
            }
            else if(j==1){
                recipeDTO.setImage2(url,filename);
            }
            else if(j==2){
                recipeDTO.setImage3(url,filename);}
            else if(j==3){
                recipeDTO.setImage4(url,filename);
            }
            else if(j==4){
                recipeDTO.setImage5(url,filename);
            }
            else if(j==5){
                recipeDTO.setImage6(url,filename);
            }
            else if(j==6){
                recipeDTO.setImage7(url,filename);
            }
            else if (j==7) {
                recipeDTO.setImage8(url, filename);
            }
            else if (j==8) {
                recipeDTO.setImage9(url, filename);
            }
            else{
                recipeDTO.setImage10(url,filename);
            }
        }

        firebaseDatabase.getReference().child("Recipe").push().setValue(recipeDTO);

        firebaseDatabase.getReference().child("Recipe").orderByChild("image1").equalTo(recipeDTO.getImage1()).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    RecipeDTO recipeDTO = dataSnapshot.getValue(RecipeDTO.class);
                    for(int i=0;i<itemList.size(); i++){
                        if(!itemList.get(i).equals("")){
                            String[] item = itemList.get(i).split(" - ");
                            recipeDTO.getItems().put(item[2],true);
                            firebaseDatabase.getReference().child("Recipe").child(key).setValue(recipeDTO);
                        }
                    }
                }

                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
/*
public class RecipeRegisterActivity extends AppCompatActivity {
    private static final String TAG = "MultiImageActivity";
    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase;

    // ??? ????????? ?????? ?????? ?????????
    private ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    private ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    private ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;

    public ArrayList<String> cuReturn=new ArrayList<String>();
    public ArrayList<String> gsReturn=new ArrayList<String>();
    public ArrayList<String> sevenReturn=new ArrayList<String>();

    ArrayList<String> cuImageReturn = new ArrayList<>();
    ArrayList<String> gsImageReturn = new ArrayList<>();
    ArrayList<String> sevenImageReturn = new ArrayList<>();

    DatabaseReference itemRef;

    ArrayList<Uri> uriList = new ArrayList<>();     // ???????????? uri??? ?????? ArrayList ??????
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> itemList = new ArrayList<>(); // ?????? ????????? ?????? ??????
    ArrayList<String> filenameList = new ArrayList<>();
    String itemname;
    String[] conv={"????????????","GS25", "SEVEN11", "CU"};
    String storename;   // ?????? ????????????
    String title;
    String content;

    RecyclerView recyclerView;  // ???????????? ????????? ??????????????????
    MultiImageAdapter adapter;  // ????????????????????? ???????????? ?????????
    EditText edit_title;
    EditText edit_Content;
    Button btn_register;
    Button additem_r;
    Spinner store_spinner;
    SearchableSpinner searchView;
    int item_id;
    String item_name;
    String image;


    private ArrayList<RegiItemsModel> mrgArrayList;
    private RegiitemsAdapter mrgAdapter;
    int dataCount = -1;


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
                        //uploadPhoto.setImageURI(data);
                        //System.out.println(data);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_register);
        edit_title = findViewById(R.id.edit_title);
        edit_Content = findViewById(R.id.edit_Content);
        btn_register = findViewById(R.id.btn_register);
        additem_r = findViewById(R.id.additem_r);
        store_spinner = findViewById(R.id.spinner_r);

        RecyclerView mrgRecyclerView = findViewById(R.id.additemresults);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mrgRecyclerView.setLayoutManager(mLinearLayoutManager);
        mrgArrayList = new ArrayList<RegiItemsModel>();





        // ?????????????????? ???????????? ??????
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // ???????????? ???????????? ??????
        Button btn_getImage = findViewById(R.id.add_photo);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });

        recyclerView = findViewById(R.id.imageRecyclerView);
        searchView= findViewById(R.id.item_spinner_r);


        //????????? ?????????
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conv);
        store_spinner.setAdapter(adapter);
        store_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String URL = "";
                switch (position) {
                    case 0:
                        storename = "";
                        break;
                    case 1:
                        storename = "GS25";
                        break;
                    case 2:
                        storename = "SEVEN";
                        break;
                    case 3:
                        storename = "CU";
                        break;
                }
                runThread(storename);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            public void runThread(String convName){
                new Thread(new Runnable() {
                    ArrayList<String> prodList=new ArrayList<>();
                    Handler mHandler=new Handler();
                    Elements elements=new Elements();
                    @Override
                    public void run() {
                        try {
                            if(convName.equals("CU")){
                                for(int i=0; i<cuItem.size(); i++){
                                    prodList.add(cuItem.get(i).getItemname());
                                    //prodPrice.add(Integer.toString(cuItem.get(i).getItemprice2()));
                                }
                            }
                            else if(convName.equals("SEVEN")){
                                for(int i=0; i<sevenItem.size(); i++){
                                    prodList.add(sevenItem.get(i).getItemname());
                                    //prodPrice.add(Integer.toString(sevenItem.get(i).getItemprice2()));
                                }
                            }
                            else{
                                for(int i=0; i<gs25Item.size(); i++){
                                    prodList.add(gs25Item.get(i).getItemname());
                                    //prodPrice.add(Integer.toString(gs25Item.get(i).getItemprice2()));
                                }
                            }
                            //prodList = getProductName(convName, URL); //getProductList(convName, URL);
                            // prodPrice = getProductPrice(convName, URL);
                            Thread.sleep(1000);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<String> arrayList=new ArrayAdapter<>(
                                            getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, prodList
                                    );

                                    searchView.setAdapter(arrayList);

                                    searchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if(position==0){
                                                //Toast.makeText(getApplicationContext(), prodList.get(position), Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                String sNumber=parent.getItemAtPosition(position).toString();
                                                //Toast.makeText(getApplicationContext(), sNumber,Toast.LENGTH_SHORT).show();
                                            }

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

        });

        //????????? ?????? ??????
        additem_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("?????? ??? ????????? ???" + storename + "  " + item_id + item_name);
                if(item_name != null){
                    mrgArrayList.add(new RegiItemsModel(storename, item_name));
                    //mrgAdapter.notifyDataSetChanged();

                    mrgAdapter = new RegiitemsAdapter(mrgArrayList);
                    mrgRecyclerView.setAdapter(mrgAdapter);

                    mrgAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_LONG).show();
                }

            }
        });

        // ????????? ?????? ??????
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edit_title.getText().toString();
                String content = edit_Content.getText().toString();

                //TODO: need storename, itemList
                if(storename.equals("")){
                    Toast.makeText(getApplicationContext(),"???????????? ??????????????????",Toast.LENGTH_SHORT).show();
                }else{
                clickregister(title, storename, content);
                showdialog();
                //finish();
                }

            }
        });

    }

    void getName(String storename,int item_id){
        if(storename.equals("cu")){
            for(int j=0; j<cuItem.size(); j++ ){
                if(cuItem.get(j).getItemid()==item_id){
                    item_name = cuItem.get(j).getItemname();
                    image = cuItem.get(j).getImage();
                    break;
                }
            }
        }else if(storename.equals("gs25")){
            for(int j=0; j<gs25Item.size(); j++ ){
                if(gs25Item.get(j).getItemid()==item_id){
                    item_name = gs25Item.get(j).getItemname();
                    image = gs25Item.get(j).getImage();
                    break;
                }
            }
        }else {
            for(int j=0; j<sevenItem.size(); j++ ){
                if(sevenItem.get(j).getItemid()==item_id){
                    item_name = sevenItem.get(j).getItemname();
                    image = sevenItem.get(j).getImage();
                    break;
                }
            }
        }
    }

    void showdialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(this)
                .setTitle("????????? ??????")
                .setMessage("?????????????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(, "??? ???", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();


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
        Document doc= Jsoup.connect(url).get();
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

    // ???????????? ????????? arraylist??? JSONarray??? ?????? ??? ??????
    public void saveProductList(ArrayList<String> returnlist, String storename){

        firebaseDatabase= FirebaseDatabase.getInstance();
        itemRef= firebaseDatabase.getReference().child("item").child(storename);

        for(int i=0; i< returnlist.size(); i++){
            ItemData itemData = new ItemData();
            itemData.setItemid(i);
            itemData.setItemname(returnlist.get(i).split(" ")[0]);
            itemData.setPlustype(returnlist.get(i).split(" ")[1]);
            itemData.setStorename(storename);

            if(storename=="cu"){
                itemData.setImage(cuImageReturn.get(i));
            }else if(storename == "gs25"){
                itemData.setImage(gsImageReturn.get(i));
            }else{
                itemData.setImage(sevenImageReturn.get(i));
            }
            itemData.setCategory("");

            itemRef.push().setValue(itemData);
        }

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

    // ???????????? ??????????????? ????????? ??? ???????????? ?????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // ?????? ???????????? ???????????? ?????? ??????
            //Toast.makeText(getApplicationContext(), "???????????? ???????????? ???????????????.", Toast.LENGTH_LONG).show();
        }
        else{   // ???????????? ???????????? ????????? ??????
            if(data.getClipData() == null){     // ???????????? ????????? ????????? ??????
                //Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // ???????????? ????????? ????????? ??????
                ClipData clipData = data.getClipData();
                //Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // ????????? ???????????? 11??? ????????? ??????
                    Toast.makeText(getApplicationContext(), "????????? 10????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                }
                else{   // ????????? ???????????? 1??? ?????? 10??? ????????? ??????
                    //Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // ????????? ??????????????? uri??? ????????????.
                        try {
                            uriList.add(imageUri);  //uri??? list??? ?????????.

                        } catch (Exception e) {
                            //Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    recyclerView.setAdapter(adapter);   // ????????????????????? ????????? ??????
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));     // ?????????????????? ?????? ????????? ??????
                }
            }
        }
    }



    /*private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    private ImageView imageview2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_register);

        imageview2 = (ImageView) findViewById(R.id.imageView2);
        imageview = (ImageView) findViewById(R.id.add_photo);
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageview2.setImageURI(selectedImageUri);

        }

    }
*/
    /*
    private void clickregister(String title, String storename,String content){
        itemname = "";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
        String fileName1=sdf.format(new Date())+"";
        for(int i =0; i<itemList.size(); i++){
            if(i==0){
                itemname = itemname+itemList.get(i);
            }
            else{
                itemname = itemname +", "+itemList.get(i);
            }
        }
        for(int j=0; j<uriList.size(); j++){
            String fileName = fileName1+"-"+j+".png";
            uploadimg(uriList.get(j),j, fileName, title, storename, content);
        }
    }

    // firebase??? ?????? ??? ????????? ??????
    private void uploadimg( Uri uri,int index,String fileName, String title, String storename,String content) {
        try{
            FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
            final StorageReference imgRef= firebaseStorage.getReference("recipeImages/"+fileName);

            UploadTask uploadTask= imgRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //????????? ???????????? ?????????????????????...
                    //????????? firebase storage??? ????????? ?????? ???????????? URL??? ????????????
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //??????????????? firebase??? ???????????? ???????????? ??????
                            //???????????? ?????? ???????????? ??????(URL)??? ???????????? ????????????
                            urlList.add(uri.toString());
                            filenameList.add(fileName);
                            if(index == uriList.size()-1){
                                saverecipe(title, storename, content);
                            }
                        }
                    });
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void saverecipe(String title, String storename,String content){
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setTitle(title);
        recipeDTO.setStorename(storename);
        recipeDTO.setContent(content);
        recipeDTO.setWriter_id(Long.toString(SaveSharedPreference.getId(this)));
        recipeDTO.setItemname(itemname);
        recipeDTO.setLike(0);

        for(int j=0; j<urlList.size(); j++){
            String url = urlList.get(j);
            String filename = filenameList.get(j);
            if(j==0){
                recipeDTO.setImage1(url,filename);
            }
            else if(j==1){
                recipeDTO.setImage2(url,filename);
            }
            else if(j==2){
                recipeDTO.setImage3(url,filename);}
            else if(j==3){
                recipeDTO.setImage4(url,filename);
            }
            else if(j==4){
                recipeDTO.setImage5(url,filename);
            }
            else if(j==5){
                recipeDTO.setImage6(url,filename);
            }
            else if(j==6){
                recipeDTO.setImage7(url,filename);
            }
            else if (j==7) {
                recipeDTO.setImage8(url, filename);
            }
            else if (j==8) {
                recipeDTO.setImage9(url, filename);
            }
            else{
                recipeDTO.setImage10(url,filename);
            }
        }

        firebaseDatabase.getReference().child("Recipe").push().setValue(recipeDTO);

        //getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new RecipeFragment()).commit();

    }
}*/