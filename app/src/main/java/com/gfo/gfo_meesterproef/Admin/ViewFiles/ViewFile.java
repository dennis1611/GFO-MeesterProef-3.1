package com.gfo.gfo_meesterproef.Admin.ViewFiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewFile extends AsyncTask<String, Void, List<String>>{

    Context context;
    public ViewFile(Context ctx) {
        context = ctx;
    }

    //    get access to ProgressBar in activity
    @SuppressLint("StaticFieldLeak")
    ProgressBar progressBar;
    public void setProgressBar(ProgressBar progressBar) { this.progressBar = progressBar; }

    @Override
    protected List<String> doInBackground(String... params) {
        String type = params[0];
        String product = params[1];
        String view_url = "https://mantixcloud.nl/gfo/products_files/viewfiles.php";
        String result;
        String[] splitResultArray;
        List<String> splitResultList = new ArrayList<String>();
        if (type.equals("view")) {
            try {
//                connect to database
                URL url = new URL(view_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
//                send data
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "iso-8859-1"));
                String post_data = URLEncoder.encode("product","iso-8859-1")+"="+URLEncoder.encode(product,"iso-8859-1");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
//                receive data
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
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
    protected void onPreExecute() { }

    @Override
    protected void onPostExecute(List<String> splitResultList) { }

}
