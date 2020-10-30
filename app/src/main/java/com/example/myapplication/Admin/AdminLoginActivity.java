package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText emailEdittext,passwordEdittext;
    private Button loginButton;
    private  String email,password,usertype;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait");



        usertype=getIntent().getStringExtra("usertype");
        emailEdittext=findViewById(R.id.adminLogin_EmailEdittextid);
        passwordEdittext=findViewById(R.id.adminLogin_PasswordEdittextid);
        loginButton=findViewById(R.id.adminLogin_LoginButtonid);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    email=emailEdittext.getText().toString();
                    password=passwordEdittext.getText().toString();


                    if(email.isEmpty()){
                        emailEdittext.setError("Enter Email");
                        emailEdittext.requestFocus();
                    }else if(password.isEmpty()){
                        passwordEdittext.setError("Enter Password");
                        passwordEdittext.requestFocus();
                    }else{

                        loginUser(email,password);



                    }





            }
        });









    }

    private void loginUser(final String email, final String password) {
        URLS urls=new URLS();
        String url=urls.getLoginUrl();
        progressDialog.show();

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject=new JSONObject(s);

                    String result=jsonObject.getString("result");
                    if(result.equals("e")){
                        emailEdittext.setError(jsonObject.getString("message"));
                        emailEdittext.requestFocus();
                        progressDialog.dismiss();
                    } else if(result.equals("p")){
                        passwordEdittext.setError(jsonObject.getString("message"));
                        passwordEdittext.requestFocus();
                        progressDialog.dismiss();
                    }
                    else if(result.equals("d")){

                        UsersShared store=new UsersShared(AdminLoginActivity.this);

                        JSONObject jsonObject2=new JSONObject(jsonObject.getString("send"));
                        store.saveUserdata(
                                jsonObject2.getString("id"),
                                jsonObject2.getString("name"),
                                jsonObject2.getString("institutename"),
                                "",
                                jsonObject2.getString("gender"),
                                jsonObject2.getString("usertype"),
                                jsonObject2.getString("password"),
                                jsonObject2.getString("email"),
                                jsonObject2.getString("phone"),
                                "",
                                ""
                        );
                        progressDialog.dismiss();
                        Toast.makeText(AdminLoginActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminLoginActivity.this, MainActivity.class));
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
                emailEdittext.setText(volleyError.toString());
                Toast.makeText(AdminLoginActivity.this, ""+volleyError, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("email",email);
                parms.put("password",password);
                parms.put("usertype",usertype);
                return  parms;
            }
        };


        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }

    }