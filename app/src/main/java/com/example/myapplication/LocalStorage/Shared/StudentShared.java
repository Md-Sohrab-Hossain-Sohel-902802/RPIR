package com.example.myapplication.LocalStorage.Shared;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class StudentShared {
   public  Activity activity;

    public StudentShared(Activity activity) {
        this.activity = activity;
    }

    public void  saveStudent(String id, String name, String roll, String registration, String phone, String group, String shift, String department, String semester, String image, String usertype){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("student", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("roll",roll);
        editor.putString("registration",registration);
        editor.putString("phone",phone);
        editor.putString("group",group);
        editor.putString("shift",shift);
        editor.putString("department",department);
        editor.putString("semester",semester);
        editor.putString("image",image);
        editor.putString("usertype",usertype);
        editor.commit();
    }

    public  String getName(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("student",Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("name","");
        return  name;
    }
    public  String getRoll(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("student",Context.MODE_PRIVATE);
        String roll=sharedPreferences.getString("roll","");
        return  roll;
    }
    public  String getDepartment(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("student",Context.MODE_PRIVATE);
        String department=sharedPreferences.getString("department","");
        return  department;
    }
    public  String getGroupd(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("student",Context.MODE_PRIVATE);
        String group=sharedPreferences.getString("group","");
        return  group;
    }
    public  String getShift(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("student",Context.MODE_PRIVATE);
        String shift=sharedPreferences.getString("shift","");
        return  shift;
    }
    public  String getSemester(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("student",Context.MODE_PRIVATE);
        String semester=sharedPreferences.getString("semester","");
        return  semester;
    }







}
