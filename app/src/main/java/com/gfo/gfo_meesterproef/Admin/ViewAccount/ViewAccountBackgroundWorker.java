package com.gfo.gfo_meesterproef.Admin.ViewAccount;

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
import java.util.List;

public class ViewAccountBackgroundWorker extends AsyncTask<String, Void, List<String>> {

    Context context;
    private OnTaskCompleted listener;
    public ViewAccountBackgroundWorker(Context ctx, OnTaskCompleted listener) {
        context = ctx;
        this.listener = listener;
    }

    //    get access to ProgressBar in activity
    @SuppressLint("StaticFieldLeak") ProgressBar progressBar;
    public void setProgressBar(ProgressBar progressBar) { this.progressBar = progressBar; }

    //    create interface to communicate with Activity
    public interface OnTaskCompleted{ void onTaskCompleted(List<String> resultList);}

    @Override
    protected List<String> doInBackground(String... params) {
        String type = params[0];
        String view_url = null;
        String result = null;
        List<String> resultList = new ArrayList<>();

//        contact database
        if (type.equals("userUsername") || type.equals("userPassword") || type.equals("userEmail")
                || type.equals("adminUsername") || type.equals("adminPassword") || type.equals("adminEmail")
                || type.equals("all")) {
            try {
                for (int i = 0; i<6; i++){
//                    set view_url for each iteration
                    if (i==0){view_url="https://mantixcloud.nl/gfo/account/viewusername-user.php";}
                    else if (i==1){view_url="https://mantixcloud.nl/gfo/account/viewpassword-user.php";}
                    else if (i==2){view_url="https://mantixcloud.nl/gfo/account/viewemail-user.php";}
                    else if (i==3){view_url="https://mantixcloud.nl/gfo/account/viewusername-admin.php";}
                    else if (i==4){view_url="https://mantixcloud.nl/gfo/account/viewpassword-admin.php";}
                    else if (i==5){view_url="https://mantixcloud.nl/gfo/account/viewemail-admin.php";}

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

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

//                add iteration's result to parent List
                    resultList.add(result);
            }//            end loop

                return resultList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<String> resultList) {
            progressBar.setVisibility(View.GONE);
//        Notify activity that AsyncTask is finished
            listener.onTaskCompleted(resultList);
    }
}