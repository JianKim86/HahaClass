package com.jian86_android.hahaclass;

public class DatasItem {
    private String l_num;
    private String class_code;
    private String week;
    private String date;
    private String title;
    private String configuration;
    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration =configuration;
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

    public DatasItem() {
    }

    public DatasItem(String l_num, String class_code, String week, String date, String title, String configuration) { //DB에서 읽어와서 넣음
        this.l_num = l_num;
        this.class_code = class_code;
        this.week = week;
        this.date = date;
        this.title = title;
        this.configuration = configuration;
    }
}//DatasItem
