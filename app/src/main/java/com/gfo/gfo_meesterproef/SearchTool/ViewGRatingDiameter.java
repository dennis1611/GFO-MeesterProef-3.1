package com.gfo.gfo_meesterproef.SearchTool;

import android.content.Context;
import android.os.AsyncTask;

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


public class ViewGRatingDiameter extends AsyncTask<String, Void, String> {

    Context context;
    public ViewGRatingDiameter(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
//        set view_url
        String view_url = null;
        if (type.equals("gRating")){view_url="https://mantixcloud.nl/gfo/searchtool/gRatings.php";}
        if (type.equals("diameter")){view_url="https://mantixcloud.nl/gfo/searchtool/diameters.php";}
        String result = null;
//        String[] splitResultArray;
//        List<String> splitResultList = new ArrayList<String>();
        if (type.equals("gRating" ) || type.equals("diameter")){
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
                //                split result at , into ArrayList
//                splitResultArray = result.split(",");
//                splitResultList = (Arrays.asList(splitResultArray));

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
}