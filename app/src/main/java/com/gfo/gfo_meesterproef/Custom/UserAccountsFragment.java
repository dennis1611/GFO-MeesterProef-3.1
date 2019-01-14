package com.gfo.gfo_meesterproef.Custom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gfo.gfo_meesterproef.Admin.ViewAccount.ViewAccountActivity;
import com.gfo.gfo_meesterproef.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAccountsFragment extends Fragment {

    ListView accountList;

    public UserAccountsFragment() {
//        Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.list, container, false);
        //        get strings with all values
        String usernames = getArguments().getString("userUsernames");
        String passwords = getArguments().getString("userPasswords");
        String emails = getArguments().getString("userEmails");

//        split strings to arrayLists
        String[] splitUsernameArray = usernames.split(",");
        List<String> splitUsernameList = (Arrays.asList(splitUsernameArray));
        String[] splitPasswordArray = passwords.split(",");
        List<String> splitPasswordList = (Arrays.asList(splitPasswordArray));
        String[] splitEmailArray = emails.split(",");
        List<String> splitEmailList = (Arrays.asList(splitEmailArray));

//        look for unhandled accounts and add them to accountList
        ArrayList<Account> accounts = new ArrayList<Account>();
        Iterator<String> usernameIterator = splitUsernameList.iterator();
        Iterator<String> passwordIterator = splitPasswordList.iterator();
        Iterator<String> emailIterator = splitEmailList.iterator();
        while(emailIterator.hasNext()){
            String user = usernameIterator.next();
            String pass = passwordIterator.next();
            String email = emailIterator.next();
            Account newAccount = new Account(user, pass, email);
            accounts.add(newAccount);
        }
//        couple accounts to layout
        accountList = (ListView) rootView.findViewById(R.id.list);
        AccountAdapter accountAdapter = new AccountAdapter(getActivity(), accounts);
        accountList.setAdapter(accountAdapter);

        registerAccountClickCallBack();
        return rootView;
    }

    private void registerAccountClickCallBack() {
        accountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                get values from selected account
                Object accountClicked = accountList.getItemAtPosition(position);
                Account selectedAccount = (Account) accountClicked;
                String selectedUsername = selectedAccount.getName();
                String selectedPassword = selectedAccount.getPass();
                String selectedEmail = selectedAccount.getEmail();
//                call method in parent activity
                ((ViewAccountActivity)getActivity()).onSelect(selectedUsername, selectedPassword, selectedEmail, "user");
            }
        });
    }
}