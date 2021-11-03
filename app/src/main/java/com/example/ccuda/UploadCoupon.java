package com.example.ccuda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class UploadCoupon extends Fragment {
    FragmentTransaction transaction;
    private String URL="http://gs25.gsretail.com/gscvs/ko/products/event-goods";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "add button listener", Toast.LENGTH_SHORT).show();
        new Thread(){
            @Override
            public void run(){
                try{
                    Document doc= Jsoup.connect(URL).get();
                    Elements temele=doc.select(".prod_list");
                    System.out.println(temele);
                    boolean isEmpty=temele.isEmpty();
                    if(isEmpty==false){
                        Bundle bundle=new Bundle();
                        bundle.putString("product",temele.get(0).text());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_uploadcoupon, container, false);

        return v;
    }
}
