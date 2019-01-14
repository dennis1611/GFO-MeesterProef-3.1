package com.gfo.gfo_meesterproef.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.R;

import java.util.ArrayList;

public class AccountAdapter extends ArrayAdapter<Account> {
    public AccountAdapter(Context context, ArrayList<Account> accounts) {
        super(context, 0, accounts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.account_item, parent, false);
        }
        // Get the {@link Word} object located at this position in the list
        Account currentAccount = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.textViewName);
        nameTextView.setText(currentAccount.getName());

        TextView passTextView = (TextView) listItemView.findViewById(R.id.textViewPass);
        passTextView.setText(currentAccount.getPass());

        TextView emailTextView = (TextView) listItemView.findViewById(R.id.textViewEmail);
        emailTextView.setText(currentAccount.getEmail());

        return listItemView;
    }
}