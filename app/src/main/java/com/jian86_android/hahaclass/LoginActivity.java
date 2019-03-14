package com.jian86_android.hahaclass;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {
//implements LoaderCallbacks<Cursor>
    private static final String baseImgePath = "http://jian86.dothome.co.kr/HahaClass/";
    private static final int img_length ="uploads/20190310031854".length();
    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final String HELLOLOGIN = "환영합니다!";
    private static final int GOMAIN = 1;
    private static final int GOFORGOTPWD = 2;
    private static final int GOSIGNUP = 3;
    public UserInfo userinfo; //읽어들어올 정보를 담을 userinfo;
    private ApplicationClass applicationClass;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private  ImageView iv_myimg ;
    private TextView tvTitle,tvSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        applicationClass = (ApplicationClass) getApplicationContext();
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



        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
      //  populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        /** 데이터를 읽어와서 이메일 확인후 받아오기
         */
        Intent getintentsettiong = getIntent();
        int i = getintentsettiong.getIntExtra("want",0);
        if (i==GOSIGNUP){
            moveActivity(GOSIGNUP,null);
        }

//btn signin
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

//btn custom
        Button mCustomerButton = (Button) findViewById(R.id.custom_sign_in);
        mCustomerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(GOMAIN,null);
            }
        });
//btn signforgotpwd
        TextView mForgotButton = (TextView) findViewById(R.id.tv_forgotpwd);
        mForgotButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(GOFORGOTPWD,null);
            }
        });
//btn signup
        Button mSignupButton = (Button) findViewById(R.id.sign_up);
        mSignupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(GOSIGNUP,null);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        iv_myimg= findViewById(R.id.iv_myimg);
        tvTitle =findViewById(R.id.tv_title);
        tvSubtitle =findViewById(R.id.tv_subtitle);

    }//onCreate

    private boolean isUser = false;
    private static final int LEVELCUSTOMER = 0;
    private static final int LEVELADMIN = 1;
    private static final int LEVELSUPERADMIN = 2;
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        // Check for a valid pwd.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            if (!(TextUtils.isEmpty(email)||!isEmailValid(email))) focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            if (!(TextUtils.isEmpty(email)||!isEmailValid(email))) focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

           // showProgress(true);
            DBaddUserInfo();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }//showProgress


//이메일유무및 비밀번호 먼저 확인
    //정보 불러와서 저장하기
    public void DBaddUserInfo(){

        String serverURL = "http://jian86.dothome.co.kr/HahaClass/check_is_user.php";

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userinfo =null;
                isUser = true;
                //new AlertDialog.Builder(LoginActivity.this).setMessage(response).show();
                Log.i("this_apply", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String email = jsonObject.getString("email");
                        String phone = jsonObject.getString("phone");
                        String password = jsonObject.getString("password");
                        String level = jsonObject.getString("level");
                        String image_path = jsonObject.getString("image_path");
                        String date = jsonObject.getString("date");
                        String getl_num = jsonObject.getString("l_num");
                        //파일경로가 서버 IP가 제외된 주소임
                        image_path = baseImgePath + image_path;
                        //userinfo 추가
                        if ( mPasswordView.getText().toString().equals(password)) {
                            userinfo = new UserInfo(name, email, phone, password, image_path, Integer.parseInt(level));

                            //강의 코드 글로벌에 담음
                            //런처때 내 이메일로 검색해서 강의 코드가 있는지 확인 있으면 담음
                            //글로벌에 담긴 강의 코드와 비교해서 이미 수강신청한 코드인지 확인
                           // applicationClass.setApplySchedule(new ApplyClassInfo(l_num,class_code));
                            //apply 정보 받기
                            if(jsonObject.has("has_apply_class")) {
                                JSONObject jsonkeyArray = jsonObject.getJSONObject("has_apply_class");
                                if (jsonkeyArray != null) {
                                    for (int y = 0; y < jsonkeyArray.length(); y++) {
                                        JSONObject jsonObject1 = jsonkeyArray.getJSONObject(y + "");

                                        String l_num = jsonObject1.getString("l_num");
                                        String class_code = jsonObject1.getString("class_code");
                                        //apply_user_num = jsonObject1.getString("apply_user_num");
                                        ApplyClassInfo applyClassInfo = new ApplyClassInfo(l_num, class_code);
                                        applicationClass.setApplySchedule(applyClassInfo); //헤시에 add로 들어감
                                    }

                                }
                            }

                            //setting_log 데이터 얻어오기
                            DBsetData(email);
                            if(Integer.parseInt(level)>1) { DBsetMyClassData(email, getl_num); DBsetData_myclass_userInfo(getl_num); }

                            // Log.i("cont_hashs",applicationClass.getApplySchedule().size()+"");
                            moveActivity(GOMAIN, userinfo);
                        } else
                            new AlertDialog.Builder(LoginActivity.this).setMessage("비밀번호가 다릅니다").show();
                    }

                    //  applicationClass.setUserInfo(userinfo);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                return;


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러남 아이디가 없음
                new AlertDialog.Builder(LoginActivity.this).setMessage("인터넷이 원활하지 않습니다.").show();
            }


        })
            {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",mEmailView.getText().toString() );
                params.put("password", mPasswordView.getText().toString());
                return params;
            }

        };

        //우체통
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    private HashMap<String,Schedule>applyClasses = new HashMap<String,Schedule>(); //내신청강의 담기
    private  HashMap<String,RecivedApplicant> recivedclasses = new HashMap<String,RecivedApplicant>();
    //setting_log데이터 얻어오기
    private void DBsetData(final String email){
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
                params.put("apply_user_email",email);
//                if(userInfo.getLevel()>1) { params.put("l_num",userInfo.getL_num()); }
                return params;
            }
        };

        //우체통
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonArrayRequest);
    }//DBsetData
    private void DBsetMyClassData(final String getemail, final String getl_num){
        /**서버에 담을때 스케쥴로 어레이에 담음 */
        String serverURL = "http://jian86.dothome.co.kr/HahaClass/get_my_class_list.php";
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                String l_num="";
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
                                    l_num = jsonObject1.getString("l_num");
                                    String class_code = jsonObject1.getString("class_code");
                                    String class_title = jsonObject1.getString("class_title");
                                    String 	start_day = jsonObject1.getString("start_day");
                                    String 	finish_day = jsonObject1.getString("finish_day");
                                    String class_apply = jsonObject1.getString("class_apply");

                                        RecivedApplicant recivedApply = new RecivedApplicant(start_day + "~" + finish_day, class_title);
                                        recivedApply.setL_num(l_num);
                                        recivedApply.setClass_code(class_code);
                                        recivedApply.setStart(start_day);
                                        recivedApply.setEnd(finish_day);
                                        recivedApply.setCnt(class_apply);
                                        // 헤시셋으로 정보 담기


                                        recivedclasses.put(class_code, recivedApply);


                                }//for
                            }//if
                        }//if

                        //내가 신청한 강의에 담음

                        if(getl_num.equals(l_num)) applicationClass.setMyReceivedClass(recivedclasses);
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
                params.put("apply_user_email",getemail);
                params.put("l_num",getl_num);
                return params;
            }
        };
        //우체통
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonArrayRequest);
    }//DBsetMyClassData

    //for setting_log_myclass
    private void DBsetData_myclass_userInfo(final String get_l_num){
        /**서버에 담을때 스케쥴로 어레이에 담음 */
        /**DB: class_apply_list에서 filter : l_num -> 정보 검색하여 ApplyClassInfo에 담음*/

        String serverURL = "http://jian86.dothome.co.kr/HahaClass/get_apply_user_info.php";
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray jsonArray = null;
                Log.i("gresponsesg",response);
                try {
                    jsonArray = new JSONArray(response);
                    ArrayList<ApplyClassInfo>arr = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String l_num = jsonObject.getString("l_num");
                        String class_code = jsonObject.getString("class_code");
                        String apply_user_email	 = jsonObject.getString("apply_user_email");
                        String 	apply_name	 = jsonObject.getString("apply_name");
                        String 	apply_phone	 = jsonObject.getString("apply_phone");
                        String 	date = jsonObject.getString("date");

                        ApplyClassInfo applyClassInfo = new ApplyClassInfo(l_num,class_code,apply_name,apply_phone,date,apply_user_email);
                        arr.add(applyClassInfo);
                    }//for
                        applicationClass.setApplyClassUserInfos(arr);//해당 강사의 모든 강의 클레스에대한 신청유저정보가 담겨있음
                        Log.i("dsdsdsdaaaa",applicationClass.getApplyClassUserInfos().size()+"");
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
                params.put("l_num",get_l_num);
                return params;
            }
        };

        //우체통
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonArrayRequest);
    }//DBsetData







    //액티비티 이동
    public void moveActivity(int state, UserInfo userInfo){

   //     new AlertDialog.Builder(LoginActivity.this).setMessage("서비스를 준비중입니다").show();
        Intent intent = null;
        switch (state){
            case GOMAIN:
                if(userInfo != null) {
                    applicationClass.setUserInfo(null);
                    applicationClass.setUserInfo(userInfo);
                    applicationClass.setLevel(userInfo.getLevel());
                    applicationClass.setState(USER);

                    tvTitle.setText(HELLOLOGIN);
                    tvSubtitle.setText(userInfo.getName()+"님");
                    /**메인 이미지 저장하기 */
                    String img =userInfo.getImagePath();
                    Log.i("img_length",img.length()+"");
//이미지
                    if(img != null && img.length()!=img_length) {
                        Uri uRi = Uri.parse(img);

                        Picasso.get().load(uRi)
                                .resize(400,400).centerCrop().into(iv_myimg, new Callback() {
                            @Override
                            public void onSuccess() {
                            }
                            @Override
                            public void onError(Exception e) {
                               // Log.d("picPath ", "pIntor img: load failed "+ img);
                            }
                        });
                    }
                    else{Glide.with(this).load(R.drawable.ic_launcher_background).into(iv_myimg);}



                }else{
                    applicationClass.setUserInfo(null);
                    applicationClass.setState(CUSTOMER);

                }


                intent = new Intent(LoginActivity.this, PIntroActivity.class);
                //intent.putExtra("Bundle", bundle);
                startActivity(intent);
                //finish();
                break;
            case GOFORGOTPWD:
//                intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
                //finish();
                break;
            case GOSIGNUP:
                intent = new Intent(LoginActivity.this, AccountActivity.class);
                startActivityForResult(intent,GOSIGNUP);
                //break;
        }

    }//moveActivity
//인텐트 돌려받고 로그인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == GOSIGNUP){
        if (resultCode==RESULT_OK) { // 정상 반환일 경우에만 동작하겠다

            String email = data.getStringExtra("email");
            String pwd = data.getStringExtra("password");

            mEmailView.setText(email);
            mPasswordView.setText(pwd);
            attemptLogin(); //로그인
//            if(img !=null &&!(img.equals(""))){
//                Uri uRi = Uri.parse(img);
//                Picasso.get().load(uRi)
//                        .resize(400,400).into(iv_myimg);}
//                mEmailView.setText(email);
//                mPasswordView.setText(pwd);

                /**서버 작업이 되면 서버에서 회원정보를 읽어와서 isUser=true 로바꾸고 비교해서 액티비티 이동**/
              //  Toast.makeText(applicationClass, "로그인시:"+applicationClass.getUserInfo().getImagePath(), Toast.LENGTH_SHORT).show();
            moveActivity(GOMAIN,userinfo);
        }
       }//GOSIGNUP


    }//onActivityResult

    @Override
    protected void onResume() {
        super.onResume();
        if(applicationClass!=null&&applicationClass.getUserInfo()!=null){
       // DBaddUserInfo();
        if(userinfo.getImagePath() != null && userinfo.getImagePath().length()!=img_length) {
            Uri uRi = Uri.parse(userinfo.getImagePath());

            Picasso.get().load(uRi)
                    .resize(400,400).centerCrop().into(iv_myimg, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError(Exception e) {
                    // Log.d("picPath ", "pIntor img: load failed "+ img);
                }
            });
        }
        else{Glide.with(this).load(R.drawable.ic_launcher_background).into(iv_myimg);}
    }
    }
    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }


}

