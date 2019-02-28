package com.jian86_android.hahaclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

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

    private View edit;
    private ImageView  btn_upload_img, btn_upload_cancel;
    private HashMap<String,String> spinnerHash;
    private TextView tv_total_count,tv_board_title;
    private ImageView iv_title_img, iv_edit_img, iv_edit_title;
    private ArrayList<String> spinnerItems = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    private  Board board;
    private Spinner spinner;
    private TextView tv_instructor, tv_date, tv_title,tv_user_name,tv_msg;
    private Button btn_reply,btn_modify, btn_save, btn_delete, btn_return;
    private LinearLayout layout_edit_title,layout_edit_msg,layout_edit_btn;
    private EditText edit_title,edit_msg,repassword;
    private TextInputLayout input_layout_title,input_layout_msg;
    private ListView lv_repply;
    private ImageView iv_img,iv_img_show;
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

        tv_instructor =findViewById(R.id.tv_instructor);
        tv_date = findViewById(R.id.tv_date);
        tv_title = findViewById(R.id.tv_title);
        tv_msg = findViewById(R.id.tv_msg);
        btn_reply = findViewById(R.id.btn_reply);
        btn_modify = findViewById(R.id.btn_modify);
        layout_edit_btn = findViewById(R.id.layout_edit_btn);

        lv_repply = findViewById(R.id.lv_repply);

        iv_img= findViewById(R.id.iv_img);
        iv_img_show= findViewById(R.id.iv_img_show);
    //include
    //include view
        edit =findViewById(R.id.edit);

        spinner =findViewById(R.id.spinner);
        tv_instructor =findViewById(R.id.tv_instructor);
        tv_user_name = findViewById(R.id.tv_user_name);

        btn_save = findViewById(R.id.btn_save);
        btn_return = findViewById(R.id.btn_return);
        btn_delete = findViewById(R.id.btn_delete);

        btn_upload_img = findViewById(R.id.btn_upload_img);
        btn_upload_cancel = findViewById(R.id.btn_upload_cancel);
        edit_title = findViewById(R.id.edit_title);
        edit_msg = findViewById(R.id.edit_msg);
        repassword = findViewById(R.id.repassword);

        input_layout_title = (TextInputLayout)findViewById(R.id.input_layout_title);
        input_layout_msg = (TextInputLayout)findViewById(R.id.input_layout_msg);

        setData();


    }//onCreate

    //비밀번호 확인
    private void checkPwd(final boolean isDelete) {
        String msg="";
        if(isDelete) msg = "삭제하시겠습니까?";
        else msg = "수정 하시겠습니까?";


        AlertDialog.Builder builder = new AlertDialog.Builder(BoarderDetailsActivity.this);
        //builder.setTitle("AlertDialog Title");
        builder.setMessage(msg);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        vsPwd(isDelete);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("아니오", null);
        builder.show();
    }//checkPwd

    //비밀번호 다이알로그
    private void vsPwd(final boolean viewCase){

        final TextInputEditText edittext = new TextInputEditText(this);
        edittext.setHint("해당 게시물의 비밀번호를 입력하세요");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 확인");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if( edittext.getText().toString().equals(board.getBoard_pwd())){
                            editViewShow(viewCase);
                        }
                        else  Toast.makeText(getApplicationContext(),"잘못된 입력입니다" ,Toast.LENGTH_LONG).show();

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();

    }//vsPwd

//수정 뷰 보이기
    private void editViewShow(boolean viewCase){

        if(viewCase) {deletData(); return;} //삭제뷰 보내기

        edit.setVisibility(View.VISIBLE);
        input_layout_title.setCounterEnabled(true);
        input_layout_msg.setCounterEnabled(true);
        input_layout_title.setCounterMaxLength(30);
        input_layout_msg.setCounterMaxLength(500);

//스피너에 값넣기
        for (String mapkey : spinnerHash.keySet()){
            spinnerItems.add(spinnerHash.get(mapkey));
        }
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        //event listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_instructor.setText(spinner.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//이름 아이디 넣기
        String strId = userInfo.getEmail().substring(0,3)+"***";
        tv_user_name.setText(userInfo.getName()+"("+strId+")"+"님 안녕하세요 !");
        edit_title.setText(changetitle);
        edit_msg.setText(changemsg);
//이미지 넣기

        if(board.getBoard_imgpath() !=null && board.getBoard_imgpath() != ""){
            iv_img.setVisibility(View.VISIBLE); setPic(board.getBoard_imgpath()); isupload = true;
        }else {iv_img.setVisibility(View.GONE); picPath=null;}

        if(isupload) btn_upload_cancel.setVisibility(View.VISIBLE);

    }//editViewShow
    private String changetitle;
    private String changemsg;
    private  void editViewHide(){
        boolean cancel = false;
        View focusView = null;
        String password = repassword.getText().toString();
        changetitle= edit_title.getText().toString();
        changemsg= edit_msg.getText().toString();

        edit_title.setError(null);
        edit_msg.setError(null);
        repassword.setError(null);
        if (TextUtils.isEmpty(changetitle)||!isTitleValid(changetitle,30)) {

            focusView = edit_title;
            cancel = true;
        } else if(TextUtils.isEmpty(changemsg)||!isTitleValid(changemsg,500)) {
            focusView = edit_msg;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            repassword.setError(getString(R.string.error_invalid_password));
            focusView = repassword;
            cancel = true;
        }else if (!isPasswordValid(board.getBoard_pwd(),password)) {
            repassword.setError(getString(R.string.error_invalid_password));
            focusView = repassword;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
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
    private boolean isupload=false;

    final static boolean ISMODIFY =false;
    final static boolean ISDELETE =true;

//삭제
    private  void deletData(){
        applicationClass.getBoards().remove(position);
    }//deletData


    private void dataChange(){
        applicationClass.getBoards().get(position).setBoard_title(changetitle);
        applicationClass.getBoards().get(position).setBoard_msg(changemsg);
        edit.setVisibility(View.GONE);
    }//dataChange
    private String picPath=null;


    public void clicklietnearSet(){


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = v.getId();
                switch (id){
                    case R.id.btn_modify: checkPwd(ISMODIFY);  break;//else failedChedkPwd();
                    case R.id.btn_delete: checkPwd(ISDELETE);  break;
                    case R.id.btn_save: editViewHide(); break;
                    case R.id.btn_reply: break;
                    case R.id.btn_upload_img: iv_img.setVisibility(View.VISIBLE); takePic(); isupload =true; break;
                    case R.id.btn_upload_cancel: if(isupload) { isupload =false; iv_img.setVisibility(View.GONE);  btn_upload_cancel.setVisibility(View.GONE); picPath = null; btmapPicPath =null; } break;
                    case R.id.btn_return:  returnList();  break;
                }
            }
        };

        btn_modify.setOnClickListener(onClickListener);
        btn_delete.setOnClickListener(onClickListener);
        btn_reply.setOnClickListener(onClickListener);

        btn_save.setOnClickListener(onClickListener);
        btn_return.setOnClickListener(onClickListener);
        btn_upload_img.setOnClickListener(onClickListener);
        btn_upload_cancel.setOnClickListener(onClickListener);

    }//clicklietnearSet
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
            iv_img_show.setVisibility(View.VISIBLE); setPic(board.getBoard_imgpath()); isupload = true;
        }else { iv_img_show.setVisibility(View.GONE); picPath=null; }

        if(level==3){
            layout_edit_btn.setVisibility(View.VISIBLE);
        }
        isOpen= false;

        clicklietnearSet();
    }//setData


    public void getData(){

        position = getintent.getIntExtra("position",0);
        board = applicationClass.getBoards().get(position);
        //받은 포지션으로 스케쥴리스트의 몇번째인지 확인
        spinnerHash = applicationClass.getBoard_instructor();


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
            if(userInfo.getEmail().equals(board.getBoard_id())){level=3;}
        }else{
            level = 0;
            userInfo = null;
        }

    }//getIntentData

//게시판으로 돌아가기
    private void returnList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("AlertDialog Title");
        String msg = "게시판으로 돌아가시겠습니까?";
        builder.setMessage(msg);
        builder.setPositiveButton("예",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BoarderDetailsActivity.this,BoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setNegativeButton("아니오", null);
        builder.show();
    }


    ///이미지작업
    void takePic(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PIC);
    }

    public void setPic(String pic){
        //Toast.makeText(applicationClass, ""+pic, Toast.LENGTH_SHORT).show();
        picPath = pic;
        ImageView iv;
        if(isupload){iv = iv_img; btn_upload_cancel.setVisibility(View.VISIBLE);  } else{ iv =iv_img_show; }
        if(picPath!=null&&!(picPath.equals(""))){
            Uri uRi = Uri.parse(picPath);
            Picasso.get().load(uRi).into(iv);
        }else {Glide.with(this).load(R.drawable.ic_launcher_background).into(iv);}
        applicationClass.getBoards().get(position).setBoard_imgpath(picPath);
    }//setPic
    Bitmap btmapPicPath;
    public void setPic(Bitmap pic){
        btmapPicPath = pic;
        ImageView iv;
        if(isupload){iv = iv_img; btn_upload_cancel.setVisibility(View.VISIBLE);  } else{ iv =iv_img_show; }
        Glide.with(this).load(pic).into(iv);
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
