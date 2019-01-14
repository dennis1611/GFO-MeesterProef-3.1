package com.gfo.gfo_meesterproef.Admin.ViewAccount;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ViewAccountBackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;

    public ViewAccountBackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String view_url = null;
        String result = null;
//        set view_url
        if (type.equals("userUsername")){view_url="https://mantixcloud.nl/gfo/account/viewusername-user.php";}
        if (type.equals("userPassword")){view_url="https://mantixcloud.nl/gfo/account/viewpassword-user.php";}
        if (type.equals("userEmail")){view_url="https://mantixcloud.nl/gfo/account/viewemail-user.php";}
        if (type.equals("adminUsername")){view_url="https://mantixcloud.nl/gfo/account/viewusername-admin.php";}
        if (type.equals("adminPassword")){view_url="https://mantixcloud.nl/gfo/account/viewpassword-admin.php";}
        if (type.equals("adminEmail")){view_url="https://mantixcloud.nl/gfo/account/viewemail-admin.php";}
//        contact database
        if (type.equals("userUsername") || type.equals("userPassword") || type.equals("userEmail")
                || type.equals("adminUsername") || type.equals("adminPassword") || type.equals("adminEmail")) {
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
    protected void onPreExecute() { }

    @Override
    protected void onPostExecute(String result) { }

}