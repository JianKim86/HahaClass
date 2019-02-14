package com.jian86_android.hahaclass;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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
    private static final String CUSTOMER = "customer";
    private static final int CUSTOMERLEVEL = 0;
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
    private String picPath="";

    UserInfo userInfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

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

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


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
            showProgress(true);
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
        return phone.length() > 10;
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

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
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

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

               // Toast.makeText(AccountActivity.this, ""+picPath, Toast.LENGTH_SHORT).show();
                if((!(picPath.equals("")))&&picPath!= null) userInfo = new UserInfo(mName, mEmail, mPhone, mPassword, picPath, CUSTOMERLEVEL);
                else userInfo = new UserInfo(mName, mEmail, mPhone, mPassword,"",CUSTOMERLEVEL);
                moveActivity(GOLOGIN);
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


    public void moveActivity(int state){
        Intent intent = null;
        Bundle bundle = null;
        switch (state){
            case GOLOGIN :
                intent = new Intent();
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

                    intent.putExtra("userinfo", bundle);
                    setResult(RESULT_OK, intent); // 호출한 화면으로 되돌려주기
                    // resultCode : 결과 돌려주는 상태
                    finish(); // 두번째 화면 종료
                }else{
                    //가입실패 TODO::dialog
                    Toast.makeText(this, "가입실패", Toast.LENGTH_SHORT).show();
                }

                break;
            case GOBACK :
                intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }//moveActivity

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
                    if(uri != null){
                        //Uri 경로로 전달되었다면
                        //iv.setImageURI(uri);
                        //라이브러리 쓰자!!!!!!!  Glide(bumptech)
                        Picasso.get().load(uri).into(mImg);

                            if(userInfo!=null && userInfo.getImagePath() !=null){
                            Uri uRi = Uri.parse(userInfo.getImagePath());
                            Picasso.get().load(uRi).into(mImg);
                        }

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

    }



    @Override
    public void onPause() {
        super.onPause();
        //intent전환 효과 무효
        overridePendingTransition(0, 0);
    }

}//AccountActivity

