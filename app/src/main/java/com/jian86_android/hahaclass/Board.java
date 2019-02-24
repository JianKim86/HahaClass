package com.jian86_android.hahaclass;

public class Board {
    private String board_no;
    private String board_title;
    private String board_date;
    private String board_reply;
    private boolean isImg;
    private String board_imgpath;
    private String board_header_title;
    private String board_writer;
    private String board_instructor;

    private String board_msg;
    private String board_pwd;


    public String getBoard_msg() {
        return board_msg;
    }

    public void setBoard_msg(String board_msg) {
        this.board_msg = board_msg;
    }

    public String getBoard_pwd() {
        return board_pwd;
    }

    public void setBoard_pwd(String board_pwd) {
        this.board_pwd = board_pwd;
    }

    public String getBoard_imgpath() {
        return board_imgpath;
    }

    public String getBoard_writer() {
        return board_writer;
    }

    public void setBoard_writer(String board_writer) {
        this.board_writer = board_writer;
    }

    public String getBoard_instructor() {
        return board_instructor;
    }

    public void setBoard_instructor(String board_instructor) {
        this.board_instructor = board_instructor;
    }

    public void setBoard_imgpath(String board_imgpath) {
        this.board_imgpath = board_imgpath;
    }

    public String getBoard_header_title() {
        return board_header_title;
    }

    public void setBoard_header_title(String board_header_title) {
        this.board_header_title = board_header_title;
    }

    public String getBoard_no() {
        return board_no;
    }

    public void setBoard_no(String board_no) {
        this.board_no = board_no;
    }

    public String getBoard_title() {
        return board_title;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public String getBoard_date() {
        return board_date;
    }

    public void setBoard_date(String board_date) {
        this.board_date = board_date;
    }

    public String getBoard_reply() {
        return board_reply;
    }

    public void setBoard_reply(String board_reply) {
        this.board_reply = board_reply;
    }

    public boolean isImg() {
        return isImg;
    }

    public void setImg(boolean img) {
        isImg = img;
    }
}//Board
