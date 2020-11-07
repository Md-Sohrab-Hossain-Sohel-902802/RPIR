package com.example.myapplication.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class StudentMainAdapter extends BaseAdapter {

    private Context context;
    String[] values;
    int[] images;
    public StudentMainAdapter(Context context,String[] values,int[] images) {
        this.context = context;
        this.values=values;
        this.images=images;
    }



    @Override
    public int getCount() {
        return values.length;
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

            convertView = inflater.inflate(R.layout.student_main_item_layout, parent, false);



            TextView textView=convertView.findViewById(R.id.student_mItem_Textviewid);
            ImageView imageView=convertView.findViewById(R.id.student_mItem_Imageviewid);
            textView.setText(values[position]);
            imageView.setImageResource(images[position]);




        }





        return convertView;
    }
}
