package com.example.myapplication.Admin.DataModuler;

public class SubjectDataModuler {

    String id,name,code,department,semester;

    public SubjectDataModuler(String id, String name, String code, String department, String semester) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.department = department;
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDepartment() {
        return department;
    }

    public String getSemester() {
        return semester;
    }
}
