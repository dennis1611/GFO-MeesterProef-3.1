package com.gfo.gfo_meesterproef.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class JSONBackgroundWorker extends AsyncTask<String, Void, String> {

    private String result;

    private String php_url;
    private String[] inputKeys;
    private PhpBranch phpBranch;

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

//        define phpCase and determine its variables in processType
        phpBranch = new PhpBranch(php_url, inputKeys, false);
        phpBranch.processType(task);

        //        determine input keys for php etc. , based on type, stored in phpCase
        php_url = phpBranch.getUrl();
        inputKeys = phpBranch.getInputKeys();

            try {
//                connect to database
                URL url = new URL(php_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

//                send data
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //                format data
                StringBuilder post_dataBuilder = new StringBuilder();
                for (int i = 0; i < inputKeys.length; i++) {
                    post_dataBuilder.append(URLEncoder.encode(inputKeys[i], "UTF-8") + "=" + URLEncoder.encode(params[i + 1], "UTF-8"));
//                    last iteration shouldn't append "&", all before should
                    if (i < (inputKeys.length - 1)) {
                        post_dataBuilder.append("&");
                    }
                }
                String post_data = post_dataBuilder.toString();
                //                post data
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
//                close output
                bufferedWriter.close();
                outputStream.close();


//                receive data
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    //appending it to string builder
                    sb.append(json + "\n");
                }
//                close input
                bufferedReader.close();
                inputStream.close();

//                disconnect to database
                httpURLConnection.disconnect();

                //format (JSON) String to return
                result = sb.toString().trim();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    @Override
    protected void onPreExecute(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result){
        progressBar.setVisibility(View.GONE);
//        notify Activity that AsyncTask is finished
        try { listener.onTaskCompleted(result); }
        catch (JSONException e) { e.printStackTrace(); }
    }
}