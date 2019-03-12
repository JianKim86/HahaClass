package com.jian86_android.hahaclass;

import java.util.ArrayList;
import java.util.HashSet;

public class Instructors {
    //게시판
    // 강사리스트등 기본적 세팅을 위한 클레스
    //강사 넘버, 강사번호로 알게된 강사 이름 ,강사별 클레스 타이틀이 담김 -DB table: instructor_list
    //apply
    //내가 신청한 강의를 강사리스트로 담음
    ArrayList<ItemInstructor> itemInstructors = new ArrayList<>();

    public ArrayList<ItemInstructor> getItemInstructors() {
        return itemInstructors;
    }

    public void setItemInstructors(ArrayList<ItemInstructor> itemInstructors) {
        this.itemInstructors = itemInstructors;
    }
}//BaseSetting
