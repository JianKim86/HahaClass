package com.jian86_android.hahaclass;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {
//implements LoaderCallbacks<Cursor>

    private static final String CUSTOMER = "customer";
    private static final String USER = "user";
    private static final String HELLOLOGIN = "환영합니다!";
    private static final int GOMAIN = 1;
    private static final int GOFORGOTPWD = 2;
    private static final int GOSIGNUP = 3;
    public UserInfo userinfo; //읽어들어올 정보를 담을 userinfo;
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

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

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
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only email addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary email addresses first. Note that there won't be
//                // a primary email address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        List<String> emails = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }
//
//        addEmailsToAutoComplete(emails);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//
//    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
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

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            /**서버에서 이메일 유무 확인*/
            //잇으면 true
            //isUser=true;
            if(isUser) {
                /**서버에서 유저정보를 읽어와서 userinfo에 담음*/
                String uName = "김지안";
                String uEmail = "o0x0oa86@gmail.com";
                String uPhone = "01062593352";
                String uPassword = "123456";
                String uImagePath = "";
                int uLevel = 2;
                /** 서버 작업들어가면 담아서 함*/
                userinfo = new UserInfo(uName, uEmail, uPhone, uPassword, uImagePath, uLevel);

                if(!(userinfo.getEmail().equals(mEmail)&&userinfo.getPassword().equals(mPassword))) return false;

            }


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
//액티비티 넘기기

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {


                    moveActivity(GOMAIN,userinfo);


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
        Bundle bundle=null;

        Intent intent = null;
        switch (state){
            case GOMAIN:
                if(userInfo != null) {
                    bundle = new Bundle();
                    int userLevel = userInfo.getLevel();
                    String userPhone = userInfo.getPhone();
                    String userName = userInfo.getName();
                    String userImgPath = userInfo.getImagePath();
                    String userEmail =userInfo.getEmail();
                    String userPassword =userInfo.getPassword();
                    bundle.putInt("Level",userLevel);
                    bundle.putString("Name",userName);
                    bundle.putString("Image",userImgPath);
                    bundle.putString("Phone",userPhone);
                    bundle.putString("Email",userEmail);
                    bundle.putString("Pass",userPassword);
                    bundle.putString("state",USER);
                }else{
                    bundle = new Bundle();
                    bundle.putString("state",CUSTOMER);
                }
                intent = new Intent(LoginActivity.this, PIntroActivity.class);
                intent.putExtra("Bundle", bundle);
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
    Bundle bundleData;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == GOSIGNUP){
        if (resultCode==RESULT_OK) { // 정상 반환일 경우에만 동작하겠다
            bundleData = data.getBundleExtra("userinfo");
            int level = bundleData.getInt("Level");
            String email =  bundleData.getString("Email");
            String pwd = bundleData.getString("Pass");
            String name =  bundleData.getString("Name");
            String img = bundleData.getString("Image");
            String phone = bundleData.getString("Phone");
            userinfo = new UserInfo(name,email,phone,pwd,img,level);
            if(img !=null &&!(img.equals(""))){
            Uri uRi = Uri.parse(img);
            Picasso.get().load(uRi).into(iv_myimg);}
            mEmailView.setText(email);
            mPasswordView.setText(pwd);
            tvTitle.setText(HELLOLOGIN);
            tvSubtitle.setText(name+"님");
            /**서버 작업이 되면 서버에서 회원정보를 읽어와서 isUser=true 로바꾸고 비교해서 액티비티 이동**/
           // isUser = true;
           // moveActivity(GOMAIN,new UserInfo(name,email,phone,pwd,img,level));
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

