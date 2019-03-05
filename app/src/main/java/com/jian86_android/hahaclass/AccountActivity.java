package com.jian86_android.hahaclass;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.text.InputFilter;
import android.text.Spanned;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class AccountActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private static final int GOLOGIN = 3;
    private static final int GOBACK = 1;
    private static final int PIC = 1000;
    private static final int IMAGEUPLOAD = 100;
    private static final String CUSTOMER = "customer";
    private static final int CUSTOMERLEVEL = 1;
    private static final int ADMINLEVEL = 2;
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
    private EditText mPasswordView, mRePasswordView;
    private EditText mNameView;
    private EditText mPhoneView;
    private View mProgressView;
    private View mLoginFormView;
    private ImageView mImg;
    private String picPath;
    private RadioGroup rg;
    private RadioButton rb;
    UserInfo userInfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        applicationClass = (ApplicationClass)getApplicationContext();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
//        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mRePasswordView = (EditText) findViewById(R.id.repassword);
        mNameView = (EditText) findViewById(R.id.name);
        mPhoneView = (EditText) findViewById(R.id.phone);
        mImg = (ImageView) findViewById(R.id.iv_myimg);
        mImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }
        });
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
        //필터
        mNameView.setFilters(new InputFilter[]{filterAlpha});
        mPhoneView.setFilters(new InputFilter[]{filterAlphaNum});
//클릭이벤트
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        TextView goBack =(TextView)findViewById(R.id.tv_return_login_activity);
        goBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveActivity(GOBACK);
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

          rg = (RadioGroup)findViewById(R.id.radioGroup1);
          rb = (RadioButton) findViewById(R.id.radio0);

          //외부저장소에 있는 이미지 파일을 서버로 보내기 위한 퍼미션
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},IMAGEUPLOAD);
            }
        }

    }//oncreate

//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
//    }

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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
  //                  populateAutoComplete();
                }
                break;
            case IMAGEUPLOAD:
                if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "외부저장소 사용 불가 \n 이미지 저장 불가", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }//onRequestPermissionsResult


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mNameView.setError(null);
        mPhoneView.setError(null);
        mRePasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String repassword = mRePasswordView.getText().toString();
        String name = mNameView.getText().toString();
        String phone= mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password,repassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        //Check for a valid name
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (!isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            focusView = mNameView;
            cancel = true;
        }
        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        // Check for a valid phone number.
        else if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        } else if (!isPoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }
        // Check for a valid pwd.
        else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(repassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mRePasswordView;
            cancel = true;
        }else if (!isPasswordValid(password,repassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
         //   showProgress(true);
            mAuthTask = new UserLoginTask(name, email, phone, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() == 11 || phone.length() == 10;
    }
    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 1;
    }
    private boolean isPasswordValid(String password,String repassword) {
        return (password.length() > 4) && (password.equals(repassword));
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(AccountActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mName;
        private final String mPhone;

        UserLoginTask(String name, String email, String phone, String password) {
            mEmail = email;
            mPassword = password;
            mName = name;
            mPhone = phone;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            if(rb.isChecked()){
                //Toast.makeText(applicationClass, ""+picPath, Toast.LENGTH_SHORT).show();
                if(picPath!= null&&(!(picPath.equals("")))) {userInfo = new UserInfo(mName, mEmail, mPhone, mPassword, picPath, ADMINLEVEL);}
                else userInfo = new UserInfo(mName, mEmail, mPhone, mPassword,"",ADMINLEVEL);
                //moveActivity(GOLOGIN);
             //   showProgress(true);
            }else{
                if(picPath!= null&&(!(picPath.equals("")))) userInfo = new UserInfo(mName, mEmail, mPhone, mPassword, picPath, CUSTOMERLEVEL);
                else userInfo = new UserInfo(mName, mEmail, mPhone, mPassword,"",CUSTOMERLEVEL);
               //
            }
            //네트워크 작업
            saveDB();

                //비교
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
           // showProgress(false);

            if (success) {

               // Toast.makeText(AccountActivity.this, ""+picPath, Toast.LENGTH_SHORT).show();

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
          //  showProgress(false);
        }
    }


    public void moveActivity(int state){
        Intent intent = null;
        switch (state){
            case GOLOGIN :
                intent = new Intent();
                if(userInfo != null) {
                    //  applicationClass.setUserInfo(userInfo);
                    //  showProgress(false);

                    intent.putExtra("email",userInfo.getEmail());
                    intent.putExtra("password",userInfo.getPassword());
                    setResult(RESULT_OK, intent); // 호출한 화면으로 되돌려주기
//                    // resultCode : 결과 돌려주는 상태
                    finish(); // 두번째 화면 종료
                }else{
                    //가입실패 TODO::dialog
                    Toast.makeText(this, "중복된 이메일 입니다", Toast.LENGTH_SHORT).show();
                }
                break;
            case GOBACK :
                intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }//moveActivity

    public  void saveDB(){

        new Thread(){
            @Override
            public void run() {
                //1. userinfo 에 있는 정보 유저 테이블에 넣기
                String serverUrl = "http://jian86.dothome.co.kr/HahaClass/info_list_insert.php";
                SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버로부터 응답을 받을때 자동 실행
                        //매개변수로 받은 Stringdl echo된 결과값

                      // new AlertDialog.Builder(AccountActivity.this).setMessage(response).show();

                        if(response.equals("already")){
                            userInfo= null;
                        }
                        else{
                            new AlertDialog.Builder(AccountActivity.this).setMessage(response).show();
                             }
                        moveActivity(GOLOGIN);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //서버 요청중 에거라 발생하면 자동 실행
                        Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish(); // 두번째 화면 종료
                    }
                });//세번째 파라미터가 응답을 받아옴


                //포스트 방식으로 보낼 데이터들 요청 객체에 추가하기
                multiPartRequest.addStringParam("name", userInfo.getName());
                multiPartRequest.addStringParam("email", userInfo.getEmail());
                multiPartRequest.addStringParam("phone", userInfo.getPhone());
                multiPartRequest.addStringParam("password", userInfo.getPassword());
                multiPartRequest.addStringParam("level", userInfo.getLevel()+"");
                multiPartRequest.addFile("image_path",picPath);

                //요청객체를 실제 서버쪽으로 보내기 위해 우체통같은 객체
                RequestQueue requestQueue = Volley.newRequestQueue(AccountActivity.this);

                //요청 객체를 우체통에 넣기
                requestQueue.add(multiPartRequest);

            }//run
        }.start();




    }//saveDB


    // 영문 + 한글만 입력 되도록
    public InputFilter filterAlpha = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
           // 한글만 입력 되도록 : "^[ㄱ-ㅣ가-힣]*$"
           // 영어만 입력 되도록 : "^[a-zA-Z]*$"
          //  숫자만 입력 되도록 : "^[0-9]*$"
            Pattern ps = Pattern.compile("^[a-zA-Z-ㄱ-ㅣ가-힣]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };
    // 숫자만 입력 되도록
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


    void takePic(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PIC);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PIC:

                //갤러리 화면에서 이미지를 선택하고 돌아왔는지 체크
                //두 번째 파라미터 : resultCode
                if (resultCode == RESULT_OK){

                    Uri uri = data.getData();
                   // picPath = uri.toString();
                    picPath = getRealPathFromUri(uri);
                    if(uri != null){
                        Picasso.get().load(uri).into(mImg);

                    }else{
                        //아니면 Intent 에 Extra 데이터로 Bitmap 이 전달되어 옴
                        Bundle bundle=data.getExtras();
                        Bitmap bm= (Bitmap) bundle.get("data"); // key 값 "data" 는 정해진거야
                        //iv.setImageBitmap(bm);
                        Glide.with(this).load(bm).into(mImg);

                    }

                }

                break;
        }

    }//onActivityResult

    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }

}//AccountActivity

