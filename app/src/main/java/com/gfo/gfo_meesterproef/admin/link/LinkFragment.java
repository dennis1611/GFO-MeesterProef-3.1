package com.gfo.gfo_meesterproef.admin.link;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.R;

import java.util.ArrayList;

public class LinkFragment extends Fragment {

    ArrayList<String> totalList = new ArrayList<>(),
            alreadyCoupled = new ArrayList<>(),
            toCouple = new ArrayList<>(),
            toUncouple = new ArrayList<>();
    ListView list;

    public LinkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.list, container, false);

        //        get all items (either products or accounts) from ViewAccountActivity
        totalList = getArguments().getStringArrayList("totalList");
        alreadyCoupled = getArguments().getStringArrayList("alreadyCoupled");

        //        couple accounts to layout
        list = rootView.findViewById(R.id.list);
        list.setBackgroundResource(R.color.white);
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, totalList){
            @NonNull @Override
//                create view correctly (even when recycled)
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View row = convertView;
//                    Check if an existing view is being reused, otherwise inflate the view
                if (row==null){ row = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false); }
//                  handle already coupled items and toUncouple items
                if (alreadyCoupled.contains(totalList.get(position))){
//                      compare row with toUncouple. If it contains, make red
                    if (toUncouple.contains(totalList.get(position))){
                        row.setBackgroundResource(R.color.red);
                        row.setTag(R.color.red);
//                      ... if not, make blue
                    } else { row.setBackgroundResource(R.color.blue);
                        row.setTag(R.color.blue); }
//                  handle not yet coupled items and toCouple items
                } else {
//                      compare row with toCouple. If it contains, make green
                    if (toCouple.contains(totalList.get(position))){
                        row.setBackgroundResource(R.color.green);
                        row.setTag(R.color.green);
//                      ... if not, make white
                    } else { row.setBackgroundResource(R.color.white);
                        row.setTag(R.color.white); }
                }
//                    set text to each row
                String currentProduct = getItem(position);
                TextView productTextView = (TextView) row;
                productTextView.setText(currentProduct);
                return row; }
        };
        list.setAdapter(listAdapter);

        registerItemClickCallBack();
        return rootView;
    }

    private void registerItemClickCallBack() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                get selected item as String
                TextView textView = (TextView) viewClicked;
                String item = textView.getText().toString();

//                get color tag and add or delete from to (un)couple list
                int ColorId = Integer.parseInt(viewClicked.getTag().toString());
                if (ColorId==R.color.blue){
                    viewClicked.setBackgroundResource(R.color.red);
                    viewClicked.setTag(R.color.red);
                    toUncouple.add(item);
                } else if (ColorId==R.color.red){
                    viewClicked.setBackgroundResource(R.color.blue);
                    viewClicked.setTag(R.color.blue);
                    toUncouple.remove(item);
                } else if (ColorId==R.color.green){
                    viewClicked.setBackgroundResource(R.color.white);
                    viewClicked.setTag(R.color.white);
                    toCouple.remove(item);
                } else if (ColorId==R.color.white){
                    viewClicked.setBackgroundResource(R.color.green);
                    viewClicked.setTag(R.color.green);
                    toCouple.add(item);
                }
            }
        });
    }

}