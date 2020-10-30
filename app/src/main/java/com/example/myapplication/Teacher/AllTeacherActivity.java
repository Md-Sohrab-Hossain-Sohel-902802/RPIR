package com.example.myapplication.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.myapplication.Admin.AdminRoutineListActivity;
import com.example.myapplication.Admin.DataModuler.RoutineDataModuler;
import com.example.myapplication.Admin.DataModuler.TeacherDataModuler;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.AllTeacherAdapter;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllTeacherActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<TeacherDataModuler> teacherdataList=new ArrayList<>();
    private AllTeacherAdapter teacherAdapter;

    private  ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_teacher);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        recyclerView=findViewById(R.id.t_AllTeacherRecyclerviewid);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacherAdapter=new AllTeacherAdapter(AllTeacherActivity.this,AllTeacherActivity.this,teacherdataList);

        recyclerView.setAdapter(teacherAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        getAllTeacher();



    }


    public void getAllTeacher(){
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(AllTeacherActivity.this);
        String url=urls.getGetteacherUser();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    teacherdataList.clear();
                  for(int i=0; i<array.length(); i++){
                        JSONObject receive=array.getJSONObject(i);

                        TeacherDataModuler dataModuler=new TeacherDataModuler(
                                receive.getString("_id"),
                                receive.getString("name"),
                                receive.getString("teacherCode"),
                                receive.getString("department"),
                                receive.getString("phone"),
                                receive.getString("image")
                        );
                        teacherdataList.add(dataModuler);
                        progressDialog.dismiss();
                        teacherAdapter.notifyDataSetChanged();

                  }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AllTeacherActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);




    }
}