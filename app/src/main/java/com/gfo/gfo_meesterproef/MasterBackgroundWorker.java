package com.gfo.gfo_meesterproef;

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
    private boolean feedbackToast = false;//    by default
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
//        determine input keys for php, based on type
        phpCase = new PhpCase(php_url, inputKeys, feedbackToast);
        phpCase.processType(type);

        php_url = phpCase.getUrl();
        inputKeys = phpCase.getInputKeys();
        feedbackToast = phpCase.getFeedbackToast();//    by default

//        copy params[] to inputValues[] without type as first index entry
        String[] inputValues = new String[inputKeys.length];
        inputValues = Arrays.copyOfRange(params,1,params.length);
//        create variables
        String result = null;
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

//                disconnect to database
                bufferedReader.close();
                inputStream.close();
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

//    private void processType(@NonNull String type) {
//        switch (type) {
//            case "login":
//                php_url = "https://mantixcloud.nl/gfo/login.php";
//                inputKeys = new String[2];
//                inputKeys[0] = "username";
//                inputKeys[1] = "password";
//                break;
//            case "openFile":
//                php_url = "https://mantixcloud.nl/gfo/openfile.php";
//                inputKeys = new String[1];
//                inputKeys[0] = "dname";
//                break;
//            case "addAccount":
//                php_url = "https://mantixcloud.nl/gfo/account/addaccount.php";
//                inputKeys = new String[4];
//                inputKeys[0] = "usernamec";
//                inputKeys[1] = "passwordc";
//                inputKeys[2] = "emailc";
//                inputKeys[3] = "adminflagc";
//                feedbackToast = true;
//                break;
//            case "editAccount":
//                php_url = "https://mantixcloud.nl/gfo/account/editaccount.php";
//                inputKeys = new String[4];
//                inputKeys[0] = "old_username";
//                inputKeys[1] = "username";
//                inputKeys[2] = "password";
//                inputKeys[3] = "email";
//                feedbackToast = true;
//                break;
//            case "deleteAccount":
//                php_url = "https://mantixcloud.nl/gfo/account/deleteaccount.php";
//                inputKeys = new String[1];
//                inputKeys[0] = "username";
//                feedbackToast = true;
//                break;
////        reserved for Couple
////        reserved for Uncouple
//            case "allProducts":
//                php_url = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
//                inputKeys = new String[0];
//                break;
//            case "coupledProduct":
//                php_url = "https://mantixcloud.nl/gfo/couple/coupledproduct.php";
//                inputKeys = new String[1];
//                inputKeys[0] = "username";
//                break;
//            case "allAccounts":
//                php_url = "https://mantixcloud.nl/gfo/account/viewusername-user.php";
//                inputKeys = new String[0];
//                break;
//            case "coupledAccount":
//                php_url = "https://mantixcloud.nl/gfo/couple/coupledaccount.php";
//                inputKeys = new String[1];
//                inputKeys[0] = "product";
//                break;
//            case "fetchFile":
//                php_url = "https://mantixcloud.nl/gfo/products_files/fetchfiles.php";
//                inputKeys = new String[1];
//                inputKeys[0] = "product";
//                break;
//            case "fetchProduct":
//                php_url = "https://mantixcloud.nl/gfo/products_files/fetchproducts.php";
//                inputKeys = new String[1];
//                inputKeys[0] = "username";
//                break;
//            case "viewFile":
//                php_url = "https://mantixcloud.nl/gfo/products_files/viewfiles.php";
//                inputKeys = new String[1];
//                inputKeys[0] = "product";
//                break;
//            case "viewProduct":
//                php_url = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
//                inputKeys = new String[0];
//                break;
//
////        ViewAccountBackgroundWorker will be separate
//
////        Couple and Uncouple don't have OnTaskCompleted yet
////            case "couple":
////                php_url = "https://mantixcloud.nl/gfo/couple/couple.php";
////                inputKeys = new String[2];
////                inputKeys[1] = "username";
////                inputKeys[2] = "product";
////                feedbackToast = true;
////                break;
////            case "uncouple":
////                php_url = "https://mantixcloud.nl/gfo/couple/uncouple.php";
////                inputKeys = new String[2];
////                inputKeys[1] = "username";
////                inputKeys[2] = "product";
////                feedbackToast = true;
////                break;
//
//        }
//    }
}