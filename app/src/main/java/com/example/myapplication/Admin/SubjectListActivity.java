package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.myapplication.Admin.Adapter.SubjectAdapter;
import com.example.myapplication.Admin.DataModuler.StudentDataModuler;
import com.example.myapplication.Admin.DataModuler.SubjectDataModuler;
import com.example.myapplication.Admin.DataModuler.TeacherDataModuler;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.StudentListAdapter;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectListActivity extends AppCompatActivity {

    private Toolbar toolbar;


    private  Button addButton;
    private Spinner departmentSpinner,semesterSpinner;

   private  TextView diolougeHeader;
   private SpinnerCustomAdapter departmentAdapter,semesterAdapter;
   private ProgressDialog progressDialog;

   String[] departments={
           "Computer",
           "Electrical",
           "Civil",
           "Power",
           "Mechanical",
           "Electronics",
           "Electromedical"
   };
   String[] semesters={
           "Semester-1",
           "Semester-2",
           "Semester-3",
           "Semester-4",
           "Semester-5",
           "Semester-6",
           "Semester-7",
           "Semester-8"
   };



   String selectedDepartment="Computer",selectedSemester="Semester-1",subjectName,subjectCode;
   private List<SubjectDataModuler> subjectDataList=new ArrayList<>();
   private SubjectAdapter subjectAdapter;
   private TextView headerTextview;
   private RecyclerView recyclerView;


   //add diolouge variable

    private EditText subjectNameEditext,subjectCodeEdittext;
    private Spinner addSubjectDepartSpinner,addSubjectSemesterSpinner;
    private  Button subjectsaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);


        progressDialog=new ProgressDialog(this);


        toolbar=findViewById(R.id.admin_addSubjectToolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Subject List");

        headerTextview=findViewById(R.id.admin_subject_headerTextviewid);
        recyclerView=findViewById(R.id.adminSubjectListRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        headerTextview.setText(selectedDepartment+"("+selectedSemester+")");


        departmentAdapter=new SpinnerCustomAdapter(this,departments);
        semesterAdapter=new SpinnerCustomAdapter(this,semesters);
        subjectAdapter=new SubjectAdapter(this,subjectDataList);


        recyclerView.setAdapter(subjectAdapter);




        subjectAdapter.setOnItemClickListner(new SubjectAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDelete(int position) {
                deleteSubject(position);
            }

            @Override
            public void onUpdate(int position) {

            }
        });










    }


    @Override
    protected void onStart() {
        super.onStart();


        getSubjectList(selectedDepartment,selectedSemester);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.studentlist_menu_itemid,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.studentmenu_FindByid){
            selectDiolouge();
        }else if(item.getItemId()==R.id.studentmenu_AddButtonid){
            addSubjectDiolouge();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSubject(int position){
        progressDialog.setTitle("Deleting");
        progressDialog.show();
        SubjectDataModuler datalist=subjectDataList.get(position);
        String uid=datalist.getId();
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(SubjectListActivity.this);
        String url=urls.getSubject()+uid;

        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                     onStart();
                    Toast.makeText(SubjectListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SubjectListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);




    }

    private void saveSubject(final AlertDialog dialog) {
        progressDialog.setTitle("Saving Subject ....");
        progressDialog.show();
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(SubjectListActivity.this);
        String url=urls.getSubject();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    String r=  jsonObject.getString("r");
                    if(r.equals("c")){

                        subjectCodeEdittext.setError("This Subject Already Exists");
                        subjectCodeEdittext.requestFocus();

                   }else if(r.equals("d")){

                        Toast.makeText(SubjectListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                       onStart();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SubjectListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("name",subjectName);
                parms.put("code",subjectCode);
                parms.put("department",selectedDepartment);
                parms.put("semester",selectedSemester);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void  getSubjectList(String d, String s){
       progressDialog.setTitle("Loading");
       progressDialog.show();
       URLS urls=new URLS();
       String url=urls.getSubject()+d+"/"+s;

       RequestQueue requestQueue= Volley.newRequestQueue(SubjectListActivity.this);

       StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {

                   progressDialog.dismiss();

                   JSONObject jsonObject=new JSONObject(response);
                   JSONArray array=jsonObject.getJSONArray("result");
                   subjectDataList.clear();


                   for(int i=0; i<array.length(); i++){

                       JSONObject receive=array.getJSONObject(i);
                       SubjectDataModuler subjectDataModuler=new SubjectDataModuler(
                               receive.getString("_id"),
                               receive.getString("name"),
                               receive.getString("code"),
                               receive.getString("department"),
                               receive.getString("semester")
                       );

                       subjectDataList.add(subjectDataModuler);
                       subjectAdapter.notifyDataSetChanged();
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
               Toast.makeText(SubjectListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
           }
       });
       requestQueue.add(stringRequest);




    }

    public void selectDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SubjectListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.student_shift_group_diolouge,null);
        builder.setView(view);
        departmentSpinner=view.findViewById(R.id.studentlist_diolougeGroupSpinnerid);
        diolougeHeader=view.findViewById(R.id.studentlistselect_Textviewid);
        semesterSpinner=view.findViewById(R.id.studentList_diolougeShiftSpinnerid);
        addButton=view.findViewById(R.id.studentlistSelect_AddButtonid);
        addButton.setText("Ok");
        diolougeHeader.setText("Choose Department and Semester");


        departmentSpinner.setAdapter(departmentAdapter);
        semesterSpinner.setAdapter(semesterAdapter);


        final AlertDialog dialog=builder.create();
        dialog.show();

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment=departments[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSemester=semesters[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    subjectDataList.clear();
                    subjectAdapter.notifyDataSetChanged();
                    onStart();
                headerTextview.setText(selectedDepartment+"("+selectedSemester+")");

                dialog.dismiss();
            }
        });
    }
    public void addSubjectDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SubjectListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.subject_add_custom_diolouge,null);
        builder.setView(view);
        subjectNameEditext=view.findViewById(R.id.addSubject_SubjectNameEdittextid);
        subjectCodeEdittext=view.findViewById(R.id.addSubject_SubjectCodeEdittextid);
        addSubjectDepartSpinner=view.findViewById(R.id.addSubject_departmentSpinnerid);
        addSubjectSemesterSpinner=view.findViewById(R.id.addSubject_SemesterSpinnerid);

        subjectsaveButton=view.findViewById(R.id.addSubject_SaveButtonid);


        addSubjectDepartSpinner.setAdapter(departmentAdapter);
        addSubjectSemesterSpinner.setAdapter(semesterAdapter);


        final AlertDialog dialog=builder.create();
        dialog.show();

        addSubjectDepartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment=departments[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); addSubjectSemesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSemester=semesters[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subjectsaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    subjectName=subjectNameEditext.getText().toString();
                    subjectCode=subjectCodeEdittext.getText().toString();
                    if(subjectName.isEmpty()){
                        subjectNameEditext.setError("Enter Subject Name");
                        subjectNameEditext.requestFocus();
                    }else if(subjectCode.isEmpty()){
                        subjectCodeEdittext.setError("Enter Subject Code");
                        subjectCodeEdittext.requestFocus();
                    }else{
                        headerTextview.setText(selectedDepartment+"("+selectedSemester+")");
                        saveSubject(dialog);
                    }

            }
        });
    }


}