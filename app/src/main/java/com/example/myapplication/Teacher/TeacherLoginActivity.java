package com.example.myapplication.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.SpinnerCustomAdapter;
import com.example.myapplication.Admin.AdminAddTeacherActivity;
import com.example.myapplication.Admin.AdminLoginActivity;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeacherLoginActivity extends AppCompatActivity {


    private EditText phoneEdittext,passwordEdittext;
    private  TextView registerTextveiw;
    private Button loginButton;

    private  ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait");


        phoneEdittext=findViewById(R.id.t_login_phoneEdittextid);
        passwordEdittext=findViewById(R.id.t_login_passwordEdittextid);
        registerTextveiw=findViewById(R.id.t_login_registerTextviewid);
        loginButton=findViewById(R.id.t_login_LoginButtonid);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String phone=phoneEdittext.getText().toString();
                    String password=passwordEdittext.getText().toString();


                    if(phone.isEmpty()){
                            phoneEdittext.setError("Enter Phone Number");
                            phoneEdittext.requestFocus();

                    }else if(password.isEmpty()){
                        passwordEdittext.setError("Enter Your Password");
                        passwordEdittext.requestFocus();
                    }
                    else{
                        loginTeacher(phone,password);
                    }
            }
        });


        registerTextveiw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherLoginActivity.this,TeacherRegisterActivity.class);
                startActivity(intent);

            }
        });










    }


    private void loginTeacher(final String email, final String password) {
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
                        phoneEdittext.setError("Phone Number Incorrect");
                        phoneEdittext.requestFocus();
                        progressDialog.dismiss();
                    } else if(result.equals("p")){
                        passwordEdittext.setError(jsonObject.getString("message"));
                        passwordEdittext.requestFocus();
                        progressDialog.dismiss();
                    }
                    else if(result.equals("d")){

                        UsersShared store=new UsersShared(TeacherLoginActivity.this);

                        JSONObject jsonObject2=new JSONObject(jsonObject.getString("send"));
                        store.saveUserdata(
                                jsonObject2.getString("id"),
                                jsonObject2.getString("name"),
                               "",
                                "",
                                "",
                                "teacher",
                              "",
                               "",
                                jsonObject2.getString("phone"),
                                jsonObject2.getString("departmentname"),
                                jsonObject2.getString("code")


                        );
                        progressDialog.dismiss();
                        Toast.makeText(TeacherLoginActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TeacherLoginActivity.this, TeacherMainActivity.class));
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
               Toast.makeText(TeacherLoginActivity.this, ""+volleyError, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("email",email);
                parms.put("password",password);
                parms.put("usertype","teacher");
                return  parms;
            }
        };


        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }
}