package com.example.myapplication.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.StudentAdapter;
import com.example.myapplication.Admin.AdminStudentListActivity;
import com.example.myapplication.Admin.DataModuler.StudentDataModuler;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.StudentListAdapter;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherStudentPresentActivity extends AppCompatActivity {
    private  String group,shift,department,semester,subjectCode;
    private List<StudentDataModuler> studentDataList=new ArrayList<>();
    private StudentListAdapter studentAdapter;
    private RecyclerView recyclerView;

    private  ProgressDialog progressDialog;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_student_present);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        department=getIntent().getStringExtra("department");
        group=getIntent().getStringExtra("groups");
        shift=getIntent().getStringExtra("shift");
        semester=getIntent().getStringExtra("semester");
        subjectCode=getIntent().getStringExtra("subjectCode");

       String shrtDepartmentName=makeSemesterShort(semester)+makeDepartmentShort(department)+group+makeShiftShort(shift);

        toolbar=findViewById(R.id.studentLIst_Toolbarid);
        setSupportActionBar(toolbar);
        this.setTitle(shrtDepartmentName);
        recyclerView=findViewById(R.id.studentpresentLIst_Recycclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentAdapter=new StudentListAdapter(this,TeacherStudentPresentActivity.this,subjectCode,department,group,shift,semester,studentDataList);
        recyclerView.setAdapter(studentAdapter);






    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllStudent();
    }

    private void getAllStudent() {

        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TeacherStudentPresentActivity.this);
        String url=urls.getSaveStudent()+group+"/"+shift+"/"+department+"/"+semester;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    studentDataList.clear();
                    for(int i=0; i<array.length(); i++){

                        JSONObject receive=array.getJSONObject(i);
                        StudentDataModuler studentDataModuler=new StudentDataModuler(
                                receive.getString("_id"),
                                receive.getString("name"),
                                receive.getString("roll"),
                                receive.getString("registration"),
                                receive.getString("groups"),
                                receive.getString("shifts"),
                                receive.getString("department"),
                                "",
                                receive.getString("phone"),
                                receive.getString("semester"),
                                "",
                                "",
                                "",
                                       "",
                                "",
                                "",
                             "",
                               ""
                        );

                        studentDataList.add(studentDataModuler);
                        progressDialog.dismiss();
                        studentAdapter.notifyDataSetChanged();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeacherStudentPresentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);



    }



    public String makeDepartmentShort(String department){
        String shrt = null;
        if(department.equals("Computer")){
            shrt="Cmt";
        } else if(department.equals("Electrical")){
            shrt="Et";
        }else if(department.equals("Mechanical")){
            shrt="Mt";
        }
        else if(department.equals("Civil")){
            shrt="Ct";
        } else if(department.equals("Electronics")){
            shrt="Ect";
        }else if(department.equals("Power")){
            shrt="Pt";
        }else if(department.equals("Electromedical")){
            shrt="Emt";
        }
        return  shrt;
    }
    public String makeShiftShort(String shift){
        String shrt=null;
        if(shift.equals("First")){
            shrt="1";
        }if(shift.equals("Second")){
            shrt="2";
        }
        return  shrt;
    }
    public String makeSemesterShort(String semester){
        String shrt=null;

        if(semester.equals("Semester-1")){
            shrt="1";
        }else  if(semester.equals("Semester-2")){
            shrt="2";
        }else  if(semester.equals("Semester-3")){
            shrt="3";
        }else  if(semester.equals("Semester-4")){
            shrt="4";
        }else  if(semester.equals("Semester-5")){
            shrt="5";
        }else  if(semester.equals("Semester-6")){
            shrt="6";
        }
        else  if(semester.equals("Semester-7")){
            shrt="7";
        } else  if(semester.equals("Semester-8")){
            shrt="8";
        }
        return  shrt;
    }


}