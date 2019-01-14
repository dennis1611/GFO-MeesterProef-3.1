package com.gfo.gfo_meesterproef.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.OpenFileBackgroundWorker;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.gfo.gfo_meesterproef.LoginActivity.contextOfLogin;

public class FetchFileActivity extends AppCompatActivity {

    ListView userProductList;
    String clickedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

//        get selected product from FetchProductActivity
        String product = getIntent().getExtras().getString("userProduct", "");
//        get saved username and set label
        SharedPreferences usernamePref = getSharedPreferences("usernamePreference", contextOfLogin.MODE_PRIVATE);
        String username = usernamePref.getString("username", "");
        setTitle("Files in "+product);

//        contact database for files
        String type = "fetch";
        List<String> products = new ArrayList<String>();
        try {
            products = new FetchFile(this).execute(type, product).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        fill listView with List
        userProductList = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, products);
        userProductList.setAdapter(productAdapter);

        registerFileClickCallBack();
    }
    private void registerFileClickCallBack() {
        userProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                get selected file
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
                TextView textView = (TextView) viewClicked;
                clickedFile = textView.getText().toString();
//                contact database for files
                String url = null;
                try{
                    url = new OpenFileBackgroundWorker(FetchFileActivity.this).execute("view", clickedFile).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace(); }
//                open file
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse(url));
                startActivity(web);
            }


        });
    }
    @Override
    public void onBackPressed(){
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
        Intent i = new Intent(FetchFileActivity.this, FetchProductActivity.class);
        FetchFileActivity.this.finish();
        startActivity(i);
    }
}
