package com.gfo.gfo_meesterproef.User.FetchProductFile;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.Support.MasterBackgroundWorker;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

import java.util.List;

public class FetchFileActivity extends AppCompatActivity {

    ListView userProductList;
    String clickedFile;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

//        setup ProgressBar
        progressBar = findViewById(R.id.progressBar);

//        get selected product from FetchProductActivity
        String product = getIntent().getExtras().getString("userProduct", "");
//        get saved username and set label
//        SharedPreferences usernamePref = getSharedPreferences("usernamePreference", contextOfLogin.MODE_PRIVATE);
//        String username = usernamePref.getString("username", "");
        setTitle("Files in "+product);

//        contact database for files
        MasterBackgroundWorker fetchFile = new MasterBackgroundWorker(this, listener);
        fetchFile.setProgressBar(progressBar);
        fetchFile.execute("fetchFile", product);}//        end method

//    create listener to wait for AsyncTask to finish
    MasterBackgroundWorker.OnTaskCompleted listener = new MasterBackgroundWorker.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(List<String> files) {
            //        fill listView with List
            userProductList = findViewById(R.id.list);
            ArrayAdapter<String> productAdapter = new ArrayAdapter<>(FetchFileActivity.this, android.R.layout.simple_list_item_1, files);
            userProductList.setAdapter(productAdapter);

            registerFileClickCallBack();
        }
    };

    private void registerFileClickCallBack() {
        userProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {

                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
                //                get selected file
                TextView textView = (TextView) viewClicked;
                clickedFile = textView.getText().toString();

//                contact database for url
                MasterBackgroundWorker openFileBackgroundWorker = new MasterBackgroundWorker(FetchFileActivity.this, listener);
                openFileBackgroundWorker.setProgressBar(progressBar);
                openFileBackgroundWorker.execute("openFile", clickedFile);}//                end method

//            create listener to wait for AsyncTask to finish
            MasterBackgroundWorker.OnTaskCompleted listener = new MasterBackgroundWorker.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(List<String> resultList) {
                    //                open file
                    String url = resultList.get(0);
                    Intent web = new Intent(Intent.ACTION_VIEW);
                    web.setData(Uri.parse(url));
                    startActivity(web); }
            };
        });
    }
    @Override
    public void onBackPressed(){
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
        Intent i = new Intent(FetchFileActivity.this, FetchProductActivity.class);
        FetchFileActivity.this.finish();
        startActivity(i); }
}