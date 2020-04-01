package com.gfo.gfo_meesterproef.support;

public class PhpBranch {
    private String mUrl;
    private String[] mInputKeys;
    private boolean mFeedbackToast;

    public PhpBranch(String url, String[] inputKeys, boolean feedbackToast) {
        mUrl = url;
        mInputKeys = inputKeys;
        mFeedbackToast = feedbackToast;
    }

    public String getUrl() { return mUrl;}
    public String[] getInputKeys() { return mInputKeys;}
    public boolean getFeedbackToast() { return mFeedbackToast;}

////        Methods below aren't meant to be used, branches are meant to be hardcoded
//    public void setUrl(String url) { mUrl = url;}
//    public void setInputKeys(String[] inputKeys) { mInputKeys = inputKeys;}
//    public void setFeedbackToast(boolean feedbackToast) { mFeedbackToast = feedbackToast;}

    void processType(String type) {
        switch (type) {
            case "login":
                mUrl = "https://mantixcloud.nl/gfo/login.php";
                mInputKeys = new String[] {"username", "password"};
                break;
            case "openFile":
                mUrl = "https://mantixcloud.nl/gfo/openfile.php";
                mInputKeys = new String[] {"dname"};
                break;
            case "addAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/addaccount.php";
                mInputKeys = new String[] {"usernamec", "passwordc", "emailc", "adminflagc"};
                mFeedbackToast = true;
                break;
            case "editAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/editaccount.php";
                mInputKeys = new String[] {"old_username", "username", "password", "email"};
                mFeedbackToast = true;
                break;
            case "deleteAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/deleteaccount.php";
                mInputKeys = new String[] {"username"};
                mFeedbackToast = true;
                break;
            case "allProducts":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
                mInputKeys = new String[] {};
                break;
            case "coupledProduct":
                mUrl = "https://mantixcloud.nl/gfo/couple/coupledproduct.php";
                mInputKeys = new String[] {"username"};
                break;
            case "allAccounts":
                mUrl = "https://mantixcloud.nl/gfo/account/viewusername-user.php";
                mInputKeys = new String[] {};
                break;
            case "coupledAccount":
                mUrl = "https://mantixcloud.nl/gfo/couple/coupledaccount.php";
                mInputKeys = new String[] {"product"};
                break;
            case "fetchFile":
                mUrl = "https://mantixcloud.nl/gfo/products_files/fetchfiles.php";
                mInputKeys = new String[] {"product"};
                break;
            case "fetchProduct":
                mUrl = "https://mantixcloud.nl/gfo/products_files/fetchproducts.php";
                mInputKeys = new String[] {"username"};
                break;
            case "viewFile":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewfiles.php";
                mInputKeys = new String[] {"product"};
                break;
            case "viewProduct":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
                mInputKeys = new String[] {};
                break;

//                JSON from here onwards
            case "getAccounts":
                mUrl = "https://mantixcloud.nl/gfo/account/getaccounts.php";
                mInputKeys = new String[] {};
                break;
            case "linkToAccount":
                mUrl = "https://mantixcloud.nl/gfo/couple/linktoaccount.php";
                mInputKeys = new String[]{"account", "tocouple", "touncouple"};
                break;
            case "linkToProduct":
                mUrl = "https://mantixcloud.nl/gfo/couple/linktoproduct.php";
                mInputKeys = new String[]{"product", "tocouple", "touncouple"};
                break;
        }
    }

}