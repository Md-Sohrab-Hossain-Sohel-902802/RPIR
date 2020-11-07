package com.example.myapplication.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.RoutineManagement;
import com.example.myapplication.Admin.AdminRoutineListActivity;
import com.example.myapplication.Admin.DataModuler.RoutineDataModuler;
import com.example.myapplication.LocalStorage.Shared.StudentShared;
import com.example.myapplication.R;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentRoutineActivity extends AppCompatActivity {

    private TextView period,p1,p2,p3,p4,p5,p6,p7;
    private  TextView timedate,p1TimeDate,p2TimeDate,p3TimeDate,p4TimeDate,p5TimeDate,p6TimeDate,p7TimeDate;
    private  TextView saturday,saturdayP1,saturdayP2,saturdayP3,saturdayP4,saturdayP5,saturdayP6,saturdayP7;
    private  TextView sunday,sundayP1,sundayP2,sundayP3,sundayP4,sundayP5,sundayP6,sundayP7;
    private  TextView monday,mondayP1,mondayP2,mondayP3,mondayP4,mondayP5,mondayP6,mondayP7;
    private  TextView tuesday,tuesdayP1,tuesdayP2,tuesdayP3,tuesdayP4,tuesdayP5,tuesdayP6,tuesdayP7;
    private  TextView wednesday,wednesdayP1,wednesdayP2,wednesdayP3,wednesdayP4,wednesdayP5,wednesdayP6,wednesdayP7;
    private  TextView thursday,thursdayP1,thursdayP2,thursdayP3,thursdayP4,thursdayP5,thursdayP6,thursdayP7;
    private  TextView header,addRoutineHeaderTextview;

    private List<RoutineDataModuler> routineDataList=new ArrayList<>();


    private  ProgressDialog progressDialog1;



    String department,group,shift,semester;
    StudentShared studentShared;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_routine);



        progressDialog1=new ProgressDialog(this);
        progressDialog1.setTitle("Loading..");
        studentShared=new StudentShared(StudentRoutineActivity.this);
        initialize();


    }


    @Override
    protected void onStart() {
        super.onStart();
        department=studentShared.getDepartment();
        group=studentShared.getGroupd();
        shift=studentShared.getShift();
        semester=studentShared.getSemester();
        getClassRoutine(department,group,shift,semester);
    }


    public void getClassRoutine(String department,String group,String shift,String semester){
        progressDialog1.show();
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(StudentRoutineActivity.this);
        String url=urls.getRoutine()+department+"/"+group+"/"+shift+"/"+semester;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog1.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    routineDataList.clear();
                    for(int i=0; i<array.length(); i++){
                        JSONObject receive=array.getJSONObject(i);

                        RoutineDataModuler dataModuler=new RoutineDataModuler(
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
                        routineDataList.add(dataModuler);
                    }

                    onGetData();

                } catch (JSONException e) {
                    progressDialog1.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(StudentRoutineActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);




    }

    private void initialize() {
        header=findViewById(R.id.rr_header);
        //find Period
        period=findViewById(R.id.rr_periodTextviewid);
        p1=findViewById(R.id.rr_p1);
        p2=findViewById(R.id.rr_p2);
        p3=findViewById(R.id.rr_p3);
        p4=findViewById(R.id.rr_p4);
        p5=findViewById(R.id.rr_p5);
        p6=findViewById(R.id.rr_p6);
        p7=findViewById(R.id.rr_p7);

        //find date and time

        timedate=findViewById(R.id.rr_timedate);
        p1TimeDate=findViewById(R.id.rr_p1_timedate);
        p2TimeDate=findViewById(R.id.rr_p2_timedate);
        p3TimeDate=findViewById(R.id.rr_p3_timedate);
        p4TimeDate=findViewById(R.id.rr_p4_timedate);
        p5TimeDate=findViewById(R.id.rr_p5_timedate);
        p6TimeDate=findViewById(R.id.rr_p6_timedate);
        p7TimeDate=findViewById(R.id.rr_p7_timedate);

        //find saturday


        saturday=findViewById(R.id.rr_saturday);
        saturdayP1=findViewById(R.id.rr_saturday_p1data);
        saturdayP2=findViewById(R.id.rr_saturday_p2data);
        saturdayP3=findViewById(R.id.rr_saturday_p3data);
        saturdayP4=findViewById(R.id.rr_saturday_p4data);
        saturdayP5=findViewById(R.id.rr_saturday_p5data);
        saturdayP6=findViewById(R.id.rr_saturday_p6data);
        saturdayP7=findViewById(R.id.rr_saturday_p7data);


        //find sunday


        sunday=findViewById(R.id.rr_sunday);
        sundayP1=findViewById(R.id.rr_sunday_p1data);
        sundayP2=findViewById(R.id.rr_sunday_p2data);
        sundayP3=findViewById(R.id.rr_sunday_p3data);
        sundayP4=findViewById(R.id.rr_sunday_p4data);
        sundayP5=findViewById(R.id.rr_sunday_p5data);
        sundayP6=findViewById(R.id.rr_sunday_p6data);
        sundayP7=findViewById(R.id.rr_sunday_p7data);


        //find monday

        monday=findViewById(R.id.rr_monday);
        mondayP1=findViewById(R.id.rr_monday_p1data);
        mondayP2=findViewById(R.id.rr_monday_p2data);
        mondayP3=findViewById(R.id.rr_monday_p3data);
        mondayP4=findViewById(R.id.rr_monday_p4data);
        mondayP5=findViewById(R.id.rr_monday_p5data);
        mondayP6=findViewById(R.id.rr_monday_p6data);
        mondayP7=findViewById(R.id.rr_monday_p7data);


        //find tuesday
        tuesday=findViewById(R.id.rr_tuesday);
        tuesdayP1=findViewById(R.id.rr_tuesday_p1data);
        tuesdayP2=findViewById(R.id.rr_tuesday_p2data);
        tuesdayP3=findViewById(R.id.rr_tuesday_p3data);
        tuesdayP4=findViewById(R.id.rr_tuesday_p4data);
        tuesdayP5=findViewById(R.id.rr_tuesday_p5data);
        tuesdayP6=findViewById(R.id.rr_tuesday_p6data);
        tuesdayP7=findViewById(R.id.rr_tuesday_p7data);

        //find wednesday


        wednesday=findViewById(R.id.rr_wednesday);
        wednesdayP1=findViewById(R.id.rr_wednesday_p1data);
        wednesdayP2=findViewById(R.id.rr_wednesday_p2data);
        wednesdayP3=findViewById(R.id.rr_wednesday_p3data);
        wednesdayP4=findViewById(R.id.rr_wednesday_p4data);
        wednesdayP5=findViewById(R.id.rr_wednesday_p5data);
        wednesdayP6=findViewById(R.id.rr_wednesday_p6data);
        wednesdayP7=findViewById(R.id.rr_wednesday_p7data);

        //find thursday


        thursday=findViewById(R.id.rr_thursday);
        thursdayP1=findViewById(R.id.rr_thursday_p1data);
        thursdayP2=findViewById(R.id.rr_thursday_p2data);
        thursdayP3=findViewById(R.id.rr_thursday_p3data);
        thursdayP4=findViewById(R.id.rr_thursday_p4data);
        thursdayP5=findViewById(R.id.rr_thursday_p5data);
        thursdayP6=findViewById(R.id.rr_thursday_p6data);
        thursdayP7=findViewById(R.id.rr_thursday_p7data);



    }

    protected void onGetData() {

            RoutineManagement routineManagement=new RoutineManagement(
                    StudentRoutineActivity.this,
                    routineDataList,
                    saturday,
                    saturdayP1,
                    saturdayP2,
                    saturdayP3,
                    saturdayP4,
                    saturdayP5,
                    saturdayP6,
                    saturdayP7,
                    sunday,
                    sundayP1,
                    sundayP2,
                    sundayP3,
                    sundayP4,
                    sundayP5,
                    sundayP6,
                    sundayP7,
                    monday,
                    mondayP1,
                    mondayP2,
                    mondayP3,
                    mondayP4,
                    mondayP5,
                    mondayP6,
                    mondayP7,
                    tuesday,
                    tuesdayP1,
                    tuesdayP2,
                    tuesdayP3,tuesdayP4,
                    tuesdayP5,
                    tuesdayP6,
                    tuesdayP7,
                    wednesday,
                    wednesdayP1,
                    wednesdayP2,
                    wednesdayP3,
                    wednesdayP4,
                    wednesdayP5,
                    wednesdayP6,
                    wednesdayP7,
                    thursday,
                    thursdayP1,
                    thursdayP2,
                    thursdayP3,
                    thursdayP4,
                    thursdayP5,
                    thursdayP6,
                    thursdayP7
            );
            routineManagement.setRoutineText();







    }

}