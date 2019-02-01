package com.gfo.gfo_meesterproef.Admin.ViewAccount.CoupleToAccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.gfo.gfo_meesterproef.Admin.Couple;
import com.gfo.gfo_meesterproef.Admin.Uncouple;
import com.gfo.gfo_meesterproef.Admin.ViewAccount.ViewAccountActivity;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;
import com.gfo.gfo_meesterproef.Support.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.gfo.gfo_meesterproef.Admin.ViewAccount.ViewAccountActivity.contextOfViewAccount;

public class CoupleToAccountActivity extends AppCompatActivity {

    String username;
    List<String> totalList, alreadyCoupled, toCouple, toUncouple;
    ListView list;
    ProgressBar progressBar;
    String allProducts;

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
//        get selected username
        SharedPreferences selectedAccountPref = getSharedPreferences("selectedAccountPreference", contextOfViewAccount.MODE_PRIVATE);
        username = selectedAccountPref.getString("selectedUsername", "");
//        change label
        setTitle("Couple to " + username);

//        get all products (in group)
        totalList = new ArrayList<>();
            try {
                allProducts = new AllProducts(this).execute("view").get(); }
                catch (InterruptedException e) { e.printStackTrace(); }
                catch (ExecutionException e) { e.printStackTrace(); }
            //        convert allProducts String to List<String>
        String[] splitResultArray = allProducts.split(",");
        totalList = (Arrays.asList(splitResultArray));
//            Converter converter = new Converter();
//            totalList = converter.splitStringToList(rawTotalList, ",");

//        get already coupled products
        alreadyCoupled = new ArrayList<>();
        try {
            alreadyCoupled = new CoupledProduct(this).execute("coupledProduct", username).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        display all products
        list = (ListView) findViewById(R.id.list);
        list.setBackgroundResource(R.color.white);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, totalList){
//            2 Overrides below prevent View recycling
            @Override public int getViewTypeCount() { return getCount(); }
            @Override public int getItemViewType(int position) { return position; }
//            @NonNull @Override
//            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
////                Check if an existing view is being reused, otherwise inflate the view
//                View listItemView = convertView;
//                if (listItemView==null){
//                    listItemView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
//                }
//                if (listItemView.getTag()!=null) {
//                    int ColorId = Integer.parseInt(listItemView.getTag().toString());
//                    if (ColorId == R.color.blue) { listItemView.setBackgroundResource(R.color.blue); }
//                    if (ColorId == R.color.red) { listItemView.setBackgroundResource(R.color.red); }
//                    if (ColorId == R.color.green) { listItemView.setBackgroundResource(R.color.green); }
//                    if (ColorId == R.color.white) { listItemView.setBackgroundResource(R.color.white); }
//                }
//                 else {listItemView.setBackgroundResource(R.color.white);}
//                return super.getView(position, listItemView, parent);
//            }
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
        registerProductClickCallBack();
    }

    private void registerProductClickCallBack() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                get productname of clicked view
                TextView textView = (TextView) viewClicked;
                String product = textView.getText().toString();
//                add or delete from to (un)couple list
                if (viewClicked.getTag()!=null){//                    if a tag has been set
                    int ColorId = Integer.parseInt(viewClicked.getTag().toString());
                    if (ColorId==R.color.blue){
                        viewClicked.setBackgroundResource(R.color.red);
                        viewClicked.setTag(R.color.red);
                        toUncouple.add(product);
                    } else if (ColorId==R.color.red){
                        viewClicked.setBackgroundResource(R.color.blue);
                        viewClicked.setTag(R.color.blue);
                        toUncouple.remove(product);
                    } else if (ColorId==R.color.green){
                        viewClicked.setBackgroundResource(R.color.white);
                        viewClicked.setTag(R.color.white);
                        toCouple.remove(product);
                    } else if (ColorId==R.color.white){
                        viewClicked.setBackgroundResource(R.color.green);
                        viewClicked.setTag(R.color.green);
                        toCouple.add(product);
                    }
                } else {//                    needed because there is no default tag (done to increase performance)
                    list.getChildAt(position).setTag(R.color.white);
                    viewClicked.setBackgroundResource(R.color.green);
                    viewClicked.setTag(R.color.green);
                    toCouple.add(product);
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
            Intent i = new Intent(CoupleToAccountActivity.this, ViewAccountActivity.class);
            CoupleToAccountActivity.this.finish();
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void couple() {
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
//        couple all selected products
        Iterator<String> toCoupleIterator = toCouple.iterator();
        while (toCoupleIterator.hasNext()) {
            String product = toCoupleIterator.next();
            Couple couple = new Couple(this);
            couple.setProgressBar(progressBar);
            try {
                String echo = couple.execute("couple", username, product).get();
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
//        uncouple all selected products
        Iterator<String> toUncoupleIterator = toUncouple.iterator();
        while (toUncoupleIterator.hasNext()) {
            String product = toUncoupleIterator.next();
            Uncouple uncouple = new Uncouple(this);
            uncouple.setProgressBar(progressBar);
            try {
                String echo = uncouple.execute("uncouple", username, product).get();
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
        Intent i = new Intent(CoupleToAccountActivity.this, ViewAccountActivity.class);
        CoupleToAccountActivity.this.finish();
        startActivity(i);
    }
}