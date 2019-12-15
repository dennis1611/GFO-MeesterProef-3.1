package com.gfo.gfo_meesterproef.user.fetchProductFile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.gfo.gfo_meesterproef.support.FolderAdapter;
import com.gfo.gfo_meesterproef.support.MasterBackgroundWorker;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.support.ConnectionCheck;
import com.gfo.gfo_meesterproef.user.UserActivity;

import java.util.List;

import static com.gfo.gfo_meesterproef.LoginActivity.contextOfLogin;

public class FetchProductActivity extends AppCompatActivity {

    GridView userProductGrid;
    String selectedProduct;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

//        setup ProgressBar
        progressBar = findViewById(R.id.progressBar);

//        get saved username
        SharedPreferences usernamePref = getSharedPreferences("usernamePreference", contextOfLogin.MODE_PRIVATE);
        String username = usernamePref.getString("username", "");

//        contact database
        MasterBackgroundWorker fetchProduct = new MasterBackgroundWorker(this, listener);
        fetchProduct.setProgressBar(progressBar);
        fetchProduct.execute("fetchProduct", username);}//        end method

//        create listener to wait for AsyncTask to finish
        MasterBackgroundWorker.OnTaskCompleted listener = new MasterBackgroundWorker.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(List<String> products) {
                //        fill gridView with (array)List
                userProductGrid = findViewById(R.id.grid);
                FolderAdapter productAdapter = new FolderAdapter(FetchProductActivity.this, products);
                userProductGrid.setAdapter(productAdapter);

                registerProductClickCallback();
            }
        };

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