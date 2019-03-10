package com.jian86_android.hahaclass;

public class SpinnerInfo {
    private String l_num;
    private String class_code;
    private String spinner_title;

    public SpinnerInfo() {
    }

    public SpinnerInfo(String l_num, String class_code, String spinner_title) {
        this.l_num = l_num;
        this.class_code = class_code;
        this.spinner_title = spinner_title;
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

    public String getSpinner_title() {
        return spinner_title;
    }

    public void setSpinner_title(String spinner_title) {
        this.spinner_title = spinner_title;
    }
}
