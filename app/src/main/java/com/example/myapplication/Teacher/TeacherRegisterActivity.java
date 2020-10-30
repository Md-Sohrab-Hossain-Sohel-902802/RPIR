package com.example.myapplication.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.SpinnerCustomAdapter;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeacherRegisterActivity extends AppCompatActivity {

    private EditText codeEdittext, nameEdittext, passwordEdittext;
    private TextView codeTextview, departmentTextview, phoneTextview;
    private Spinner departmentSpinner;
    private Button searchButton, registerButton;
    private SpinnerCustomAdapter adapter;

    final String[] values = {"Non-Teach", "Computer", "Electrical", "Power", "Civil", "Mechanical", "Electronics", "Electromedical"};

    String mCode, mDepartment, mName, mPhone, mPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        codeEdittext = findViewById(R.id.teacherLogin_CodeEdittextid);
        departmentSpinner = findViewById(R.id.teacherlogin_selectDepartmentSpinnerid);
        searchButton = findViewById(R.id.teacherLogin_SearchButtonid);

        codeTextview = findViewById(R.id.teacherLogin_CodeTextviewid);
        departmentTextview = findViewById(R.id.teacherlogin_DepartmentTextviewid);
        nameEdittext = findViewById(R.id.teacherlogin_NameEdittextid);
        passwordEdittext = findViewById(R.id.teacherlogin_PasswordEdittextid);
        phoneTextview = findViewById(R.id.teacherlogin_PhoneTextviewid);
        registerButton = findViewById(R.id.teacherRegisterButtonid);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait");


        adapter = new SpinnerCustomAdapter(TeacherRegisterActivity.this, values);
        departmentSpinner.setAdapter(adapter);


        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDepartment = values[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEdittext.getText().toString();
                if (code.isEmpty()) {
                    codeEdittext.setError("Enter Your Code");
                    codeEdittext.requestFocus();
                } else {
                    validateCodeAndDepartment(code, mDepartment);
                }
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEdittext.getText().toString().isEmpty()) {
                    nameEdittext.setError("Enter Your Name");
                    nameEdittext.requestFocus();
                } else if (passwordEdittext.getText().toString().isEmpty()) {
                    passwordEdittext.setError("Enter New Password");
                    passwordEdittext.requestFocus();
                } else {
                    mName = nameEdittext.getText().toString();
                    mPassword = passwordEdittext.getText().toString();
                    registerTeacher(mName, mCode, mPhone, mDepartment, mPassword);


                }

            }
        });


    }

    private void registerTeacher(final String mName, final String mCode, final String mPhone, final String mDepartment, final String mPassword) {

        progressDialog.show();
        URLS urls = new URLS();
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherRegisterActivity.this);
        String url = urls.getRegisterTeacher();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.get("result").equals("d")) {
                        JSONObject jsonObject2 = new JSONObject(jsonObject.getString("send"));
                        UsersShared shared = new UsersShared(TeacherRegisterActivity.this);
                        shared.saveUserdata(
                                jsonObject2.getString("id"),
                                jsonObject2.getString("name"),
                                "",
                                "",
                                "",
                                "teacher",
                                "",
                                "",
                                jsonObject2.getString("phone"),
                                jsonObject2.getString("department"),
                                jsonObject2.getString("code")

                        );

                        startActivity(new Intent(TeacherRegisterActivity.this, TeacherMainActivity.class));
                        Toast.makeText(TeacherRegisterActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TeacherRegisterActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TeacherRegisterActivity.this, "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parms = new HashMap<String, String>();


                parms.put("name", mName);
                parms.put("teacherCode", mCode);
                parms.put("phone", mPhone);
                parms.put("department", mDepartment);
                parms.put("password", mPassword);
                parms.put("usertype", "teacher");
                return parms;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void validateCodeAndDepartment(final String code, final String department) {

        progressDialog.show();
        URLS urls = new URLS();
        String url = urls.getTeacherAdd() + "get/" + code + "/" + department;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    String result = jsonObject.getString("l");
                    if (result.equals("e")) {
                        progressDialog.dismiss();
                        Toast.makeText(TeacherRegisterActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (result.equals("d")) {
                        JSONObject jsonObject2 = new JSONObject(jsonObject.getString("send"));

                        codeEdittext.setVisibility(View.GONE);
                        departmentSpinner.setVisibility(View.GONE);
                        searchButton.setVisibility(View.GONE);

                        nameEdittext.setVisibility(View.VISIBLE);
                        phoneTextview.setVisibility(View.VISIBLE);
                        codeTextview.setVisibility(View.VISIBLE);
                        departmentTextview.setVisibility(View.VISIBLE);
                        passwordEdittext.setVisibility(View.VISIBLE);
                        registerButton.setVisibility(View.VISIBLE);

                        mCode = jsonObject2.getString("code");
                        mDepartment = jsonObject2.getString("departmentname");
                        mPhone = jsonObject2.getString("phone");

                        nameEdittext.setText(jsonObject2.getString("name"));
                        codeTextview.setText("Code: " + mCode);
                        departmentTextview.setText("Department: " + mDepartment);
                        phoneTextview.setText("Phone: " + mPhone);


                        //     jsonObject2.getString("image");
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(TeacherRegisterActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(TeacherRegisterActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}












