package com.example.myapplication.Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.HashMap;
import java.util.List;

public class ExpandebleCustomAdapter extends BaseExpandableListAdapter {
   private Context context;
    List<String> listDataHeader;
    HashMap<String,List<String>> listDatachilde;

    public ExpandebleCustomAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listDatachilde) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDatachilde = listDatachilde;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDatachilde.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDatachilde.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
       String headertext= (String) getGroup(groupPosition);

       if(convertView==null){
           LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView=inflater.inflate(R.layout.group_layoute,null);

       }
        TextView headerTextview=convertView.findViewById(R.id.headerTextviewid);
       headerTextview.setText(headertext);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String chiledtext= (String) getChild(groupPosition,childPosition);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.chiled_layoute,null);

        }
        TextView chiledTextview;
        chiledTextview=convertView.findViewById(R.id.chiledTextviewid);
        chiledTextview.setText(chiledtext);


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
