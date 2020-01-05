package com.gfo.gfo_meesterproef.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONBackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    private OnTaskCompleted listener;
    public JSONBackgroundWorker(Context ctx, OnTaskCompleted listener){
        context = ctx;
        this.listener = listener;
    }
    //    get access to ProgressBar in activity
    @SuppressLint("StaticFieldLeak") private ProgressBar progressBar;
    public void setProgressBar(ProgressBar progressBar) { this.progressBar = progressBar; }
    //    create interface to communicate with Activity
    public interface OnTaskCompleted{ void onTaskCompleted(String result) throws JSONException;}

    @Override
    protected String doInBackground(String... params) {
        String task = params[0];
        String php_url = null;
        switch (task) {
            case "getAccounts":
                php_url = "https://mantixcloud.nl/gfo/account/getaccounts.php";
                break;
        }

        try {
//            connect to database
            URL url = new URL(php_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(false);// ... to begin with
            httpURLConnection.setDoInput(true);

//            receive data
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                //appending it to string builder
                sb.append(json + "\n");
            }
//            close input
            bufferedReader.close();
            inputStream.close();

//            disconnect to database
            httpURLConnection.disconnect();

            //format (JSON) String to return
            return sb.toString().trim();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPreExecute(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result){
        progressBar.setVisibility(View.GONE);
//        notify Activity that AsyncTask is finished
        try {
            listener.onTaskCompleted(result);
        } catch (JSONException e) { e.printStackTrace(); }
    }
}