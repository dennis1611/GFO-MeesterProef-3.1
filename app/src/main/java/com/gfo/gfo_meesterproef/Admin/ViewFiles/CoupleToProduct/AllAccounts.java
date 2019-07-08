package com.gfo.gfo_meesterproef.Admin.ViewFiles.CoupleToProduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllAccounts extends AsyncTask<String, Void, List<String>> {

    Context context;
    private OnTaskCompleted listener;
    public AllAccounts(Context ctx, OnTaskCompleted listener) {
        context = ctx;
        this.listener = listener;
    }

    //    get access to ProgressBar in activity
    @SuppressLint("StaticFieldLeak") ProgressBar progressBar;
    public void setProgressBar(ProgressBar progressBar) { this.progressBar = progressBar; }

    //    create interface to communicate with Activity
    public interface OnTaskCompleted{ void onTaskCompleted(List<String> splitResultList);}

    @Override
    protected List<String> doInBackground(String... params) {
        String type = params[0];
        String result = null;
        String[] splitResultArray;
        List<String> splitResultList = new ArrayList<>();
//        set view_url
        String view_url="https://mantixcloud.nl/gfo/account/viewusername-user.php";
        if (type.equals("allAccounts")) {
            try {
                //                connect to database
                URL url = new URL(view_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
//                receive data
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                //                split result at , into array
                splitResultArray = result.split(",");
                splitResultList = (Arrays.asList(splitResultArray));

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return splitResultList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return splitResultList;
    }
    @Override
    protected void onPreExecute() { progressBar.setVisibility(View.VISIBLE);}

    @Override
    protected void onPostExecute(List<String> splitResultList) {
        progressBar.setVisibility(View.GONE);
        //        Notify activity that AsyncTask is finished
        listener.onTaskCompleted(splitResultList);
    }
}