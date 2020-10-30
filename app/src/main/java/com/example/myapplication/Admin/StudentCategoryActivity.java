package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.myapplication.Admin.Adapter.ExpandebleCustomAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentCategoryActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String,List<String>> listDataChiled;
    ExpandebleCustomAdapter customAdapter;
    int value=-1;

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_category);

        from=getIntent().getStringExtra("from");
        preparedata();
        expandableListView=findViewById(R.id.expandAbleLIstviewid);

        customAdapter=new ExpandebleCustomAdapter(this,listDataHeader,listDataChiled);
        expandableListView.setAdapter(customAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(value !=-1 && value!=groupPosition){
                    expandableListView.collapseGroup(value);
                }
                value=groupPosition;
            }
        });



        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String childString=listDataChiled.get(listDataHeader.get(groupPosition)).get(childPosition);
                String groupString=listDataHeader.get(groupPosition);

                if(from.equals("student")) {

                    Intent intent = new Intent(StudentCategoryActivity.this, AdminStudentListActivity.class);
                    intent.putExtra("department", groupString);
                    intent.putExtra("semester", childString);
                    startActivity(intent);
                }else if(from.equals("routine")){

                    Intent intent = new Intent(StudentCategoryActivity.this, AdminRoutineListActivity.class);
                    intent.putExtra("department", groupString);
                    intent.putExtra("semester", childString);
                    startActivity(intent);
                }


                return false;
            }
        });








    }










    private void preparedata() {

        listDataHeader=new ArrayList<>();
        listDataChiled=new HashMap<>();

        listDataHeader.add("Computer");
        List<String> overview=new ArrayList<>();
        overview.add("Semester-1");
        overview.add("Semester-2");
        overview.add("Semester-3");
        overview.add("Semester-4");
        overview.add("Semester-5");
        overview.add("Semester-6");
        overview.add("Semester-7");
        overview.add("Semester-8");
        listDataChiled.put(listDataHeader.get(0),overview);
        listDataHeader.add("Electrical ");
        List<String> overview2=new ArrayList<>();
        overview2.add("Semester-1");
        overview2.add("Semester-2");
        overview2.add("Semester-3");
        overview2.add("Semester-4");
        overview2.add("Semester-5");
        overview2.add("Semester-6");
        overview2.add("Semester-7");
        overview2.add("Semester-8");
        listDataChiled.put(listDataHeader.get(1),overview2);
        listDataHeader.add("Mechanical");
        List<String> overview3=new ArrayList<>();
        overview3.add("Semester-1");
        overview3.add("Semester-2");
        overview3.add("Semester-3");
        overview3.add("Semester-4");
        overview3.add("Semester-5");
        overview3.add("Semester-6");
        overview3.add("Semester-7");
        overview3.add("Semester-8");
        listDataChiled.put(listDataHeader.get(2),overview3);
        listDataHeader.add("Civil");
        List<String> overview4=new ArrayList<>();
        overview4.add("Semester-1");
        overview4.add("Semester-2");
        overview4.add("Semester-3");
        overview4.add("Semester-4");
        overview4.add("Semester-5");
        overview4.add("Semester-6");
        overview4.add("Semester-7");
        overview4.add("Semester-8");
        listDataChiled.put(listDataHeader.get(3),overview4);

        listDataHeader.add("Power");
        List<String> overview5=new ArrayList<>();
        overview5.add("Semester-1");
        overview5.add("Semester-2");
        overview5.add("Semester-3");
        overview5.add("Semester-4");
        overview5.add("Semester-5");
        overview5.add("Semester-6");
        overview5.add("Semester-7");
        overview5.add("Semester-8");
        listDataChiled.put(listDataHeader.get(4),overview5);


        listDataHeader.add("Electronics");
        List<String> overview6=new ArrayList<>();
        overview6.add("Semester-1");
        overview6.add("Semester-2");
        overview6.add("Semester-3");
        overview6.add("Semester-4");
        overview6.add("Semester-5");
        overview6.add("Semester-6");
        overview6.add("Semester-7");
        overview6.add("Semester-8");
        listDataChiled.put(listDataHeader.get(5),overview6);


        listDataHeader.add("Electromedical");
        List<String> overview7=new ArrayList<>();
        overview7.add("Semester-1");
        overview7.add("Semester-2");
        overview7.add("Semester-3");
        overview7.add("Semester-4");
        overview7.add("Semester-5");
        overview7.add("Semester-6");
        overview7.add("Semester-7");
        overview7.add("Semester-8");
        listDataChiled.put(listDataHeader.get(6),overview7);



    }
}