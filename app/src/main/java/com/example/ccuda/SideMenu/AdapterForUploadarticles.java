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

public class AdapterForUploadarticles extends ArrayAdapter<CouponData> {
    ArrayList<CouponData> myUploadProductList=new ArrayList<CouponData>();
    private Context context;
    private ListView listView;
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemIds;

    private ImageView iconImageView;
    private TextView nameTextView;
    private TextView storeTextView;
    private TextView priceTextView;
    private TextView validityTextView;
    ArrayList<View> viewArrayList=new ArrayList<>();


    public AdapterForUploadarticles(@NonNull Context context, int resource, ArrayList myUploadProductList) {
        super(context, resource, myUploadProductList);
        this.context=context;
        this.myUploadProductList=myUploadProductList;
        this.inflater=LayoutInflater.from(context);
        //this.myUploadProductList=objects;
    }

    private class ViewHolder{
        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView storeTextView;
        private TextView priceTextView;
        private TextView validityTextView;
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
        final int pos=position;
        //final Context context=parent.getContext();
        final ViewHolder holder;

        if (convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.product_list, null);
            //LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView=inflater.inflate(R.layout.product_list,parent,false);
            //convertView=LayoutInflater.from(context).inflate(R.layout.product_list,parent,false);
            holder.nameTextView=(TextView) convertView.findViewById(R.id.name);
            holder.iconImageView=(ImageView) convertView.findViewById(R.id.icon);
            holder.storeTextView=(TextView) convertView.findViewById(R.id.store);
            holder.priceTextView=(TextView) convertView.findViewById(R.id.price1);
            holder.validityTextView=(TextView) convertView.findViewById(R.id.validity1);
            convertView.setTag(holder);
            //convertView.setBackgroundColor(Color.parseColor("#FFC107"));
        }
        else {
            holder=(ViewHolder) convertView.getTag();
        }
        //nameTextView=(TextView) convertView.findViewById(R.id.name);
        //iconImageView=(ImageView) convertView.findViewById(R.id.icon);
        //storeTextView=(TextView) convertView.findViewById(R.id.store);
        //priceTextView=(TextView) convertView.findViewById(R.id.price1);
        //validityTextView=(TextView) convertView.findViewById(R.id.validity1);

        CouponData listViewProduct=myUploadProductList.get(position);

        holder.nameTextView.setText(listViewProduct.getItem_name());
        if(listViewProduct.getImageurl()=="" || listViewProduct.getImageurl()==null){
            holder.iconImageView.setImageResource(R.drawable.add);
        }else{
            Glide.with(convertView).load(listViewProduct.getImageurl()).into(holder.iconImageView);
        }
        holder.storeTextView.setText(listViewProduct.getStorename());
        holder.priceTextView.setText("  "+listViewProduct.getPrice() + " 원");
        holder.validityTextView.setText(listViewProduct.getExpiration_date());

        viewArrayList.add(convertView);
        return  convertView;

        //return null;
    }

    @Override
    public void remove(CouponData object){
        myUploadProductList.remove(object);
        notifyDataSetChanged();
    }

    public ArrayList<CouponData> getMyList(){
        return myUploadProductList;
    }

    public void toggleSelection(int position){
        if(mSelectedItemIds==null){
            mSelectedItemIds=new SparseBooleanArray();
            selectView(position, true);
        }
        else{
            selectView(position, !mSelectedItemIds.get(position));
        }




    }

    public void removeSelection(){
        mSelectedItemIds=new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value){
        if(value){
            viewArrayList.get(position).setBackgroundColor(Color.parseColor("#FFC107"));
            Toast.makeText(getContext(), position+" "+value, Toast.LENGTH_SHORT).show();
            mSelectedItemIds.put(position, value);
        }
        else {
            viewArrayList.get(position).setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            Toast.makeText(getContext(), position+" "+value, Toast.LENGTH_SHORT).show();
            mSelectedItemIds.delete(position);
        }

        notifyDataSetChanged();
    }

    public int getSelectedCount(){
        return mSelectedItemIds.size();
    }

    public SparseBooleanArray getSelectedIds(){
        return mSelectedItemIds;
    }



}
