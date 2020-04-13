package com.gfo.gfo_meesterproef.support;

public class PhpBranch {
    private String mUrl;
    private String[] mInputKeys;
    private boolean mFeedbackToast;
    private boolean mUseType;
    private boolean mOutputJSON;

    public PhpBranch() {
//        empty, information in this class is meant to be hardcoded and can only be retrieved
    }

    public String getUrl() { return mUrl;}
    public String[] getInputKeys() { return mInputKeys;}
    public boolean getFeedbackToast() { return mFeedbackToast;}
    public boolean getUseType() { return mUseType;}
    public boolean getOutputJSON() { return mOutputJSON;}

    void processType(String type) {
        switch (type) {
            case "login":
                mUrl = "https://mantixcloud.nl/gfo/login.php";
                mInputKeys = new String[] {"username", "password"}; // {String, String}
                break;
            case "openFile":
                mUrl = "https://mantixcloud.nl/gfo/openfile.php";
                mInputKeys = new String[] {"dname"}; // {String}
                break;
            case "addAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/addaccount.php";
                mInputKeys = new String[] {"usernamec", "passwordc", "emailc", "adminflagc"}; // {String, String, String, String}
                mFeedbackToast = true;
                break;
            case "editAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/editaccount.php";
                mInputKeys = new String[] {"old_username", "username", "password", "email"}; // {String, String, String, String}
                mFeedbackToast = true;
                break;
            case "deleteAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/deleteaccount.php";
                mInputKeys = new String[] {"username"}; // {String}
                mFeedbackToast = true;
                break;
            case "fetchFile":
                mUrl = "https://mantixcloud.nl/gfo/products_files/fetchfiles.php";
                mInputKeys = new String[] {"product"}; // {String}
                mOutputJSON = true;
                break;
            case "fetchProduct":
                mUrl = "https://mantixcloud.nl/gfo/products_files/fetchproducts.php";
                mInputKeys = new String[] {"username"}; // {String}
                mOutputJSON = true;
                break;
            case "viewFile":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewfiles.php";
                mInputKeys = new String[] {"product"}; // {String}
                mOutputJSON = true;
                break;
            case "viewProduct":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
                mInputKeys = new String[] {};
                mOutputJSON = true;
                break;
            case "allProducts":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
                mInputKeys = new String[] {};
                mOutputJSON = true;
                break;
            case "allAccounts":
                mUrl = "https://mantixcloud.nl/gfo/account/viewusername-user.php";
                mInputKeys = new String[] {};
                mOutputJSON = true;
                break;
            case "getAccounts":
                mUrl = "https://mantixcloud.nl/gfo/account/getaccounts.php";
                mInputKeys = new String[] {};
                mOutputJSON = true;
                break;
            case "linkToAccount":
            case "linkToProduct":
                mUrl = "https://mantixcloud.nl/gfo/couple/link.php";
                mInputKeys = new String[]{"type", "subject", "tocouple", "touncouple"}; // {String, String, JSONArray, JSONArray}
                mUseType = true;
                break;
            case "coupledProducts":
            case "coupledAccounts":
                mUrl = "https://mantixcloud.nl/gfo/couple/coupled.php";
                mInputKeys = new String[] {"type", "subject"}; // {String, String}
                mUseType = true;
                mOutputJSON = true;
                break;
        }
    }

}