package com.example.myapplication.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.QuestionListAdapter;
import com.example.myapplication.Teacher.DataModuler.QuestionListDataModuler;
import com.example.myapplication.Teacher.DataModuler.QuizeDataModuler;
import com.example.myapplication.URLS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherQuizQuestionListActivity extends AppCompatActivity {
    private  String quizKey,userId,quizName,selectedAnswerNumber,mark,question,option1,option2,option3,option4;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private UsersShared shared;


    //<-----------------Diolouge Variable------------------->

    private  EditText markEdittext,questionEdittext,option1Edittext,option2Edittext,option3Edittext,option4Edittext;
    private Spinner answerNumberSpinner;
    private  Button saveQuestionButton;

    private SpinnerCustomAdapter  answernumberSpinnerAdapter;
    private  String[] answerNumbers={"1","2","3","4"};




    private  DatabaseReference databaseReference;

    private  ProgressDialog progressDialog;

    private  List<QuestionListDataModuler> questionDataList=new ArrayList<>();
    private QuestionListAdapter questionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_quiz_question_list);

        shared=new UsersShared(TeacherQuizQuestionListActivity.this);
        userId=shared.getId();

        quizKey=getIntent().getStringExtra("quizKey");
        quizName=getIntent().getStringExtra("quizName");

        databaseReference= FirebaseDatabase.getInstance().getReference().child(userId).child("Quiz").child(quizKey).child("questions");
        progressDialog=new ProgressDialog(this);

        toolbar=findViewById(R.id.teacher_Question_ListToolbarid);
        setSupportActionBar(toolbar);
        this.setTitle(""+quizName);

        recyclerView=findViewById(R.id.question_listRecyclerviewid);
        floatingActionButton=findViewById(R.id.addquestionFloatingButtonid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        answernumberSpinnerAdapter=new SpinnerCustomAdapter(this,answerNumbers);
        questionListAdapter=new QuestionListAdapter(this,questionDataList);
        recyclerView.setAdapter(questionListAdapter);





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addQuestionDiolouge();
            }
        });


        questionListAdapter.setOnItemClickListner(new QuestionListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDelete(int position) {
                        deleteQuestion(questionDataList.get(position).getKey());
            }

            @Override
            public void onUpdate(int position) {
                        updateDiolouge(position);
            }
        });
    }

    private void deleteQuestion(String key) {
        progressDialog.setTitle("Deleting........");
        progressDialog.show();


        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizQuestionListActivity.this);
        String url=urls.getQuiz()+"question/"+quizKey+"/"+key;

        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(TeacherQuizQuestionListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                  onStart();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeacherQuizQuestionListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
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


        RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizQuestionListActivity.this);
        String url=urls.getQuiz()+"question/"+quizKey;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    questionDataList.clear();
                    for(int i=0; i<array.length(); i++){

                        JSONObject receive=array.getJSONObject(i);
                        QuestionListDataModuler dataModuler=new QuestionListDataModuler(
                                receive.getString("_id"),
                                receive.getString("question"),
                                receive.getString("option1"),
                                receive.getString("option2"),
                                receive.getString("option3"),
                                receive.getString("option4"),
                                receive.getString("answerNr"),
                                receive.getString("mark")
                        );

                        questionDataList.add(dataModuler);
                        questionListAdapter.notifyDataSetChanged();
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
                Toast.makeText(TeacherQuizQuestionListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);



    }
    public void addQuestionDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherQuizQuestionListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_question_diolouge_layoute,null);
        builder.setView(view);
        questionEdittext=view.findViewById(R.id.question_diolouge_QuestionEdittextid);
        option1Edittext=view.findViewById(R.id.question_diolouge_Option1QuestionEdittextid);
        option2Edittext=view.findViewById(R.id.question_diolouge_Option2QuestionEdittextid);
        option3Edittext=view.findViewById(R.id.question_diolouge_Option3QuestionEdittextid);
        option4Edittext=view.findViewById(R.id.question_diolouge_Option4QuestionEdittextid);
        markEdittext=view.findViewById(R.id.question_diolouge_QuestionMarkEdittextid);
        answerNumberSpinner=view.findViewById(R.id.question_diolouge_AnswerNrSpinnerid);
        saveQuestionButton=view.findViewById(R.id.question_SaveButtonid);

        answerNumberSpinner.setAdapter(answernumberSpinnerAdapter);



        final AlertDialog dialog=builder.create();
        dialog.show();

        answerNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnswerNumber=answerNumbers[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                question=questionEdittext.getText().toString();
                option1=option1Edittext.getText().toString();
                option2=option2Edittext.getText().toString();
                option3=option3Edittext.getText().toString();
                option4=option4Edittext.getText().toString();
                mark=markEdittext.getText().toString();
                if(question.isEmpty()){
                    questionEdittext.setError("Please Write Your Question");
                    questionEdittext.requestFocus();
                }else if(option1.isEmpty()){
                    option1Edittext.setError("Enter Option 1");
                    option1Edittext.requestFocus();
                }else if(option2.isEmpty()){
                    option2Edittext.setError("Enter Option 2");
                    option2Edittext.requestFocus();
                }else if(option3.isEmpty()){
                    option3Edittext.setError("Enter Option 3");
                    option3Edittext.requestFocus();
                }else if(option4.isEmpty()){
                    option4Edittext.setError("Enter Option 4");
                    option4Edittext.requestFocus();
                }else if(mark.isEmpty()){
                    markEdittext.setError("Enter Marks For This Question");
                    markEdittext.requestFocus();
                }else if(selectedAnswerNumber.isEmpty()){
                    Toast.makeText(TeacherQuizQuestionListActivity.this, "Please Choose An Answer Number", Toast.LENGTH_SHORT).show();
                }else{
                    saveQuestion(dialog);
                }
            }
        });
    }
    public void updateDiolouge(int position) {
       final QuestionListDataModuler d= questionDataList.get(position);

        AlertDialog.Builder builder=new AlertDialog.Builder(TeacherQuizQuestionListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_question_diolouge_layoute,null);
        builder.setView(view);
        questionEdittext=view.findViewById(R.id.question_diolouge_QuestionEdittextid);
        option1Edittext=view.findViewById(R.id.question_diolouge_Option1QuestionEdittextid);
        option2Edittext=view.findViewById(R.id.question_diolouge_Option2QuestionEdittextid);
        option3Edittext=view.findViewById(R.id.question_diolouge_Option3QuestionEdittextid);
        option4Edittext=view.findViewById(R.id.question_diolouge_Option4QuestionEdittextid);
        answerNumberSpinner=view.findViewById(R.id.question_diolouge_AnswerNrSpinnerid);
        saveQuestionButton=view.findViewById(R.id.question_SaveButtonid);

        answerNumberSpinner.setAdapter(answernumberSpinnerAdapter);

        saveQuestionButton.setText("Update");
        questionEdittext.setText(d.getQuestion());
        option1Edittext.setText(d.getOption1());
        option2Edittext.setText(d.getOption2());
        option3Edittext.setText(d.getOption3());
        option4Edittext.setText(d.getOption4());

        int selected=Integer.parseInt(d.getAnswerNumber());
        answerNumberSpinner.setSelection(selected-1);

        final AlertDialog dialog=builder.create();
        dialog.show();

        answerNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnswerNumber=answerNumbers[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                question=questionEdittext.getText().toString();
                option1=option1Edittext.getText().toString();
                option2=option2Edittext.getText().toString();
                option3=option3Edittext.getText().toString();
                option4=option4Edittext.getText().toString();
                if(question.isEmpty()){
                    questionEdittext.setError("Please Write Your Question");
                    questionEdittext.requestFocus();
                }else if(option1.isEmpty()){
                    option1Edittext.setError("Enter Option 1");
                    option1Edittext.requestFocus();
                }else if(option2.isEmpty()){
                    option2Edittext.setError("Enter Option 2");
                    option2Edittext.requestFocus();
                }else if(option3.isEmpty()){
                    option3Edittext.setError("Enter Option 3");
                    option3Edittext.requestFocus();
                }else if(option4.isEmpty()){
                    option4Edittext.setError("Enter Option 4");
                    option4Edittext.requestFocus();
                }else if(selectedAnswerNumber.isEmpty()){
                    Toast.makeText(TeacherQuizQuestionListActivity.this, "Please Choose An Answer Number", Toast.LENGTH_SHORT).show();
                }else{
                    updateQuestion(dialog,d.getKey());
                }
            }
        });
    }

    public void updateQuestion(final Dialog dialog, String key){
        progressDialog.setTitle("Updating Question");
        progressDialog.show();



        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizQuestionListActivity.this);
        String url=urls.getQuiz()+"question/"+quizKey+"/"+key;

        StringRequest stringRequest=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(TeacherQuizQuestionListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TeacherQuizQuestionListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("question",question);
                parms.put("option1",option1);
                parms.put("option2",option2);
                parms.put("option3",option3);
                parms.put("option4",option4);
                parms.put("answerNr",selectedAnswerNumber);
                parms.put("mark","1");
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }




    public void saveQuestion(final Dialog dialog){
            progressDialog.setTitle("Saving Question");
            progressDialog.show();


        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(TeacherQuizQuestionListActivity.this);
        String url=urls.getQuiz()+"question/"+quizKey;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(TeacherQuizQuestionListActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TeacherQuizQuestionListActivity.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("question",question);
                parms.put("option1",option1);
                parms.put("option2",option2);
                parms.put("option3",option3);
                parms.put("option4",option4);
                parms.put("answerNr",selectedAnswerNumber);
                parms.put("mark",mark);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);
    }


}