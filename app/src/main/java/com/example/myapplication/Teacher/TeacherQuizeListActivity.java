package com.example.myapplication.Teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Adapter.SpinnerCustomAdapter;
import com.example.myapplication.Admin.AdminAddTeacherActivity;
import com.example.myapplication.Admin.DataModuler.TeacherDataModuler;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.QuizListAdapter;
import com.example.myapplication.Teacher.DataModuler.QuizeDataModuler;
import com.example.myapplication.URLS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherQuizeListActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView quizeListRecyclerviewid;
    private FloatingActionButton floatingActionButton;

    // variable for database

    private  DatabaseReference databaseReference;
    private StorageReference storageReference;
    private  Uri imageUri;

    private UsersShared shared;
    String userId;
    //variable for diolouge

    private  EditText quizNameEidttext;
    private  Spinner accessSpinner;
    private  Button quizSaveButton;
    private String quizeName,selectedAccess;
    private  String quizKey;

    String[] access={"Only me","All Teachers","All Student"};
    String[] departments={"Computer","Electrical","Power","Mechanical","Civil","Electronics","Electromedical"};
    String[] groups={"A","B"};
    String[] shifts={"First","Second"};
    String[] semesters={"Semester-1","Semester-2","Semester-3","Semester-4","Semester-5","Semester-6","Semester-7","Semester-8"};
    String[] times={"5min","10min","15min","20min"};

    private SpinnerCustomAdapter accessSpinnerAdapter;

    private List<QuizeDataModuler> quizDataList=new ArrayList<>();
    private QuizListAdapter quizListAdapter;

    private ProgressDialog progressDialog;

    private  SpinnerCustomAdapter timeAdapter,departmentAdapter,groupAdapter,shiftAdapter,semesterAdapter;

    private  Spinner timeSpinner,groupSpinner,shiftSpinner,semesterSpinner,departmentSpinner;
    private  Button publishquizeTakeExamButton;
     String group,shift,semester,department,time;
     long timeinMIllis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_quize_list);

        toolbar=findViewById(R.id.teacher_Quize_ListToolbarid);
        setSupportActionBar(toolbar);
        this.setTitle("Quiz List");

        quizeListRecyclerviewid=findViewById(R.id.quiz_listRecyclerviewid);
        floatingActionButton=findViewById(R.id.addQuizeFloatingButtonid);
        quizeListRecyclerviewid.setHasFixedSize(true);
        quizeListRecyclerviewid.setLayoutManager(new LinearLayoutManager(this));

        quizListAdapter=new QuizListAdapter(this,quizDataList);
        quizeListRecyclerviewid.setAdapter(quizListAdapter);

        progressDialog=new ProgressDialog(this);



        accessSpinnerAdapter=new SpinnerCustomAdapter(this,access);
        departmentAdapter=new SpinnerCustomAdapter(this,departments);
        semesterAdapter=new SpinnerCustomAdapter(this,semesters);
        groupAdapter=new SpinnerCustomAdapter(this,groups);
        shiftAdapter=new SpinnerCustomAdapter(this,shifts);
        timeAdapter=new SpinnerCustomAdapter(this,times);

        shared=new UsersShared(TeacherQuizeListActivity.this);
        userId=shared.getId();
        databaseReference= FirebaseDatabase.getInstance().getReference().child(userId).child("Quiz");
        storageReference= FirebaseStorage.getInstance().getReference().child("QuizImage");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    quizSaveDiolouge();
            }
        });



        quizListAdapter.setOnItemClickListner(new QuizListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(TeacherQuizeListActivity.this,TeacherQuizQuestionListActivity.class);
                intent.putExtra("quizKey",quizDataList.get(position).getKey());
                intent.putExtra("quizName",quizDataList.get(position).getQuizName());
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {
                    deleteQuiz(quizDataList.get(position).getKey());
            }

            @Override
            public void onUpdate(int position) {
                quizKey=quizDataList.get(position).getKey();
                quizUpdateDiolouge(position);
            }

            @Override
            public void onImageSet(int position) {
                quizKey=quizDataList.get(position).getKey();
                    openfilechooser();
            }

            @Override
            public void onTakeExam(int position) {
                quizKey=quizDataList.get(position).getKey();
                int questionnumber=Integer.parseInt(quizDataList.get(position).getQnumber());
                if(questionnumber<5){
                    Toast.makeText(TeacherQuizeListActivity.this, "Minimum 5 Question Need For Take Exam", Toast.LENGTH_SHORT).show();
                }else{
                    publishQuizDiolouge(position);
                }
            }
        });
    }



    public void deleteQuiz(String key){
        progressDialog.setTitle("Deleting Quiz");
        progressDialog.show();
       URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizeListActivity.this);
        String url=urls.getQuiz()+key;

        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(TeacherQuizeListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                      onStart();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeacherQuizeListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }



    public void setQuizImage(){
        progressDialog.setTitle("Uploading Image");
        progressDialog.show();
        String imagename=databaseReference.push().getKey();
        StorageReference filePath=storageReference.child(imagename+databaseReference.push().getKey()
        +"."+getFileExtension(imageUri));

        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!urlTask.isSuccessful());
                Uri downloaduri=urlTask.getResult();

                    saveImageLink(downloaduri.toString());


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        getAllQuiz();
    }
    private void openfilechooser() {

        Intent intentf=new Intent();
        intentf.setType("image/*");
        intentf.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentf,1);

    }

    public String getFileExtension(Uri imageuri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
    }
    public void quizSaveDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherQuizeListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_quize_diolouge_layoute,null);
        builder.setView(view);
        quizNameEidttext=view.findViewById(R.id.quiz_diolouge_QuizeNameEdittext);
        accessSpinner=view.findViewById(R.id.quize_diolouge_AccessSpinnerid);
        quizSaveButton=view.findViewById(R.id.quize_SaveButtonid);

        accessSpinner.setAdapter(accessSpinnerAdapter);



        final AlertDialog dialog=builder.create();
        dialog.show();

        accessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccess=access[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        quizSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quizeName=quizNameEidttext.getText().toString();
              if(quizeName.isEmpty()){
                    quizNameEidttext.setError("Enter Quiz Name");
                  quizNameEidttext.requestFocus();
                }else if(selectedAccess.isEmpty()){
                    Toast.makeText(TeacherQuizeListActivity.this, "Please Choose Access Type", Toast.LENGTH_SHORT).show();
                }else{
                    saveQuiz(dialog);
                }
            }
        });
    }
    public void publishQuizDiolouge(final int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherQuizeListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.quiz_publish_select_class_diolouge,null);
        builder.setView(view);
        departmentSpinner=view.findViewById(R.id.publish_quiz_DepartmentSpinnerId);
        semesterSpinner=view.findViewById(R.id.publish_quiz_SemesterSpinnerId);
        groupSpinner=view.findViewById(R.id.publish_quiz_GroupSpinnerId);
        shiftSpinner=view.findViewById(R.id.publish_quiz_ShiftSpinnerId);
        publishquizeTakeExamButton=view.findViewById(R.id.publish_quiz_Button);
        timeSpinner=view.findViewById(R.id.publish_quiz_TimeSpinnerId);

        departmentSpinner.setAdapter(departmentAdapter);
        semesterSpinner.setAdapter(semesterAdapter);
        groupSpinner.setAdapter(groupAdapter);
        shiftSpinner.setAdapter(shiftAdapter);
        timeSpinner.setAdapter(timeAdapter);


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
        });
        shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shift=shifts[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester=semesters[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department=departments[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    time=times[position];
                    if(time.equals("5min")){
                        timeinMIllis=300000;
                    }else  if(time.equals("10min")){
                        timeinMIllis=600000;
                    }else  if(time.equals("15min")){
                        timeinMIllis=900000;
                    }else  if(time.equals("20min")){
                        timeinMIllis=1200000;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });








        publishquizeTakeExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(group.isEmpty() || semester.isEmpty() || department.isEmpty() || shift.isEmpty()){
                    Toast.makeText(TeacherQuizeListActivity.this, "Please Select All The Filed", Toast.LENGTH_SHORT).show();
                }else{
                    publishQuiz(position,group,semester,department,shift);
                }



            }
        });
    }

    public void publishQuiz(final int position, String group, String semester, String department, String shift){
            progressDialog.setTitle("Publishing Quiz");
            progressDialog.show();

        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizeListActivity.this);
        String url=urls.getSaveStudent()+"quiz/"+group+"/"+shift+"/"+department+"/"+semester;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(TeacherQuizeListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    onStart();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeacherQuizeListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("qId",quizKey);
                parms.put("qStatus","start");
                parms.put("image",quizDataList.get(position).getImage());
                parms.put("quizName",quizDataList.get(position).getQuizName());
                parms.put("totalQuestion",quizDataList.get(position).getQnumber());
                parms.put("time", String.valueOf(timeinMIllis));
                parms.put("endTime", String.valueOf(System.currentTimeMillis()));
                return  parms;
            }
        };
        requestQueue.add(stringRequest);





    }
    public void quizUpdateDiolouge(int position) {

        QuizeDataModuler d=quizDataList.get(position);

        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherQuizeListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_quize_diolouge_layoute,null);
        builder.setView(view);
        quizNameEidttext=view.findViewById(R.id.quiz_diolouge_QuizeNameEdittext);
        accessSpinner=view.findViewById(R.id.quize_diolouge_AccessSpinnerid);
        quizSaveButton=view.findViewById(R.id.quize_SaveButtonid);

        accessSpinner.setAdapter(accessSpinnerAdapter);

        quizNameEidttext.setText(d.getQuizName());
        quizSaveButton.setText("Update");
        int i=0;
        if(d.getAccesstype().equals("Only me")){
            i=0;
        }else if(d.getAccesstype().equals("All Teachers")){
            i=1;
        }else if(d.getAccesstype().equals("All Student")){
            i=2;
        }
        accessSpinner.setSelection(i);

        final AlertDialog dialog=builder.create();
        dialog.show();

        accessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccess=access[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        quizSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quizeName=quizNameEidttext.getText().toString();
              if(quizeName.isEmpty()){
                    quizNameEidttext.setError("Enter Quize Name");
                  quizNameEidttext.requestFocus();
                }else if(selectedAccess.isEmpty()){
                    Toast.makeText(TeacherQuizeListActivity.this, "Please Choose Access Type", Toast.LENGTH_SHORT).show();
                }else{
                    updateQuize(dialog);
                }
            }
        });
    }


    public void saveImageLink(final String image){
   URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizeListActivity.this);
        String url=urls.getQuiz()+"image/"+quizKey;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(TeacherQuizeListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    onStart();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeacherQuizeListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();
           parms.put("image",image);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);
    }


  public void saveQuiz(final Dialog dialog){
        progressDialog.setTitle("Saving Quiz");
      progressDialog.show();
      URLS urls=new URLS();
      RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizeListActivity.this);
      String url=urls.getQuiz();

      StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              try {
                  progressDialog.dismiss();
                JSONObject jsonObject=new JSONObject(response);
                  Toast.makeText(TeacherQuizeListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                  dialog.dismiss();
                  onStart();
              } catch (JSONException e) {
                  e.printStackTrace();
                  progressDialog.dismiss();
              }


          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              Toast.makeText(TeacherQuizeListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
              progressDialog.dismiss();
          }
      }){

          @Override
          protected Map<String, String> getParams()  {

              Map<String, String>  parms=new HashMap<String, String>();
              parms.put("quizName",quizeName);
              parms.put("access",selectedAccess);
              parms.put("teacherId",userId);
              parms.put("image","null");
              return  parms;
          }
      };
      requestQueue.add(stringRequest);
  }


  public void getAllQuiz(){
        progressDialog.setTitle("Loading.....");
        progressDialog.show();
      URLS urls=new URLS();


      RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizeListActivity.this);
      String url=urls.getQuiz()+"teacher/"+userId;

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
                      QuizeDataModuler dataModuler=new QuizeDataModuler(
                              receive.getString("_id"),
                              receive.getString("quizName"),
                              receive.getString("access"),
                              receive.getString("image"),
                              receive.getString("numberofquestions")
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
              Toast.makeText(TeacherQuizeListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
          }
      });
      requestQueue.add(stringRequest);



  }

  public void updateQuize(final Dialog dialog){
        progressDialog.setTitle("Updating......");
        progressDialog.show();

      URLS urls=new URLS();
      RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizeListActivity.this);
      String url=urls.getQuiz()+quizKey;

      StringRequest stringRequest=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              try {
                  progressDialog.dismiss();
                  JSONObject jsonObject=new JSONObject(response);
                  Toast.makeText(TeacherQuizeListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                  dialog.dismiss();
                  onStart();
              } catch (JSONException e) {
                  e.printStackTrace();
                  progressDialog.dismiss();
              }


          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              Toast.makeText(TeacherQuizeListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
              progressDialog.dismiss();
          }
      }){

          @Override
          protected Map<String, String> getParams()  {

              Map<String, String>  parms=new HashMap<String, String>();
              parms.put("quizName",quizeName);
              parms.put("access",selectedAccess);
              return  parms;
          }
      };
      requestQueue.add(stringRequest);
  }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode==RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            setQuizImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}