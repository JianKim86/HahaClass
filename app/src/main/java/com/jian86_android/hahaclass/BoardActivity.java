package com.jian86_android.hahaclass;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BoardActivity extends AppCompatActivity {
    private static final String baseImgePath = "http://jian86.dothome.co.kr/HahaClass/";
    private static final int img_length ="uploads/20190310031854".length();
    private static final int DATACHANGE =100;

    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final int PIC = 1000;
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private UserInfo userInfo;
    String state,name,email,phone,pass,img;
    int level;
    private String picPath;
    private Bitmap btmapPicPath;
    View header;
    ListView lv_board;
    ArrayList<Board>boards;
    AdapterBoard adapterBoard;


    private View edit;
    private Spinner spinner;
    private TextView tv_instructor, tv_user_name;
    private Button btn_save,btn_delete,btn_return;
    private EditText edit_title,edit_msg,repassword;
    private TextInputLayout input_layout_title,input_layout_msg;
    private ImageView iv_img, btn_upload_img,btn_upload_cancel;
    private HashMap<String,SpinnerInfo> spinnerHash;
    private TextView tv_total_count,tv_board_title;
    private ImageView iv_title_img,iv_edit_img,iv_edit_title;
    private ArrayList<String> spinnerItems = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    private Button btn_write;//메뉴
    private ActionBarDrawerToggle drawerToggle;

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
        drawerToggle.onOptionsItemSelected(item);
        //아이템 클릭상황을 토글 버튼에 전달
        return super.onOptionsItemSelected(item);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        applicationClass =(ApplicationClass)getApplicationContext();
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
        getData();

        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu = findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);

        lv_board = findViewById(R.id.lv_board);
        btn_write = findViewById(R.id.btn_write);

        //include view
        edit =findViewById(R.id.edit);

        spinner =findViewById(R.id.spinner);
        tv_instructor =findViewById(R.id.tv_instructors);
        tv_user_name = findViewById(R.id.tv_user_name);

        btn_save = findViewById(R.id.btn_save);
        btn_return = findViewById(R.id.btn_return);
        btn_delete = findViewById(R.id.btn_delete);

        btn_upload_img = findViewById(R.id.btn_upload_img);
        btn_upload_cancel = findViewById(R.id.btn_upload_cancel);
        edit_title = findViewById(R.id.edit_title);
        edit_msg = findViewById(R.id.edit_msg);
        repassword = findViewById(R.id.repassword);
        iv_img= findViewById(R.id.iv_img);


        input_layout_title = (TextInputLayout)findViewById(R.id.input_layout_title);
        input_layout_msg = (TextInputLayout)findViewById(R.id.input_layout_msg);

        header = getLayoutInflater().inflate(R.layout.pboard_list_item_header, null, false);
        adapterBoard = new AdapterBoard(boards,BoardActivity.this);
        lv_board.addHeaderView(header);
        lv_board.setAdapter(adapterBoard);

        setData();
        navSetting();


    }//onCreate
    View nav_header_view =null;
    private void navSetting(){
        if (nav_header_view == null) nav_header_view = navMenu.inflateHeaderView(R.layout.nav_header);
        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        ImageView profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);
        if(state.equals(CUSTOMER))nav_header_id_text.setText(CUSTOMER);
        else nav_header_id_text.setText(name);

        if(img != null && !(img.equals(""))) {
            Uri uRi = Uri.parse(img);

            Picasso.get().load(uRi)
                    .resize(400,400).into(profile_image, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError(Exception e) {
                    Log.d("picPath ", "pIntor img: load failed "+ img);
                }
            });
        }
        else{Glide.with(this).load(R.drawable.ic_launcher_background).into(profile_image);}


        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //Todo: 아이탬 연결
                    case R.id.item1 :goSetting(0);break;
                    case R.id.item2 :goSetting(1);break;
                    case R.id.item3 :goSetting(2);break;
                    case R.id.item4 :goSetting(3);break;

                }//switch
                drawerLayout.closeDrawer(navMenu,true);
                return false;
            }//onNavigationItemSelected
        });//listener
        //자바로 액션메뉴에 햄버거 버튼 만들기
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.draw_open, R.string.draw_close);
        //토글버튼 아이콘이 보이도록 붙이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //삼선 아이콘 모양으로 동기마추기
        drawerToggle.syncState();
        //삼선 아이콘과 화살표아이콘이 자동 변환되도록
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle("게시판");

    }//navSetting
    private void setData(){

        isupload= false; //new board를 위한 is upload
        tv_total_count = header.findViewById(R.id.tv_total_count);
        iv_title_img = header.findViewById(R.id.iv_title_img);
        iv_edit_img = header.findViewById(R.id.iv_edit);

        tv_board_title = header.findViewById(R.id.tv_board_title);
        iv_edit_title = header.findViewById(R.id.iv_edit_title);
        //기본 해더 관련
        if(level==4){
            iv_edit_img.setVisibility(View.VISIBLE);
            iv_edit_title.setVisibility(View.VISIBLE);
        }
        tv_board_title.setText(applicationClass.getBoard_title());
        tv_total_count.setText(boards.size()+"");

        if(applicationClass.getBoard_imgpath()!=null&&(applicationClass.getBoard_imgpath().length()>img_length)){ //게시판 기본 이미지 설정
            isupload = false; setPic(applicationClass.getBoard_imgpath());
        }
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.iv_title_img:
                        if(level==4); isupload =false; break;
                    case R.id.iv_edit_title:
                        if(level==4); break;
                    case R.id.btn_upload_img: iv_img.setVisibility(View.VISIBLE); isupload =true; takePic();  break; //게시판 사진
                    case R.id.btn_upload_cancel: if(isupload) { isupload =false; iv_img.setVisibility(View.GONE);  btn_upload_cancel.setVisibility(View.GONE); } break;
                    case R.id.btn_save:  editSave();  break;
                    case R.id.btn_return:  returnList();  break;
                    case R.id.btn_write:
                        if(level!=0) writeBoard();
                        else {
                            //TODO::dial 회원만 가능
                            String msg =getString(R.string.cont_use_custom);
                            AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                            //builder.setTitle("AlertDialog Title");
                            builder.setMessage(msg);
                            builder.setPositiveButton("예",

                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(BoardActivity.this,LoginActivity.class);
                                            intent.putExtra("want",3);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            builder.setNegativeButton("아니오", null);
                            builder.show();
                        }
                }//switch
            }
        };

        if(level==4) iv_title_img.setOnClickListener(clickListener);
        if(level==4) iv_edit_title.setOnClickListener(clickListener);

        btn_write.setOnClickListener(clickListener);
        btn_upload_img.setOnClickListener(clickListener);
        btn_upload_cancel.setOnClickListener(clickListener);
        btn_save.setOnClickListener(clickListener);
        btn_return.setOnClickListener(clickListener);


        selectListitem();




    }//setData

    private int cPosition;
    private void selectListitem(){
        lv_board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    cPosition =position - lv_board.getHeaderViewsCount();
//                    Log.i("cposition",lv_board.getHeaderViewsCount()+"/"+applicationClass.getBoards().size()+"/"+position+"/"+cPosition);
//                    Log.i("sizei",applicationClass.getBoards().size()+"");
                    Intent intent = new Intent(BoardActivity.this,BoarderDetailsActivity.class);
                    intent.putExtra("position",cPosition);
//                    Log.i("itemcont",adapterBoard.getCount()+"");
                    startActivityForResult(intent,DATACHANGE);
                }//if 헤더 제외
            }
        });
    }//selectListitem
    //글쓰기 다이알로그인척하는 인크루드
    private void returnList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("AlertDialog Title");
        String msg = "게시판으로 돌아가시겠습니까?";
        builder.setMessage(msg);
        builder.setPositiveButton("예",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      resetWrite();
                    }
                });
        builder.setNegativeButton("아니오", null);
        builder.show();
    }

//new 보드 작성 : 새글 쓰기 기본 세팅
    private void writeBoard(){

        edit.setVisibility(View.VISIBLE);
        input_layout_title.setCounterEnabled(true);
        input_layout_msg.setCounterEnabled(true);
        input_layout_title.setCounterMaxLength(30);
        input_layout_msg.setCounterMaxLength(500);

//스피너에 값넣기 , Launch에서 받는 DB에서 넘어온 스피너 값 뿌리기
        for (String mapkey : spinnerHash.keySet()){
            SpinnerInfo s = spinnerHash.get(mapkey);
            spinnerItems.add(s.getSpinner_title()); //스피너 헤시에서 타이틀만 담음
        }
      //  spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerItems);
        spinnerAdapter = new ArrayAdapter(this, R.layout.spinner_layout, spinnerItems);

        spinner.setAdapter(spinnerAdapter);
        //event listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_instructor.setText(spinner.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_instructor.setText(spinner.getItemAtPosition(0).toString());
            }
        });

//이름 아이디 넣기
        String strId = userInfo.getEmail().substring(0,3)+"***";
        tv_user_name.setText(userInfo.getName()+"("+strId+")"+"님 안녕하세요 !");

    }//writeBoard
    private String changetitle; //보드 타이틀 저장을 위한
    private String changemsg; // 보드 내용을 저장하기 위한
//new 보드 저장
    private  void editSave(){
        boolean cancel = false;
        View focusView = null;
        String password = repassword.getText().toString(); //번호
        changetitle= edit_title.getText().toString(); // 타이틀
        changemsg= edit_msg.getText().toString(); //메세지
        edit_title.setError(null);
        edit_msg.setError(null);
        repassword.setError(null);
        //업로드 체크
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
        }else if (!isPasswordValid(password)) {
            repassword.setError(getString(R.string.error_invalid_password));
            focusView = repassword;
            cancel = true;
        }

        //내용 확인후 저장
        if (cancel) {
            focusView.requestFocus();
        } else {
            dataChange();  //성공하면 저장을 위한 작업 method로 이동
        }

    }

//정보저장
    private void dataChange(){
        Board board = new Board();
        board.setBoard_title(changetitle);
        board.setBoard_msg(changemsg);
        board.setBoard_id(userInfo.getEmail());
        board.setBoard_writer(userInfo.getName());
        //spinner hash에서 밸류로 정보 찾아 보드에 저장
        String spinnerValue =tv_instructor.getText().toString();
        for (String mapkey : spinnerHash.keySet()){
            SpinnerInfo s = spinnerHash.get(mapkey);
            if(s.getSpinner_title().equals(spinnerValue)){
                board.setBoard_instructor(s); break;
            }
        }
        board.setBoard_pwd(repassword.getText().toString());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String time = df.format(new Date());
        board.setBoard_date(time);

        //이미지
        if(isupload){ board.setImg(true); board.setBoard_imgpath(realPicPath); }
        else {board.setBoard_imgpath(""); board.setImg(false);}
       //서버에 저장
       DBsaveBoardData(board);

    }

//디비에 넣는 작업 new 보드 업로드
    private void DBsaveBoardData(final Board board){
        if(board == null) return;
        new Thread(){
            @Override
            public void run() {
                //php url
                String serverURL = "http://jian86.dothome.co.kr/HahaClass/board_insert_data.php";
                SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new AlertDialog.Builder(BoardActivity.this).setMessage("성공하였습니다").show();
                        //확인용 창 띄우기 성공하면 성공맨트 돌려받음
                        board.setBoard_imgpath(baseImgePath+response);
                        applicationClass.getBoards().add(board);
                        //DBgetBoardInfo();
                        resetWrite();// 리셋
                        reLoaddata();// 리로드
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                simpleMultiPartRequest.addStringParam("board_user_email",userInfo.getEmail());
                simpleMultiPartRequest.addStringParam("class_code",board.getBoard_instructor().getClass_code());
                simpleMultiPartRequest.addStringParam("l_num",board.getBoard_instructor().getL_num());
                simpleMultiPartRequest.addStringParam("board_title",board.getBoard_title());
                simpleMultiPartRequest.addStringParam("board_msg",board.getBoard_msg());
                simpleMultiPartRequest.addStringParam("board_pwd",board.getBoard_pwd());
                simpleMultiPartRequest.addStringParam("date",board.getBoard_date());
                simpleMultiPartRequest.addFile("board_image_path",board.getBoard_imgpath());
//                Log.i("picpathtt",realPicPath);
                //요청객체를 실제 서버쪽으로 보내기 위해 우체통같은 객체
                RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
                //요청 객체를 우체통에 넣기
                requestQueue.add(simpleMultiPartRequest);
            }//run
        }.start();
    }//DBsaveBoardData
//닫기
    private void resetWrite(){
        isupload=false;
        picPath="";
        btmapPicPath=null;
        edit_msg.setText("");
        edit_title.setText("");
        repassword.setText("");
        spinnerItems.clear();
        iv_img.setImageBitmap(null);
        iv_edit_img.setVisibility(View.GONE);
        btn_upload_cancel.setVisibility(View.GONE);
        spinner.setSelection(0);
        edit.setVisibility(View.GONE);
    }
    private void reLoaddata(){
        setData();
        getData();
        hidekeyboard();
    }

    private void getData(){

        boards = applicationClass.getBoards();
        spinnerHash = applicationClass.getBoard_instructor();
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
            level = 0;
            userInfo = null;
        }
    }//getData

    //서브밋전 확인
    private boolean isPasswordValid(String password) {
        return (password.length() > 4 && password.length() < 21);
    }
    private boolean isTitleValid(String title, int len ) {
        return (title.length() <= len);
    }
//키보드 내리기
    private void hidekeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
//사진관련
    private Boolean isupload; //사진을 업로드 했으면 true

    //디비에서 오는 이미지인지 핸드폰에서 오는 이미지인지 구별
    public void setPic(String pic) {
        picPath = pic;
        if (picPath != null && !(picPath.equals(""))) {
            Uri uRi = Uri.parse(picPath);
            ImageView iv;
            if(isupload){iv = iv_img; btn_upload_cancel.setVisibility(View.VISIBLE);} else{ iv =iv_title_img; }
            Picasso.get().load(uRi)
                    .resize(400, 400).into(iv, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Exception e) {
                    Log.d("picPath ", "pIntor img: load failed " + picPath);
                }
            });
        } else {

            ImageView iv;
            if(isupload){ iv = iv_img; btn_upload_cancel.setVisibility(View.VISIBLE); } else { iv =iv_title_img; }
            Glide.with(this).load(R.drawable.ic_launcher_background).into(iv);
        }


    }
    public void setPic(Bitmap pic){
        btmapPicPath = pic;
        ImageView iv;
        if(isupload){ iv = btn_upload_img;}else{ iv =iv_title_img; }
        Glide.with(this).load(pic).into(iv);
       // picPath=null;
    }
//네비
    private void goSetting(int item){
        if(state.equals(USER)){
            Intent intent= new Intent(BoardActivity.this,SettingActivity.class);
            intent.putExtra("Item",item);
            intent.putExtra("stage",2);
            startActivity(intent);
        }else {
            //TODO::dial 회원만 가능
            String msg =getString(R.string.cont_use_custom);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("AlertDialog Title");
            builder.setMessage(msg);
            builder.setPositiveButton("예",

                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(BoardActivity.this,LoginActivity.class);
                            intent.putExtra("want",3);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton("아니오", null);
            builder.show();
        }
    }
//사진
    private String realPicPath;
    private void takePic(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PIC);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PIC:

                //갤러리 화면에서 이미지를 선택하고 돌아왔는지 체크
                //두 번째 파라미터 : resultCode
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    picPath = uri.toString();
                    realPicPath = getRealPathFromUri(uri);
                    if (uri != null) {
                            ImageView iv;
                            if(isupload){iv = iv_img; btn_upload_cancel.setVisibility(View.VISIBLE);} else{ iv =iv_title_img; }

                            Picasso.get().load(uri)
                                    .resize(400, 400).into(iv, new Callback() {
                                @Override
                                public void onSuccess() {
                                }
                                @Override
                                public void onError(Exception e) {
                                    Log.d("picPath ", "pIntor img: load failed " + picPath);
                                }
                            });

                    } else {
                        ImageView iv;
                        if(isupload){iv = iv_img; btn_upload_cancel.setVisibility(View.VISIBLE);  } else{ iv =iv_title_img; }
                        //아니면 Intent 에 Extra 데이터로 Bitmap 이 전달되어 옴
                        Bundle bundle = data.getExtras();
                        Bitmap bm = (Bitmap) bundle.get("data"); // key 값 "data" 는 정해진거야
                        //iv.setImageBitmap(bm);
                         Glide.with(this).load(bm).into(iv);
                        setPic(bm);
                    }

                }//if

                break;
            case DATACHANGE:

                //갤러리 화면에서 이미지를 선택하고 돌아왔는지 체크
                //두 번째 파라미터 : resultCode
                if (resultCode == RESULT_OK) {
                    adapterBoard.notifyDataSetChanged();
                }

                break;


        }
    }

    //이미지 절대경로로 바꾸기
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        android.support.v4.content.CursorLoader loader= new android.support.v4.content.CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }


//
//    //보드 정보 얻어오기
//    void DBgetBoardInfo(){
//        String serverURL = "http://jian86.dothome.co.kr/HahaClass/get_board_info_list.php";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if(boards.size()>0) boards.clear();
//                JSONArray jsonArray = null;
//                try {
//                    jsonArray = new JSONArray(response);
//                    String b_num;
//                    String board_user_email;
//                    String name;
//                    String class_code;
//                    String l_num;
//                    String board_title;
//                    String board_msg;
//                    String board_image_path="";
//                    String board_pwd;
//                    String board_cnt_reply;
//                    String date;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        b_num = jsonObject.getString("b_num"); //보드 확인용
//                        board_user_email = jsonObject.getString("board_user_email");
//                        class_code = jsonObject.getString("class_code");
//                        l_num = jsonObject.getString("l_num");
//                        board_title = jsonObject.getString("board_title");
//                        board_msg = jsonObject.getString("board_msg");
//                        board_pwd = jsonObject.getString("board_pwd");
//                        board_cnt_reply = jsonObject.getString("board_cnt_reply");
//                        date = jsonObject.getString("date");
//                        name = jsonObject.getString("name");
//                        board_image_path = jsonObject.getString("board_image_path");
//                        boolean is_img;
//
//                        if(board_image_path!=null && board_image_path.length()>"uploads/20190310044626".length()) is_img = true;
//                        else is_img= false;
//                        board_image_path = baseImgePath + board_image_path;
//                        String spinner_title="";
//                        String spinner_l_name="";
//                        String spinner_l_project_title="";
//                        if(jsonObject.has("class_title")) { spinner_title =  jsonObject.getString("class_title"); }
//                        if(jsonObject.has("l_name")) {spinner_l_name =  jsonObject.getString("l_name"); }
//                        if(jsonObject.has("l_project_title")) {spinner_l_project_title =  jsonObject.getString("l_project_title"); }
//                        String spinnerTitle="";
//                        if(spinner_title == null || spinner_title.length() <= 0) spinnerTitle = "테스트용";
//                        else spinnerTitle = spinner_l_name + " / "+spinner_l_project_title + " / "+ spinner_title;
//                        SpinnerInfo spinnerInfo = new SpinnerInfo(l_num,class_code,spinnerTitle);
//
//                        Board board = new Board(b_num,board_title,date,"( "+board_cnt_reply+" ) ",is_img,board_image_path,board_title,name,spinnerInfo,board_msg,board_pwd,board_user_email);
//                        boards.add(board);
//
//                    }//for
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
//        requestQueue.add(stringRequest);
//        applicationClass.setBoards(boards);
//
//    }//DBgetBoardInfo


    @Override
    protected void onResume() {
        super.onResume();
        getData();
        navSetting();
    }
}//onCreate

