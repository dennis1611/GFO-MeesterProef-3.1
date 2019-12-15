package com.gfo.gfo_meesterproef.support;

public class PhpCase {
    private String mUrl;
    private String[] mInputKeys;
    private boolean mFeedbackToast;

    public PhpCase(String url, String[] inputKeys, boolean feedbackToast) {
        mUrl = url;
        mInputKeys = inputKeys;
        mFeedbackToast = feedbackToast;
    }

    public String getUrl() { return mUrl;}
    public String[] getInputKeys() { return mInputKeys;}
    public boolean getFeedbackToast() { return mFeedbackToast;}

    public void setUrl(String url) { mUrl = url;}
    public void setInputKeys(String[] inputKeys) { mInputKeys = inputKeys;}
    public void setFeedbackToast(boolean feedbackToast) { mFeedbackToast = feedbackToast;}

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
//        reserved for Couple
//        reserved for Uncouple
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

//        ViewAccountBackgroundWorker will be separate

//        Couple and Uncouple don't have OnTaskCompleted yet
//            case "couple":
//                mUrl = "https://mantixcloud.nl/gfo/couple/couple.php";
//                mInputKeys = new String[] {"username", "product"};
//                mFeedbackToast = true;
//                break;
//            case "uncouple":
//                mUrl = "https://mantixcloud.nl/gfo/couple/uncouple.php";
//                mInputKeys = new String[] {"username", "product"};
//                mFeedbackToast = true;
//                break;

        }
    }

}