package com.jian86_android.hahaclass;

import android.app.Application;
import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class ApplicationClass extends Application {

    public UserInfo userInfo;
    public String state;
    public int level;
    public ItemInstructor itemInstructor;
    public int instructorNo;


    public HashSet<ApplyClassInfo> applySchedule = new HashSet<>();//강사강의 번호// 내가 신청한 강의 확인을 하기위한
    public ArrayList<Board>boards= new ArrayList<>();

    //기본세팅 instructors
    public  Instructors setupInstructors;


    //기본세팅 보드관련
    public String board_title;
    public String board_imgpath; //기본 이미지
    public HashMap<String,SpinnerInfo > board_instructor= new HashMap<>();//instructor_no, instructor title+subtitle

    //세팅로그에서 보는 applyClass 리스트용
    public  ArrayList<Schedule> myApplyClass = new ArrayList<>();
    public  ArrayList<RecivedApplicant> myReceivedClass = new ArrayList<>();


    //게터 셋터

    public void setApplySchedule(HashSet<ApplyClassInfo> applySchedule) {
        this.applySchedule = applySchedule;
    }

    public ArrayList<RecivedApplicant> getMyReceivedClass() {
        return myReceivedClass;
    }

    public void setMyReceivedClass(ArrayList<RecivedApplicant> myReceivedClass) {
        this.myReceivedClass = myReceivedClass;
    }

    public ArrayList<Schedule> getMyApplyClass() {
        return myApplyClass;
    }

    public void setMyApplyClass(ArrayList<Schedule> myApplyClass) {
        this.myApplyClass = myApplyClass;
    }

    public HashMap<String, SpinnerInfo> getBoard_instructor() {
        return board_instructor;
    }

    public void setBoard_instructor(HashMap<String, SpinnerInfo> board_instructor) {
        this.board_instructor = board_instructor;
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public String getBoard_title() {
        return board_title;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public String getBoard_imgpath() {
        return board_imgpath;
    }

    public void setBoard_imgpath(String board_imgpath) {
        this.board_imgpath = board_imgpath;
    }

    public void setBoards(ArrayList<Board> boards) {
        this.boards = boards;
    }

    public HashSet<ApplyClassInfo> getApplySchedule() {
        return applySchedule;
    }//강사별 스케쥴을 담음

    public void setApplySchedule(ApplyClassInfo applySchedule) {

        this.applySchedule.add(applySchedule);
    }



    public int getInstructorNo() {
        return instructorNo;
    }

    public void setInstructorNo(int instructorNo) {
        this.instructorNo = instructorNo; //강사 번호
    }



    public ItemInstructor getItemInstructor() {
        return itemInstructor;
    }

    public void setItemInstructor(ItemInstructor itemInstructor) {
        this.itemInstructor = itemInstructor;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Instructors getSetupInstructors() {
        return setupInstructors;
    }

    public void setSetupInstructors(Instructors setupInstructors) {
        this.setupInstructors = setupInstructors;
    }

    /** onCreate()
     * 액티비티, 리시버, 서비스가 생성되기전 어플리케이션이 시작 중일때
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * onConfigurationChanged()
     * 컴포넌트가 실행되는 동안 단말의 화면이 바뀌면 시스템이 실행
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
