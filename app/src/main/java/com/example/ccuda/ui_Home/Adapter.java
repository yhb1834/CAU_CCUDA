package com.example.ccuda.ui_Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ccuda.R;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    Context mContext=null;
    LayoutInflater mLayoutInflater=null;
    ArrayList<productData> listViewProductList=new ArrayList<productData>();

    private ImageView iconImageView;
    private TextView nameTextView;
    private TextView storeTextView;

    @Override
    public int getCount() {
        return listViewProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewProductList.get(position);
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
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.product_list,parent,false);

        }
        nameTextView=(TextView) convertView.findViewById(R.id.name);
        iconImageView=(ImageView) convertView.findViewById(R.id.icon);
        storeTextView=(TextView) convertView.findViewById(R.id.store);

        productData listViewProduct=listViewProductList.get(position);

        nameTextView.setText(listViewProduct.getProductName());
        if(listViewProduct.getPhoto()=="" || listViewProduct.getPhoto()==null){
            iconImageView.setImageResource(R.drawable.add);
        }else{
            Glide.with(convertView).load(listViewProduct.getPhoto()).into(iconImageView);
        }
        storeTextView.setText(listViewProduct.getConvenientStore());
        return  convertView;
    }
    public void addItem(String name, String icon, String store){
        productData product=new productData();

        product.setProductName(name);
        product.setConvenientStore(store);
        product.setPhoto(icon);

        listViewProductList.add(product);
    }
}
/*
public class Adapter extends ArrayAdapter<productData> {


    private List<productData> contracts;
    private ImageView iconImageView;
    private TextView nameTextView;
    private TextView storeTextView;

    public Adapter(Context context, int view, List<productData> passedContracts) {
        super(context, view, passedContracts);
        contracts = passedContracts;
    }

    @Override
    public int getCount() {
        return contracts.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos=position;
        final Context context=parent.getContext();

        if (convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.product_list,parent,false);

        }
        nameTextView=(TextView) convertView.findViewById(R.id.name);
        iconImageView=(ImageView) convertView.findViewById(R.id.icon);
        storeTextView=(TextView) convertView.findViewById(R.id.store);

        productData listViewProduct=contracts.get(position);

        nameTextView.setText(listViewProduct.getProductName());
        iconImageView.setImageResource(listViewProduct.getPhoto());
        storeTextView.setText(listViewProduct.getConvenientStore());
        return  convertView;
    }
}

 */
