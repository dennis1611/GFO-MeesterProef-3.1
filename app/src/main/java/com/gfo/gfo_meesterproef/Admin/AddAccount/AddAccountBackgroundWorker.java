package com.gfo.gfo_meesterproef.Admin.AddAccount;

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

public class AddAccountBackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    AddAccountBackgroundWorker(Context ctx) {
        context = ctx;
    }
//    get access to ProgressBar in activity
@SuppressLint("StaticFieldLeak") ProgressBar progressBar;
public void setProgressBar(ProgressBar progressBar) { this.progressBar = progressBar; }

    @Override
    protected String doInBackground(String... params) {
        String result = null;
        String type = params[0];
        String login_url = "https://mantixcloud.nl/gfo/account/addaccount.php";
        if(type.equals("add_account")) {
            try {
                String usernamec = params[1];
                String passwordc = params[2];
                String emailc = params[3];
                String adminflagc = params[4];
//                connect to database
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
//                send data
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("usernamec","UTF-8")+"="+URLEncoder.encode(usernamec,"UTF-8")+"&"
                        +URLEncoder.encode("passwordc","UTF-8")+"="+URLEncoder.encode(passwordc,"UTF-8")+"&"
                        +URLEncoder.encode("emailc","UTF-8")+"="+URLEncoder.encode(emailc,"UTF-8")+"&"
                        +URLEncoder.encode("adminflagc","UTF-8")+"="+URLEncoder.encode(adminflagc,"UTF-8");
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
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
    progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result) {
    progressBar.setVisibility(View.GONE);
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}