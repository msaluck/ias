package com.ias.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
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

import com.ias.handler.Constant;
import com.ias.handler.HttpHandler;
import com.ias.iaslogin.R;

import java.util.ArrayList;
import java.util.List;














import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

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
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Fullname = "fullname";
    public static final String Id = "id";
    public static final String UrlImage = "url_image";
    public SharedPreferences sharedpreferences;
    private String android_id=null;
    private void addNotification(Bitmap iconNotif) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(iconNotif).setSmallIcon(R.drawable.ic_notifications)
                        .setContentTitle("TRCI EVENT")
                        .setContentText("WARNING EVENT TRCI - Tanggal 10 November");

        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences =  PreferenceManager.getDefaultSharedPreferences(this);

        android_id = Settings.Secure.getString(this.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        setContentView(R.layout.activity_login);

            // Set up the login form.
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            populateAutoComplete();

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button signUp = (Button) findViewById(R.id.email_sign_up_button);
        Button activation = (Button) findViewById(R.id.activation);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                attemptLogin();
//                Drawable myDrawable = getResources().getDrawable(R.mipmap.icon);
//                Bitmap iconNotif = ((BitmapDrawable) myDrawable).getBitmap();
//                addNotification(iconNotif);
//            }
//        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
//
            }
        });
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });

        activation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ActivationActivity.class);
                startActivity(i);
            }
        });

        if (null!=sharedpreferences.getString("fullname",null)){
            mProgressView.showContextMenu();
            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }

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

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Password harus diisi");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Username harus diisi");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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
            new LoginTaskWithUsername(email, password, android_id).execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("");
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
                finish();
                Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(i);
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

        public class LoginTaskWithUsername extends AsyncTask<String, Void, JSONObject> {


            private final String mEmail;
            private final String mPassword;
            private final String device_id;

            LoginTaskWithUsername(String email, String password, String android_id) {
                mEmail = email;
                mPassword = password;
                device_id=android_id;
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                // TODO: attempt authentication against a network service.
                // getJSON with PhoneNumber;
                HttpHandler sh = new HttpHandler();

                // Making a request to url and getting response
                String url = Constant.URL_STRING + "/LoginServlet?username=" + mEmail + "&&password=" + mPassword+"&&device_id=" + device_id;

                String jsonStr = sh.makeServiceCall(url, "GET");
                JSONObject jsonObj = null;
//
                Log.e("TAG", "Response from url from login activity: " + jsonStr);
                if (jsonStr != null) {
                    try {
                        jsonObj = new JSONObject(jsonStr);

                    } catch (final JSONException e) {
                        Log.e("TAG", "Json parsing error: " + e.getMessage());

                    }

                } else {
                    Log.e("TAG", "Couldn't get json from server.");
                }
                return jsonObj;


            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                showProgress(false);
                String id = null;
                String username = null;
                String fullname;
                String url_image = null;
                String no_stnk = null;
                String chapter_name = null;
                String no_sim = null;
                String branch_name = null;
                String brand_name = null;
                String car_type_name = null;
                String year = null;
                String community_admin_name = null;
                String ias_admin_name = null;
                String region_name = null;

                try {
                    id = jsonObject.getString("id");
                    username = jsonObject.getString("username") == null ? null : jsonObject.getString("username");
                    fullname = jsonObject.getString("fullname") == null ? null : jsonObject.getString("fullname");
                    no_stnk = jsonObject.getString("no_stnk") == null ? null : jsonObject.getString("no_stnk");
                    chapter_name = jsonObject.getString("chapter_name") == null ? null : jsonObject.getString("chapter_name");
                    url_image = null != jsonObject.getString("url_image") ? jsonObject.getString("url_image") : null;
                    no_sim = jsonObject.getString("no_sim") == null ? null : jsonObject.getString("no_sim");
                    branch_name = jsonObject.getString("branch_name") == null ? null : jsonObject.getString("branch_name");
                    brand_name = jsonObject.getString("brand_name") == null ? null : jsonObject.getString("brand_name");
                    car_type_name = jsonObject.getString("car_type_name") == null ? null : jsonObject.getString("car_type_name");
                    year = jsonObject.getString("year") == null ? null : jsonObject.getString("year");
                    community_admin_name = jsonObject.getString("community_admin_name") == null ? null : jsonObject.getString("community_admin_name");
                    ias_admin_name = jsonObject.getString("ias_admin_name") == null ? null : jsonObject.getString("ias_admin_name");
                    region_name = jsonObject.getString("region_name") == null ? null : jsonObject.getString("region_name");
                } catch (Exception e) {
                    fullname = null;
                    Log.e("Catch", "Error");
                }

                if (null != fullname) {
                    finish();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Id, id);
                    editor.putString("username", username);
                    editor.putString(Fullname, fullname);
                    editor.putString(UrlImage, url_image);
                    editor.putString("no_stnk", no_stnk);
                    editor.putString("chapter_name", chapter_name);
                    editor.putString("no_sim", no_sim);
                    editor.putString("branch_name", branch_name);
                    editor.putString("brand_name", brand_name);
                    editor.putString("car_type_name", car_type_name);
                    editor.putString("year", year);
                    editor.putString("community_admin_name", community_admin_name);
                    editor.putString("ias_admin_name", ias_admin_name);
                    editor.putString("region_name", region_name);
                    editor.commit();
                    i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Username dan Password yang anda masukkan salah", Toast.LENGTH_LONG).show();

                }


    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

}

