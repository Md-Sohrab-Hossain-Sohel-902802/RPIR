package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Admin.AdminLoginActivity;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.Teacher.TeacherLoginActivity;
import com.example.myapplication.Teacher.TeacherMainActivity;

public class StartActivity extends AppCompatActivity {

    private Button adminLoginButon,teacherLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        adminLoginButon=findViewById(R.id.start_adminLoginButton);
        teacherLoginButton=findViewById(R.id.start_teacherLoginButton);
        adminLoginButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StartActivity.this, AdminLoginActivity.class);
                intent.putExtra("usertype","admin");
                startActivity(intent);
                finish();
            }
        });
        teacherLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StartActivity.this, TeacherLoginActivity.class);
                intent.putExtra("usertype","teacher");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        UsersShared usersShared=new UsersShared(StartActivity.this);
        if(usersShared.getUertype().equals("admin")){
            startActivity(new Intent(StartActivity.this,MainActivity.class));
        }else  if(usersShared.getUertype().equals("teacher")){
            startActivity(new Intent(StartActivity.this, TeacherMainActivity.class));
        }








    }
}