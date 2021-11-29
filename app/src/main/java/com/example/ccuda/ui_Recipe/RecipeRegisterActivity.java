package com.example.ccuda.ui_Recipe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class RecipeRegisterActivity extends AppCompatActivity {
    private static final String TAG = "MultiImageActivity";
    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase;

    // 각 편의점 상품 객체 리스트
    private ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    private ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    private ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;

    DatabaseReference itemRef;

    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> itemList = new ArrayList<>(); // 고른 상품명 담을 객체
    ArrayList<String> filenameList = new ArrayList<>();
    String itemname;
    String[] conv={"선택없음","GS25", "SEVEN11", "CU"};
    String storename;   // 고른 편의점명
    String title;
    String content;

    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
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

        // 파이어베이스 인스턴스 생성
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // 앨범으로 이동하는 버튼
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


        //편의점 고르기
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
                        storename = "gs25";
                        break;
                    case 2:
                        storename = "seven";
                        break;
                    case 3:
                        storename = "cu";
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
                            if(convName == "cu"){
                                for(int i=0; i<cuItem.size(); i++){
                                    prodList.add(cuItem.get(i).getItemname());
                                    //prodPrice.add(Integer.toString(cuItem.get(i).getItemprice2()));
                                }
                            }
                            else if(convName == "seven"){
                                for(int i=0; i<sevenItem.size(); i++){
                                    prodList.add(sevenItem.get(i).getItemname());
                                    //prodPrice.add(Integer.toString(sevenItem.get(i).getItemprice2()));
                                }
                            }
                            if(convName == "gs25"){
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
                                            item_id = getitem_id(convName,prodList.get(position));
                                            getName(convName, item_id);

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

        //아이템 추가 버튼
        additem_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클릭 시 편의점 이" + storename + "  " + item_id + item_name);
                if(item_name != null){
                    mrgArrayList.add(new RegiItemsModel(storename, item_id, item_name));
                    //mrgAdapter.notifyDataSetChanged();

                    mrgAdapter = new RegiitemsAdapter(mrgArrayList);
                    mrgRecyclerView.setAdapter(mrgAdapter);

                    // 아이템리스트 추가
                    itemList.add(item_id+"/"+storename);

                    mrgAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getApplicationContext(), "품목을 선택하세요", Toast.LENGTH_LONG).show();
                }

            }
        });

        // 레시피 등록 버튼
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = edit_title.getText().toString();
                content = edit_Content.getText().toString();

                //TODO: need storename, itemList
                if(storename.equals("")){
                    Toast.makeText(getApplicationContext(),"편의점을 선택해주세요",Toast.LENGTH_SHORT).show();
                }else{
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
                .setTitle("레시피 등록")
                .setMessage("등록하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        clickregister(title, storename, content);
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(, "안 끔", Toast.LENGTH_SHORT).show();
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

    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            //Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                //Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                //Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    //Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.

                        } catch (Exception e) {
                            //Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    recyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));     // 리사이클러뷰 수평 스크롤 적용
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

    // firebase에 파일 및 이미지 저장
    private void uploadimg( Uri uri,int index,String fileName, String title, String storename,String content) {
        try{
            FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
            final StorageReference imgRef= firebaseStorage.getReference("recipeImages/"+fileName);

            UploadTask uploadTask= imgRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //이미지 업로드가 성공되었으므로...
                    //곧바로 firebase storage의 이미지 파일 다운로드 URL을 얻어오기
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //파라미터로 firebase의 저장소에 저장되어 있는
                            //이미지에 대한 다운로드 주소(URL)을 문자열로 얻어오기
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
}