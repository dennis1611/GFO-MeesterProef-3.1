package com.gfo.gfo_meesterproef.Admin.ViewFiles.CoupleToProduct;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.gfo.gfo_meesterproef.Admin.ViewFiles.ViewProductActivity.contextOfViewProduct;

import com.gfo.gfo_meesterproef.Admin.Couple;
import com.gfo.gfo_meesterproef.Admin.Uncouple;
import com.gfo.gfo_meesterproef.Admin.ViewFiles.ViewProductActivity;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoupleToProductActivity extends AppCompatActivity {

    String product;
    List<String> totalList, alreadyCoupled, toCouple, toUncouple;
    ListView list;
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

//        get all usernames as (Array)List
        AllAccounts allAccounts = new AllAccounts(CoupleToProductActivity.this, totalListener);
        allAccounts.setProgressBar(progressBar);
        allAccounts.execute("allAccounts");}//        end method

//    create totalListener to wait for AsyncTask to finish
    AllAccounts.OnTaskCompleted totalListener = new AllAccounts.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(List<String> splitResultList) {
//            get already coupled usernames
            totalList = splitResultList;
            CoupledAccount coupledAccount = new CoupledAccount(CoupleToProductActivity.this, coupledListener );
            coupledAccount.setProgressBar(progressBar);
            coupledAccount.execute("coupledAccount", product); }
    };//    end totalListener

//    create coupledListener to wait for AsyncTask to finish
    CoupledAccount.OnTaskCompleted coupledListener = new CoupledAccount.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(final List<String> splitResultList) {
            //        display all usernames
            alreadyCoupled = splitResultList;
            list = (ListView) findViewById(R.id.list);
            list.setBackgroundResource(R.color.white);
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(CoupleToProductActivity.this, android.R.layout.simple_list_item_1, totalList){
                @NonNull @Override
//            create view correctly (even when recycled)
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View row = convertView;
//                Check if an existing view is being reused, otherwise inflate the view
                    if (row==null){ row = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false); }
//              handle already coupled items and toUncouple items
                    if (alreadyCoupled.contains(totalList.get(position))){
//                  compare row with toUncouple. If it contains, make red
                        if (toUncouple.contains(totalList.get(position))){
                            row.setBackgroundResource(R.color.red);
                            row.setTag(R.color.red);
//                  ... if not, make blue
                        } else { row.setBackgroundResource(R.color.blue);
                            row.setTag(R.color.blue); }
//              handle not yet coupled items and toCouple items
                    } else {
//                  compare row with toCouple. If it contains, make green
                        if (toCouple.contains(totalList.get(position))){
                            row.setBackgroundResource(R.color.green);
                            row.setTag(R.color.green);
//                  ... if not, make white
                        } else { row.setBackgroundResource(R.color.white);
                            row.setTag(R.color.white); }
                    }
//                set text to each row
                    String currentProduct = getItem(position);
                    TextView productTextView = (TextView) row;
                    productTextView.setText(currentProduct);
                    return row; }
            };
            list.setAdapter(listAdapter);

            registerNameClickCallBack();
        }
    };

    private void registerNameClickCallBack() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                get username of clicked view
                TextView textView = (TextView) viewClicked;
                String accountName = textView.getText().toString();
//                get color tag and add or delete from to (un)couple list
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
                List<String> echo = couple.execute("couple", name, product).get();
            }
            catch (InterruptedException e) { e.printStackTrace(); }
            catch (ExecutionException e) { e.printStackTrace(); }
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
                List<String> echo = uncouple.execute("uncouple", name, product).get();
            }
            catch (InterruptedException e) { e.printStackTrace(); }
            catch (ExecutionException e) { e.printStackTrace(); }
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