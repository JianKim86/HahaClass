package com.jian86_android.hahaclass;

public class Board {
    private String b_num; //보드 넘버
    private String board_title;//보드 타이틀
    private String board_date;//보드 리스트뷰에 들어가는 데이트
    private String board_reply;//리플수


    private boolean isImg;// 보드 리스트뷰에 들어가는 이미지 확인
    private String board_imgpath = "";// 이미지 패스
    private String board_header_title;
    private String board_writer;//작성자

    private SpinnerInfo board_instructor;//분류

    private String board_msg;//메세지
    private String board_pwd;//업로드시 비밀번호
    private String board_id;//작성자 아이디

    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

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

    public SpinnerInfo getBoard_instructor() {
        return board_instructor;
    }

    public void setBoard_instructor(SpinnerInfo board_instructor) {
        this.board_instructor = board_instructor;
    }

    public void setBoard_imgpath(String board_imgpath) {
        this.board_imgpath = board_imgpath;
    }

    public String getBoard_header_title() {
        return board_header_title;
    }

    public String getB_num() {
        return b_num;
    }

    public void setB_num(String b_num) {
        this.b_num = b_num;
    }

    public String getBoard_title() {
        return board_title;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
        this.board_header_title = board_title;
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

    public Board() {
    }

    public Board(String b_num, String board_title, String board_date, String board_reply, boolean isImg, String board_imgpath, String board_header_title, String board_writer, SpinnerInfo board_instructor, String board_msg, String board_pwd, String board_id) {
        this.b_num = b_num;
        this.board_title = board_title;
        this.board_date = board_date;
        this.board_reply = board_reply;
        this.isImg = isImg;
        this.board_imgpath = board_imgpath;
        this.board_header_title = board_header_title;
        this.board_writer = board_writer;
        this.board_instructor = board_instructor;
        this.board_msg = board_msg;
        this.board_pwd = board_pwd;
        this.board_id = board_id;
    }
}//Board
