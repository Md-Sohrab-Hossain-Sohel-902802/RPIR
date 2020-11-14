package com.example.myapplication.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.Student.DataModuler.StudentQuizQuestion;
import com.example.myapplication.Teacher.DataModuler.QuestionListDataModuler;
import com.example.myapplication.URLS;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadPoolExecutor;

public class StudentQuizDetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView quizNameTextview;
    private TextView totalQuestionTextview;
    private  TextView timeTextview;
    private  TextView endTimeTextview;
    private  Button startQuizButton;

    private CountDownTimer countDownTimer;


    String image,qName,totalQuestion,time,key,endTime;
    List<StudentQuizQuestion> questionList=new ArrayList<>();
    long endTimeInMillis;

    long timeleft;

    String submit="done";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_details);


        image=getIntent().getStringExtra("image");
        qName=getIntent().getStringExtra("qName");
        totalQuestion=getIntent().getStringExtra("totalQuestion");
        time=getIntent().getStringExtra("time");
        endTime=getIntent().getStringExtra("endTime");
        key=getIntent().getStringExtra("key");
        getAllQuiz();

        submit=getSubmit(key);


        long timeInMIllis=Long.parseLong(time);
        endTimeInMillis=Long.parseLong(endTime);

        int minutes=(int) (timeInMIllis/1000/60);
        int seconds=(int) (timeInMIllis/1000)%60;

        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);


        imageView=findViewById(R.id.studentQuizImageviewid);
        quizNameTextview=findViewById(R.id.studentQuiz_QuizNameTextview);
        totalQuestionTextview=findViewById(R.id.studentQuiz_TotalQuestionTextview);
        timeTextview=findViewById(R.id.studentQuiz_TimeTextview);
        endTimeTextview=findViewById(R.id.studentQuiz_EndTimeTextview);
        startQuizButton=findViewById(R.id.student_StartQuizButton);

        if(endTimeInMillis+900000>System.currentTimeMillis()){
                startCountDown();
        }

        if(image.equals("null")){
            Picasso.get().load(R.drawable.cg4).into(imageView);
        }else{
            Picasso.get().load(image).placeholder(R.drawable.cg4).into(imageView);
        }

        quizNameTextview.setText("Quiz Name : "+qName);
        totalQuestionTextview.setText("Total Question : "+totalQuestion);
        timeTextview.setText("Time: "+timeFormatted+"(min)");

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(endTimeInMillis+900000>System.currentTimeMillis() && submit.equals("null")){

                    Intent intent=new Intent(StudentQuizDetailsActivity.this,QuizActiviy.class);
                    intent.putExtra("quizKey",key);
                    intent.putExtra("data", (Serializable) questionList);
                    intent.putExtra("time", time);
                    startActivity(intent);
                }else if(submit.equals("done")){
                    Toast.makeText(StudentQuizDetailsActivity.this, "You Already Completed This Quiz", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(StudentQuizDetailsActivity.this, "Quiz join time finished.", Toast.LENGTH_SHORT).show();
                }



            }
        });




    }

    public String getSubmit(String key){
        SharedPreferences sharedPreferences=getSharedPreferences("quiz", Context.MODE_PRIVATE);
        String submit=sharedPreferences.getString(key+"submit","null");
        return  submit;
    }

    private  void startCountDown(){

        timeleft=(endTimeInMillis+900000)-System.currentTimeMillis();

        countDownTimer=new CountDownTimer(timeleft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeleft=0;
                updateCountDownText();
            }
        }.start();
    }
    private  void updateCountDownText(){

        int minutes=(int)(timeleft/1000/60);
        int seconds=(int)(timeleft/1000)%60;

        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        endTimeTextview.setText("Join Time Left: "+timeFormatted);


    }
    public void getAllQuiz(){

      URLS urls=new URLS();


        RequestQueue requestQueue= Volley.newRequestQueue(StudentQuizDetailsActivity.this);
        String url=urls.getQuiz()+"question/"+key;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    for(int i=0; i<array.length(); i++){

                        JSONObject receive=array.getJSONObject(i);
                        StudentQuizQuestion dataModuler=new StudentQuizQuestion(
                                receive.getString("_id"),
                                receive.getString("question"),
                                receive.getString("option1"),
                                receive.getString("option2"),
                                receive.getString("option3"),
                                receive.getString("option4"),
                                receive.getString("answerNr"),
                                receive.getString("mark")
                        );

                        questionList.add(dataModuler);
                    }

                } catch (JSONException e) {
                   e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 Toast.makeText(StudentQuizDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

}