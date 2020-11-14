package com.example.myapplication.Student.DataModuler;

public class StudentQuizDataModuler {

    String id,quizId,quizStatus,quizName,image,totalQuestion,time,endTime;

    public StudentQuizDataModuler(String id, String quizId, String quizStatus, String quizName, String image, String totalQuestion, String time,String endTime) {
        this.id = id;
        this.quizId = quizId;
        this.quizStatus = quizStatus;
        this.quizName = quizName;
        this.image = image;
        this.totalQuestion = totalQuestion;
        this.time = time;
        this.endTime=endTime;
    }

    public String getId() {
        return id;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getQuizStatus() {
        return quizStatus;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getImage() {
        return image;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTime() {
        return time;

    }



}
