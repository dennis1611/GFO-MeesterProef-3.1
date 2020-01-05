package com.gfo.gfo_meesterproef.admin.viewAccount;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gfo.gfo_meesterproef.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAccountsFragment extends Fragment {

    ListView accountList;

    public AdminAccountsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.list, container, false);

//        get ArrayLists with all values
        ArrayList<String> splitUsernameList = getArguments().getStringArrayList("adminUsernames");
        ArrayList<String> splitPasswordList = getArguments().getStringArrayList("adminPasswords");
        ArrayList<String> splitEmailList = getArguments().getStringArrayList("adminEmails");

//        look for unhandled accounts and add them to accountList
        ArrayList<Account> accounts = new ArrayList<>();
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
        accountList = rootView.findViewById(R.id.list);
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
                ((ViewAccountActivity)getActivity()).onSelect(selectedUsername, selectedPassword, selectedEmail, "admin");
            }
        });
    }
}