package com.example.myapplication.Teacher.DataModuler;

public class PresentDataModuler {


    String id,teacherCode,presence,day,date,time,department,group,shift,semester,name,roll,registration,subjectName,subjectCode;


    public PresentDataModuler(String id, String presence, String day, String date, String time, String department, String group, String shift, String semester, String name, String roll, String registration, String subjectName, String subjectCode,String teacherCode) {
        this.id = id;
        this.presence = presence;
        this.day = day;
        this.date = date;
        this.time = time;
        this.department = department;
        this.group = group;
        this.shift = shift;
        this.semester = semester;
        this.name = name;
        this.roll = roll;
        this.registration = registration;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.teacherCode=teacherCode;
    }


    public String getTeacherCode() {
        return teacherCode;
    }

    public String getId() {
        return id;
    }

    public String getPresence() {
        return presence;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDepartment() {
        return department;
    }

    public String getGroup() {
        return group;
    }

    public String getShift() {
        return shift;
    }

    public String getSemester() {
        return semester;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getRegistration() {
        return registration;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }
}
