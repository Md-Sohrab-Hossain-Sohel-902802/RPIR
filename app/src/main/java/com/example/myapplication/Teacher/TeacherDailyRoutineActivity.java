package com.example.myapplication.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.SpinnerCustomAdapter;
import com.example.myapplication.Admin.DataModuler.RoutineDataModuler;
import com.example.myapplication.Admin.DataModuler.TeacherDataModuler;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.TeacherRoutineAdapter;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TeacherDailyRoutineActivity extends AppCompatActivity {

    private Spinner daySpinner;
    private  RecyclerView recyclerView;
    private  String[] days={"Select Day","Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday"};
    private SpinnerCustomAdapter daySpinneradapter;

    String teacherCode,day;

    private  List<RoutineDataModuler> routineDataList=new ArrayList<>();
    private  ProgressDialog progressDialog;

    private TeacherRoutineAdapter routineAdapter;
    private  boolean isfirstselected=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_daily_routine);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
         day = sdf.format(d);
         if(day.equals("Friday")){
             day="Saturday";
         }else{
             day= sdf.format(d);
         }


        daySpinner=findViewById(R.id.teacher_d_routine_daySpinnerid);
        recyclerView=findViewById(R.id.teacher_d_routineListRecyclerViewid);


        daySpinneradapter=new SpinnerCustomAdapter(this,days);
        daySpinner.setAdapter(daySpinneradapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routineAdapter=new TeacherRoutineAdapter(this,routineDataList);
        recyclerView.setAdapter(routineAdapter);

        routineAdapter.setOnItemClickListner(new TeacherRoutineAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {

            }
        });


        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isfirstselected==true){
                    isfirstselected=false;
                }else{
                    day=days[position];
                    routineDataList.clear();
                    routineAdapter.notifyDataSetChanged();
                    onStart();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();


        UsersShared shared=new UsersShared(TeacherDailyRoutineActivity.this);
        getRoutine(shared.getTeacherCode(),day);




    }

    private void getRoutine(String teacherCode, String day) {
        URLS urls=new URLS();
        String url=urls.getRoutine()+"teacherroutine/"+day+"/"+teacherCode;


        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("routine");
                    routineDataList.clear();
                    for(int i=0; i<array.length(); i++){

                        JSONObject receive=array.getJSONObject(i);
                        String id=   receive.getString("endTime");
                        RoutineDataModuler routineDataModuler=new RoutineDataModuler(
                                receive.getString("_id"),
                                receive.getString("startTime"),
                                receive.getString("endTime"),
                                receive.getString("day"),
                                receive.getString("teacherName"),
                                receive.getString("roomNumber"),
                                receive.getString("teacherCode"),
                                receive.getString("department"),
                                receive.getString("groups"),
                                receive.getString("shift"),
                                receive.getString("semester"),
                                receive.getString("period"),
                                receive.getString("subjectCode"),
                                receive.getString("serial")


                        );

                        routineDataList.add(routineDataModuler);

                        routineAdapter.notifyDataSetChanged();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeacherDailyRoutineActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);



    }
}