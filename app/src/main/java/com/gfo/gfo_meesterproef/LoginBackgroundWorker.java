package com.gfo.gfo_meesterproef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

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


public class LoginBackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    private OnTaskCompleted listener;
    LoginBackgroundWorker(Context ctx, OnTaskCompleted listener) {
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
        String username = params[1];
        String password = params[2];
        String login_url = "https://mantixcloud.nl/gfo/login.php";
        String result = null;
        if(type.equals("login")) {
            try {
//                connect to database
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

//                send data
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "iso-8859-1"));
                String post_data = URLEncoder.encode("username","iso-8859-1")+"="+URLEncoder.encode(username,"iso-8859-1")+"&"
                        +URLEncoder.encode("password","iso-8859-1")+"="+URLEncoder.encode(password,"iso-8859-1");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

//                receive data
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
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
        }return result;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result) {
        progressBar.setVisibility(View.GONE);
//        notify Activity that AsyncTask is completed
            listener.onTaskCompleted(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}