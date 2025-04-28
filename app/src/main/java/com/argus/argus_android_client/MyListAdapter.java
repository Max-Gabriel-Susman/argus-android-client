package com.argus.argus_android_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> {

    private final List<String> items;
    private final LayoutInflater inflater;

    public MyListAdapter(Context context, List<String> items) {
        super(context, 0, items);
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reuse convertView if possible, otherwise inflate a new view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        // 1) Get the current item text
        String currentItem = items.get(position);

        // 2) Find views in the layout
        TextView textView = convertView.findViewById(R.id.itemTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        // 3) Set the text
        textView.setText(currentItem);

        // 4) Handle the delete button click
        deleteButton.setOnClickListener(v -> {
            items.remove(position);
            notifyDataSetChanged(); // refresh the ListView
        });

        return convertView;
    }
}
