package com.example.myapplication.Admin.DataModuler;

public class RoutineDataModuler {
    String id,startTime,endTime,day,teacherName,roomNumber,teacherCode,department,groups,shift,semester,period,subjectCode,serial;

    public RoutineDataModuler(String id, String startTime, String endTime, String day, String teacherName, String roomNumber, String teacherCode, String department, String groups, String shift, String semester, String period, String subjectCode,String serial) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.teacherName = teacherName;
        this.roomNumber = roomNumber;
        this.teacherCode = teacherCode;
        this.department = department;
        this.groups = groups;
        this.shift = shift;
        this.semester = semester;
        this.period = period;
        this.subjectCode = subjectCode;
        this.serial=serial;
    }


    public String getId() {
        return id;
    }

    public String getSerial() {
        return serial;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
