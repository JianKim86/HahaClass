package com.jian86_android.hahaclass;

public class ApplyClassInfo {
    private String l_num;
    private String class_code;

    //신청자 정보를읽기 위한
    private String apply_name;
    private String apply_phone ;
    private String date ;
    private String apply_user_email;

    public ApplyClassInfo(String l_num, String class_code, String apply_name, String apply_phone, String date, String apply_user_email) {//for setting_log : user Info
        this.l_num = l_num;
        this.apply_user_email = apply_user_email;
        this.class_code = class_code;
        this.apply_name = apply_name;
        this.apply_phone = apply_phone;
        this.date = date;
    }

    public String getApply_user_email() {
        return apply_user_email;
    }

    public void setApply_user_email(String apply_user_email) {
        this.apply_user_email = apply_user_email;
    }

    public String getApply_name() {
        return apply_name;
    }

    public void setApply_name(String apply_name) {
        this.apply_name = apply_name;
    }

    public String getApply_phone() {
        return apply_phone;
    }

    public void setApply_phone(String apply_phone) {
        this.apply_phone = apply_phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ApplyClassInfo(String l_num, String class_code) {
        this.l_num = l_num;
        this.class_code = class_code;
    }

    public ApplyClassInfo() {
    }

    public String getL_num() {
        return l_num;
    }

    public void setL_num(String l_num) {
        this.l_num = l_num;
    }

    public String getClass_code() {
        return class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }
}//ApplyClassInfo
