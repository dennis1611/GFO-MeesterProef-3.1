package com.gfo.gfo_meesterproef.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gfo.gfo_meesterproef.Custom.FolderAdapter;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.gfo.gfo_meesterproef.LoginActivity.contextOfLogin;

public class FetchProductActivity extends AppCompatActivity {

    GridView userProductGrid;
    String selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
//        get saved username
        SharedPreferences usernamePref = getSharedPreferences("usernamePreference", contextOfLogin.MODE_PRIVATE);
        String username = usernamePref.getString("username", "");

//        contact database
        String type = "fetch";
        List<String> products = new ArrayList<String>();
        try {
            products = new FetchProduct(this).execute(type, username).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        fill listView with (array)List
        userProductGrid = (GridView) findViewById(R.id.grid);
        FolderAdapter productAdapter = new FolderAdapter(this, products);
        userProductGrid.setAdapter(productAdapter);

        registerProductClickCallback();
    }

    private void registerProductClickCallback() {
        userProductGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
//                get selected product
                selectedProduct = (String) userProductGrid.getItemAtPosition(position);
//                start FetchFileActivity
                Intent i = new Intent(FetchProductActivity.this, FetchFileActivity.class);
                i.putExtra("userProduct", selectedProduct);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(FetchProductActivity.this, UserActivity.class);
        FetchProductActivity.this.finish();
        startActivity(i);
    }
}