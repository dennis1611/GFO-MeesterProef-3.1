package com.gfo.gfo_meesterproef.admin.accountAction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gfo.gfo_meesterproef.admin.viewAccount.ViewAccountActivity;
import com.gfo.gfo_meesterproef.support.MasterBackgroundWorker;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.support.ConnectionCheck;

import java.util.List;

import static com.gfo.gfo_meesterproef.admin.viewAccount.ViewAccountActivity.contextOfViewAccount;

public class EditAccountActivity extends AppCompatActivity{

    String oldUsername, oldPassword, oldEmail;
    String newUsername, newPassword, newEmail;
    TextView usernameTV, passwordTV, emailTV;
    EditText usernameET, passwordET, emailET;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

//        get saved account values
        SharedPreferences selectedAccountPref = getSharedPreferences("selectedAccountPreference", contextOfViewAccount.MODE_PRIVATE);
        oldUsername = selectedAccountPref.getString("selectedUsername", "");
        oldPassword = selectedAccountPref.getString("selectedPassword", "");
        oldEmail = selectedAccountPref.getString("selectedEmail", "");
//        connect views with id
        usernameTV = findViewById(R.id.usernameTextView);
        passwordTV = findViewById(R.id.passwordTextView);
        emailTV = findViewById(R.id.emailTextView);
        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        emailET = findViewById(R.id.emailEditText);
        progressBar = findViewById(R.id.progressBar);
//        fill views
        usernameTV.setText(oldUsername);
        passwordTV.setText(oldPassword);
        emailTV.setText(oldEmail);
        usernameET.setText(oldUsername);
        passwordET.setText(oldPassword);
        emailET.setText(oldEmail);
    }

    public void editAccount(View view) {
//        check if username is not empty
        newUsername = usernameET.getText().toString();
        if (TextUtils.isEmpty(newUsername)) {
            usernameET.setError("Username can't be empty");
            return;
        }//        check if password is not empty
        newPassword = passwordET.getText().toString();
        if (TextUtils.isEmpty(newPassword)) {
            passwordET.setError("Password can't be empty");
            return;
        }//        check if email is valid
        newEmail = emailET.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            emailET.setError("Please enter a valid E-mail address");
            return;
        }
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
//        create alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);
        builder.setTitle("Edit Account?");
        builder.setMessage("Are you sure you want to overwrite " + oldUsername + "?");
//        confirm overwrite
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                edit account (if a value has changed)
                newUsername = usernameET.getText().toString();
                newPassword = passwordET.getText().toString();
                newEmail = emailET.getText().toString();
                if (newUsername.equals(oldUsername) && newPassword.equals(oldPassword) && newEmail.equals(oldEmail)) {
                    Toast.makeText(EditAccountActivity.this, "Values have not changed", Toast.LENGTH_SHORT).show();
                } else { editConfirmed(); }
            }
        });
//        cancel overwrite
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void editConfirmed() {
//        separate method needed because listener could't be recognized in OnClick
        MasterBackgroundWorker editAccount = new MasterBackgroundWorker(EditAccountActivity.this, editListener);
        editAccount.setProgressBar(progressBar);
        editAccount.execute("editAccount", oldUsername, newUsername, newPassword, newEmail); }//        end method
//    create listener to wait for AsyncTask to finish
    MasterBackgroundWorker.OnTaskCompleted editListener = new MasterBackgroundWorker.OnTaskCompleted() {
//        code below won't get executed until AsyncTask is finished
        @Override
        public void onTaskCompleted(List<String> resultList) {
//            close Activity
            Intent i = new Intent(EditAccountActivity.this, ViewAccountActivity.class);
            EditAccountActivity.this.finish();
            startActivity(i);
        }
    };

    public void deleteAccount() {
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
//        create alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);
        builder.setTitle("Delete Account?");
        builder.setMessage("Are you sure you want to delete " + oldUsername + "?");
//        confirm deletion
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { deleteConfirmed(); }
        });
//        cancel deletion
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void deleteConfirmed() {
//        separate method needed because listener could't be recognized in OnClick
        MasterBackgroundWorker deleteAccount = new MasterBackgroundWorker(EditAccountActivity.this, deleteListener);
        deleteAccount.setProgressBar(progressBar);
        deleteAccount.execute("deleteAccount", oldUsername); }//        end method
//        create listener to wait for AsyncTask to finish
        MasterBackgroundWorker.OnTaskCompleted deleteListener = new MasterBackgroundWorker.OnTaskCompleted() {
//        code below won't get executed until AsyncTask is finished
            @Override
            public void onTaskCompleted(List<String> resultList) {
//                close Activity
                Intent i = new Intent(EditAccountActivity.this, ViewAccountActivity.class);
                EditAccountActivity.this.finish();
                startActivity(i);
            }
        };

    @Override
//    needed for icons in toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }
    @Override
//    set onClick to icons in toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_item_delete) {
            deleteAccount();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
        Intent i = new Intent(EditAccountActivity.this, ViewAccountActivity.class);
        EditAccountActivity.this.finish();
        startActivity(i);
    }
}