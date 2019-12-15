package com.gfo.gfo_meesterproef.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gfo.gfo_meesterproef.LoginActivity;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.searchTool.lookUp.LookUpMeterActivity;
import com.gfo.gfo_meesterproef.searchTool.search.SearchMeterActivity;
import com.gfo.gfo_meesterproef.support.ConnectionCheck;
import com.gfo.gfo_meesterproef.user.fetchProductFile.FetchProductActivity;

public class UserActivity extends AppCompatActivity {

    Button fetchProductButton, searchMeterButton, lookUpMeterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        Fetch product, position: top
        fetchProductButton = findViewById(R.id.top);
        fetchProductButton.setText("Fetch Product");
        fetchProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
                Intent i = new Intent(UserActivity.this, FetchProductActivity.class);
                startActivity(i);
            }
        });
//        Search meter, position: bottom right
        searchMeterButton = findViewById(R.id.bottom_right);
        searchMeterButton.setText("Search Meter");
        searchMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, SearchMeterActivity.class);
                startActivity(i);
            }
        });
//        Look up meter, position: bottom left
        lookUpMeterButton = findViewById(R.id.bottom_left);
        lookUpMeterButton.setText("Look Up Meter");
        lookUpMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
                Intent i = new Intent(UserActivity.this, LookUpMeterActivity.class);
                startActivity(i);
            }
        });
    }//    end onCreate

    @Override//    confirmation for logout
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(UserActivity.this, LoginActivity.class);
                UserActivity.this.finish();
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}