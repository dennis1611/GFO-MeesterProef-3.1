package com.gfo.gfo_meesterproef.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MasterBackgroundWorker extends AsyncTask<String, Void, List<String>> {

    private String php_url;
    private String[] inputKeys;
    private boolean feedbackToast;
    private PhpCase phpCase;

    Context context;
    private OnTaskCompleted listener;
    public MasterBackgroundWorker(Context ctx, OnTaskCompleted listener){
        context = ctx;
        this.listener = listener;
    }
    //    get access to ProgressBar in activity
    @SuppressLint("StaticFieldLeak") private ProgressBar progressBar;
    public void setProgressBar(ProgressBar progressBar) { this.progressBar = progressBar; }
    //    create interface to communicate with Activity
    public interface OnTaskCompleted{ void onTaskCompleted(List<String> resultList);}

    @Override
    protected List<String> doInBackground(String... params) {
        String type = params[0];

//        define phpCase and determine its variables in processType
        phpCase = new PhpCase(php_url, inputKeys, feedbackToast);
        phpCase.processType(type);

//        determine input keys for php etc. , based on type, stored in phpCase
        php_url = phpCase.getUrl();
        inputKeys = phpCase.getInputKeys();
        feedbackToast = phpCase.getFeedbackToast();

//        copy params[] to inputValues[] without type as first index entry
        String[] inputValues = new String[inputKeys.length];
        inputValues = Arrays.copyOfRange(params,1,params.length);
//        create variables
        String result = null;//        define as null is needed
        List<String> resultList = new ArrayList<>();

        if (type != null){
            try {
//                connect to database
                URL url = new URL(php_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

//                send data
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                format data
                StringBuilder post_dataBuilder = new StringBuilder();
                for (int i = 0; i < inputKeys.length; i++) {
                    post_dataBuilder.append(URLEncoder.encode(inputKeys[i],"UTF-8")+"="+URLEncoder.encode(inputValues[i],"UTF-8"));
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
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;//   IGNORE WARNING: php sends result as a single line anyway
                }
                //                split result at , into array (IF NOT NEEDED: just places result as only entry in resultList)
                String[] splitResultArray = result.split(",");
                resultList = (Arrays.asList(splitResultArray));
//                close input
                bufferedReader.close();
                inputStream.close();

//                disconnect to database
                httpURLConnection.disconnect();
                return resultList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }return resultList;
    }

    @Override
    protected void onPreExecute(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<String> resultList){
        progressBar.setVisibility(View.GONE);
        if (feedbackToast) { Toast.makeText(context, resultList.get(0), Toast.LENGTH_LONG).show(); }
//        notify Activity that AsyncTask is finished
        listener.onTaskCompleted(resultList);
    }
}