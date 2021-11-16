package com.example.ccuda;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.ccuda.data.SaveSharedPreference;
import com.example.ccuda.ui_Cart.addToCart;
import com.example.ccuda.ui_Home.Adapter;
import com.example.ccuda.ui_Home.UploadCoupon;

public class MyPage extends Fragment {
    private TextView mypagetv;
    private ImageView mypageiv;
    Context context;

    Button upload;
    ListView cartList;
    Adapter adapter=new Adapter();
    Button addCart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mypage, container, false);
        context = getActivity();
        mypagetv = view.findViewById(R.id.mypagetv);
        mypageiv = view.findViewById(R.id.mypageiv);
        upload=view.findViewById(R.id.mypage_coupon_upload);
        cartList=view.findViewById(R.id.cartList);

        /*set userinfo*/
        String email;
        if(SaveSharedPreference.getEmail(context).length() == 0)
            email = "kakao login user";
        else
            email = SaveSharedPreference.getEmail(context);
        mypagetv.setText(SaveSharedPreference.getNicname(context)+"\n"+email);
        Glide.with(this).load(SaveSharedPreference.getProfileimage(context)).into(mypageiv);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, new UploadCoupon());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        adapter.addItem("물건1", "", "gs");
        adapter.addItem("물건2", "", "gs");
        adapter.addItem("물건3", "", "gs");
        adapter.addItem("물건4", "", "gs");
        adapter.addItem("물건5", "", "gs");
        cartList.setAdapter(adapter);


        addCart=view.findViewById(R.id.add_to_cart_button);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.innerLayout, new addToCart());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
