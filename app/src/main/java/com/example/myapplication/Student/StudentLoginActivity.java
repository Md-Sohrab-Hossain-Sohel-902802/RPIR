package com.example.myapplication.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.LocalStorage.Shared.StudentShared;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.TeacherLoginActivity;
import com.example.myapplication.Teacher.TeacherMainActivity;
import com.example.myapplication.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentLoginActivity extends AppCompatActivity {
    private  EditText rollEdittext,registrationEdittext;
    private Button loginButton;

    private String  roll,registration;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loging in");


        rollEdittext=findViewById(R.id.s_login_rollEdittextid);
        registrationEdittext=findViewById(R.id.s_login_registrationEdittextid);
        loginButton=findViewById(R.id.s_login_LoginButtonid);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll=rollEdittext.getText().toString();
                registration=registrationEdittext.getText().toString();



                if(roll.isEmpty()){
                    rollEdittext.setError("Enter Your Roll");
                    rollEdittext.requestFocus();
                }else if(registration.isEmpty()){
                    registrationEdittext.setError("Enter Your Registration ");
                    registrationEdittext.requestFocus();
                }
                else{
                    loginStudent();
                }
            }

        });















    }


    private void loginStudent() {


        URLS urls=new URLS();
        String url=urls.getLoginStudent();
        progressDialog.show();

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject=new JSONObject(s);

                    String result=jsonObject.getString("result");
                    if(result.equals("r")){
                        rollEdittext.setError("Roll Number Doesn\'t Match");
                        rollEdittext.requestFocus();
                        progressDialog.dismiss();
                    } else if(result.equals("rg")){
                        registrationEdittext.setError("Registration Number Doesn\'t Match");
                        registrationEdittext.requestFocus();
                        progressDialog.dismiss();
                    }
                    else if(result.equals("d")){

                        UsersShared store=new UsersShared(StudentLoginActivity.this);
                        StudentShared store2=new StudentShared(StudentLoginActivity.this);

                        JSONObject jsonObject2=new JSONObject(jsonObject.getString("send"));
                        store.saveUserdata("", "", "", "", "", "student", "", "", "", "", "");
                        store2.saveStudent(
                                jsonObject2.getString("id"),
                                jsonObject2.getString("name"),
                                jsonObject2.getString("roll"),
                                jsonObject2.getString("registration"),
                                jsonObject2.getString("phone"),
                                jsonObject2.getString("group"),
                                jsonObject2.getString("shift"),
                                jsonObject2.getString("department"),
                                jsonObject2.getString("semester"),
                                jsonObject2.getString("image"),
                                jsonObject2.getString("usertype")
                        );
                        progressDialog.dismiss();
                        Toast.makeText(StudentLoginActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StudentLoginActivity.this, StudentMainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(StudentLoginActivity.this, ""+volleyError, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("roll",roll);
                parms.put("registration",registration);
               return  parms;
            }
        };


        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);





    }
}