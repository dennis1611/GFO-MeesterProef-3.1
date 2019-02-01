package com.gfo.gfo_meesterproef.Admin.ViewFiles.CoupleToProduct;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.gfo.gfo_meesterproef.Admin.ViewFiles.ViewProductActivity.contextOfViewProduct;

import com.gfo.gfo_meesterproef.Admin.Couple;
import com.gfo.gfo_meesterproef.Admin.Uncouple;
import com.gfo.gfo_meesterproef.Admin.ViewFiles.ViewProductActivity;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoupleToProductActivity extends AppCompatActivity {

    String product;
    List<String> totalList, alreadyCoupled, toCouple, toUncouple;
    ListView list;
    String allUsernames;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
//        connect views with id
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        reset toCouple and toUncouple
        toCouple = new ArrayList<String>();
        toCouple.clear();
        toUncouple = new ArrayList<String>();
        toUncouple.clear();
//        get selected product
        SharedPreferences selectedProductPref = getSharedPreferences("selectedProductPreference", contextOfViewProduct.MODE_PRIVATE);
        product = selectedProductPref.getString("selectedProduct", "");
//        change label
        setTitle("Couple to "+product);

//        get all usernames as String
        totalList = new ArrayList<>();
        try {
            allUsernames = new AllAccounts(this).execute("userUsername").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }//                convert allUsernames String to List<String>
        String[] allUsernamesArray = allUsernames.split(",");
        totalList = (Arrays.asList(allUsernamesArray));

//        get already coupled usernames
        alreadyCoupled = new ArrayList<>();
        try {
            alreadyCoupled = new CoupledAccount(this).execute("coupledAccount", product).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        display all usernames
        list = (ListView) findViewById(R.id.list);
        list.setBackgroundResource(R.color.white);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, totalList){
//            2 Overrides below prevent View recycling
            @Override public int getViewTypeCount() { return getCount(); }
            @Override public int getItemViewType(int position) { return position; }
        };
        list.setAdapter(listAdapter);

//        compare total list and already coupled list
        list.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < totalList.size(); i++) {
            if (alreadyCoupled.contains(totalList.get(i))) {
                list.getChildAt(i).setBackgroundResource(R.color.blue);
                list.getChildAt(i).setTag(R.color.blue);
            }
        }
            }
        });

        registerNameClickCallBack();
    }

    private void registerNameClickCallBack() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                get username of clicked view
                TextView textView = (TextView) viewClicked;
                String accountName = textView.getText().toString();
//                get color tag and add or delete from to (un)couple list
                if (viewClicked.getTag()!=null){//                    if a tag has been set
                    int ColorId = Integer.parseInt(viewClicked.getTag().toString());
                    if (ColorId==R.color.blue){
                        viewClicked.setBackgroundResource(R.color.red);
                        viewClicked.setTag(R.color.red);
                        toUncouple.add(accountName);
                    } else if (ColorId==R.color.red){
                        viewClicked.setBackgroundResource(R.color.blue);
                        viewClicked.setTag(R.color.blue);
                        toUncouple.remove(accountName);
                    } else if (ColorId==R.color.green){
                        viewClicked.setBackgroundResource(R.color.white);
                        viewClicked.setTag(R.color.white);
                        toCouple.remove(accountName);
                    } else if (ColorId==R.color.white){
                        viewClicked.setBackgroundResource(R.color.green);
                        viewClicked.setTag(R.color.green);
                        toCouple.add(accountName);
                    }
                } else {//                    needed because there is no default tag (done to increase performance)
                    list.getChildAt(position).setTag(R.color.white);
                    viewClicked.setBackgroundResource(R.color.green);
                    viewClicked.setTag(R.color.green);
                    toCouple.add(accountName);
                }
            }
        });
    }

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
            couple();
            uncouple();
            Intent i = new Intent(CoupleToProductActivity.this, ViewProductActivity.class);
            i.putExtra("adminProduct", product);
            CoupleToProductActivity.this.finish();
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void couple() {
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
//        couple all selected accounts
        Iterator<String> toCoupleIterator = toCouple.iterator();
        while (toCoupleIterator.hasNext()) {
            String name = toCoupleIterator.next();
            Couple couple = new Couple(this);
            couple.setProgressBar(progressBar);
            try {
                String echo = couple.execute("couple", name, product).get();
                Toast.makeText(this, echo, Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    private void uncouple() {
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
//        uncouple all selected accounts
        Iterator<String> toUncoupleIterator = toUncouple.iterator();
        while (toUncoupleIterator.hasNext()) {
            String name = toUncoupleIterator.next();
            Uncouple uncouple = new Uncouple(this);
            uncouple.setProgressBar(progressBar);
            try {
                String echo = uncouple.execute("uncouple", name, product).get();
                Toast.makeText(this, echo, Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
        Intent i = new Intent(CoupleToProductActivity.this, ViewProductActivity.class);
        CoupleToProductActivity.this.finish();
        startActivity(i);
    }
}