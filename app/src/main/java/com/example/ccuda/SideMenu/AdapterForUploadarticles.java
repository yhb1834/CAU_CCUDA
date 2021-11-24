package com.example.ccuda.SideMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;
import com.example.ccuda.data.CouponData;
import com.example.ccuda.ui_Home.productData;

import java.util.ArrayList;
import java.util.List;

public class AdapterForUploadarticles extends ArrayAdapter<CouponData> {
    ArrayList<CouponData> myUploadProductList=new ArrayList<CouponData>();
    private Context context;
    private ListView listView;

    private ImageView iconImageView;
    private TextView nameTextView;
    private TextView storeTextView;
    private TextView priceTextView;
    private TextView validityTextView;

    public AdapterForUploadarticles(@NonNull Context context, int resource, ArrayList myUploadProductList) {
        super(context, android.R.layout.simple_list_item_multiple_choice, myUploadProductList);
        this.context=context;
        this.myUploadProductList=myUploadProductList;
        //this.myUploadProductList=objects;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos=position;
        final Context context=parent.getContext();

        if (convertView==null){
            //LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView=inflater.inflate(R.layout.product_list,parent,false);
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.product_list,parent,false);

        }
        nameTextView=(TextView) convertView.findViewById(R.id.name);
        iconImageView=(ImageView) convertView.findViewById(R.id.icon);
        storeTextView=(TextView) convertView.findViewById(R.id.store);
        priceTextView=(TextView) convertView.findViewById(R.id.price1);
        validityTextView=(TextView) convertView.findViewById(R.id.validity1);

        CouponData listViewProduct=myUploadProductList.get(position);

        nameTextView.setText(listViewProduct.getItem_name());
        if(listViewProduct.getImageurl()=="" || listViewProduct.getImageurl()==null){
            iconImageView.setImageResource(R.drawable.add);
        }else{
            Glide.with(convertView).load(listViewProduct.getImageurl()).into(iconImageView);
        }
        storeTextView.setText(listViewProduct.getStorename());
        priceTextView.setText("  "+listViewProduct.getPrice() + " 원");
        validityTextView.setText(listViewProduct.getExpiration_date());

        return  convertView;

        //return null;
    }
}
