package com.gfo.gfo_meesterproef.Custom;

/**
 * Created by Denni on 12-11-2017.
 */

public class Account {
    private String mName;
    private String mPass;
    private String mEmail;

    public Account(String name, String pass, String email) {
        mName = name;
        mPass = pass;
        mEmail = email;
    }

    public String getName() {
        return mName;
    }
    public String getPass() {
        return mPass;
    }
    public String getEmail() {
        return mEmail;}

    public void setName(String name) {mName = name;}
    public void setPass(String pass) {mPass = pass;}
    public void setEmail(String email) {mEmail = email;}
}