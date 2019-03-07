package com.jian86_android.hahaclass;

import java.util.ArrayList;

public class ItemInstructor {
    private String l_num; //강사 번호
    private String title; // php l_name, 이름
    private String subTitle;//프로젝트 타이틀
    private String imgPath; //메인 이미지
    private String license,field, career; //intro
    private ArrayList<Schedule>schedules;




    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    //for level3
    public ItemInstructor(String title, String subTitle, String imgPath) {
        this.title = title;
        this.subTitle = subTitle;
        this.imgPath = imgPath;
    }
    public ItemInstructor(String title, String subTitle, String imgPath, String license, String field, String career) {
        this.title = title;
        this.subTitle = subTitle;
        this.imgPath = imgPath;
        this.license = license;
        this.field = field;
        this.career = career;
    }

    public ItemInstructor(String l_num, String title, String subTitle, String imgPath) {
        this.l_num = l_num;
        this.title = title;
        this.subTitle = subTitle;
        this.imgPath = imgPath;
    }

    public ItemInstructor(String l_num, String title, String subTitle, String imgPath, String license, String field, String career) { //기본세팅 :Instructors를 위한
        this.l_num = l_num;
        this.title = title;
        this.subTitle = subTitle;
        this.imgPath = imgPath;
        this.license = license;
        this.field = field;
        this.career = career;
    }

    public String getL_num() {
        return l_num;
    }

    public void setL_num(String l_num) {
        this.l_num = l_num;
    }
}
