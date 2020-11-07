package com.example.myapplication;

public class URLS {

        String loginUrl="http://192.168.43.59:4002/user/login";
        String registerTeacher="http://192.168.43.59:4002/user/registerTeacher";
        String teacherAdd="http://192.168.43.59:4002/teacher/";
        String getDepartmentTeacher="http://192.168.43.59:4002/teacher/";
        String saveStudent="http://192.168.43.59:4002/student/";
        String teacherCode="http://192.168.43.59:4002/teacher/get/code";
        String routine="http://192.168.43.59:4002/routine/";
        String present="http://192.168.43.59:4002/present/";
        String getteacherUser="http://192.168.43.59:4002/user/getTeacher";
        String loginStudent="http://192.168.43.59:4002/user/loginstudent";
        String subject="http://192.168.43.59:4002/subject/";

/*
        String loginUrl="https://rpibackend.herokuapp.com/user/login";
        String registerTeacher="https://rpibackend.herokuapp.com/user/registerTeacher";
        String teacherAdd="https://rpibackend.herokuapp.com/teacher/";
        String getDepartmentTeacher="https://rpibackend.herokuapp.com/teacher/";
        String saveStudent="https://rpibackend.herokuapp.com/student/";
        String teacherCode="https://rpibackend.herokuapp.com/teacher/get/code";
        String routine="https://rpibackend.herokuapp.com/routine/";
        String getteacherUser="https://rpibackend.herokuapp.com/user/getTeacher";
*/


    public String getSubject() {
        return subject;
    }

    public String getLoginStudent() {
        return loginStudent;
    }

    public String getPresent() {
        return present;
    }

    public String getGetteacherUser() {
        return getteacherUser;
    }

    public String getRegisterTeacher() {
        return registerTeacher;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public String getRoutine() {
        return routine;
    }

    public String getGetDepartmentTeacher() {
        return getDepartmentTeacher;
    }

    public String getTeacherAdd() {
        return teacherAdd;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getSaveStudent() {
        return saveStudent;
    }
}
