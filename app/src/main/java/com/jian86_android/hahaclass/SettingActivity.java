package com.jian86_android.hahaclass;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    private ApplicationClass applicationClass;
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final int PIC = 1000;
    private Toolbar toolbar;
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private UserInfo userInfo;
    private String state,name,email,phone,pass,img;
    private int level;
    private ActionBarDrawerToggle drawerToggle;
    private View nav_header_view;
    private  TextView nav_header_id_text;
    private  ImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar =findViewById(R.id.toolbar_t);
        setSupportActionBar(toolbar);
        navMenu =findViewById(R.id.nav_menu);
        drawerLayout =findViewById(R.id.drawer_layout);
        nav_header_view = navMenu.inflateHeaderView(R.layout.nav_header);
        nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_name);
        profile_image =(ImageView) nav_header_view.findViewById(R.id.profile_image);
        setUpNav();
        setFragmentPlace();
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //Todo: 아이탬 연결
                    case R.id.item1 :goSetting(0); break;
                    case R.id.item2 :goSetting(1); break;
                    case R.id.item3 :goSetting(2); break;
                    case R.id.item4 :goSetting(3); break;
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
        actionBar.setTitle("Setting");
        //삼선 아이콘 모양으로 동기마추기
        drawerToggle.syncState();
        //삼선 아이콘과 화살표아이콘이 자동 변환되도록
        drawerLayout.addDrawerListener(drawerToggle);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }//onCreate

    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }//onCreateOptionsMenu
    private int stage;
    private final int MAINACTIVITY=0;
    private final int PINTROACTIVITY=1;
    private final int BOARDACTIVITY=2;
    //토글 버튼과 drawer 연결
    //엑티비티 입장에서 토글버튼도 액션바 메뉴 인가 느낌
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.goback:
                Intent intents;
                switch (stage){
                    case MAINACTIVITY :
                        startActivity(new Intent(SettingActivity.this,MainActivity.class));
                        finish();
                        break;
                    case PINTROACTIVITY :
                        startActivity(new Intent(SettingActivity.this,PIntroActivity.class));
                        finish();
                        break;
                    case BOARDACTIVITY :
                    startActivity(new Intent(SettingActivity.this,BoardActivity.class));
                    finish();
                    break;
                }
                break;
        }
        //아이템 클릭상황을 토글 버튼에 전달
        drawerToggle.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);


    }
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    Fragment fragment;
    //데이터 세팅
    private void  getIntentData() {

        applicationClass =(ApplicationClass)getApplicationContext();
        state = applicationClass.getState();
        if (state.equals(USER)) {
            userInfo = applicationClass.getUserInfo();
            name = userInfo.getName();
            email = userInfo.getEmail();
            phone = userInfo.getPhone();
            pass = userInfo.getPassword();
            img = userInfo.getImagePath();
            level = userInfo.getLevel();
        } else {
            userInfo = null;
        }
        //데이터 얻어오기
        DBsetData();
        if(userInfo.getLevel()>1) DBsetMyClassData();

    }
    public void setUpNav(){
        //데이터 받기
        getIntentData();
      // Toast.makeText(applicationClass, "2::"+applicationClass.getUserInfo().getImagePath(), Toast.LENGTH_SHORT).show();
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
        }else{Glide.with(this).load(R.drawable.ic_launcher_background).into(profile_image);}
    }
    public void setFragmentPlace(){
        Intent intent = getIntent();
        int item = intent.getIntExtra("Item",0);
        stage = intent.getIntExtra("stage",0);
        goSetting(item);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item){
            case 0: fragment = new FragSettingAccount();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 1: fragment = new FragSettingPwd();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 2: fragment = new FragSettingLog();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 3: fragment = new FragSettingQnA();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
        }
    }
//세팅페이지이동
    private void goSetting(int item){
      //세팅페이지 이동

       fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item){
            case 0: fragment = new FragSettingAccount();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 1: fragment = new FragSettingPwd();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 2: fragment = new FragSettingLog();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
            case 3: fragment = new FragSettingQnA();  fragmentTransaction.replace(R.id.layout_setting, fragment).commit(); break;
        }




        //TODO: 프레그먼트 이동
    }

    private HashMap<String,Schedule>applyClasses = new HashMap<String,Schedule>(); //내신청강의 담기
    private  HashMap<String,RecivedApplicant> recivedclasses = new HashMap<>();
//데이터 얻어오기
    private void DBsetData(){
        /**서버에 담을때 스케쥴로 어레이에 담음 */
        /**DB: class_apply_list에서 filter : userEmail -> l_num과 class_code를 얻어 DB: class_list에서 정보 검색하여 schadule에 담음*/
        String serverURL = "http://jian86.dothome.co.kr/HahaClass/get_apply_class_list.php";
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    JSONArray jsonArray = null;
                    Log.i("gresponsesg",response);
                    try {
                        jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.has("my_apply_class_info")) {
                                JSONObject jsonkeyArray = jsonObject.getJSONObject("my_apply_class_info");
                                if (jsonkeyArray != null) {
                                    //  applicationClass.getMyApplyClasses().clear();
                                    for (int y = 0; y < jsonkeyArray.length(); y++) {
                                        JSONObject jsonObject1 = jsonkeyArray.getJSONObject(y + "");
                                        String l_num = jsonObject1.getString("l_num");
                                        String class_code = jsonObject1.getString("class_code");
                                        String class_title = jsonObject1.getString("class_title");
                                        String 	start_day = jsonObject1.getString("start_day");
                                        String 	finish_day = jsonObject1.getString("finish_day");
                                        Schedule applyClassInfo = new Schedule(l_num,class_title,start_day,finish_day,class_code);
                                        applyClasses.put(class_code,applyClassInfo);

                                    }//for
                                }//if
                            }//if

                            //내가 신청한 강의에 담음
                            applicationClass.setMyApplyClasses(applyClasses);
                            // Log.i("gstts",applicationClass.getMyApplyClasses().size()+"");
                        }//for
                        //log: my_apply


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("apply_user_email",userInfo.getEmail());
                if(userInfo.getLevel()>1) { params.put("l_num",userInfo.getL_num()); }
                return params;
            }
        };

        //우체통
        RequestQueue requestQueue = Volley.newRequestQueue(SettingActivity.this);
        requestQueue.add(jsonArrayRequest);
    }//DBsetData
   private void DBsetMyClassData(){
       /**서버에 담을때 스케쥴로 어레이에 담음 */
       String serverURL = "http://jian86.dothome.co.kr/HahaClass/get_my_class_list.php";
       StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
            JSONArray jsonArray = null;
               Log.i("gresponsesg",response);
               try {
                   jsonArray = new JSONArray(response);

                   for (int i = 0; i < jsonArray.length(); i++) {
                       JSONObject jsonObject = jsonArray.getJSONObject(i);

                       if (jsonObject.has("r_class_info")) {
                           JSONObject jsonkeyArray = jsonObject.getJSONObject("r_class_info");
                           if (jsonkeyArray != null) {
                               //  applicationClass.getMyApplyClasses().clear();
                               for (int y = 0; y < jsonkeyArray.length(); y++) {
                                   JSONObject jsonObject1 = jsonkeyArray.getJSONObject(y + "");
                                   String l_num = jsonObject1.getString("l_num");
                                    String class_code = jsonObject1.getString("class_code");
                                    String class_title = jsonObject1.getString("class_title");
                                    String 	start_day = jsonObject1.getString("start_day");
                                    String 	finish_day = jsonObject1.getString("finish_day");

                                    RecivedApplicant recivedApply = new RecivedApplicant(start_day+"~"+finish_day,class_title);
                                    recivedApply.setL_num(l_num);
                                    recivedApply.setClass_code(class_code);
                                    recivedApply.setStart(start_day);
                                    recivedApply.setEnd(finish_day);

                                     recivedclasses.put(class_code,recivedApply);


                               }//for
                           }//if
                       }//if

                       //내가 신청한 강의에 담음

                       applicationClass.setMyReceivedClass(recivedclasses);
                   }//for
                   //log: my_apply


               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }){
           @Override
           protected Map<String, String> getParams() {
               // Posting parameters to login url
               Map<String, String> params = new HashMap<String, String>();
               params.put("apply_user_email",userInfo.getEmail());
               params.put("l_num",userInfo.getL_num());
               return params;
           }
       };

       //우체통
       RequestQueue requestQueue = Volley.newRequestQueue(SettingActivity.this);
       requestQueue.add(jsonArrayRequest);
   }//DBsetMyClassData


//사진
    void takePic(){

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PIC);
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
                    String picPathh = uri.toString();
                    String picPath = getRealPathFromUri(uri);

                        //Uri 경로로 전달되었다면
                        //iv.setImageURI(uri);
                        //라이브러리 쓰자!!!!!!!  Glide(bumptech)
                            ((FragSettingAccount)fragment).setPic(picPathh, picPath);

                    }else{
                        //아니면 Intent 에 Extra 데이터로 Bitmap 이 전달되어 옴
                        Bitmap bm=null;
                            ((FragSettingAccount)fragment).setPic(bm);

                }//if

                break;
        }


    }//onActivityResult

    //이미지 절대경로로 바꾸기
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        android.support.v4.content.CursorLoader loader= new android.support.v4.content.CursorLoader(SettingActivity.this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }


}//SettingActivity
