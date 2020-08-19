package com.gfo.gfo_meesterproef.admin.link;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gfo.gfo_meesterproef.admin.viewAccount.ViewAccountActivity;
import com.gfo.gfo_meesterproef.admin.viewAccount.ViewPagerAdapter;
import com.gfo.gfo_meesterproef.support.MasterBackgroundWorker;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.support.ConnectionCheck;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CoupleToAccountActivity extends AppCompatActivity {

    String username;
    ArrayList<String> totalList = new ArrayList<>(),
            alreadyCoupled = new ArrayList<>();
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    LinkFragment linkFragment;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment);
//        connect views with id
        progressBar = findViewById(R.id.progressBar);
//        get selected username
        username = getIntent().getStringExtra("selectedUsername");
//        change label
        setTitle("Couple to " + username);

//        get all products (in group)
        MasterBackgroundWorker allProducts = new MasterBackgroundWorker(CoupleToAccountActivity.this, totalListener);
        allProducts.setProgressBar(progressBar);
        allProducts.execute("allProducts");}//        end method

//    create totalListener to wait for AsyncTask to finish
    MasterBackgroundWorker.OnTaskCompleted totalListener = new MasterBackgroundWorker.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String result) throws JSONException {
//            convert (JSON) String result to ArrayList<> totalList
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                totalList.add(jsonArray.getString(i));
            }

//            get already coupled products
            MasterBackgroundWorker coupledProducts = new MasterBackgroundWorker(CoupleToAccountActivity.this, coupledListener);
            coupledProducts.setProgressBar(progressBar);
            coupledProducts.execute("coupledProducts", username); }
    };//        end totalListener

//    create coupledListener to wait for AsyncTask to finish
    MasterBackgroundWorker.OnTaskCompleted coupledListener = new MasterBackgroundWorker.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String result) throws JSONException {
//            convert (JSON) String result to ArrayList<> alreadyCoupled
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                alreadyCoupled.add(jsonArray.getString(i));
            }

//            setup ViewPager, ViewPagerAdapter and LinkFragment
            viewPager = findViewById(R.id.viewPager);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            linkFragment = new LinkFragment();
            viewPagerAdapter.addFragment(linkFragment, "Link");
//            send data to fragment
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("totalList", totalList);
            bundle.putStringArrayList("alreadyCoupled", alreadyCoupled);
            linkFragment.setArguments(bundle);
//            viewpager.setAdapter(...) must come after ...Fragment.setArguments(...)
            viewPager.setAdapter(viewPagerAdapter);
        }
    };

    @Override
//    needed for icon in toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.link, menu);
        return true;
    }
    @Override
//    set onClick to icons in toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_item_link) {
            link();
            Intent i = new Intent(CoupleToAccountActivity.this, ViewAccountActivity.class);
            CoupleToAccountActivity.this.finish();
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void link() {
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
//        get toCouple and toUncouple from LinkFragment
        ArrayList<String> toCouple = linkFragment.toCouple;
        ArrayList<String> toUncouple = linkFragment.toUncouple;
//        create JSON arrays from to(Un)Couple products, saved as String
        String toCoupleJSON = new JSONArray(toCouple).toString(),
                toUncoupleJSON = new JSONArray(toUncouple).toString();
//        contact database
        MasterBackgroundWorker masterBackgroundWorker = new MasterBackgroundWorker(this, listener);
        masterBackgroundWorker.setProgressBar(progressBar);
        masterBackgroundWorker.execute("linkToAccount", username, toCoupleJSON, toUncoupleJSON);
    }//    end method
//    create listener to wait for AsyncTask to finish
    MasterBackgroundWorker.OnTaskCompleted listener = new MasterBackgroundWorker.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String result) {
            Toast.makeText(CoupleToAccountActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
        Intent i = new Intent(CoupleToAccountActivity.this, ViewAccountActivity.class);
        CoupleToAccountActivity.this.finish();
        startActivity(i);
    }
}