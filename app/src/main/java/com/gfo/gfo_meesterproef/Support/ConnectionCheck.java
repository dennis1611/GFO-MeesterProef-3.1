package com.gfo.gfo_meesterproef.Support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by Denni on 19-1-2018.
 */

public class ConnectionCheck {

    public boolean test(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if-statement needs attention for newer version
        if(cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting()){
            return true;
        } else {
            Toast.makeText(context, "No Connection Found", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}