package com.example.myapplication.Teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.AdminAddTeacherActivity;
import com.example.myapplication.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PresentController {
    private  Context context;
    String teacherCode,presence,day,date,time,department,group,shift,semester,name,roll,registration,subjectName,subjectCode;

    public PresentController(Context context,String teacherCode, String presence, String day, String date, String time, String department, String group, String shift, String semester, String name, String roll, String registration, String subjectName, String subjectCode) {
        this.context = context;
        this.teacherCode=teacherCode;
        this.presence = presence;
        this.day = day;
        this.date = date;
        this.time = time;
        this.department = department;
        this.group = group;
        this.shift = shift;
        this.semester = semester;
        this.name = name;
        this.roll = roll;
        this.registration = registration;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    public void presentSubmit() {
         URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        String url=urls.getPresent();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("presence",presence);
                parms.put("day",day);
                parms.put("date",date);
                parms.put("time",time);
                parms.put("department",department);
                parms.put("group",group);
                parms.put("shift",shift);
                parms.put("semester",semester);
                parms.put("name",name);
                parms.put("roll",roll);
                parms.put("registration",registration);
                parms.put("subjectName",subjectName);
                parms.put("subjectCode",subjectCode);
                parms.put("teacherCode",teacherCode);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }


}
