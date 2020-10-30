package com.example.myapplication.Admin.DataModuler;

public class TeacherDataModuler {
    String id,name,code,departmentname,phone,image;

    public TeacherDataModuler(String id, String name, String code, String departmentname, String phone, String image) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.departmentname = departmentname;
        this.phone = phone;
        this.image = image;
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

    public String getDepartmentname() {
        return departmentname;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }
}
