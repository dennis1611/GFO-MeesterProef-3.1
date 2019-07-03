package com.gfo.gfo_meesterproef.Admin.AddAccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.gfo.gfo_meesterproef.Admin.AdminActivity;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

public class AddAccountActivity extends AppCompatActivity {

    EditText usernamecET, passwordcET, passConfirmET,emailcET;
    String usernamec, passwordc, emailc, adminflagc;
    CheckBox AdminCheck;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
//        connect views with id
        usernamecET = (EditText) findViewById(R.id.editTextUsername);
        passwordcET = (EditText) findViewById(R.id.editTextPassword);
        passConfirmET = (EditText) findViewById(R.id.editTextPasswordConfirm);
        emailcET = (EditText) findViewById(R.id.editTextEmail);
        AdminCheck = (CheckBox) findViewById(R.id.CheckboxAdmin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void addAccount(View view) {

//        check if username is not empty
        usernamec = usernamecET.getText().toString();
        if (TextUtils.isEmpty(usernamec)) {
            usernamecET.setError("Username can't be empty");
            return;
        }//        check if password is not empty
        passwordc = passwordcET.getText().toString();
        if (TextUtils.isEmpty(passwordc)) {
            passwordcET.setError("Password can't be empty");
            return;
        }//        check if passwords match
        String passwordConfirm = passConfirmET.getText().toString();
        if(passwordc.equals(passwordConfirm)){//            do nothing
        } else{
            passConfirmET.setError("Passwords do not match");
            return;
        }//        check if email is valid
        emailc = emailcET.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailc).matches()) {//            do nothing
        } else{
            emailcET.setError("Please enter a valid E-mail address");
            return;
        }//        check if admin checkbox is checked
        adminflagc = "n";
        if ((AdminCheck).isChecked()){
            adminflagc = "Y";
        }
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
//        sends data to backgroundWorker
        String type = "add_account";
        AddAccountBackgroundWorker addAccountBackgroundWorker = new AddAccountBackgroundWorker(this, listener);
        addAccountBackgroundWorker.setProgressBar(progressBar);
        addAccountBackgroundWorker.execute(type, usernamec, passwordc, emailc, adminflagc);
    }//    end method

//    create listener to wait for AsyncTask to finish
    AddAccountBackgroundWorker.OnTaskCompleted listener = new AddAccountBackgroundWorker.OnTaskCompleted() {
//        code below won't get executed until AsyncTask is finished
        @Override
        public void onTaskCompleted() {
            //        return to AdminActivity
            onBackPressed();
        }
    };

    public void reset () {//        reset editText
        ((EditText) findViewById(R.id.editTextUsername)).setText("");
        ((EditText) findViewById(R.id.editTextPassword)).setText("");
        ((EditText) findViewById(R.id.editTextEmail)).setText("");
        ((EditText) findViewById(R.id.editTextPasswordConfirm)).setText("");
        ((CheckBox) findViewById(R.id.CheckboxAdmin)).setChecked(false);
    }

    @Override
//    needed for icons in toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reset, menu);
        return true;
    }
    @Override
//    set onClick to icons in toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_item_reset) {
            reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed(){
        Intent i = new Intent(AddAccountActivity.this, AdminActivity.class);
        AddAccountActivity.this.finish();
        startActivity(i);
    }
}