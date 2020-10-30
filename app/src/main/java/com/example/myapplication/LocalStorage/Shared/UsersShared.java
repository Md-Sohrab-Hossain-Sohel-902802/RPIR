package com.example.myapplication.LocalStorage.Shared;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UsersShared {

    Activity activity;

    public UsersShared(Activity activity) {
        this.activity = activity;
    }

    public void  saveUserdata(String id,String name,String institutename,String image,String gender,String usertype,String password,String email,String phone,String department,String teacherCode){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("image",image);
        editor.putString("gender",gender);
        editor.putString("phone",phone);
        editor.putString("institute",institutename);
        editor.putString("usertype",usertype);
        editor.putString("password",password);
        editor.putString("teacherCode",teacherCode);
        editor.putString("department",department);

        editor.commit();
    }

    public String getEmail(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String email=sharedPreferences.getString("email","");
        return  email;
    }
    public String getInstitute(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String institute=sharedPreferences.getString("institute","");
        return  institute;
    }
    public String getphone(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String eiin=sharedPreferences.getString("phone","");
        return  eiin;
    }
    public String getUertype(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String eiin=sharedPreferences.getString("usertype","");
        return  eiin;
    }
  public String getTeacherCode(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String eiin=sharedPreferences.getString("teacherCode","");
        return  eiin;
    }
  public String getId(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String eiin=sharedPreferences.getString("id","");
        return  eiin;
    }





}
