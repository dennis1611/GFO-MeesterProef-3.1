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

    public Account(String name, String pass, String email) {
        mName = name;
        mPass = pass;
        mEmail = email;
    }

    protected Account(Parcel in) {
        mName = in.readString();
        mPass = in.readString();
        mEmail = in.readString();
    }

    public String getName() { return mName; }
    public String getPass() { return mPass; }
    public String getEmail() { return mEmail; }

    public void setName(String name) {mName = name;}
    public void setPass(String pass) {mPass = pass;}
    public void setEmail(String email) {mEmail = email;}

//    makes Account Parcelable
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mPass);
        dest.writeString(mEmail);
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