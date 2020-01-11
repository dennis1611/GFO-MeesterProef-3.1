package com.gfo.gfo_meesterproef.admin.viewAccount;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.gfo.gfo_meesterproef.admin.AdminActivity;
import com.gfo.gfo_meesterproef.admin.link.CoupleToAccountActivity;
import com.gfo.gfo_meesterproef.admin.accountAction.EditAccountActivity;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.support.ConnectionCheck;
import com.gfo.gfo_meesterproef.support.JSONBackgroundWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewAccountActivity extends AppCompatActivity{

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    UserAccountsFragment userFragment;
    AdminAccountsFragment adminFragment;
    ProgressBar progressBar;

    public static Context contextOfViewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

//        needed to save selectedUsername
        contextOfViewAccount = getApplicationContext();
//        setup ProgressBar
        progressBar = findViewById(R.id.progressBar);

//        get all accounts in JSON format as String
        JSONBackgroundWorker jsonBackgroundWorker = new JSONBackgroundWorker(this, listener);
        jsonBackgroundWorker.setProgressBar(progressBar);
        jsonBackgroundWorker.execute("getAccounts");}//        end method

//    create listener to wait for AsyncTask to finish
        JSONBackgroundWorker.OnTaskCompleted listener = new JSONBackgroundWorker.OnTaskCompleted() {
//    code below won't get executed until AsyncTask is finished
            @Override
            public void onTaskCompleted(String result) throws JSONException {

//                create two ArrayList<Account> out of JSON string result
                ArrayList<Account> userAccounts = new ArrayList<>(),
                        adminAccounts = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String username = obj.getString("username"),
                            password = obj.getString("password"),
                            email = obj.getString("email"),
                            type = obj.getString("adminflag");
                    Account newAccount = new Account(username, password, email);
                    if (type.equals("n")) { userAccounts.add(newAccount); }
                    else if (type.equals("Y")) { adminAccounts.add(newAccount); }
                }

                //        needed for toolbar
                toolbar = findViewById(R.id.tabbedToolbar);
                setSupportActionBar(toolbar);
                viewPager = findViewById(R.id.viewPager);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                userFragment = new UserAccountsFragment();
                viewPagerAdapter.addFragment(userFragment, "Users");
                adminFragment = new AdminAccountsFragment();
                viewPagerAdapter.addFragment(adminFragment, "Admins");

//                send data to fragments
                Bundle userBundle = new Bundle();
                userBundle.putParcelableArrayList("userAccounts", userAccounts);
                userFragment.setArguments(userBundle);
                Bundle adminBundle = new Bundle();
                adminBundle.putParcelableArrayList("adminAccounts", adminAccounts);
                adminFragment.setArguments(adminBundle);

//                viewpager.setAdapter(...) must come after ...Fragment.setArguments(...)
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout = findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(viewPager);
            }
        };

//    gets called from within fragment
    public void onSelect(String selectedUsername, String selectedPassword, String selectedEmail, String type){
        //                save selected username, password and email
        SharedPreferences selectedAccountPref = getSharedPreferences("selectedAccountPreference", contextOfViewAccount.MODE_PRIVATE);
        selectedAccountPref.edit().putString("selectedUsername", selectedUsername).apply();
        selectedAccountPref.edit().putString("selectedPassword", selectedPassword).apply();
        selectedAccountPref.edit().putString("selectedEmail", selectedEmail).apply();

        //                choice to edit account or couple products
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAccountActivity.this);
        builder.setTitle(selectedUsername);
//                builder.setMessage("What do you want to do?");
        builder.setPositiveButton("Edit Account", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(ViewAccountActivity.this, EditAccountActivity.class);
                ViewAccountActivity.this.startActivity(i);
            }
        });
//        no need to show (all) products to an admin
        if (type.equals("user")){
        builder.setNegativeButton("Couple Account to Products", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
                Intent i = new Intent(ViewAccountActivity.this, CoupleToAccountActivity.class);
                startActivity(i);
            }
        });}
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(ViewAccountActivity.this, AdminActivity.class);
        ViewAccountActivity.this.finish();
        startActivity(i);
    }
}