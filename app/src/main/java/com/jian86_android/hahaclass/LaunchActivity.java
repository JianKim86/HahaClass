package com.jian86_android.hahaclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.jian86_android.hahaclass.databinding.Lunch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {
    Lunch binding;
    Timer timer = new Timer();
    ApplicationClass applicationClass;
    //RelativeLayout relativeLayout;
    private ArrayList<Board>boards= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_launch);
        binding.setLaunch(this);
//        setContentView(R.layout.activity_launch);
//        relativeLayout =findViewById(R.id.relativeLayout);
//        Animation animation = AnimationUtils.loadAnimation(LaunchActivity.this,R.anim.appear_logo);
//        binding.layoutRelative.startAnimation(animation);
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


        HashMap<String, String> board_write_spinner= new HashMap<>();
        board_write_spinner.put("1","윤나영"+ "하하 웃음 클레스");//instructor_no, instructor title+subtitle
        board_write_spinner.put("2","윤나영"+ "하하 호호 클레스");
        board_write_spinner.put("3","윤나영"+ "하하 흐흐 클레스");
        board_write_spinner.put("4","윤나영"+ "하하 히히 클레스");
        board_write_spinner.put("5","윤나영"+ "하하 용용 클레스");
        applicationClass.setBoard_instructor(board_write_spinner);



        Board board;
        board= new Board();
//        board.setBoard_no("1");
        board.setBoard_date("2018-02-22");
        board.setImg(true);
        board.setBoard_reply("(10)");
        board.setBoard_title("블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라");
        board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
        board.setBoard_writer("김지안");
        board.setBoard_id("o0x0oa@naver.com");
        board.setBoard_msg("내용1");

        boards.add(board);

        board = new Board();
//        board.setBoard_no("2");
        board.setBoard_date("2018-02-22");
        board.setImg(true);
        board.setBoard_reply("(10)");
        board.setBoard_title("블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라");
        board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
        board.setBoard_writer("김지안");
        board.setBoard_id("o0x0oa@naver.com");
        board.setBoard_msg("내용2");
        boards.add(board);

        board = new Board();
//        board.setBoard_no("3");
        board.setBoard_date("2018-02-22");
        board.setImg(false);
        board.setBoard_reply("(10)");
        board.setBoard_title("블라블라블라블라블라블라");
        board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
        board.setBoard_writer("김지안");
        board.setBoard_id("o0x0oa@naver.com");
        board.setBoard_msg("내용3");
        boards.add(board);

        board = new Board();
//        board.setBoard_no("4");
        board.setBoard_date("2018-02-22");
        board.setImg(true);
        board.setBoard_reply("(10)");
        board.setBoard_title("블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라");
        board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
        board.setBoard_writer("김지안");
        board.setBoard_id("o0x0oa@naver.com");
        board.setBoard_msg("내용4");
        boards.add(board);

        board = new Board();
//        board.setBoard_no("5");
        board.setBoard_date("2018-02-22");
        board.setImg(true);
        board.setBoard_reply("(10)");
        board.setBoard_title("블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라블라");
        board.setBoard_instructor("윤나영 / 하하 웃음 클레스");
        board.setBoard_writer("김지안");
        board.setBoard_id("o0x0oa@naver.com");
        board.setBoard_msg("내용5");
        boards.add(board);

        applicationClass.setBoards(boards);
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
    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }
}
