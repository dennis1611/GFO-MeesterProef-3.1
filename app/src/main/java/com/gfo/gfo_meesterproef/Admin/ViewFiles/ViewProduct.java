package com.gfo.gfo_meesterproef.Admin.ViewFiles;

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

public class ViewProduct extends AsyncTask<String, Void, String> {

    Context context;
    private OnTaskCompleted listener;
    public ViewProduct(Context ctx, OnTaskCompleted listener) {
        context = ctx;
        this.listener = listener;
    }

    //    get access to ProgressBar in activity
    @SuppressLint("StaticFieldLeak") ProgressBar progressBar;
    public void setProgressBar(ProgressBar progressBar) { this.progressBar = progressBar; }

    //    create interface to communicate with Activity
    public interface OnTaskCompleted{ void onTaskCompleted(String result);}

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String view_url = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
        String result = null;
        if (type.equals("view")) {
            try {
//                connect to database
                URL url = new URL(view_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
//                receive data
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPreExecute() {progressBar.setVisibility(View.VISIBLE);}

    @Override
    protected void onPostExecute(String result) {
        progressBar.setVisibility(View.GONE);
//        Notify activity that AsyncTask is finished
   listener.onTaskCompleted(result);
    }
}