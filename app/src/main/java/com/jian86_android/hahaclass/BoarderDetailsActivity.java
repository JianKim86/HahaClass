package com.jian86_android.hahaclass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BoarderDetailsActivity extends AppCompatActivity {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final int PIC = 1000;
    private Toolbar toolbar;
    private UserInfo userInfo;
    private Intent getintent;
    String state,name,email,phone,pass,img;
    int level;

    private int position;
    private  Board board;
    private Spinner spinner;
    private TextView tv_instructor, tv_date, tv_title,tv_user_name,tv_msg;
    private Button btn_reply,btn_modify,btn_delete;
    private LinearLayout layout_edit_title,layout_edit_msg,layout_edit_btn;
    private EditText edit_title,edit_msg,repassword;
    private TextInputLayout input_layout_title,input_layout_msg;
    private ListView lv_apply;
    private ImageView iv_img;
    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }//onCreateOptionsMenu
    //메뉴
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.goback:
                finish();
                break;
        }
        //아이템 클릭상황을 토글 버튼에 전달
        return super.onOptionsItemSelected(item);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarder_details);
        applicationClass =(ApplicationClass)getApplicationContext();
        getintent = getIntent();
        getData();
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);

        spinner =findViewById(R.id.spinner);
        tv_instructor =findViewById(R.id.tv_instructor);
        tv_date = findViewById(R.id.tv_date);
        tv_title = findViewById(R.id.tv_title);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_msg = findViewById(R.id.tv_msg);

        btn_reply = findViewById(R.id.btn_reply);
        btn_modify = findViewById(R.id.btn_modify);
        btn_delete = findViewById(R.id.btn_delete);

        layout_edit_title = findViewById(R.id.layout_edit_title);
        layout_edit_msg = findViewById(R.id.layout_edit_msg);
        layout_edit_btn = findViewById(R.id.layout_edit_btn);

        edit_title = findViewById(R.id.edit_title);
        edit_msg = findViewById(R.id.edit_msg);
        repassword = findViewById(R.id.repassword);
        lv_apply = findViewById(R.id.lv_apply);
        iv_img= findViewById(R.id.iv_img);
        input_layout_title = (TextInputLayout)findViewById(R.id.input_layout_title);
        input_layout_msg = (TextInputLayout)findViewById(R.id.input_layout_msg);


        setData();
        clicklietnearSet();

    }//onCreate
    private void editViewShow(){

        input_layout_title.setCounterEnabled(true);
        input_layout_msg.setCounterEnabled(true);
        input_layout_title.setCounterMaxLength(30);
        input_layout_msg.setCounterMaxLength(500);
        layout_edit_title.setVisibility(View.VISIBLE);
        layout_edit_msg.setVisibility(View.VISIBLE);
        btn_modify.setText("저장");
        spinner.setVisibility(View.VISIBLE);

        edit_title.setText(changetitle);
        edit_msg.setText(changemsg);
        tv_title.setVisibility(View.GONE);
        tv_msg.setVisibility(View.GONE);

    }

    private String changetitle;
    private String changemsg;
    private  void editViewHide(){
        boolean cancel = false;
        View focusView = null;
        String password = repassword.getText().toString();
        changetitle= edit_title.getText().toString();
        changemsg= edit_msg.getText().toString();

        if (TextUtils.isEmpty(changetitle)) {
            edit_title.setError(getString(R.string.error_field_required));
            focusView = edit_title;
            cancel = true;
        } else if(TextUtils.isEmpty(changemsg)) {
            edit_msg.setError(getString(R.string.error_field_required));
            focusView = edit_msg;
            cancel = true;
        }else if(TextUtils.isEmpty(changemsg)) {
            edit_msg.setError(getString(R.string.error_field_required));
            focusView = edit_msg;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(board.getBoard_pwd(),password)) {
            repassword.setError(getString(R.string.error_invalid_password));
            focusView = repassword;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            layout_edit_title.setVisibility(View.GONE);
            layout_edit_msg.setVisibility(View.GONE);
            btn_modify.setText("수정");
            spinner.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);
            tv_msg.setVisibility(View.VISIBLE);

            tv_title.setText(changetitle);
            tv_msg.setText(changemsg);
            dataChange();
        }

    }
    //서브밋전 확인
    private boolean isPasswordValid(String password,String setPassword) {
        return (password.length() > 4) && (password.equals(setPassword));
    }
    private boolean isTitleValid(String title, int len ) {
        return (title.length() <= len);
    }
    private boolean isOpen;
    public void clicklietnearSet(){


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = v.getId();
                switch (id){
                    case R.id.btn_modify: if(!isOpen){ editViewShow(); isOpen = true; } else {editViewHide(); isOpen = false; } break;
                    case R.id.btn_delete: break;
                    case R.id.btn_reply: break;
                }
            }
        };

        btn_modify.setOnClickListener(onClickListener);
        btn_delete.setOnClickListener(onClickListener);
        btn_reply.setOnClickListener(onClickListener);

    }//clicklietnearSet

    private void dataChange(){
        applicationClass.getBoards().get(position).setBoard_title(changetitle);
        applicationClass.getBoards().get(position).setBoard_msg(changemsg);
    }//dataChange
    private String picPath=null;
    public void setData(){
        getSupportActionBar().setTitle("");
        changetitle=board.getBoard_title();
        changemsg =board.getBoard_msg();
        String strId = board.getBoard_id().substring(0,3)+"***";
        tv_date.setText(board.getBoard_date());
        tv_user_name.setText(board.getBoard_writer()+"("+strId+")");
        tv_title.setText(changetitle);
        tv_msg.setText(changemsg);
        tv_instructor.setText(board.getBoard_instructor());


        if(board.getBoard_imgpath() !=null && board.getBoard_imgpath() != ""){
            iv_img.setVisibility(View.VISIBLE); setPic(board.getBoard_imgpath());
        }else {iv_img.setVisibility(View.GONE); picPath=null;}


        if(level==3){
            layout_edit_btn.setVisibility(View.VISIBLE);
        }
        isOpen= false;


    }//setData


    public void getData(){

        position = getintent.getIntExtra("position",0);
        board = applicationClass.getBoards().get(position);
        //받은 포지션으로 스케쥴리스트의 몇번째인지 확인

        //유저정보 가져오기
        state = applicationClass.getState();
        if(state.equals(USER)){
            userInfo= applicationClass.getUserInfo();
            name =userInfo.getName();
            email = userInfo.getEmail();
            phone = userInfo.getPhone();
            pass = userInfo.getPassword();
            img = userInfo.getImagePath();
            level = userInfo.getLevel();
        }else{
            level = 3;
            userInfo = null;
        }

    }//getIntentData

    void takePic(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PIC);
    }

    public void setPic(String pic){
        //Toast.makeText(applicationClass, ""+pic, Toast.LENGTH_SHORT).show();
        picPath = pic;
        if(picPath!=null&&!(picPath.equals(""))){
            Uri uRi = Uri.parse(picPath);
            Picasso.get().load(uRi).into(iv_img);
        }else {Glide.with(this).load(R.drawable.ic_launcher_background).into(iv_img);}
        applicationClass.getBoards().get(position).setBoard_imgpath(picPath);
    }//setPic
    Bitmap btmapPicPath;
    public void setPic(Bitmap pic){
        btmapPicPath = pic;
        Glide.with(this).load(pic).into(iv_img);
        picPath=null;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PIC:

                //갤러리 화면에서 이미지를 선택하고 돌아왔는지 체크
                //두 번째 파라미터 : resultCode
                if (resultCode == RESULT_OK){

                    Uri uri=data.getData();
                    picPath = uri.toString();
                    if(uri != null){
                           setPic(picPath);

                    }else{
                        //아니면 Intent 에 Extra 데이터로 Bitmap 이 전달되어 옴
                        Bundle bundle=data.getExtras();
                        Bitmap bm= (Bitmap) bundle.get("data"); // key 값 "data" 는 정해진거야
                        //iv.setImageBitmap(bm);

                           setPic(bm);
                        }




                }//if

                break;
        }

    }//onActivityResult
}//BoarderDetailsActivity
