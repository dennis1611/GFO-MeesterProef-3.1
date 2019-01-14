package com.gfo.gfo_meesterproef.Admin.ViewFiles;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.OpenFileBackgroundWorker;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewFileActivity extends AppCompatActivity {

    public static Context fileContext;

    String clickedFile;
    ListView adminProductList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        fileContext = getApplicationContext();

//        setup ProgressBar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//        get selected product from ViewProductActivity
        String product = getIntent().getExtras().getString("adminProduct", "");

//        set label
        setTitle("View Files from "+product);

//        contact database for files
        String type = "view";
        List<String> files = new ArrayList<>();
        ViewFile viewFile = new ViewFile(this);
        viewFile.setProgressBar(progressBar);
        try {
            files = new ViewFile(this).execute(type, product).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // fill listView with List
        adminProductList = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, files);
        adminProductList.setAdapter(productAdapter);

        registerFileClickCallBack();
    }

    private void registerFileClickCallBack() {
        adminProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                //        check for internet connection
                boolean connection = new ConnectionCheck().test(getApplicationContext());
                if (!connection){return;}
//                get selected file
                TextView textView = (TextView) viewClicked;
                clickedFile = textView.getText().toString();
//                contact database to open file
                String url = null;
                try{
                    url = new OpenFileBackgroundWorker(ViewFileActivity.this).execute("view", clickedFile).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace(); }
//                        open file
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse(url));
                startActivity(web);
            }
        });
    }
    @Override
    public void onBackPressed() {
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
        Intent i = new Intent(ViewFileActivity.this, ViewProductActivity.class);
        ViewFileActivity.this.finish();
        startActivity(i);
    }
}
