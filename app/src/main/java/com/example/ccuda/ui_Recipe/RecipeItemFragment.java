package com.example.ccuda.ui_Recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.data.RecipeDTO;
import com.example.ccuda.data.RecipeItem;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Home.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.FeedTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.helper.log.Logger;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;

public class RecipeItemFragment extends Fragment { //implements OnBackPressedListener{

    private TextView like11, recipeTitle11, recipecontent;
    private ImageView recipeImage11;
    private RecipeItem item;
    private ImageButton like2;

    private ArrayList<RegiItemsModel> mrgArrayList;
    private RecipeItemAdapter mrgAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference recipeRef;
    ImageButton share;
    int likecheck, likecheck2;      //개별페이지 like버튼 재설정위한 변수
    int islike;

    ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;

    public RecipeItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment1_recipe_item, container, false);


        //파이어베이스 데이터베이스 생성
        firebaseDatabase = FirebaseDatabase.getInstance();
        recipeRef = firebaseDatabase.getReference().child("Recipe");

        item = getArguments().getParcelable(Global.KEY_DATA);
        if ( item != null) {
            String isImage = item.getImage();
            islike = item.getLike();
            String isTitle = item.getTitle();
            String content = item.getContent();

            recipeImage11 = (ImageView) v.findViewById(R.id.recipeImage2);
            like11 = (TextView) v.findViewById(R.id.likenumber2);
            recipeTitle11 = (TextView) v.findViewById(R.id.recipetitle);
            recipecontent = v.findViewById(R.id.recipecontent);
            like2 = v.findViewById(R.id.like2);

            Glide.with(this).load(isImage).into(recipeImage11);
            //recipeImage11.setImageResource(isImage);
            like11.setText(String.valueOf(islike));
            recipeTitle11.setText(isTitle);
            recipecontent.setText(content);

            RecyclerView mrgRecyclerView = v.findViewById(R.id.ritemrecycler);

            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
            mrgRecyclerView.setLayoutManager(mLinearLayoutManager);
            mrgArrayList = new ArrayList<RegiItemsModel>();

            //상품 품목
            String[] itemname = item.getItemname();
            for(int i=0; i<itemname.length; i++){
                if(itemname[i].equals(""))
                    break;
                String[] data=itemname[i].split("/");
                if(data[1].equals("CU")){
                    for(int j=0; j<cuItem.size(); j++){
                        if(cuItem.get(j).getItemid() == Integer.parseInt(data[0])){
                            mrgArrayList.add(new RegiItemsModel(cuItem.get(j).getStorename(), cuItem.get(j).getImage(),cuItem.get(j).getItemname(),cuItem.get(j).getItemid()));
                            break;
                        }
                    }
                }else if(data[1].equals("seven")){
                    for(int j=0; j<sevenItem.size(); j++){
                        if(sevenItem.get(j).getItemid() == Integer.parseInt(data[0])){
                            mrgArrayList.add(new RegiItemsModel(sevenItem.get(j).getStorename(), sevenItem.get(j).getImage(),sevenItem.get(j).getItemname(),sevenItem.get(j).getItemid()));
                            break;
                        }
                    }
                }else{
                    for(int j=0; j<gs25Item.size(); j++){
                        if(gs25Item.get(j).getItemid() == Integer.parseInt(data[0])){
                            mrgArrayList.add(new RegiItemsModel(gs25Item.get(j).getStorename(), gs25Item.get(j).getImage(),gs25Item.get(j).getItemname(),gs25Item.get(j).getItemid()));
                            break;
                        }
                    }
                }
            }

            mrgAdapter = new RecipeItemAdapter(mrgArrayList);
            mrgRecyclerView.setAdapter(mrgAdapter);
            mrgAdapter.notifyDataSetChanged();



        }



        //다른 앱 이용해서 공유하기
        share = v.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //shareKaKao();
                /*try {
                    KakaoLinkService kakaoLink = KakaoLinkService.getInstance(d);
                    KakakakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                    kakaoTalkLinkMessageBuilder.addText("TEST TEXT");
                    kakaoTalkLinkMessageBuilder.addImage(imageSrc, 300, 200);
                    kakaoTalkLinkMessageBuilder.addWebButton("GO WEBSITE", siteUrl);
                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), getActivity());
                } catch (KakaoParameterException e) {
                    Log.d(TAG, e.getMessage());
                }*/

                Intent msg = new Intent(Intent.ACTION_SEND);

                msg.addCategory(Intent.CATEGORY_DEFAULT);

                String isTitle = item.getTitle();
                String isContent = item.getContent();
                ArrayList<String> image = item.getImageurl();
                System.out.println(image);

                msg.putExtra(Intent.EXTRA_SUBJECT, isTitle + "\n\n");
                msg.putExtra(Intent.EXTRA_TEXT, isContent);
                msg.setType("image/png");
                msg.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(image)));
                msg.setPackage("com.kakao.talk");
                //msg.putExtra(Intent.EXTRA_STREAM, url);
                //msg.setType("image/*");

                msg.putExtra(Intent.EXTRA_TITLE, isTitle); //앱 공유할 때 나오는 제

                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "공유하기"));
            }
        });

        // like 버튼 setting
        if (item.getLikes().containsKey(Long.toString(SaveSharedPreference.getId(getActivity())))) {
            likecheck = 1; likecheck2 = 0;
        }else {
            likecheck =0; likecheck2 = 0;
        }
        resetlikebtn(likecheck);

        // 좋아요 누르기
        like2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeclick();
            }
        });
        return v;
    }


    /*public void shareKaKao(){
        String templateId = "피트키값";
        ArrayList<String> image = item.getImageurl();

        Map<String, String> templateArgs = new HashMap<String, String>();
        //templateArgs.put("img", Url);
        templateArgs.put("sub","텍스트");

        KakaoLinkService.getInstance().sendCustom(getContext(), templateId, templateArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다.
            }
        });
    }*/
    /*public void shareKakao(View v){
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            메시지 추가
            kakaoBuilder.addText("카카오링크 테스트");

            이미지 가로/세로 사이즈는 80px 보다 커야하며, 이미지 용량은 500kb 이하로 제한된다.
            String url = "http://upload2.inven.co.kr/upload/2015/09/27/bbs/i12820605286.jpg";
            kakaoBuilder.addImage(url, 1080, 1920);

            앱 실행버튼 추가
            kakaoBuilder.addAppButton("앱 실행");

            메시지 발송
            kakaoLink.sendMessage(kakaoBuilder, this);
        } catch (KakaoParameterException e){
            e.printStackTrace();
        }
    }*/

    //@Override
    /*public void onBackPressed() {
        getActivity().finish();
    }*/

    private void resetlikebtn(int likecheck){
        if(likecheck==1){
            like2.setImageResource(R.drawable.favorite_full);
        }else{
            like2.setImageResource(R.drawable.favorite);
        }
    }

    private void likeclick(){
        new RecipeFragment().likeClicked(recipeRef,item.getImageurl().get(0),getActivity());
        if(likecheck==0){
            //좋아요 안누른 경우
            if(likecheck2==0) {
                // 좋아요
                likecheck2 = 1;
                like11.setText(String.valueOf(islike + 1));
                resetlikebtn(1);
            }
            else {
                // 취소
                likecheck2 = 0;
                like11.setText(String.valueOf(islike));
                resetlikebtn(0);
            }
        }else{
            //좋아요 누른 경우
            if(likecheck2==0){
                // 취소
                likecheck2 = 1;
                like11.setText(String.valueOf(islike-1));
                resetlikebtn(0);
            }
            else{
                // 좋아요
                likecheck2 = 0;
                like11.setText(String.valueOf(islike));
                resetlikebtn(1);
            }
        }
    }

}