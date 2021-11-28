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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.R;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.data.ItemData;
import com.example.ccuda.db.CouponpageRequest;
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
public class DeleteUploadarticlesFragment extends Fragment {

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

    private ArrayList<CouponData> CouponArrayList;
    DeleteAdapterForUploadarticles adapter;
    ArrayAdapter<CouponData> arrayAdapter;

    View viewForAppbar;
    View viewForToolbar;
    AppBarLayout mainAppbar;
    Toolbar toolbar;

    public DeleteUploadarticlesFragment() {
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


        CouponArrayList = new ArrayList<>();

        //uploadCouponListView.setAdapter(arrayAdapter);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT);
                ArrayList<CouponData> leftCoupon=new ArrayList<>();
                for(CouponData data:CouponArrayList){
                    if(data.getIsClicked()==false){
                        leftCoupon.add(data);
                    }
                }
                System.out.println("남은거남은거남은거: "+leftCoupon);
                adapter.setMyUploadProductList(leftCoupon);
                adapter.notifyDataSetChanged();
            }
        });


        return view;
    }





    protected void load_couponlist(Context context){

    }

}