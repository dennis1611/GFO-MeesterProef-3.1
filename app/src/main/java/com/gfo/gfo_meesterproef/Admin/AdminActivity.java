package com.gfo.gfo_meesterproef.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gfo.gfo_meesterproef.Admin.AddAccount.AddAccountActivity;
import com.gfo.gfo_meesterproef.Admin.ViewAccount.ViewAccountActivity;
import com.gfo.gfo_meesterproef.Admin.ViewFiles.ViewProductActivity;
import com.gfo.gfo_meesterproef.LoginActivity;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

public class AdminActivity extends AppCompatActivity {
    Button addAccountButton, viewProductButton, viewAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        Add account, position: top
        addAccountButton = findViewById(R.id.top);
        addAccountButton.setText("Add Account");
        addAccountButton.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, AddAccountActivity.class);
                startActivity(i);
            }
        });
//        View product, position: bottom right
        viewProductButton = findViewById(R.id.bottom_right);
        viewProductButton.setText("View Product");
        viewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
                Intent i = new Intent(AdminActivity.this, ViewProductActivity.class);
                startActivity(i);
            }
        });
//        View account, position: bottom left
        viewAccountButton = findViewById(R.id.bottom_left);
        viewAccountButton.setText("View Account");
        viewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
                Intent i = new Intent(AdminActivity.this, ViewAccountActivity.class);
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
                Intent i = new Intent(AdminActivity.this, LoginActivity.class);
                AdminActivity.this.finish();
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