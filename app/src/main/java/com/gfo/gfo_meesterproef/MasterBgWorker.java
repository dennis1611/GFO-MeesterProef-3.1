package com.gfo.gfo_meesterproef;

import android.os.AsyncTask;

import java.util.List;

public class MasterBgWorker extends AsyncTask<String, Void, List<String>> {

    String php_url;


    @Override
    protected List<String> doInBackground(String... params) {
        String type = params[0];
        setPath(type);


        return null;
    }


    @Override
    protected void onPreExecute(){

    }

    @Override
    protected void onPostExecute(List<String> resultList){

    }



    void setPath(String type){
        if(type.equals("login")) {              php_url = "https://mantixcloud.nl/gfo/login.php";}
        if(type.equals("openFile")) {           php_url = "https://mantixcloud.nl/gfo/openfile.php";}
        if(type.equals("addAccount")) {         php_url = "https://mantixcloud.nl/gfo/account/addaccount.php";}
        if(type.equals("editAccount")) {        php_url = "https://mantixcloud.nl/gfo/account/editaccount.php";}
        if(type.equals("deleteAccount")) {      php_url = "https://mantixcloud.nl/gfo/account/deleteaccount.php";}
//        if(type.equals("couple")) {             php_url = "https://mantixcloud.nl/gfo/couple/couple.php";}
//        if(type.equals("uncouple")) {           php_url = "https://mantixcloud.nl/gfo/couple/uncouple.php";}
        if(type.equals("allProducts")) {        php_url = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";}
        if(type.equals("coupledProduct")) {     php_url = "https://mantixcloud.nl/gfo/couple/coupledproduct.php";}
        if(type.equals("allAccounts")) {        php_url = "https://mantixcloud.nl/gfo/account/viewusername-user.php";}
        if(type.equals("coupledAccount")) {     php_url = "https://mantixcloud.nl/gfo/couple/coupledaccount.php";}
        if(type.equals("fetchFile")) {          php_url = "https://mantixcloud.nl/gfo/products_files/fetchfiles.php";}
        if(type.equals("fetchProduct")) {       php_url = "https://mantixcloud.nl/gfo/products_files/fetchproducts.php";}
        if(type.equals("viewFile")) {           php_url = "https://mantixcloud.nl/gfo/products_files/viewfiles.php";}
        if(type.equals("viewProduct")) {        php_url = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";}
//        ViewAccountBackgroundWorker will be separate
    }
}