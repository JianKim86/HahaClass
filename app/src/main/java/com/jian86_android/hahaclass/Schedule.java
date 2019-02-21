package com.jian86_android.hahaclass;

import java.util.ArrayList;

public class Schedule {
    private String projectTitle;
    private String projectImgPath;
    private String host;
    private String support;
    private String date;
    private String start;
    private String end;

    //detailed schedule
    private ArrayList<DatasItem>datas= new ArrayList<>();




    public Schedule(){}

    public Schedule(String projectTitle, String host, String start, String end) {
        this.projectTitle = projectTitle;
        this.host = host;
        this.start = start;
        this.end = end;
        setDate(start+"~"+end);
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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

    public String getProjectImgPath() {
        return projectImgPath;
    }

    public void setProjectImgPath(String projectImgPath) {
        this.projectImgPath = projectImgPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public ArrayList<DatasItem> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<DatasItem> datas) {
        this.datas = datas;
    }
}
