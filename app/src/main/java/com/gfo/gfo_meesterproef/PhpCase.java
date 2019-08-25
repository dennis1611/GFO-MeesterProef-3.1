package com.gfo.gfo_meesterproef;

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
                mInputKeys = new String[2];
                mInputKeys[0] = "username";
                mInputKeys[1] = "password";
                break;
            case "openFile":
                mUrl = "https://mantixcloud.nl/gfo/openfile.php";
                mInputKeys = new String[1];
                mInputKeys[0] = "dname";
                break;
            case "addAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/addaccount.php";
                mInputKeys = new String[4];
                mInputKeys[0] = "usernamec";
                mInputKeys[1] = "passwordc";
                mInputKeys[2] = "emailc";
                mInputKeys[3] = "adminflagc";
                mFeedbackToast = true;
                break;
            case "editAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/editaccount.php";
                mInputKeys = new String[4];
                mInputKeys[0] = "old_username";
                mInputKeys[1] = "username";
                mInputKeys[2] = "password";
                mInputKeys[3] = "email";
                mFeedbackToast = true;
                break;
            case "deleteAccount":
                mUrl = "https://mantixcloud.nl/gfo/account/deleteaccount.php";
                mInputKeys = new String[1];
                mInputKeys[0] = "username";
                mFeedbackToast = true;
                break;
//        reserved for Couple
//        reserved for Uncouple
            case "allProducts":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
                mInputKeys = new String[0];
                break;
            case "coupledProduct":
                mUrl = "https://mantixcloud.nl/gfo/couple/coupledproduct.php";
                mInputKeys = new String[1];
                mInputKeys[0] = "username";
                break;
            case "allAccounts":
                mUrl = "https://mantixcloud.nl/gfo/account/viewusername-user.php";
                mInputKeys = new String[0];
                break;
            case "coupledAccount":
                mUrl = "https://mantixcloud.nl/gfo/couple/coupledaccount.php";
                mInputKeys = new String[1];
                mInputKeys[0] = "product";
                break;
            case "fetchFile":
                mUrl = "https://mantixcloud.nl/gfo/products_files/fetchfiles.php";
                mInputKeys = new String[1];
                mInputKeys[0] = "product";
                break;
            case "fetchProduct":
                mUrl = "https://mantixcloud.nl/gfo/products_files/fetchproducts.php";
                mInputKeys = new String[1];
                mInputKeys[0] = "username";
                break;
            case "viewFile":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewfiles.php";
                mInputKeys = new String[1];
                mInputKeys[0] = "product";
                break;
            case "viewProduct":
                mUrl = "https://mantixcloud.nl/gfo/products_files/viewproducts.php";
                mInputKeys = new String[0];
                break;

//        ViewAccountBackgroundWorker will be separate

//        Couple and Uncouple don't have OnTaskCompleted yet
//            case "couple":
//                mUrl = "https://mantixcloud.nl/gfo/couple/couple.php";
//                mInputKeys = new String[2];
//                mInputKeys[1] = "username";
//                mInputKeys[2] = "product";
//                mFeedbackToast = true;
//                break;
//            case "uncouple":
//                mUrl = "https://mantixcloud.nl/gfo/couple/uncouple.php";
//                mInputKeys = new String[2];
//                mInputKeys[1] = "username";
//                mInputKeys[2] = "product";
//                mFeedbackToast = true;
//                break;

        }
    }

}