package com.example.ccuda.SideMenu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ccuda.data.PeopleItem;
import com.example.ccuda.data.SaveSharedPreference;

import com.example.ccuda.R;
import com.example.ccuda.db.ChatRequest;
import com.example.ccuda.db.ReportRequest;
import com.google.firebase.database.core.Repo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifyFragment extends Fragment {
    ArrayList<String> cheaterlist = new ArrayList<>();
/*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotifyFragment() {
        // Required empty public constructor
    }

    *//**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifyFragment.
     *//*
    // TODO: Rename and change types and number of parameters
    public static NotifyFragment newInstance(String param1, String param2) {
        NotifyFragment fragment = new NotifyFragment();
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
    }*/

    private TextView myID;
    private Button btn_report;
    Context context;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment2_notify, container, false);
        myID = v.findViewById(R.id.notify_myID);
        //btn_report = v.findViewById(R.id.btn_report);

        context = getActivity();
        myID.setText(SaveSharedPreference.getNicname(context));

        getcheaterlist();

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, cheaterlist);
        Spinner btn_report = (Spinner) v.findViewById(R.id.notify_spinner);
        btn_report.setAdapter(arrayAdapter);
        btn_report.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*String cheater_name = "";
                String content = "";
                // TODO: get cheater_name, content
                if(!content.equals(""))
                    clickreport(cheater_name, content);*/
                //String notify;
                //notify = cheaterlist.get(position);
                String notify =  btn_report.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cheater_name = "";
                String content = "";
                // TODO: get cheater_name, content
                if(!content.equals(""))
                    clickreport(cheater_name, content);
            }
        });*/

        return v;
    }

    private void clickreport(String cheater_name, String content){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(context,"신고접수 완료",Toast.LENGTH_SHORT).show();
                    // TODO: 신고접수완료 후 동작

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ReportRequest reportRequest = new ReportRequest("report", SaveSharedPreference.getId(context)+"",cheater_name,content,responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(reportRequest);

    }

    private void getcheaterlist(){
        Response.Listener<String> responsListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("cheaterlist");
                    int length = jsonArray.length();
                    for(int i = 0; i<length; i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        cheaterlist.add(object.getString("nicname"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ReportRequest reportRequest = new ReportRequest("cheaterlist", SaveSharedPreference.getId(context)+"","","",responsListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(reportRequest);
    }


}