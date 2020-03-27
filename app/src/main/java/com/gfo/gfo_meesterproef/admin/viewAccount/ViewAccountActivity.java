package com.gfo.gfo_meesterproef.admin.viewAccount;

import android.content.DialogInterface;
import android.content.Intent;
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
    AccountsFragment userFragment, adminFragment;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

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
//                loop through all account entries
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String username = obj.getString("username"),
                            password = obj.getString("password"),
                            email = obj.getString("email"),
                            type = obj.getString("adminflag");
//                    check type and add entry to corresponding ArrayList
                    switch (type) {
                        case "Y"://    admin
                            adminAccounts.add(new Account(username, password, email, true));
                            break;
                        case "n"://    user
                            userAccounts.add(new Account(username, password, email, false));
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + type);
                    }
                }

                //        needed for toolbar
                toolbar = findViewById(R.id.tabbedToolbar);
                setSupportActionBar(toolbar);
                viewPager = findViewById(R.id.viewPager);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                userFragment = new AccountsFragment();
                viewPagerAdapter.addFragment(userFragment, "Users");
                adminFragment = new AccountsFragment();
                viewPagerAdapter.addFragment(adminFragment, "Admins");

//                send data to fragments
                Bundle userBundle = new Bundle();
                userBundle.putParcelableArrayList("accounts", userAccounts);
                userFragment.setArguments(userBundle);
                Bundle adminBundle = new Bundle();
                adminBundle.putParcelableArrayList("accounts", adminAccounts);
                adminFragment.setArguments(adminBundle);

//                viewpager.setAdapter(...) must come after ...Fragment.setArguments(...)
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout = findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(viewPager);
            }
        };

//    gets called from within fragment when account is selected
    public void onSelect(final Account selectedAccount){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAccountActivity.this);
        builder.setTitle("Account: " + selectedAccount.getName());
        builder.setMessage("What would you like to do?");
//        option to edit account, for all accounts
        builder.setPositiveButton("Edit Account", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(ViewAccountActivity.this, EditAccountActivity.class);
                i.putExtra("selectedAccount", selectedAccount);
                ViewAccountActivity.this.startActivity(i);
            }
        });
//        option to show products, only needed for user-accounts
        if (!selectedAccount.getAdminFlag()){
        builder.setNegativeButton("Couple Account to Products", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
                Intent i = new Intent(ViewAccountActivity.this, CoupleToAccountActivity.class);
                i.putExtra("selectedUsername", selectedAccount.getName());
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