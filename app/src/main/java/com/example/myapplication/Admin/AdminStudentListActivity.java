package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.myapplication.Admin.Adapter.StudentAdapter;
import com.example.myapplication.Admin.DataModuler.StudentDataModuler;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminStudentListActivity extends AppCompatActivity {

    String department,semester,group,shift,name,roll,registration,phone,getgroup,getshift;

    private Spinner groupSpinner,shiftSpinner;
    private TextView diolougeHeader;
    private Button addButton;
    String[] groups={"A","B"};
    String[] shifts={"First","Second"};
    private EditText nameEdittext,rollEdittext,phoneEdittext,registrationEdittext;
    private  TextView headerTextview;
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private List<StudentDataModuler> studentDataList=new ArrayList<>();

    private  ProgressDialog progressDialog1,progressDialog2;



    private SpinnerCustomAdapter groupsAdapter,shiftsAdapter;

    String totalShortform;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_list);

        progressDialog1=new ProgressDialog(this);
        progressDialog2=new ProgressDialog(this);
      progressDialog2.setTitle("Saving Data..");
        progressDialog1.setCanceledOnTouchOutside(false);
        progressDialog2.setCanceledOnTouchOutside(false);

        toolbar=findViewById(R.id.admin_studentLIstToolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Students");

        department=getIntent().getStringExtra("department");
          semester=getIntent().getStringExtra("semester");
        groupsAdapter=new SpinnerCustomAdapter(this,groups);
        shiftsAdapter=new SpinnerCustomAdapter(this,shifts);
        selectDiolouge(department,semester);


        recyclerView=findViewById(R.id.adminstudentlist_Recyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentAdapter=new StudentAdapter(this,studentDataList);
        recyclerView.setAdapter(studentAdapter);


        studentAdapter.setOnItemClickListner(new StudentAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(AdminStudentListActivity.this, ""+position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDelete(int position) {
                Toast.makeText(AdminStudentListActivity.this, ""+position, Toast.LENGTH_SHORT).show();
              deleteStudent(position);
            }

            @Override
            public void onUpdate(int position) {
                Toast.makeText(AdminStudentListActivity.this, ""+position, Toast.LENGTH_SHORT).show();

             updateStudentDiolouge(position);
            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllStudent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.studentlist_menu_itemid,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.studentmenu_AddButtonid){
            addStudentDiolouge();
        }
        else if(item.getItemId()==R.id.studentmenu_FindByid){
            selectDiolouge(department,semester);
        }



        return super.onOptionsItemSelected(item);
    }



    public void addStudentDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminStudentListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.student_diolouge_custom_layout,null);
        builder.setView(view);
        nameEdittext=view.findViewById(R.id.student_diolouge_NameEdittext);
        rollEdittext=view.findViewById(R.id.student_diolouge_RollEdittext);
        registrationEdittext=view.findViewById(R.id.student_diolouge_RegistrationEdittext);
       phoneEdittext=view.findViewById(R.id.student_diolouge_PhoneEdittext);
       addButton=view.findViewById(R.id.student_diolouge_Addbuttonid);
       headerTextview=view.findViewById(R.id.student_diolouge_HeaderTextviewid);
       
       headerTextview.setText("Department: "+department+"   Group: "+group+"   Semester: "+semester+"  Shift: "+shift);



        final AlertDialog dialog=builder.create();
        dialog.show();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=nameEdittext.getText().toString();
                roll=rollEdittext.getText().toString();
                phone=phoneEdittext.getText().toString();
                registration=registrationEdittext.getText().toString();


                if(name.isEmpty()){
                    nameEdittext.setError("Enter Student Name");
                    nameEdittext.requestFocus();
                }else if(roll.isEmpty()){
                    rollEdittext.setError("Enter Student Roll");
                    rollEdittext.requestFocus();
                }else if(registration.isEmpty()){
                    registrationEdittext.setError("Enter Registration ");
                    registrationEdittext.requestFocus();
                }else{
                    saveStudent(dialog);
                }
            }
        });
    }
    public void selectDiolouge(final String department, final String semester) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminStudentListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.student_shift_group_diolouge,null);
        builder.setView(view);
        groupSpinner=view.findViewById(R.id.studentlist_diolougeGroupSpinnerid);
        diolougeHeader=view.findViewById(R.id.studentlistselect_Textviewid);
        shiftSpinner=view.findViewById(R.id.studentList_diolougeShiftSpinnerid);
        addButton=view.findViewById(R.id.studentlistSelect_AddButtonid);

        diolougeHeader.setText(department+" ( "+semester+" )");


        groupSpinner.setAdapter(groupsAdapter);
        shiftSpinner.setAdapter(shiftsAdapter);


        final AlertDialog dialog=builder.create();
        dialog.show();

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group=groups[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shift=shifts[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveShiftGroups(shift,group,dialog);

                getgroup=getGroups();
                getshift=getShift();
                // onStart();
                studentDataList.clear();
                studentAdapter.notifyDataSetChanged();
                progressDialog1.setTitle("Loading");
                progressDialog1.show();

                getAllStudent();



//                String shrtdep=makeDepartmentShort(department);
//                String shrtshift=makeShiftShort(getshift);
//                String shrtsem=makeSemesterShort(semester);

                //    totalShortform= shrtsem+""+shrtdep+""+group+""+shrtshift;

            }
        });
    }



    private void getAllStudent() {


        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(AdminStudentListActivity.this);
        String url=urls.getSaveStudent()+getgroup+"/"+getshift+"/"+department+"/"+semester;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog1.dismiss();
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
                                receive.getString("image"),
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
                        studentAdapter.notifyDataSetChanged();


                    }

                } catch (JSONException e) {
                    progressDialog1.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(AdminStudentListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);



    }


    public void updateStudentDiolouge(int position) {

        final StudentDataModuler data=studentDataList.get(position);




        AlertDialog.Builder builder=new AlertDialog.Builder(AdminStudentListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.student_diolouge_custom_layout,null);
        builder.setView(view);
        nameEdittext=view.findViewById(R.id.student_diolouge_NameEdittext);
        rollEdittext=view.findViewById(R.id.student_diolouge_RollEdittext);
        registrationEdittext=view.findViewById(R.id.student_diolouge_RegistrationEdittext);
        phoneEdittext=view.findViewById(R.id.student_diolouge_PhoneEdittext);
        addButton=view.findViewById(R.id.student_diolouge_Addbuttonid);
        headerTextview=view.findViewById(R.id.student_diolouge_HeaderTextviewid);

        headerTextview.setText("Department: "+department+"   Group: "+group+"   Semester: "+semester+"  Shift: "+shift);
        nameEdittext.setText(data.getName());
        rollEdittext.setText(data.getRoll());
        registrationEdittext.setText(data.getRegistration());
        phoneEdittext.setText(data.getPhone());


        final AlertDialog dialog=builder.create();
        dialog.show();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=nameEdittext.getText().toString();
                roll=rollEdittext.getText().toString();
                phone=phoneEdittext.getText().toString();
                registration=registrationEdittext.getText().toString();


                if(name.isEmpty()){
                    nameEdittext.setError("Enter Student Name");
                    nameEdittext.requestFocus();
                }else if(roll.isEmpty()){
                    rollEdittext.setError("Enter Student Roll");
                    rollEdittext.requestFocus();
                }else if(registration.isEmpty()){
                    registrationEdittext.setError("Enter Registration ");
                    registrationEdittext.requestFocus();
                }else{
                    updateStudent(dialog,data.getId());
                }
            }
        });
    }






    private void updateStudent(final AlertDialog dialog,String id) {
    progressDialog1.setTitle("Updating");

    progressDialog1.show();
        URLS urls=new URLS();

        RequestQueue requestQueue= Volley.newRequestQueue(AdminStudentListActivity.this);
        String url=urls.getSaveStudent()+id;

        StringRequest stringRequest=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog1.dismiss();
                    JSONObject jsonObject=new JSONObject(response);

                    Toast.makeText(AdminStudentListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    studentDataList.clear();
                    onStart();
                } catch (JSONException e) {
                    progressDialog1.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(AdminStudentListActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("name",name);
                parms.put("roll",roll);
                parms.put("registration",registration);
                parms.put("shift",getshift);
                parms.put("department",department);
                parms.put("phone",phone);
                parms.put("image","");
                parms.put("group",getgroup);
                parms.put("semester",semester);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void saveStudent(final AlertDialog dialog) {

        progressDialog2.show();
        URLS urls=new URLS();

        RequestQueue requestQueue= Volley.newRequestQueue(AdminStudentListActivity.this);
        String url=urls.getSaveStudent();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog2.dismiss();
                    JSONObject jsonObject=new JSONObject(response);

                    Toast.makeText(AdminStudentListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    int nextRoll=(Integer.parseInt(roll))+1;
                    int nextReg=(Integer.parseInt(registration))+1;
                    nameEdittext.setText("");
                    rollEdittext.setText(""+nextRoll);
                    registrationEdittext.setText(""+nextReg);
                    phoneEdittext.setText("");

                    studentDataList.clear();
                    onStart();
                } catch (JSONException e) {
                    progressDialog2.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog2.dismiss();
                Toast.makeText(AdminStudentListActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("name",name);
                parms.put("roll",roll);
                parms.put("registration",registration);
                parms.put("shift",getshift);
                parms.put("department",department);
                parms.put("phone",phone);
                parms.put("image","");
                parms.put("group",getgroup);
                parms.put("semester",semester);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }


private void deleteStudent(int position){
        progressDialog1.setTitle("Deleting");
        progressDialog1.show();
        StudentDataModuler datalist=studentDataList.get(position);
        String uid=datalist.getId();
    Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();
    URLS urls=new URLS();
    RequestQueue requestQueue= Volley.newRequestQueue(AdminStudentListActivity.this);
    String url=urls.getSaveStudent()+uid;

    StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                progressDialog1.dismiss();
                JSONObject jsonObject=new JSONObject(response);
                studentDataList.clear();
                studentAdapter.notifyDataSetChanged();
                onStart();
                Toast.makeText(AdminStudentListActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                progressDialog1.dismiss();
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog1.dismiss();
            Toast.makeText(AdminStudentListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    });
    requestQueue.add(stringRequest);




}






    private void saveShiftGroups(String shift, String group, AlertDialog dialog) {
        SharedPreferences sharedPreferences=getSharedPreferences("gps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("shift",shift);
        editor.putString("group",group);
        editor.commit();
        dialog.dismiss();
    }


    public String getGroups(){
        SharedPreferences sharedPreferences=getSharedPreferences("gps", Context.MODE_PRIVATE);
        String group=sharedPreferences.getString("group","");
        return  group;
    }
    public String getShift(){
        SharedPreferences sharedPreferences=getSharedPreferences("gps", Context.MODE_PRIVATE);
        String shift=sharedPreferences.getString("shift","");
        return  shift;
    }


    public String makeDepartmentShort(String department){
        String shrt = null;
        if(department.equals("Computer")){
            shrt="Cmt";
        } else if(department.equals("Electrical")){
            shrt="Ele";
        }else if(department.equals("Mechanical")){
            shrt="Mc";
        }
            else if(department.equals("Civil")){
            shrt="Cv";
        } else if(department.equals("Electronics")){
            shrt="Elc";
        }else if(department.equals("Power")){
            shrt="Pow";
        }else if(department.equals("Electromedical")){
            shrt="Elm";
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