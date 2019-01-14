package com.gfo.gfo_meesterproef.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OenO on 5-12-2017.
 */

public class FolderAdapter extends ArrayAdapter<String> {
        public FolderAdapter(Context context, List<String> groups){
            super(context, 0, groups);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            Check if an existing view is being reused, otherwise inflate the view
            View gridItemView = convertView;
            if (gridItemView == null) {
                gridItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.folder_item, parent, false);
            }
//            Get the {@link Word} object located at this position in the list
            String currentGroup = getItem(position);

            TextView groupTextView = (TextView) gridItemView.findViewById(R.id.textViewGroup);
            groupTextView.setText(currentGroup);

            return gridItemView;
        }
}