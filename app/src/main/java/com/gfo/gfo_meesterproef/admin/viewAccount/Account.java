package com.gfo.gfo_meesterproef.admin.viewAccount;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Denni on 12-11-2017.
 */

public class Account implements Parcelable {
    private String mName;
    private String mPass;
    private String mEmail;
    private boolean mAdminFlag;

    public Account(String name, String pass, String email, boolean adminFlag) {
        mName = name;
        mPass = pass;
        mEmail = email;
        mAdminFlag = adminFlag;
    }

    public String getName() { return mName; }
    public String getPass() { return mPass; }
    public String getEmail() { return mEmail; }
    public boolean getAdminFlag() { return mAdminFlag; }

    public void setName(String name) {mName = name;}
    public void setPass(String pass) {mPass = pass;}
    public void setEmail(String email) {mEmail = email;}
    public void setAdminFlag(boolean adminFlag) {mAdminFlag = adminFlag;}

//    makes Account Parcelable
    protected Account(Parcel in) {
        mName = in.readString();
        mPass = in.readString();
        mEmail = in.readString();
        mAdminFlag = (boolean) in.readValue(null);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mPass);
        dest.writeString(mEmail);
        dest.writeValue(mAdminFlag);
    }
    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }
        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}