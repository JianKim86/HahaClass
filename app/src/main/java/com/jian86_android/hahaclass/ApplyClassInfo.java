package com.jian86_android.hahaclass;

public class ApplyClassInfo {
    private String l_num;
    private String class_code;

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
