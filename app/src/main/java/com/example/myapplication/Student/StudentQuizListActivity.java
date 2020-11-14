package com.example.myapplication.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.LocalStorage.Shared.StudentShared;
import com.example.myapplication.R;
import com.example.myapplication.Student.Adapter.StudentQuizListAdapter;
import com.example.myapplication.Student.DataModuler.StudentQuizDataModuler;
import com.example.myapplication.Teacher.Adapter.QuizListAdapter;
import com.example.myapplication.Teacher.DataModuler.QuizeDataModuler;
import com.example.myapplication.Teacher.TeacherQuizeListActivity;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentQuizListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<StudentQuizDataModuler> quizDataList=new ArrayList<>();
    private ProgressDialog progressDialog;
    private String roll;

    private StudentShared shared;
    private StudentQuizListAdapter quizListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_list);

        shared=new StudentShared(this);
        progressDialog=new ProgressDialog(this);

        toolbar=findViewById(R.id.studentQuizListToolbarid);
        setSupportActionBar(toolbar);
        this.setTitle("Quiz List");


        roll=shared.getRoll();

        recyclerView=findViewById(R.id.student_QuizListRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizListAdapter=new StudentQuizListAdapter(this,quizDataList);
        recyclerView.setAdapter(quizListAdapter);



        quizListAdapter.setOnItemClickListner(new StudentQuizListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                    StudentQuizDataModuler data=quizDataList.get(position);

                    Intent intent=new Intent(StudentQuizListActivity.this,StudentQuizDetailsActivity.class);
                    intent.putExtra("image",data.getImage());
                    intent.putExtra("qName",data.getQuizName());
                    intent.putExtra("totalQuestion",data.getTotalQuestion());
                    intent.putExtra("time",data.getTime());
                    intent.putExtra("endTime",data.getEndTime());
                    intent.putExtra("key",data.getQuizId());
                    String id=data.getId();
                    startActivity(intent);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllQuiz();
    }

    public void getAllQuiz(){
        progressDialog.setTitle("Loading.....");
        progressDialog.show();
        URLS urls=new URLS();


        RequestQueue requestQueue= Volley.newRequestQueue(StudentQuizListActivity.this);
        String url=urls.getSaveStudent()+"quiz/getquiz/"+roll;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    quizDataList.clear();
                    for(int i=0; i<array.length(); i++){

                        JSONObject receive=array.getJSONObject(i);
                        StudentQuizDataModuler dataModuler=new StudentQuizDataModuler(
                                receive.getString("_id"),
                                receive.getString("quizId"),
                                receive.getString("quizStatus"),
                                receive.getString("quizName"),
                                receive.getString("image"),
                                receive.getString("totalQuestion"),
                                receive.getString("time"),
                                receive.getString("endTime")
                        );

                        quizDataList.add(dataModuler);
                        quizListAdapter.notifyDataSetChanged();
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
                Toast.makeText(StudentQuizListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);



    }
}