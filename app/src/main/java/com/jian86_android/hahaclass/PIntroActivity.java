package com.jian86_android.hahaclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PIntroActivity extends AppCompatActivity {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ArrayList<ItemInstructor> itemInstructors = new ArrayList<>();
    private UserInfo userInfo;
    private Intent getintent;
    String state,name,email,phone,pass,img;
    private ArrayList<Schedule>schedules = new ArrayList<>();
    int level;
    private ActionBarDrawerToggle drawerToggle;
    private Button tv_goto_instructor,tv_goto_board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pintro);
        applicationClass =(ApplicationClass)getApplicationContext();
        getintent = getIntent();
        getIntentData();
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu = findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);
        listView = findViewById(R.id.lv_instructor);
        tv_goto_instructor =findViewById(R.id.tv_goto_instructor);
        tv_goto_board =findViewById(R.id.tv_goto_board);
        if(itemInstructors.size()<2) listView.setVisibility(View.VISIBLE);
        tv_goto_instructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView.getVisibility()!= View.GONE){
                    listView.setVisibility(View.GONE);
                }else{listView.setVisibility(View.VISIBLE);}
            }
        });
        tv_goto_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //넘기기
                Intent intent = new Intent(PIntroActivity.this,BoardActivity.class);
                startActivity(intent);
            }
        });



       // navMenu.setNavigationItemSelectedListener(this);

        View nav_header_view = navMenu.inflateHeaderView(R.layout.nav_header);
        //View nav_header_view = navigationView.getHeaderView(0);
       // String name = getintent.getBundleExtra("Bundle").getString("Name");
        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        ImageView profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);
        if(state.equals(CUSTOMER))nav_header_id_text.setText(CUSTOMER);
            else nav_header_id_text.setText(name);

//        if(state.equals(USER)){img = applicationClass.getUserInfo().getImagePath();
//            Toast.makeText(applicationClass, "pIntor img :"+ img, Toast.LENGTH_SHORT).show();
//            Log.d("picPath ", "pIntor img:"+img);
//        }

        if(img != null && !(img.equals(""))) {
            Uri uRi = Uri.parse(img);

//            Picasso picasso = new Picasso.Builder(PIntroActivity.this).listener(new Picasso.Listener(){
//                @Override
//                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                    exception.printStackTrace();
//                    Log.d("picPath ", "pIntor img: load failed "+ img);
//                }
//            }).build();
//            picasso.load(uRi).fit().centerCrop().into(profile_image);
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
        dataSetting();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
//                Bundle userBundle = new Bundle();
//
//
//                userBundle.putString("state",state);
//                if(state.equals(USER)){
//                    userBundle.putString("Name",userInfo.getName());
//                    userBundle.putString("Email",userInfo.getEmail());
//                    userBundle.putString("Phone",userInfo.getPhone());
//                    userBundle.putString("Pass",userInfo.getPassword());
//                    userBundle.putString("Image",userInfo.getImagePath());
//                    userBundle.putInt("Level",userInfo.getLevel());
//                }

                Bundle selectTeacher = new Bundle();
                selectTeacher.putString("Instructor",itemInstructors.get(position).getSubTitle());
                ItemInstructor itemInstructor = itemInstructors.get(position);
                applicationClass.setItemInstructor(itemInstructor);
                applicationClass.setInstructorNo(position);

                intent = new Intent(PIntroActivity.this, MainActivity.class);
               // intent.putExtra("userBundle", userBundle);
                //intent.putExtra("selectTeacher",selectTeacher);
                startActivity(intent);

            }
        });
        //자바로 액션메뉴에 햄버거 버튼 만들기
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.draw_open, R.string.draw_close);
        //토글버튼 아이콘이 보이도록 붙이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //삼선 아이콘 모양으로 동기마추기
        drawerToggle.syncState();
        //삼선 아이콘과 화살표아이콘이 자동 변환되도록
        drawerLayout.addDrawerListener(drawerToggle);
    }//onCreate


    //토글 버튼과 drawer 연결
    //엑티비티 입장에서 토글버튼도 액션바 메뉴 인가 느낌
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //아이템 클릭상황을 토글 버튼에 전달
        drawerToggle.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);


    }



    private void  getIntentData(){
        state = applicationClass.getState();
        if(state.equals(USER)){
        userInfo= applicationClass.getUserInfo();
            name =userInfo.getName();
             email = userInfo.getEmail();
             phone = userInfo.getPhone();
             pass = userInfo.getPassword();
             img = userInfo.getImagePath();
             level = userInfo.getLevel();
           // Toast.makeText(applicationClass, "1"+img, Toast.LENGTH_SHORT).show();
        }else{
            userInfo = null;
        }
//        Bundle bundle = getintent.getBundleExtra("Bundle");
//         state = bundle.getString("state");
//        if(state.equals(USER)) {
//             name = bundle.getString("Name");
//             email = bundle.getString("Email");
//             phone = bundle.getString("Phone");
//             pass = bundle.getString("Pass");
//             img = bundle.getString("Image");
//             level = bundle.getInt("Level", 0);
//            userInfo = new UserInfo(name, email, phone, pass, img, level);
//        }else userInfo = null;


    }//getIntentData;

    private String ptitle;
    private String phost;
    private String pstart;
    private String pend;
    //datasItem
    private String itemWeek;
    private String itemDate;
    private String itemTitle;
    private String itemConfiguration;
    private void dataSetting(){

        String title ="윤나영";
        String subTitle ="하하 웃음 클레스";
        String license = "웃음치료1급자격, 레크레이션1급자격, 실버 웃음 전문지도사자격 ,케어세라피1급자격, 시낭송1급지도사자격 스피치 1급 지도자 자격, 명강사 1급 지도사자격, 펀리더십지도사1급자격, 금연 금주 예방지도사자격, 창의인성교육지도사 자격, 부모코칭지도사자격, 평생교육사 2급 자격,  사회복지사 2급 자격, 심리웃음치료사1급 자격, 건강걷기2급지도사자격, 산업카운셀러1급자격, 심리상담 2급자격(논문: 유머감각이 자아 존중감과 대인관계에 미치는 영향)"
        ;
        String field ="감성 리더십, 펀(웃음) 리더십(자아,관계,미래리더십), 감성 스피치,  소통, 강의 코칭, 자원봉사교육, 스피치코칭 PING과 함께 (청소년, 기업, 학부모) 동기부여, 펀 리더십강사 양성 과정, 생애재설계교육, 인성, 금연,금주 학부모코칭, 시낭송 코칭"
        ;
        String career ="학력:명지대 사회교육대학원 졸업\n"+
        "現) (행복을 만드는)창의융합교육 연구소(대표)\n"+
        "現) 구리아리랑연구보존회 이사 및 사무국장\n"+
        "現) 아리랑컨텐츠협의회 회장\n"+
        "現) (행복한 세상 만들기) 하하하웃음크럽 회장\n"+
        "現) 구리시 평화통일 자문위원회 위원\n"+
        "現) 구리전통시장ict보이는 라디오 DJ\n"+
        "現) 이천여성장애인자립센터\n"+
        "웃음 지도사자격과정 전임강사\n"+
        "前) 고대평교명품명강사과정,스피치과정지도강사\n"+
        "前) 한성대 평생교육원 명강사과정 코칭교수\n"+
        "前) 명지대 사회교육대학원 3대 총동문회장\n"+
        "前) 시니어 파트너즈  리스타트 전문강사\n"+
        "前) 한울촌 노인요양원  요양보호사 힐링전문강사\n"+
        "前) 서울특별시새마을협의회 부설 사회교육원  웃음전문강사\n"+
        "前) 이대최고명강사과정 코칭교수,\n"+
        "      구리경찰서 보안협력위원회 위원\n"+
        "前) 동인초등학교 운영위원장\n"+
        "前) 대한적십자 경기도지사 구리나누미 회장,\n"+
        "구리남양주교육지원청\n"+
        "      (교육공무원(영양교사)인사위원회 위원,친절평가위원)\n"+
        "前) 구리청소년통합지원체계(CYS_NET) 운영협의회 위원\n";
        String imgPath = "";

        ItemInstructor item = new ItemInstructor(title,subTitle,imgPath,license, field, career);


        /**
         * 서버에서 instructors 정보 읽어와서 add시키기 **서버작업시 따로 리스트로 빼야됨 db따로 설계**/


        ptitle ="하박수웃음지도사 12주 프로그램";
        phost ="(행복을 만드는)\n창의융합교육연구소";
        pstart = "2019-03-22";
        pend ="2019-04-11";



        Schedule sitem = new Schedule(ptitle,phost,pstart,pend);
        sitem.setSupport("블라블라");
        sitem.setProjectImgPath("");

        ArrayList<DatasItem>datasItemArrayList = new ArrayList<>();
        DatasItem ditem = new DatasItem();
        itemWeek="1";
        itemDate="2월14일";
        itemTitle="개 강 식";
        itemConfiguration="오랜테이션, 동기부여, 하박수란?";
        ditem.setWeek(itemWeek);
        ditem.setDate(itemDate);
        ditem.setTitle(itemTitle);
        ditem.setConfiguration(itemConfiguration);

        datasItemArrayList.add(ditem);

        ditem = new DatasItem();
        itemWeek="2";
        itemDate="2월21일";
        itemTitle="셀프리더십1 나는 누구인가?";
        itemConfiguration="(DISC)나의 유형?, 스피치훈련 ,셀프웃음운동 ,시낭송(감성훈련과 심신치유)";
        ditem.setWeek(itemWeek);
        ditem.setDate(itemDate);
        ditem.setTitle(itemTitle);
        ditem.setConfiguration(itemConfiguration);
        datasItemArrayList.add(ditem);

        ditem = new DatasItem();
        itemWeek="3";
        itemDate="2월28일";
        itemTitle="셀프리더십2 나의 강점 찾기";
        itemConfiguration="다중지능, 스피치훈련, 셀프웃음 운동,시낭송(감성훈련과 심신치유)";
        ditem.setWeek(itemWeek);
        ditem.setDate(itemDate);
        ditem.setTitle(itemTitle);
        ditem.setConfiguration(itemConfiguration);
        datasItemArrayList.add(ditem);

        ditem = new DatasItem();
        itemWeek="4";
        itemDate="3월7일";
        itemTitle="셀프리더십3 나의꿈 나의 로드맵";
        itemConfiguration="꿈 지도 그려 발표하기, 스피치 훈련,셀프 웃음운동, 시낭송(감성훈련과 심신치유)";
        ditem.setWeek(itemWeek);
        ditem.setDate(itemDate);
        ditem.setTitle(itemTitle);
        ditem.setConfiguration(itemConfiguration);
        datasItemArrayList.add(ditem);


        sitem.setDatas(datasItemArrayList);

        schedules.add(sitem);
/*************************************************************************************/

        ptitle ="하박수웃음지도사 24주 프로그램";
        phost ="(행복을 만드는)\n창의융합교육연구소";
        pstart = "2019-03-22";
        pend ="2019-04-21";
        sitem = new Schedule(ptitle,phost,pstart,pend);
        sitem.setSupport("블라블라");
        sitem.setProjectImgPath("");


        datasItemArrayList = new ArrayList<>();
        ditem = new DatasItem();
        itemWeek="1";
        itemDate="3월14일";
        itemTitle="개 강 식";
        itemConfiguration="오랜테이션, 동기부여, 하박수란?";
        ditem.setWeek(itemWeek);
        ditem.setDate(itemDate);
        ditem.setTitle(itemTitle);
        ditem.setConfiguration(itemConfiguration);
        datasItemArrayList.add(ditem);
        ditem = new DatasItem();
        itemWeek="2";
        itemDate="3월21일";
        itemTitle="셀프리더십1 나는 누구인가?";
        itemConfiguration="(DISC)나의 유형?, 스피치훈련 ,셀프웃음운동 ,시낭송(감성훈련과 심신치유)";
        ditem.setWeek(itemWeek);
        ditem.setDate(itemDate);
        ditem.setTitle(itemTitle);
        ditem.setConfiguration(itemConfiguration);
        datasItemArrayList.add(ditem);
        ditem = new DatasItem();
        itemWeek="3";
        itemDate="2월28일";
        itemTitle="셀프리더십2 나의 강점 찾기";
        itemConfiguration="다중지능, 스피치훈련, 셀프웃음 운동,시낭송(감성훈련과 심신치유)";
        ditem.setWeek(itemWeek);
        ditem.setDate(itemDate);
        ditem.setTitle(itemTitle);
        ditem.setConfiguration(itemConfiguration);
        datasItemArrayList.add(ditem);
        ditem = new DatasItem();
        itemWeek="4";
        itemDate="3월7일";
        itemTitle="셀프리더십3 나의꿈 나의 로드맵";
        itemConfiguration="꿈 지도 그려 발표하기, 스피치 훈련,셀프 웃음운동, 시낭송(감성훈련과 심신치유)";
        ditem.setWeek(itemWeek);
        ditem.setDate(itemDate);
        ditem.setTitle(itemTitle);
        ditem.setConfiguration(itemConfiguration);
        datasItemArrayList.add(ditem);


        sitem.setDatas(datasItemArrayList);

        schedules.add(sitem);



/*************************************************************************************/


        sitem.setDatas(datasItemArrayList);
        item.setSchedules(schedules);

        itemInstructors.add(item);

        /** dataItem 등록**/










        AdapterPIntroList mMyAdapter = new AdapterPIntroList(itemInstructors,PIntroActivity.this);
        /* 리스트뷰에 어댑터 등록 */
        listView.setAdapter(mMyAdapter);

    }//dataSetting
    private void goSetting(int item){
        if(state.equals(USER)){
        Intent intent= new Intent(PIntroActivity.this,SettingActivity.class);
            intent.putExtra("Item",item);
            intent.putExtra("stage",1);
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
                            Intent intent = new Intent(PIntroActivity.this,LoginActivity.class);
                            intent.putExtra("want",3);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton("아니오", null);
            builder.show();
        }
    }


}//PIntroActivity
