package com.example.ccuda.SideMenu;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.ui_Home.productData;

import java.util.ArrayList;
import java.util.List;

public class DeleteAdapterForUploadarticles extends ArrayAdapter<CouponData> {
    ArrayList<CouponData> myUploadProductList=new ArrayList<CouponData>();
    private Context context;
    private ListView listView;
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemIds;


    public DeleteAdapterForUploadarticles(@NonNull Context context, int resource, ArrayList myUploadProductList) {
        super(context, resource, myUploadProductList);
        this.context=context;
        this.myUploadProductList=myUploadProductList;
        this.inflater=LayoutInflater.from(context);
    }


    public void addItem(CouponData a){
        myUploadProductList.add(a);
    }

    @Override
    public int getCount() {
        return myUploadProductList.size();
    }

    @Override
    public CouponData getItem(int position) {
        return myUploadProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView itemImage;
        TextView prodName;
        TextView prodDuration;
        TextView prodStore;
        CheckBox checkBox;
        boolean isChecked;

        final  Context context= parent.getContext();
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.product_list2, parent, false);
        }

        itemImage=(ImageView) convertView.findViewById(R.id.icon2);
        prodName=(TextView) convertView.findViewById(R.id.name2);
        prodDuration=(TextView) convertView.findViewById(R.id.price12);
        prodStore=(TextView) convertView.findViewById(R.id.validity12);
        checkBox=(CheckBox) convertView.findViewById(R.id.product_list_checkbox);

        Glide.with(context).load(myUploadProductList.get(position).getImageurl())
                .into(itemImage);
        prodName.setText(myUploadProductList.get(position).getItem_name());
        prodDuration.setText(myUploadProductList.get(position).getExpiration_date());
        prodStore.setText(myUploadProductList.get(position).getStorename());
        //myUploadProductList.get(position).setIsClicked(myUploadProductList.get(position).getIsClicked());
        checkBox.setChecked(myUploadProductList.get(position).getIsClicked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUploadProductList.get(position).setIsClicked(!myUploadProductList.get(position).getIsClicked());
                System.out.println("isClicked: "+myUploadProductList.get(position).getIsClicked());
            }
        });

        return convertView;
    }

    @Override
    public void remove(CouponData object){
        myUploadProductList.remove(object);
        notifyDataSetChanged();
    }



    public void setMyUploadProductList(ArrayList<CouponData> inputArrayList){
        myUploadProductList=inputArrayList;
    }






}
