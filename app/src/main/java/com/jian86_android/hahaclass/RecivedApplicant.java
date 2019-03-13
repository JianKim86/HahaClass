package com.jian86_android.hahaclass;

public class RecivedApplicant extends Schedule {
    //강의 신청자 정보를 담는 클레스

    private String date;
    private String projectTitle;
    private String cnt;
    private ApplyClassInfo classInfo; //l_num, class_code

    public ApplyClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ApplyClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public RecivedApplicant(String date, String projectTitle) {
        this.date = date;
        this.projectTitle = projectTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}
