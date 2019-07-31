package com.gfo.gfo_meesterproef;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.gfo.gfo_meesterproef.Admin.AdminActivity;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;
import com.gfo.gfo_meesterproef.User.UserActivity;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    EditText usernameET, passwordET;
    ProgressBar progressBar;
    public static Context contextOfLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        connect views with id
        usernameET = (EditText) findViewById(R.id.editTextUser);
        passwordET = (EditText) findViewById(R.id.editTextPass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        needed to save username
        contextOfLogin = getApplicationContext();
    }

//    shortcut to select user account
    public void setUser(View view){ usernameET.setText("user");
        passwordET.setText("user"); }
//    shortcut to select admin account
    public void setAdmin(View view){ usernameET.setText("admin");
        passwordET.setText("admin"); }

    public void login(View view) {
//        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection) { return; }
//        check if username is not empty
        String username = usernameET.getText().toString();
        if (TextUtils.isEmpty(username)) {
            usernameET.setError("Please enter a username");
            return;
        }//        check if password is not empty
        String password = passwordET.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Please enter a password");
            return;
        }//        contact database
        MasterBackgroundWorker loginBackgroundWorker = new MasterBackgroundWorker(this, listener);
        loginBackgroundWorker.setProgressBar(progressBar);
        loginBackgroundWorker.execute("login", username, password);
//        save username
        SharedPreferences usernamePref = getSharedPreferences("usernamePreference", contextOfLogin.MODE_PRIVATE);
        usernamePref.edit().putString("username", username).apply(); }//        end method

//    create listener to wait for AsyncTask to finish
        MasterBackgroundWorker.OnTaskCompleted listener = new MasterBackgroundWorker.OnTaskCompleted() {
//        code below won't get executed until AsyncTask is finished
            @Override
            public void onTaskCompleted(List<String> resultList) {
                String adminFlag = resultList.get(0);
                //        check adminFlag and clear EditTexts
                switch (adminFlag) {
                    case "Y"://    admin
                        usernameET.getText().clear();
                        passwordET.getText().clear();
                        Intent i = new Intent(contextOfLogin, AdminActivity.class);
                        LoginActivity.this.startActivity(i);
                        break;
                    case "n"://    user
                        usernameET.getText().clear();
                        passwordET.getText().clear();
                        Intent k = new Intent(contextOfLogin, UserActivity.class);
                        LoginActivity.this.startActivity(k);
                        break;
                    default:
                        Toast.makeText(contextOfLogin,
                                adminFlag, Toast.LENGTH_LONG).show();
                        break; }
            }
        };
}