package com.gfo.gfo_meesterproef.admin.link;

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
import java.util.List;

public class CouplerBackgroundWorker extends AsyncTask<String, Void, List<String>> {

    Context context;

    public CouplerBackgroundWorker(Context ctx) {
        context = ctx;
    }
    //    get access to ProgressBar in activity
    @SuppressLint("StaticFieldLeak") ProgressBar progressBar;
    public void setProgressBar(ProgressBar progressBar) { this.progressBar = progressBar; }

    @Override
    protected List<String> doInBackground(String... params) {
        String type = params[0];
        String username = params[1];
        String product = params[2];
        String login_url = null;
        switch (type) {
            case "couple":
                login_url = "https://mantixcloud.nl/gfo/couple/couple.php";
                break;
            case "uncouple":
                login_url = "https://mantixcloud.nl/gfo/couple/uncouple.php";
                break;
        }
        String result = null;
        List<String> resultList = new ArrayList<>();
        if (type.equals("couple") || type.equals("uncouple")) {
            try {
//                connect to database
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //                send data
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("product","UTF-8")+"="+URLEncoder.encode(product,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

//                receive data
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
//                set String in List (for future MasterBackgroundWorker)
                resultList.add(result);
                return resultList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }return resultList;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<String> resultList) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context, resultList.get(0), Toast.LENGTH_SHORT).show();
    }
}