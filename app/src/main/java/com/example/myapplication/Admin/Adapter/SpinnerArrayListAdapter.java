package com.example.myapplication.Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Admin.DataModuler.TeachercodeDataModuler;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerArrayListAdapter extends BaseAdapter {



    private  Context context;

    List<TeachercodeDataModuler> arrayList=new ArrayList<>();

    public SpinnerArrayListAdapter(Context context, List<TeachercodeDataModuler> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.spinner_item_layout, parent, false);



            TextView textView=convertView.findViewById(R.id.admin_spinnerlistTextviewid);
            textView.setText(arrayList.get(position).getCode()+"("+arrayList.get(position).getName()+")");





        }





        return convertView;
    }
}
