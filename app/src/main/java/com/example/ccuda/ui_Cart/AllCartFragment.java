package com.example.ccuda.ui_Cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.ccuda.R;

import java.util.ArrayList;

public class AllCartFragment extends Fragment {
    GridView cartItemList;
    AllCartListAdapter adapter;
    ArrayList<ItemParccelable> itemList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChildFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                itemList=result.getParcelableArrayList("itemlist");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_cartlist, container, false);
        cartItemList=view.findViewById(R.id.cartItemGrid);

        return view;
    }
}
