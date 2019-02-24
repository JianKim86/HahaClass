package com.jian86_android.hahaclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

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
    private LinearLayout submit_form;
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
        getintent = getIntent();
        applicationClass =(ApplicationClass)getApplicationContext();
        getData();
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("강의 신청하기");
        setData();

    }

    public void getData(){
        position = getintent.getIntExtra("position",0);
        //받은 포지션으로 스케쥴리스트의 몇번째인지 확인

//강사정보 가져오기
        instructor= applicationClass.getItemInstructor();
        schedule = applicationClass.getItemInstructor().getSchedules().get(position);
        datasItems = schedule.getDatas(); //null일수 있음 유의


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
        //리스트뷰
        header = getLayoutInflater().inflate(R.layout.pchalendar_apply_list_header, null, false);
        mMyAdapter = new AdapterDetailedSchedule(datasItems, this);
        lv_apply = findViewById(R.id.lv_apply);

        /* 리스트뷰에 어댑터 등록 */
        lv_apply.setAdapter(mMyAdapter);
            submit_form = (LinearLayout)header.findViewById(R.id.submit_form);
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
        et_phone.setOnClickListener(onClickListener);
        et_pwd.setOnClickListener(onClickListener);
        sign_up_btn.setOnClickListener(onClickListener);
    }//setData



    private boolean checkLap(int position){
        HashSet<Integer> set = applicationClass.getApplySchedule();
        Iterator<Integer> it = set.iterator();
        Log.i("its_p",position+"");
        Object[] myArr = set.toArray();
        if(set.size()>0) {
            for (int i = 0; i < myArr.length; i++) {
                int z = (Integer) myArr[i];
                if (z == position) {
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

        if(checkLap(position)) {
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

                            //강사 번호와 포지션 저장
                            //schedule = applicationClass.getItemInstructor().getSchedules().get(position);
                            applicationClass.setApplySchedule(position);
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
