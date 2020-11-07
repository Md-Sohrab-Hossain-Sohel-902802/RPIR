package com.example.myapplication.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.text.DecimalFormat;

public class StudentCgpaCalculatorActivity extends AppCompatActivity {

    private  EditText firstSemester,secondSemester,thirdSemester,fourthSemester,fifthSemester,sixSemester,seventhSemester,eightSemester;
    private   Button calculateButton,resetButton;
    private TextView resultTextview;

    double r1=0,r2=0,r3=0,r4=0,r5=0,r6=0,r7=0,r8=0;
    String result1,result2,result3,result4,result5,result6,result7,result8;

    double sr1,sr2,sr3,sr4,sr5,sr6,sr7,sr8;
    double finalResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_cgpa_calculator);

        firstSemester=findViewById(R.id.cgpa_Semester1Edittextid);
        secondSemester=findViewById(R.id.cgpa_Semester2Edittextid);
        thirdSemester=findViewById(R.id.cgpa_Semester3Edittextid);
        fourthSemester=findViewById(R.id.cgpa_Semester4Edittextid);
        fifthSemester=findViewById(R.id.cgpa_Semester5Edittextid);
        sixSemester=findViewById(R.id.cgpa_Semester6Edittextid);
        seventhSemester=findViewById(R.id.cgpa_Semester7Edittextid);
        eightSemester=findViewById(R.id.cgpa_Semester8Edittextid);

        calculateButton=findViewById(R.id.cgpa_CalculateButtonviewid);
        resetButton=findViewById(R.id.cgpa_ResetButtonid);
        resultTextview=findViewById(R.id.cgpa_ResultTextviewid);



        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        result1=firstSemester.getText().toString();
                        result2=secondSemester.getText().toString();
                        result3=thirdSemester.getText().toString();
                        result4=fourthSemester.getText().toString();
                        result5=fifthSemester.getText().toString();
                        result6=sixSemester.getText().toString();
                        result7=seventhSemester.getText().toString();
                        result8=eightSemester.getText().toString();

                        if(result1.isEmpty() && result2.isEmpty() && result3.isEmpty() && result4.isEmpty() && result5.isEmpty() && result6.isEmpty() && result7.isEmpty() && result8.isEmpty()){
                            Toast.makeText(StudentCgpaCalculatorActivity.this, "Write Your Result", Toast.LENGTH_SHORT).show();
                        }
                        if(!result1.isEmpty()){
                            r1=Double.parseDouble(result1);
                        }
                         if(!result2.isEmpty()){
                            r2=Double.parseDouble(result2);
                        }
                         if(!result3.isEmpty()){
                            r3=Double.parseDouble(result3);
                        }
                         if(!result4.isEmpty()){
                            r4=Double.parseDouble(result4);
                        }
                         if(!result5.isEmpty()){
                            r5=Double.parseDouble(result5);
                        }
                         if(!result6.isEmpty()){
                            r6=Double.parseDouble(result6);
                        }
                         if(!result7.isEmpty()){
                            r7=Double.parseDouble(result7);
                        }
                         if(!result8.isEmpty()){
                            r8=Double.parseDouble(result8);
                        }


                         if(r1>4.00){
                             firstSemester.setError("Max 4.00");
                             firstSemester.requestFocus();
                             return;
                         }
                         if(r2>4.00){
                             secondSemester.setError("Max 4.00");
                             secondSemester.requestFocus();
                             return;
                         }
                         if(r3>4.00){
                             thirdSemester.setError("Max 4.00");
                             thirdSemester.requestFocus();
                             return;
                         }
                         if(r4>4.00){
                             fourthSemester.setError("Max 4.00");
                             fourthSemester.requestFocus();
                             return;
                         }
                         if(r5>4.00){
                            fifthSemester.setError("Max 4.00");
                             fifthSemester.requestFocus();
                             return;
                        }
                         if(r6>4.00){
                            sixSemester.setError("Max 4.00");
                             sixSemester.requestFocus();
                             return;
                        }
                         if(r7>4.00){
                            seventhSemester.setError("Max 4.00");
                             seventhSemester.requestFocus();
                             return;
                        }
                         if(r8>4.00){
                            eightSemester.setError("Max 4.00");
                             eightSemester.requestFocus();
                             return;
                        }








                    sr1=(r1*5)/100;
                    sr2=(r2*5)/100;
                    sr3=(r3*5)/100;
                    sr4=(r4*10)/100;
                    sr5=(r5*15)/100;
                    sr6=(r6*20)/100;
                    sr7=(r7*25)/100;
                    sr8=(r8*15)/100;

                    finalResult=sr1+sr2+sr3+sr4+sr5+sr6+sr7+sr8;
                String gpa=gpaGreat(finalResult);
                    resultTextview.setText("Total: "+new DecimalFormat("##.##").format(finalResult)+"  "+gpa);


            }
        });



        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    resultTextview.setText("Total:");
                    firstSemester.setText("");
                    secondSemester.setText("");
                    thirdSemester.setText("");
                    fourthSemester.setText("");
                    fifthSemester.setText("");
                    sixSemester.setText("");
                    seventhSemester.setText("");
                    eightSemester.setText("");
                    firstSemester.requestFocus();
            }
        });




    }

    public String gpaGreat(Double cgpa){
        String gpa="";

        if(cgpa>=4.00){
            gpa="A+";
        }else if(cgpa>=3.75){
            gpa="A";
        }else if(cgpa>=3.50){
            gpa="A-";
        }else if(cgpa>=3.00){
            gpa="B+";
        }else if(cgpa>=2.75){
            gpa="B";
        }else if(cgpa>=2.50){
            gpa="C";
        }




    return  gpa;
    }










}