package com.jian86_android.hahaclass;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity  {
    private static final String baseImgePath = "http://jian86.dothome.co.kr/HahaClass/";
    private static final int img_length ="uploads/20190310031854".length();

    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final int PIC = 1000;
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private ArrayList<ItemInstructor> itemInstructors = new ArrayList<>();
    private ItemInstructor instructor;
    private UserInfo userInfo;
    private Intent getintent;
    String state,name,email,phone,pass,img;
    int level;
    private ActionBarDrawerToggle drawerToggle;

    private Animation fab_open, fab_close, fab_action_close, fab_action_open;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2,fab3 ;

    private ViewPager pagerLayout;
    private AdapterFragment adapter;

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
        setContentView(R.layout.activity_main);
        getintent = getIntent();
        applicationClass =(ApplicationClass)getApplicationContext();
        getIntentData();
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu =findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_action_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_action_close);
        fab_action_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_action_open);
        fab =findViewById(R.id.fab_btn);
        fab1 =findViewById(R.id.fab1_btn);
        fab2 =findViewById(R.id.fab2_btn);
        fab3 =findViewById(R.id.fab3_btn);
       // fab4 =findViewById(R.id.fab4_btn);
        pagerLayout =findViewById(R.id.pager_layout);
        adapter = new AdapterFragment(getSupportFragmentManager());
        pagerLayout.setAdapter(adapter);



        fab.setOnClickListener(onClickListener);
        fab1.setOnClickListener(onClickListener);
        fab2.setOnClickListener(onClickListener);
        fab3.setOnClickListener(onClickListener);
      //  fab4.setOnClickListener(onClickListener);

        navSetting();


    }//onCreate
    View nav_header_view =null;
    private void navSetting(){
        if (nav_header_view == null) nav_header_view = navMenu.inflateHeaderView(R.layout.nav_header);
        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        ImageView profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);
        if(state.equals(CUSTOMER))nav_header_id_text.setText(CUSTOMER);
        else nav_header_id_text.setText(name);

        if(img != null && img.length() != img_length) {
            Uri uRi = Uri.parse(img);
            Picasso.get().load(uRi)
                    .resize(400,400).centerCrop().into(profile_image, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError(Exception e) {

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
        //타이틀 변경
        String instructorTitle = instructor.getTitle()+"의 "+instructor.getSubTitle();
        getSupportActionBar().setTitle(instructorTitle);

    }//navSetting

//fab버튼 클릭시 화면전환 이벤트
 View.OnClickListener onClickListener = new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         int id = v.getId();
         switch (id) {
             case R.id.fab_btn:
                 anim();

                 break;
             case R.id.fab1_btn:
                 anim();
                 pagerLayout.setCurrentItem(0);
                 Toast.makeText(MainActivity.this, "Button1", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab2_btn:
                 anim();
                 pagerLayout.setCurrentItem(1);
                 Toast.makeText(MainActivity.this, "Button2", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.fab3_btn:
                 anim();
                 pagerLayout.setCurrentItem(2);
                 Toast.makeText(MainActivity.this, "Button3", Toast.LENGTH_SHORT).show();
                 break;
//             case R.id.fab4_btn:
//                 anim();
//                 pagerLayout.setCurrentItem(3);
//                 Toast.makeText(MainActivity.this, "Button4", Toast.LENGTH_SHORT).show();
//                 break;
         }

     }
 };
    public void anim() {

        if (isFabOpen) {
            fab.startAnimation(fab_action_open);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
     //       fab4.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
      //      fab4.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(fab_action_close);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
   //         fab4.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
  //          fab4.setClickable(true);
            isFabOpen = true;
        }
    }//anim

    private void  getIntentData(){
      //  Bundle userBundle = getintent.getBundleExtra("userBundle");
       // Bundle selectTeacher =  getintent.getBundleExtra("selectTeacher");
        instructor = applicationClass.getItemInstructor();

        //TODO:DB작업: instructor에 스케쥴 담기
        DBgetClassDetailList();

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
            userInfo = null;
        }

    }//getIntentData;

    private void DBgetClassDetailList() {
        instructor.setSchedules(new ArrayList<Schedule>());
        //classlist 에서 강사별 클레스 검색 ->list
        //classdetaillist에서 세부 내용 검색 -> 세부
        //instructor 에 스케쥴 담기

        //강사 번호로 강의 검색 리스트에서 검색
        final String l_num = instructor.getL_num();

        String serverURL = "http://jian86.dothome.co.kr/HahaClass/get_instructor_detail_info.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Log.i("responsei",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String class_code = jsonObject.getString("class_code");
                        String class_host = jsonObject.getString("class_host");
                        String class_title = jsonObject.getString("class_title");
                        String start_day = jsonObject.getString("start_day");
                        String finish_day = jsonObject.getString("finish_day");
                        String class_image_path = jsonObject.getString("class_image_path");
                        String class_support = jsonObject.getString("class_support");

                        //String date = jsonObject.getString("date");
                        //파일경로가 서버 IP가 제외된 주소임
                        class_image_path = baseImgePath + class_image_path;

                        //스케쥴에 넣기
                        Schedule schedule = new Schedule(l_num, class_title,class_image_path,class_host,class_support,start_day,finish_day,class_code);
                        String l_number="";
                        String class_code_recheck="";
                        String week="";
                        String c_day="";
                        String title="";
                        String configuration="";
                        String date="";

                        //세부 디테일 받기
                        JSONObject jsonkeyArray = jsonObject.getJSONObject("key");

                     //   JSONArray jsonkeyArray = jsonArray.getJSONArray(i);
                      // Log.i("jsonkeyArray",response);
                     //   Log.i("responseii_",jsonkeyArray.length()+"");

                        if(jsonkeyArray!=null) {
                            schedule.setDatas(new ArrayList<DatasItem>());
                            for (int y = 0; y < jsonkeyArray.length(); y++) {
                                JSONObject jsonObject1 = jsonkeyArray.getJSONObject(y + "");
                                l_number = jsonObject1.getString("l_num");
                                class_code_recheck = jsonObject1.getString("class_code");
                                week = jsonObject1.getString("week");
                                c_day = jsonObject1.getString("c_day");
                                title = jsonObject1.getString("title");
                                configuration = jsonObject1.getString("configuration");
                                date = jsonObject1.getString("date");
                                if(class_code_recheck.equals(class_code)){
                                    DatasItem datasItem = new DatasItem(l_number, class_code_recheck, week, c_day, title, configuration);
                                    schedule.getDatas().add(datasItem);
                                }
                            }
                        }
                        instructor.getSchedules().add(schedule);

                    }//for

                    //  applicationClass.setUserInfo(userinfo);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                return;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("l_num", l_num );
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);


    }//getClassDetailList

    private void goSetting(int item){
        if(state.equals(USER)){
            Intent intent= new Intent(MainActivity.this,SettingActivity.class);
            intent.putExtra("Item",item);
            intent.putExtra("stage",0);
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
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            intent.putExtra("want",3);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton("아니오", null);
            builder.show();
        }
    }
    void takePic(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PIC);
    }
    private String picPath="";
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
                    Fragment fragment = (Fragment) adapter.instantiateItem(pagerLayout,0);
                    if(uri != null){
                        if(pagerLayout.getCurrentItem()==0) {
                            ((FragInfo)fragment).setPic(picPath);
                        }
                    }else{
                        //아니면 Intent 에 Extra 데이터로 Bitmap 이 전달되어 옴
                        Bundle bundle=data.getExtras();
                        Bitmap bm= (Bitmap) bundle.get("data"); // key 값 "data" 는 정해진거야
                        //iv.setImageBitmap(bm);
                        if(pagerLayout.getCurrentItem()==0) {
                            ((FragInfo)fragment).setPic(bm);
                        }
                    }
                }//if

                break;
        }

    }//onActivityResult


    int APPLYITEM;
    public void turnActivity(int i){
        Intent intent = new Intent(MainActivity.this, ApplyActivity.class);
        intent.putExtra("position", i);
        APPLYITEM = i;
        startActivityForResult(intent, APPLYITEM);

    }



//    public Bundle sendData(){
//        //디비에서 읽어올 회원정보 이름과이메일만 보냄 하지만 지금은 전체 다 보내기
//        Bundle bundleData = new Bundle();
//        if(userInfo != null) {
//            bundleData = new Bundle();
//            int userLevel = userInfo.getLevel();
//            String userPhone = userInfo.getPhone();
//            String userName = userInfo.getName();
//            String userImgPath = userInfo.getImagePath();
//            String userEmail =userInfo.getEmail();
//            String userPassword =userInfo.getPassword();
//            bundleData.putInt("Level",userLevel);
//            bundleData.putString("Name",userName);
//            bundleData.putString("Image",userImgPath);
//            bundleData.putString("Phone",userPhone);
//            bundleData.putString("Email",userEmail);
//            bundleData.putString("Pass",userPassword);
//            bundleData.putString("state",state);
//        }else{
//            bundleData = new Bundle();
//            bundleData.putString("state",state);
//        }
//        return bundleData;
//    }


    @Override
    protected void onResume() {
        super.onResume();
        getIntentData();
     navSetting();
    }

    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }
}//MainActivity
