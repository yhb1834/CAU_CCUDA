package com.example.ccuda.ui_Recipe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.RecipeDTO;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Home.HomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecipeRegisterActivity extends AppCompatActivity {
    private static final String TAG = "MultiImageActivity";
    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase;

    // 각 편의점 상품 객체 리스트
    private ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    private ArrayList<ItemData> gsItem = HomeActivity.gs25Item;
    private ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;

    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> itemList = new ArrayList<>(); // 고른 상품명 담을 객체
    ArrayList<String> filenameList = new ArrayList<>();
    String itemname;
    String[] conv={"선택없음","GS25", "SEVEN11", "CU"};
    String storename;   // 고른 편의점명

    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    EditText edit_title;
    EditText edit_Content;
    Button btn_register;
    Spinner store_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_register);
        edit_title = findViewById(R.id.edit_title);
        edit_Content = findViewById(R.id.edit_Content);
        btn_register = findViewById(R.id.btn_register);
        store_spinner = findViewById(R.id.spinner1);

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

        //편의점 고르기
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conv);
        store_spinner.setAdapter(adapter);
        store_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }

                                                });

        // 레시피 등록 버튼
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edit_title.getText().toString();
                String content = edit_Content.getText().toString();

                //TODO: need storename, itemList
                if(storename.equals("")){
                    Toast.makeText(getApplicationContext(),"편의점을 선택해주세요",Toast.LENGTH_SHORT).show();
                }else{
                clickregister(title, storename, content);
                }
            }
        });

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

        //TODO: 레시피 등록 완료 후 동작
        //getSupportFragmentManager().beginTransaction().replace(R.id.innerLayout, new RecipeFragment()).commit();

    }
}