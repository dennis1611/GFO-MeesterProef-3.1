package com.gfo.gfo_meesterproef.user.fetchProductFile;

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

import com.gfo.gfo_meesterproef.support.MasterBackgroundWorker;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.support.ConnectionCheck;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FetchFileActivity extends AppCompatActivity {

    String clickedFile;
    ArrayList<String> files;
    ListView userProductList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

//        setup ProgressBar
        progressBar = findViewById(R.id.progressBar);

//        get selected product from FetchProductActivity
        String product = getIntent().getExtras().getString("userProduct", "");
//        set label
        setTitle("Files in "+product);

//        contact database for files
        MasterBackgroundWorker fetchFile = new MasterBackgroundWorker(this, listener);
        fetchFile.setProgressBar(progressBar);
        fetchFile.execute("fetchFile", product);}//        end method

//    create listener to wait for AsyncTask to finish
    MasterBackgroundWorker.OnTaskCompleted listener = new MasterBackgroundWorker.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String result) throws JSONException {
//            convert (JSON) String result to ArrayList<> files
            files = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                files.add(jsonArray.getString(i));
            }

            //        fill listView with (Array)List
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
                public void onTaskCompleted(String url) {
                    //                open file
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