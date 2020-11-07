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

import com.example.myapplication.Admin.Adapter.SpinnerCustomAdapter;
import com.example.myapplication.LocalStorage.Shared.UsersShared;
import com.example.myapplication.R;
import com.example.myapplication.Teacher.Adapter.QuestionListAdapter;
import com.example.myapplication.Teacher.DataModuler.QuestionListDataModuler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeacherQuizQuestionListActivity extends AppCompatActivity {
    private  String quizKey,userId,quizName,selectedAnswerNumber,question,option1,option2,option3,option4;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private UsersShared shared;


    //<-----------------Diolouge Variable------------------->

    private  EditText questionEdittext,option1Edittext,option2Edittext,option3Edittext,option4Edittext;
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
        databaseReference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(TeacherQuizQuestionListActivity.this, "Question Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setTitle("Loading.......");
        progressDialog.show();
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        questionDataList.clear();
                        for (DataSnapshot snapshot1: snapshot.getChildren()){
                                String key=snapshot1.child("key").getValue().toString();
                                String question=snapshot1.child("question").getValue().toString();
                                String option1=snapshot1.child("option1").getValue().toString();
                                String option2=snapshot1.child("option2").getValue().toString();
                                String option3=snapshot1.child("option3").getValue().toString();
                                String option4=snapshot1.child("option4").getValue().toString();
                                String answerNumber=snapshot1.child("answerNumber").getValue().toString();

                                QuestionListDataModuler data=new QuestionListDataModuler(key,question,option1,option2,option3,option4,answerNumber);
                                questionDataList.add(data);
                                progressDialog.dismiss();
                                questionListAdapter.notifyDataSetChanged();


                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





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

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("question",question);
        hashMap.put("option1",option1);
        hashMap.put("option2",option2);
        hashMap.put("option3",option3);
        hashMap.put("option4",option4);
        hashMap.put("answerNumber",selectedAnswerNumber);

        databaseReference.child(key).updateChildren(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(TeacherQuizQuestionListActivity.this, "Updated Successful", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                        }
                    });


    }




    public void saveQuestion(final Dialog dialog){
            progressDialog.setTitle("Saving Question");
            progressDialog.show();
        String questionKey=databaseReference.push().getKey();

        QuestionListDataModuler dataModuler=new QuestionListDataModuler(questionKey,question,option1,option2,option3,option4,selectedAnswerNumber);

        databaseReference.child(questionKey).setValue(dataModuler).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(TeacherQuizQuestionListActivity.this, "Question Saved", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    dialog.dismiss();


                }
            }
        });
    }


}