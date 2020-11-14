package com.example.myapplication.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.AdminAddTeacherActivity;
import com.example.myapplication.LocalStorage.Shared.StudentShared;
import com.example.myapplication.R;
import com.example.myapplication.Student.DataModuler.StudentQuizQuestion;
import com.example.myapplication.Teacher.DataModuler.QuestionListDataModuler;
import com.example.myapplication.Teacher.TeacherQuizQuestionListActivity;
import com.example.myapplication.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuizActiviy extends AppCompatActivity {
    private  long timeLeftInMIllis;
    private  static  final long COUNTDOWN_IN_MILLIS=30000;
    private CountDownTimer countDownTimer;






    private ProgressDialog progressDialog;
    private String quizKey,time;
    private  List<StudentQuizQuestion> questionDataList=new ArrayList<>();


    //<----------------------layout variable----------------------->

    private RadioGroup quizRadioGroup;
    private  RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    private  TextView quizQuestionTextview,scoreTextview,totalQuestionTextview,countDownTimerTextview;
    private  Button quizConfirmButton,publishButton;




    //<-----------------some variable------------------------->

    private  int questionCounter=0,questionCountTotal;
    private  StudentQuizQuestion currentQuestion;
    private  int score;
    private  boolean answered;
    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_activiy);

     Bundle bundle=getIntent().getExtras();
        if(bundle!=null){

            questionDataList = (List<StudentQuizQuestion>) getIntent().getSerializableExtra("data");
            quizKey=getIntent().getStringExtra("quizKey");
            time=getIntent().getStringExtra("time");
        }

        long pastTime=getTimeLeft();
        if(pastTime!=0){
            timeLeftInMIllis=pastTime;
            startCountDown();

        }else{
            timeLeftInMIllis=Long.parseLong(time);
            startCountDown();

        }

       progressDialog=new ProgressDialog(this);


        quizQuestionTextview=findViewById(R.id.quiz_QuestionTextviewid);
        quizRadioGroup=findViewById(R.id.quiz_radio_group);
        radioButton1=findViewById(R.id.quiz_radio_button1);
        radioButton2=findViewById(R.id.quiz_radio_button2);
        radioButton3=findViewById(R.id.quiz_radio_button3);
        radioButton4=findViewById(R.id.quiz_radio_button4);
        quizConfirmButton=findViewById(R.id.quizConfirmButtonid);
        publishButton=findViewById(R.id.quizPublishButtonid);
        scoreTextview=findViewById(R.id.quiz_ScoreTextviewid);
        totalQuestionTextview=findViewById(R.id.quiz_TotalQuestionTextviewid);
        countDownTimerTextview=findViewById(R.id.quiz_CountDownTimerTextviewid);



        textColorDefaultRb=radioButton1.getTextColors();
        textColorDefaultCd=countDownTimerTextview.getTextColors();

        if(questionDataList.size()>0){
            questionCountTotal=questionDataList.size();
            Collections.shuffle(questionDataList);
            showNextQuestion();
        }



        quizConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(radioButton1.isChecked() || radioButton2.isChecked() || radioButton3.isChecked() || radioButton4.isChecked()){
                        checkAnswer();
                    }else{
                        Toast.makeText(QuizActiviy.this, "Select An Answer", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showNextQuestion();
                }
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        publishResult();
            }
        });










    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveQuizTime();
    }

    public void showNextQuestion(){

        radioButton1.setTextColor(textColorDefaultRb);
        radioButton2.setTextColor(textColorDefaultRb);
        radioButton3.setTextColor(textColorDefaultRb);
        radioButton4.setTextColor(textColorDefaultRb);

        quizRadioGroup.clearCheck();
        if(questionCounter<questionCountTotal){
                currentQuestion=questionDataList.get(questionCounter);
                quizQuestionTextview.setText(currentQuestion.getQuestion());
                radioButton1.setText(currentQuestion.getOption1());
                radioButton2.setText(currentQuestion.getOption2());
                radioButton3.setText(currentQuestion.getOption3());
                radioButton4.setText(currentQuestion.getOption4());
                questionCounter++;
                totalQuestionTextview.setText("Question: "+questionCounter+"/"+questionCountTotal);
                answered=false;
                quizConfirmButton.setText("Confirm");
        }
    }



    public void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeftInMIllis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMIllis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMIllis=0;
                updateCountDownText();
                quizConfirmButton.setVisibility(View.GONE);
                publishButton.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    public void updateCountDownText(){

        int minutes=(int) (timeLeftInMIllis/1000/60);
        int seconds=(int) (timeLeftInMIllis/1000)%60;

        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countDownTimerTextview.setText(timeFormatted);
        if(timeLeftInMIllis<10000){
            countDownTimerTextview.setTextColor(Color.RED);
        }else{
            countDownTimerTextview.setTextColor(textColorDefaultCd);
        }
    }



 public void checkAnswer(){
        answered=true;
       RadioButton rbSelected=findViewById(quizRadioGroup.getCheckedRadioButtonId());
        int prAnswer=quizRadioGroup.indexOfChild(rbSelected)+1;
        int  mAnswer=Integer.parseInt(currentQuestion.getAnswerNumber());
        if(prAnswer==mAnswer){
            score=score+Integer.parseInt(currentQuestion.getMark());
            scoreTextview.setText("Score: "+score);
        }
        showSolution();
 }
 private  void showSolution(){
     int  mAnswer=Integer.parseInt(currentQuestion.getAnswerNumber());
    radioButton1.setTextColor(Color.RED);
    radioButton2.setTextColor(Color.RED);
    radioButton3.setTextColor(Color.RED);
    radioButton4.setTextColor(Color.RED);

    switch (mAnswer){
        case 1:
            radioButton1.setTextColor(Color.GREEN);
            break;
        case 2:
            radioButton2.setTextColor(Color.GREEN);
            break;
        case 3:
            radioButton3.setTextColor(Color.GREEN);
            break;
        case 4:
            radioButton4.setTextColor(Color.GREEN);
            break;
    }
    if(questionCounter<questionCountTotal){
        quizConfirmButton.setText("Next");
    }else{
        quizConfirmButton.setVisibility(View.GONE);
        publishButton.setVisibility(View.VISIBLE);
    }
 }

 public void saveQuizTime(){
        SharedPreferences sharedPreferences=getSharedPreferences("quiz",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(quizKey,timeLeftInMIllis);
        editor.commit();
 }
 public void saveSubmit(){
        SharedPreferences sharedPreferences=getSharedPreferences("quiz",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(quizKey+"submit","done");
        editor.commit();
 }

 public long getTimeLeft(){
     SharedPreferences sharedPreferences=getSharedPreferences("quiz",Context.MODE_PRIVATE);
    long time=sharedPreferences.getLong(quizKey,0);
    return  time;
 }





    private void publishResult() {
        progressDialog.setTitle("Sending Your Result");
        progressDialog.show();
        URLS urls=new URLS();
        RequestQueue requestQueue= Volley.newRequestQueue(QuizActiviy.this);
        String url=urls.getQuizAnswer()+"submit/"+quizKey;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveSubmit();
                progressDialog.dismiss();
                Toast.makeText(QuizActiviy.this, "Your Result Sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(QuizActiviy.this,StudentQuizListActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuizActiviy.this, "Failed"+error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){

            @Override
            protected Map<String, String> getParams()  {

                StudentShared studentShared=new StudentShared(QuizActiviy.this);
                String roll=studentShared.getRoll();
                String registraiton=studentShared.getRegistration();

              long  totaltimeLeft=Long.parseLong(time);
                long timeSpent=totaltimeLeft-timeLeftInMIllis;

                Map<String, String>  parms=new HashMap<String, String>();


                parms.put("roll",roll);
                parms.put("registration",registraiton);
                parms.put("timeSpent",String.valueOf(timeSpent));
                parms.put("mark",String.valueOf(score));
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }


}