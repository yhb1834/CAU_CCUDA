package com.example.ccuda.SideMenu;
//https://asukim.tistory.com/34
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.db.CouponpageRequest;
import com.example.ccuda.ui_Cart.AllCartFragment;
import com.example.ccuda.ui_Home.Adapter;
import com.example.ccuda.ui_Home.HomeActivity;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadarticlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadarticlesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView uploadCouponListView;
    private Button deleteButton;
    private Button selectAllButton;

    private ArrayList<CouponData> CouponArrayList = new ArrayList<>();
    ArrayList<ItemData> cuItem = HomeActivity.cuItem;
    ArrayList<ItemData> gs25Item = HomeActivity.gs25Item;
    ArrayList<ItemData> sevenItem = HomeActivity.sevenItem;
    AdapterForUploadarticles adapter;
    ArrayAdapter<CouponData> arrayAdapter;

    View viewForAppbar;
    View viewForToolbar;
    AppBarLayout mainAppbar;
    Toolbar toolbar;

    public UploadarticlesFragment() {
        // Required empty public constructor
        //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadarticlesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadarticlesFragment newInstance(String param1, String param2) {
        UploadarticlesFragment fragment = new UploadarticlesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        load_couponlist(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment2_uploadarticles, container, false);
        uploadCouponListView=view.findViewById(R.id.upload_coupon_listView);
        //uploadCouponListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        deleteButton=view.findViewById(R.id.delete_button);
        selectAllButton=view.findViewById(R.id.selectAll_button);

        viewForToolbar=getLayoutInflater().inflate(R.layout.toolbar_home, null, false);
        toolbar=viewForToolbar.findViewById(R.id.main_toolbar);

        viewForAppbar=getLayoutInflater().inflate(R.layout.activity_home, null, false);
        mainAppbar=viewForAppbar.findViewById(R.id.main_app_bar);

        uploadCouponListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        //uploadCouponListView.setSelector(new PaintDrawable("#FFC107"));
        //uploadCouponListView.setMultiChoiceModeListener(new MultiChoiceListenr);


        //uploadCouponListView.setAdapter(arrayAdapter);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                DeleteUploadarticlesFragment fragment=new DeleteUploadarticlesFragment();
                transaction.replace(R.id.innerLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return view;
    }





    protected void load_couponlist(Context context){
        CouponArrayList.clear();

        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("couponlist");
                    int length = jsonArray.length();

                    for(int i=0; i<length; i++){
                        CouponData couponData = new CouponData();
                        JSONObject object = jsonArray.getJSONObject(i);

                        couponData.setCoupon_id(Integer.parseInt(object.getString("coupon_id")));
                        couponData.setPrice(Integer.parseInt(object.getString("price")));       //쿠폰 가격
                        couponData.setExpiration_date(object.getString("expiration_date")); // 쿠폰 유효기간 "Y-m-d" 형식
                        couponData.setContent(object.getString("content")); // 글 내용
                        String storename = object.getString("storename");
                        storename = storename.toLowerCase();
                        couponData.setStorename(storename);
                        couponData.setPlustype(object.getString("category"));
                        couponData.setSeller_id(Long.parseLong(object.getString("seller_id"))); // 판매자 확인용 id
                        couponData.setPost_date(object.getString("post_date")); // "Y-m-d H:i:s" 형식
                        couponData.setSeller_name(object.getString("seller_nicname")); // 판매자 닉네임
                        couponData.setSeller_score(object.getString("seller_score")); // 판매자 평점

                        int item_id = Integer.parseInt(object.getString("item_id"));
                        if(storename.equals("cu")){
                            for(int j=0; j<cuItem.size(); j++ ){
                                if(cuItem.get(j).getItemid()==item_id){
                                    couponData.setItem_name(cuItem.get(j).getItemname());
                                    couponData.setCategory(cuItem.get(j).getCategory());
                                    couponData.setImage(cuItem.get(j).getImage());
                                    break;
                                }
                            }
                        }else if(storename.equals("gs25")){
                            for(int j=0; j<gs25Item.size(); j++ ){
                                if(gs25Item.get(j).getItemid()==item_id){
                                    couponData.setItem_name(gs25Item.get(j).getItemname());
                                    couponData.setCategory(gs25Item.get(j).getCategory());
                                    couponData.setPlustype(gs25Item.get(j).getPlustype());
                                    couponData.setImage(gs25Item.get(j).getImage());
                                    break;
                                }
                            }
                        }else {
                            for(int j=0; j<sevenItem.size(); j++ ){
                                if(sevenItem.get(j).getItemid()==item_id){
                                    couponData.setItem_name(sevenItem.get(j).getItemname());
                                    couponData.setCategory(sevenItem.get(j).getCategory());
                                    couponData.setPlustype(sevenItem.get(j).getPlustype());
                                    couponData.setImage(sevenItem.get(j).getImage());
                                    break;
                                }
                            }
                        }
                        CouponArrayList.add(couponData);
                        //adapter.addItem(couponData.getItem_name(), R.drawable.add, couponData.getStorename());

                    }
                    System.out.println("coupon list: "+CouponArrayList);
                    adapter=new AdapterForUploadarticles(getActivity(),
                            R.layout.product_list,CouponArrayList); //android.R.layout.simple_list_item_multiple_choice

                    uploadCouponListView.setAdapter(adapter);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CouponpageRequest couponpageRequest = new CouponpageRequest("couponlist", "","","",responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(couponpageRequest);
    }

}