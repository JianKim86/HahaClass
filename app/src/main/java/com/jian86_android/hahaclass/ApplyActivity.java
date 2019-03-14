package com.jian86_android.hahaclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

public class ApplyActivity extends AppCompatActivity {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final int PIC = 1000;
    private Toolbar toolbar;private NavigationView navMenu;
    private ArrayList<ItemInstructor> itemInstructors = new ArrayList<>();
    private ItemInstructor instructor;
    private UserInfo userInfo;
    private Intent getintent;
    String state,name,email,phone,pass,img,instructorTitle;
    int level;

    private Schedule schedule;
    private int position;
    private ArrayList<DatasItem>datasItems= new ArrayList<>();

    private View header;
    private ListView lv_apply;
    private LinearLayout layout_not_user;
    private Button sign_up_btn;
    private AdapterDetailedSchedule mMyAdapter;
    private TextInputEditText et_name,et_phone,et_email,et_pwd;
    private Button btn_apply;
    private ScrollView submit_form;
    private int isFocus;
    private static final int PHONE = 0;
    private static final int PASSWORD = 1;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        setContentView(R.layout.activity_apply);
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
        getintent = getIntent();
        applicationClass =(ApplicationClass)getApplicationContext();


        getData();

        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        setData();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getData(){
        position = getintent.getIntExtra("position",0);
        //받은 포지션으로 스케쥴리스트의 몇번째인지 확인

//강사정보 가져오기
        instructor= applicationClass.getItemInstructor();
        schedule = null;
        datasItems.clear();
        schedule = applicationClass.getItemInstructor().getSchedules().get(position);
        datasItems = schedule.getDatas(); //null일수 있음 유의
       // Log.i("scr_schedule",applicationClass.getItemInstructor().getSchedules().get(1).getDatas().size()+"");
        l_num= schedule.getL_num();
        class_code = schedule.getClass_code();

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
          //  Log.i("naemw",userInfo.getName());
        }else{
            level = 0;
            userInfo = null;
        }

    }//getIntentData

    public void setData(){
        getSupportActionBar().setTitle("강의 신청하기");
        //리스트뷰
        header = getLayoutInflater().inflate(R.layout.pchalendar_apply_list_header, null, false);
        mMyAdapter = new AdapterDetailedSchedule(datasItems, this);
        lv_apply = findViewById(R.id.lv_apply);

        /* 리스트뷰에 어댑터 등록 */
        lv_apply.setAdapter(mMyAdapter);
            submit_form = (ScrollView) header.findViewById(R.id.submit_form);
            et_name = (TextInputEditText) header.findViewById(R.id.name);
            et_email = (TextInputEditText) header.findViewById(R.id.email);
            et_phone = (TextInputEditText) header.findViewById(R.id.phone);
            et_pwd = (TextInputEditText) header.findViewById(R.id.password);
            btn_apply = header.findViewById(R.id.btn_apply);

            layout_not_user= header.findViewById(R.id.layout_not_user);
            sign_up_btn= header.findViewById(R.id.sign_up_btn);

            if(level==0){layout_not_user.setVisibility(View.VISIBLE); submit_form.setVisibility(View.GONE);}
            else{submit_form.setVisibility(View.VISIBLE);layout_not_user.setVisibility(View.GONE);}
            lv_apply.addHeaderView(header);


        if(userInfo != null) {
            et_name.setText(userInfo.getName());
            et_email.setText(userInfo.getEmail());
            et_phone.setText(userInfo.getPhone());

        }

        lv_apply.setItemsCanFocus(true);
        lv_apply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==lv_apply.getHeaderViewsCount()){

                    switch (isFocus){
//                        case PHONE: submit_form.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS); et_phone.requestFocus(); break;
//                        case PASSWORD: submit_form.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS); et_pwd.requestFocus(); break;
                    }

                }
            }
        });
        et_phone.setFilters(new InputFilter[]{filterAlphaNum});
        btn_apply.setOnClickListener(onClickListener);
//        et_phone.setOnClickListener(onClickListener);
//        et_pwd.setOnClickListener(onClickListener);
        sign_up_btn.setOnClickListener(onClickListener);



    }//setData



    //신청한 강의면 false
    private boolean checkLap(){
        HashSet<ApplyClassInfo> set = applicationClass.getApplySchedule();
        Iterator<ApplyClassInfo> it = set.iterator();
     //   Log.i("its_p",position+"");
        Object[] myArr = set.toArray();
        if(set.size()>0) {
            for (int i = 0; i < myArr.length; i++) {
                ApplyClassInfo z = (ApplyClassInfo) myArr[i];
                if (z.getL_num().equals(l_num)&&z.getClass_code().equals(class_code)) {
                    return false;
                }
            }
        }
        return true;
//
//        if(set.size()>0) {
//            while (it.hasNext()) {
//                it.next();
//                if (it.equals(position)) return false;
//                Log.i("its",it+"");
//
//            }
//        } return true;


    }
    //데이터 업데이트
    public void submitForm(){ //다이알로그

        if(checkLap()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ApplyActivity.this);

            TextView title = new TextView(this);
            title.setText(schedule.getDate() + "\n" + schedule.getProjectTitle());
            title.setBackgroundColor(getResources().getColor(R.color.colorDialTitle));
            title.setPadding(10, 30, 10, 30);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTextSize(20);

            builder.setCustomTitle(title)
                    .setMessage("\n강의를 신청하시겠습니까?")        // 메세지 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("신청", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                           //디비저장
                            DBinsultApply();
                            //Toast.makeText(applicationClass, "성공", Toast.LENGTH_SHORT).show();
                            ApplyActivity.this.finish();
                            dialog.dismiss();
                        }

                    })

                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
//            new AlertDialog.Builder(ApplyActivity.this).setMessage("이미 신청한 강의 입니다.").create().show();
            AlertDialog.Builder builder = new AlertDialog.Builder(ApplyActivity.this);
            builder
                    .setMessage("이미 신청한 강의 입니다.")       // 메세지 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            ApplyActivity.this.finish();
                            dialog.dismiss();
                        }

                    })

                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }
     String l_num;
     String class_code;
    //디비저장
    private void DBinsultApply(){

                //강사 번호와 신청 코드 저장 (lunch에서 함께 읽어옴)
                //schedule = applicationClass.getItemInstructor().getSchedules().get(position);
                //스케쥴에 정보가 있음

                //php에 보낼 parms
                final String apply_name = userInfo.getName();
                final String apply_phone = userInfo.getPhone();
                final String email = userInfo.getEmail();
                //php: info_list DB에서  email로 user_num 조회 -> apply_user_num 에 삽입
                //php: DB class_apply_list에 보내는 parms와 user_num 저장
                String serverURL = "http://jian86.dothome.co.kr/HahaClass/apply_class_list_insert.php";
                //1. userinfo 에 있는 정보 유저 테이블에 넣기
                SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버로부터 응답을 받을때 자동 실행
                        //매개변수로 받은 Stringdl echo된 결과값
                        new AlertDialog.Builder(ApplyActivity.this).setMessage("신청이 접수되었습니다.").show();
                        //강의 코드 글로벌에 담음
                        //로그인시 내 이메일로 검색해서 강의 코드가 있는지 확인 있으면 담음
                        //글로벌에 담긴 강의 코드와 비교해서 이미 수강신청한 코드인지 확인
                        applicationClass.setApplySchedule(new ApplyClassInfo(l_num,class_code));
                        //강사 번호와 신청코드 저장
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //서버 요청중 에거라 발생하면 자동 실행
                        Toast.makeText(ApplyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish(); // 두번째 화면 종료
                    }
                });//세번째 파라미터가 응답을 받아옴


                //포스트 방식으로 보낼 데이터들 요청 객체에 추가하기
                multiPartRequest.addStringParam("l_num", l_num);
                multiPartRequest.addStringParam("class_code", class_code);
                multiPartRequest.addStringParam("apply_name", apply_name);
                multiPartRequest.addStringParam("apply_phone", apply_phone);
                multiPartRequest.addStringParam("email", email);

                //요청객체를 실제 서버쪽으로 보내기 위해 우체통같은 객체
                RequestQueue requestQueue = Volley.newRequestQueue(ApplyActivity.this);

                //요청 객체를 우체통에 넣기
                requestQueue.add(multiPartRequest);

    }// DBinsultApply

    //입력정보 확인
    private void confirmUserInfo(){
        boolean cancel = false;
        View focusView = null;
        String phone = et_phone.getText().toString();
        String password =et_pwd.getText().toString();

        //Check for a valid phone
        if (!TextUtils.isEmpty(phone) && !isPoneValid(phone)) {
            et_phone.setError(getString(R.string.error_invalid_phone));
            focusView = et_phone;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            et_pwd.setError(getString(R.string.error_field_required));
            focusView = et_pwd;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            et_pwd.setError(getString(R.string.error_invalid_password));
            focusView = et_pwd;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            submitForm();//폼보내기
        }



    }//confirmUserInfo

    //회원 가입여부 묻기
    private void goMainActivity(){


        Intent intent = new Intent(ApplyActivity.this,LoginActivity.class);
        intent.putExtra("want",3);
        startActivity(intent);
        finish();


    }//goMainActivity

    private boolean isPoneValid(String phone) {
        return phone.length() == 11 || phone.length() == 10;
    }
    private boolean isPasswordValid(String pwd) {
        return userInfo.getPassword().equals(pwd);
    }

    public InputFilter filterAlphaNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //  숫자만 입력 되도록 : "^[0-9]*$"
            Pattern ps = Pattern.compile("^[0-9]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };






    //클릭리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.phone: et_phone.requestFocus(); isFocus=PHONE; break;
                case R.id.password: et_pwd.requestFocus(); isFocus=PASSWORD; break; //
                case R.id.btn_apply: confirmUserInfo(); break;// 비밀번호 검사로
                case R.id.sign_up_btn: goMainActivity(); break;// 회원가입
            }
        }
    };

}//ApplyActivity
