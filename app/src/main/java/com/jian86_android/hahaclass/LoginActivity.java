package com.jian86_android.hahaclass;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    private static final int img_length =60;
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
    private UserLoginTask mAuthTask = null;

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

//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
//    }
//
//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private boolean isUser = false;
    private static final int LEVELCUSTOMER = 0;
    private static final int LEVELADMIN = 1;
    private static final int LEVELSUPERADMIN = 2;
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
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
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
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
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            /**서버에서 이메일 유무 확인*/
            addUserInfo();
            // TODO: register the new account here.
            return true;
        }

    private void searchEmailDB(){
        new Thread(){
            @Override
            public void run() {
                //1. userinfo 에 있는 정보 유저 테이블에 넣기
                String serverUrl = "http://jian86.dothome.co.kr/HahaClass/check_is_user.php";



                SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("nohave")){
                            isUser = false;
                            new AlertDialog.Builder(LoginActivity.this).setMessage(response).show();
                        }
                        else{
                            isUser = true;
                            //비밀번호 비교 TODO:비밀번호 비교해서 userInfo에 담아 글로벌로
                            //response 가 0이면 비밀번호 맞음
                            if(response.equals("0")) {
                               // new AlertDialog.Builder(LoginActivity.this).setMessage("비밀번호가 맞음").show();
                                addUserInfo(); return;
                            }else { new AlertDialog.Builder(LoginActivity.this).setMessage("비밀번호가 틀림").show(); return;}
                            //new AlertDialog.Builder(LoginActivity.this).setMessage("비밀번호가 틀림").show();


                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //서버 요청중 에거라 발생하면 자동 실행
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish(); //화면 종료
                    }
                });//세번째 파라미터가 응답을 받아옴


                //포스트 방식으로 보낼 데이터들 요청 객체에 추가하기
                multiPartRequest.addStringParam("email", mEmail);
                multiPartRequest.addStringParam("password", mPassword);

                //요청객체를 실제 서버쪽으로 보내기 위해 우체통같은 객체
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

                //요청 객체를 우체통에 넣기
                requestQueue.add(multiPartRequest);

            }//run
        }.start();




    }//searchEmailDB

//이메일유무및 비밀번호 확인
    private void addUserInfo(){

        String serverURL = "http://jian86.dothome.co.kr/HahaClass/check_is_user.php";
//            Map<String, String> params = new HashMap<>();
//            params.put("email",mEmailView.getText().toString() );
//            params.put("password", mPasswordView.getText().toString());
        //결과를 json array로 받을 것임으로 JsonArrayRequest요청 객체 생성
        StringRequest jsonArrayRequest = new StringRequest(serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //받음
                //서버로 부터 에코된 데이터 가 JSONArray response
//                    if(response==null){
//                        isUser = false;  new AlertDialog.Builder(LoginActivity.this).setMessage("이메일이 없습니다").show(); return;
//                    }

                isUser = true;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    StringBuffer buffer1 = new StringBuffer();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String email = jsonObject.getString("email");
                        String phone = jsonObject.getString("phone");
                        String password = jsonObject.getString("password");
                        String level = jsonObject.getString("level");
                        String image_path = jsonObject.getString("image_path");
                        String date = jsonObject.getString("date");
                        //파일경로가 서버 IP가 제외된 주소임
                        image_path = baseImgePath + image_path;
                        //userinfo 추가
                        if (mPassword.equals(password)) {
                            userinfo = new UserInfo(name, email, phone, password, image_path, Integer.parseInt(level));
                            moveActivity(GOMAIN, userinfo);
                        } else
                            new AlertDialog.Builder(LoginActivity.this).setMessage("비밀번호가 다릅니다").show();


                    }


                    //  applicationClass.setUserInfo(userinfo);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                //new AlertDialog.Builder(LoginActivity.this).setMessage(str.toString()).show();
                return;


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러남 아이디가 없음

                //new AlertDialog.Builder(LoginActivity.this).setMessage(String.valueOf(error)).show();
                //Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(LoginActivity.this).setMessage("회원이 아닙니다").show();
            }


    })
             {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", mEmail);
                    params.put("password", mPassword);
                    return params;
                }

                @Override
                public int getMethod() {
                    return Method.POST;
                }
            };

//            {
//                //Pass Your Parameters here
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError{
//                    Map<String, String> params = new HashMap<>();
//                    params.put("email", mEmail);
//                    params.put("password", mPassword);
//                    return params;
//                }
//            };
//        Map<String, String> params = new HashMap<>();
//        params.put("email", mEmail);
//      //  params.put("password", mPassword);
//
//        jsonArrayRequest.setParams(params);

        //우체통
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

//액티비티 넘기기
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {


//            if(isUser) {new AlertDialog.Builder(LoginActivity.this).setMessage("회원입니다").show();}//
//            else new AlertDialog.Builder(LoginActivity.this).setMessage("회원이 아닙니다").show();

            } else {

                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

//액티비티 이동
    public void moveActivity(int state, UserInfo userInfo){

   //     new AlertDialog.Builder(LoginActivity.this).setMessage("서비스를 준비중입니다").show();
        Intent intent = null;
        switch (state){
            case GOMAIN:
                if(userInfo != null) {
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
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }


}

