package com.jian86_android.hahaclass;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.jian86_android.hahaclass.databinding.Lunch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {

    Lunch binding;
    Timer timer = new Timer();
    private static final int img_length =60;
    private static final String baseImgePath = "http://jian86.dothome.co.kr/HahaClass/";
    private static final int IMAGEUPLOAD = 100;
    ApplicationClass applicationClass=null;
    //RelativeLayout relativeLayout;
    private ArrayList<Board>boards= new ArrayList<>();
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case IMAGEUPLOAD:
                if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "외부저장소 사용 불가 \n 이미지 저장 불가", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }//onRequestPermissionsResult
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
            }
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }



        binding = DataBindingUtil.setContentView(this,R.layout.activity_launch);
        //외부저장소에 있는 이미지 파일을 서버로 보내기 위한 퍼미션
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},IMAGEUPLOAD);
            }
        }



        binding.setLaunch(this);
        Glide.with(this).load(R.drawable.loding).into(binding.ivLoding);

        applicationClass = (ApplicationClass)getApplicationContext();
        getData();


    }

    void  getData(){
        //TODO: 서버에서 내용 가져와야함

        String board_title ="자유로운 소통의 공간입니다\n행복한 에너지를 공유해요";
        applicationClass.setBoard_title(board_title);
        String board_img= "";
        applicationClass.setBoard_imgpath(board_img);
        //서버에서  클레스코드와 강사정보를 읽어와서 담기

//
//
//        Board board;
//        board= new Board();
////        board.setBoard_no("1");
//        board.setBoard_date("2018-02-22");
//        board.setImg(true);
//        board.setBoard_reply("(10)");
//        board.setBoard_title("블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라");
//      //  board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
//        board.setBoard_writer("김지안");
//        board.setBoard_id("o0x0oa@naver.com");
//        board.setBoard_msg("내용1");
//
//        boards.add(board);
//
//        board = new Board();
////        board.setBoard_no("2");
//        board.setBoard_date("2018-02-22");
//        board.setImg(true);
//        board.setBoard_reply("(10)");
//        board.setBoard_title("블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라");
//       // board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
//        board.setBoard_writer("김지안");
//        board.setBoard_id("o0x0oa@naver.com");
//        board.setBoard_msg("내용2");
//        boards.add(board);
//
//        board = new Board();
////        board.setBoard_no("3");
//        board.setBoard_date("2018-02-22");
//        board.setImg(false);
//        board.setBoard_reply("(10)");
//        board.setBoard_title("블라블라블라블라블라블라");
//        //board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
//        board.setBoard_writer("김지안");
//        board.setBoard_id("o0x0oa@naver.com");
//        board.setBoard_msg("내용3");
//        boards.add(board);
//
//        board = new Board();
////        board.setBoard_no("4");
//        board.setBoard_date("2018-02-22");
//        board.setImg(true);
//        board.setBoard_reply("(10)");
//        board.setBoard_title("블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라");
//       // board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
//        board.setBoard_writer("김지안");
//        board.setBoard_id("o0x0oa@naver.com");
//        board.setBoard_msg("내용4");
//        boards.add(board);
//
//        board = new Board();
////        board.setBoard_no("5");
//        board.setBoard_date("2018-02-22");
//        board.setImg(true);
//        board.setBoard_reply("(10)");
//        board.setBoard_title("블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라");
//       // board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
//        board.setBoard_writer("김지안");
//        board.setBoard_id("o0x0oa@naver.com");
//        board.setBoard_msg("내용5");
//        boards.add(board);
//
//        applicationClass.setBoards(boards);

        //강사리스트 셋업 //강사 이름 , 클레스 타이틀 대표 이미지 가 arrayList로 담겨있음
        setupInstructors = new Instructors();
        //데이터 작업
        DBgetInstructorsList();

        //데이터를 다읽어오면 넘김
        timer.schedule(task,4000);
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }//run
    };
    Instructors setupInstructors;
    ItemInstructor itemInstructor;
    //기본세팅 강사 /스피너
    void DBgetInstructorsList(){
                //db table : instructor_list 에서 정보 읽어와서 setupInstructors에 담음
                //db table : instructor_list 에서 정보를 읽어와 스피너 만들기 'class_key'
        final HashMap<String, SpinnerInfo> board_write_spinner= new HashMap<>();

        String serverURL = "http://jian86.dothome.co.kr/HahaClass/get_instructor_list.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,serverURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.i("rrii",response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            String l_num;
                            String title;
                            String subTitle;
                            String imgPath="";
                            String l_license;
                            String l_field;
                            String l_career;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                l_num = jsonObject.getString("l_num");
                                title = jsonObject.getString("l_name");
                                subTitle = jsonObject.getString("l_project_title");
                                l_license = jsonObject.getString("l_license");
                                l_field = jsonObject.getString("l_field");
                                l_career = jsonObject.getString("l_career");
                                imgPath = jsonObject.getString("l_project_image_path");
//                                Log.i("imgpathtt",imgPath);
                                imgPath = baseImgePath + imgPath;
                                itemInstructor = new ItemInstructor(l_num,title,subTitle,imgPath,l_license,l_field,l_career);
                                setupInstructors.getItemInstructors().add(itemInstructor);// 선생님 리스트에 추가
                               // Log.i("setupInstructors",setupInstructors.getItemInstructors().size()+"");

                                //spinner
                                if(jsonObject.has("class_key")) {
                                    JSONObject jsonkeyArray = jsonObject.getJSONObject("class_key");
                                    if (jsonkeyArray != null) {
                                        for (int y = 0; y < jsonkeyArray.length(); y++) {
                                            JSONObject jsonObject1 = jsonkeyArray.getJSONObject(y + "");

                                            String check_l_num = jsonObject1.getString("l_num");
                                            String class_code = jsonObject1.getString("class_code");
                                            String class_title = jsonObject1.getString("class_title");
                                            //   apply_user_num = jsonObject1.getString("apply_user_num");
                                            String spinnerTitle= title + " / "+subTitle + " / "+ class_title;
                                            board_write_spinner.put(Integer.parseInt(i+""+y)+"",new SpinnerInfo(check_l_num,class_code,spinnerTitle)); //헤시에 add로 들어감
                                        }
                                    }
                                }
                                applicationClass.setBoard_instructor(board_write_spinner); //스피너 리스트 추가
                            }//for
                            //게시판
                            DBgetBoardInfo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(LaunchActivity.this);
                requestQueue.add(stringRequest);

                applicationClass.setSetupInstructors(setupInstructors);
            }
//보드 정보 얻어오기
    void DBgetBoardInfo(){
        String serverURL = "http://jian86.dothome.co.kr/HahaClass/get_board_info_list.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    String b_num;
                    String board_user_email;
                    String name;
                    String class_code;
                    String l_num;
                    String board_title;
                    String board_msg;
                    String board_image_path="";
                    String board_pwd;
                    String board_cnt_reply;
                    String date;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        b_num = jsonObject.getString("b_num"); //보드 확인용
                        board_user_email = jsonObject.getString("board_user_email");
                        class_code = jsonObject.getString("class_code");
                        l_num = jsonObject.getString("l_num");
                        board_title = jsonObject.getString("board_title");
                        board_msg = jsonObject.getString("board_msg");
                        board_pwd = jsonObject.getString("board_pwd");
                        board_cnt_reply = jsonObject.getString("board_cnt_reply");
                        date = jsonObject.getString("date");
                        name = jsonObject.getString("name");
                        board_image_path = jsonObject.getString("board_image_path");
                        boolean is_img;

                        if(board_image_path!=null && board_image_path.length()>"uploads/20190310044626".length()) is_img = true;
                        else is_img= false;
                        board_image_path = baseImgePath + board_image_path;
                        String spinner_title="";
                        String spinner_l_name="";
                        String spinner_l_project_title="";
                        if(jsonObject.has("class_title")) { spinner_title =  jsonObject.getString("class_title"); }
                        if(jsonObject.has("l_name")) {spinner_l_name =  jsonObject.getString("l_name"); }
                        if(jsonObject.has("l_project_title")) {spinner_l_project_title =  jsonObject.getString("l_project_title"); }
                        String spinnerTitle="";
                        if(spinner_title == null || spinner_title.length() <= 0) spinnerTitle = "테스트용";
                        else spinnerTitle = spinner_l_name + " / "+spinner_l_project_title + " / "+ spinner_title;
                        SpinnerInfo spinnerInfo = new SpinnerInfo(l_num,class_code,spinnerTitle);

                        Board board = new Board(b_num,board_title,date,"( "+board_cnt_reply+" ) ",is_img,board_image_path,board_title,name,spinnerInfo,board_msg,board_pwd,board_user_email);
                        boards.add(board);

                    }//for

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(LaunchActivity.this);
        requestQueue.add(stringRequest);
        applicationClass.setBoards(boards);

    }//DBgetBoardInfo



    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }
}
