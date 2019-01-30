package com.gfo.gfo_meesterproef.Admin.ViewAccount;

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
import android.widget.ListView;
import android.widget.ProgressBar;

import com.gfo.gfo_meesterproef.Admin.AdminActivity;
import com.gfo.gfo_meesterproef.Admin.ViewAccount.CoupleToAccount.CoupleToAccountActivity;
import com.gfo.gfo_meesterproef.Admin.ViewAccount.EditAccount.EditAccountActivity;
import com.gfo.gfo_meesterproef.Custom.AdminAccountsFragment;
import com.gfo.gfo_meesterproef.Custom.UserAccountsFragment;
import com.gfo.gfo_meesterproef.Custom.ViewPagerAdapter;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//        get 6 separate account value lists (not separated, contained in parent list) from database
        ViewAccountBackgroundWorker viewAccountBackgroundWorker = new ViewAccountBackgroundWorker(this, listener);
        viewAccountBackgroundWorker.setProgressBar(progressBar);
        viewAccountBackgroundWorker.execute("all");}//        end method

//    create listener to wait for AsyncTask to finish
        ViewAccountBackgroundWorker.OnTaskCompleted listener = new ViewAccountBackgroundWorker.OnTaskCompleted() {
//    code below won't get executed until AsyncTask is finished
            @Override
            public void onTaskCompleted(ArrayList<String> resultList) {
//                get  value lists (not separated) from parent list
                String userUsernames = resultList.get(0);
                String userPasswords = resultList.get(1);
                String userEmails = resultList.get(2);
                String adminUsernames = resultList.get(3);
                String adminPasswords = resultList.get(4);
                String adminEmails = resultList.get(5);

                //        needed for toolbar
                toolbar = (Toolbar) findViewById(R.id.tabbedToolbar);
                setSupportActionBar(toolbar);
                viewPager = (ViewPager) findViewById(R.id.viewPager);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                userFragment = new UserAccountsFragment();
                viewPagerAdapter.addFragment(userFragment, "Users");
                adminFragment = new AdminAccountsFragment();
                viewPagerAdapter.addFragment(adminFragment, "Admins");

//                send data to fragments
                Bundle userBundle = new Bundle();
                userBundle.putString("userUsernames", userUsernames);
                userBundle.putString("userPasswords", userPasswords);
                userBundle.putString("userEmails", userEmails);
                userFragment.setArguments(userBundle);
                Bundle adminBundle = new Bundle();
                adminBundle.putString("adminUsernames", adminUsernames);
                adminBundle.putString("adminPasswords", adminPasswords);
                adminBundle.putString("adminEmails", adminEmails);
                adminFragment.setArguments(adminBundle);

//                viewpager.setAdapter(...) must come after ...Fragment.setArguments(...)
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout = (TabLayout) findViewById(R.id.tabLayout);
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