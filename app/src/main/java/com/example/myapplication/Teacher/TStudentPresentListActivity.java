package com.example.myapplication.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.DataModuler.StudentDataModuler;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.PresentListAdapter;
import com.example.myapplication.Teacher.Adapter.StudentListAdapter;
import com.example.myapplication.Teacher.DataModuler.PresentDataModuler;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TStudentPresentListActivity extends AppCompatActivity {

    private  String name,roll,registration,department,semester,group,shift,subjectCode,teacherCode;
    private List<PresentDataModuler> presentDataList=new ArrayList<>();
    private PresentListAdapter presentListAdapter;
    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    public int totalCounter=0,presentCounter=0,absentcounter=0;
    private TextView presentDetailsTextview;

    private UsersShared shared;


    private EditText diolougeClassNameEdittext;
    private TextView diolougeTextviewid;
    private Button diolougeOkButtonid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_student_present_list);
        shared=new UsersShared(this);


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);




        roll=getIntent().getStringExtra("roll");
        name=getIntent().getStringExtra("name");
        registration=getIntent().getStringExtra("registration");
        department=getIntent().getStringExtra("department");
        semester=getIntent().getStringExtra("semester");
        group=getIntent().getStringExtra("group");
        shift=getIntent().getStringExtra("shift");
        subjectCode=getIntent().getStringExtra("subjectCode");
        teacherCode= shared.getTeacherCode();




        toolbar=findViewById(R.id.presentLIstToolbarid);
        setSupportActionBar(toolbar);
        this.setTitle(name+"("+roll+")");

      recyclerView=findViewById(R.id.presentList_RecyclerViewid);
        presentDetailsTextview=findViewById(R.id.presentDetailstTextviewid);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presentListAdapter=new PresentListAdapter(TStudentPresentListActivity.this,this,presentDataList);
        recyclerView.setAdapter(presentListAdapter);



        presentListAdapter.setOnItemClickListner(new PresentListAdapter.OnItemClickListner() {
            @Override
            public void onDelete(int position) {
                    deletePresent(presentDataList.get(position).getId());
         }

            @Override
            public void onUpdate(int position) {
                PresentDataModuler selectedItem=presentDataList.get(position);
                updatePresentDiolouge(selectedItem.getId(),selectedItem.getDate(),selectedItem.getPresence());


            }
        });







    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllPresent();
    }



    private void getAllPresent() {


                progressDialog.show();
                URLS urls=new URLS();
                RequestQueue requestQueue= Volley.newRequestQueue(TStudentPresentListActivity.this);
                String url=urls.getPresent()+department+"/"+group+"/"+semester+"/"+shift+"/"+roll+"/"+registration+"/"+subjectCode+"/"+teacherCode;

                StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array=jsonObject.getJSONArray("result");
                            presentDataList.clear();
                            totalCounter=0;
                            presentCounter=0;
                            absentcounter=0;

                            for(int i=0; i<array.length(); i++){

                                JSONObject receive=array.getJSONObject(i);
                                totalCounter++;

                                String presentance=  receive.getString("presence");
                                if(presentance.equals("Present")){
                                    presentCounter++;
                                }else if(presentance.equals("Absent")){
                                    absentcounter++;
                                }




                                PresentDataModuler presentDataModuler=new PresentDataModuler(
                                        receive.getString("_id"),
                                        receive.getString("presence"),
                                        receive.getString("day"),
                                        receive.getString("date"),
                                        receive.getString("time"),
                                        receive.getString("department"),
                                        receive.getString("group"),
                                        receive.getString("shift"),
                                        receive.getString("semester"),
                                        receive.getString("name"),
                                        receive.getString("roll"),
                                        receive.getString("registration"),
                                        receive.getString("subjectName"),
                                        receive.getString("subjectCode"),
                                        receive.getString("teacherCode")

                                );

                                presentDataList.add(presentDataModuler);
                                presentListAdapter.notifyDataSetChanged();


                            }
                            presentDetailsTextview.setText(name+": \nTotal  Class : "+totalCounter+"\nPresent: "+presentCounter+"              Absent : "+absentcounter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TStudentPresentListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);



    }


    public void deletePresent(String id){
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TStudentPresentListActivity.this);
        String url=urls.getPresent()+id;

        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(TStudentPresentListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    presentDataList.clear();
                    onStart();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TStudentPresentListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }




    private void updatePresentDiolouge(final String key,String date,String pattendence) {


        AlertDialog.Builder builder=new AlertDialog.Builder(TStudentPresentListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.present_update_customdiolouge,null);
        builder.setView(view);
        diolougeClassNameEdittext=view.findViewById(R.id.classlistDiolouge_ClassnameEdittextid);
        diolougeOkButtonid=view.findViewById(R.id.classlistDiolouge_OkButtonid);
        diolougeTextviewid=view.findViewById(R.id.classlistDiolouge_tilteTextviewid);

        diolougeTextviewid.setText("Date : "+date+"("+pattendence+")");

        diolougeClassNameEdittext.setHint("Enter Attendance (Present or Absent)");

        final AlertDialog dialog=builder.create();
        dialog.show();
        diolougeOkButtonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String attendance=diolougeClassNameEdittext.getText().toString();


                if(attendance.isEmpty()){
                    diolougeClassNameEdittext.setError("Enter Attendance (Present or Absent)");
                    diolougeClassNameEdittext.requestFocus();
                    return;
                }else if(!(attendance.equals("Present") || attendance.equals("Absent"))){
                    diolougeClassNameEdittext.setError("Attendance Can't be different (Present or Absent) ");
                    diolougeClassNameEdittext.requestFocus();
                    return;
                }
                else{

                    updatePresent(dialog,key,attendance);

                }

            }
        });
    }




    private void updatePresent(final AlertDialog dialog, String id, final String attendance) {
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TStudentPresentListActivity.this);
        String url=urls.getPresent()+id;

        StringRequest stringRequest=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(TStudentPresentListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    presentDataList.clear();
                    onStart();


                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TStudentPresentListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("presence",attendance);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }




}