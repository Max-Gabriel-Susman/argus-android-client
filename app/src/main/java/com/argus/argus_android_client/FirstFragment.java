package com.argus.argus_android_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.argus.argus_android_client.databinding.FragmentFirstBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private MyListAdapter myListAdapter;
    private ArrayList<String> listItems;

    // 1) We'll define keys for SharedPreferences
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_LIST_ITEMS = "list_items_json";

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // 2) Load saved data from SharedPreferences.
        loadListItemsFromPrefs();

        // 3) Create and set the custom adapter
        myListAdapter = new MyListAdapter(requireContext(), listItems);
        ListView listView = binding.myListView;
        listView.setAdapter(myListAdapter);

        // 4) Handle the "Add" button click
        binding.btnAddItem.setOnClickListener(v -> {
            EditText etNewItem = binding.etNewItem;
            String newItemText = etNewItem.getText().toString().trim();

            if (!TextUtils.isEmpty(newItemText)) {
                listItems.add(newItemText);
                myListAdapter.notifyDataSetChanged();
                etNewItem.setText("");

                // 5) Immediately save the updated list to SharedPreferences
                saveListItemsToPrefs();
            }
        });
    }

    /**
     * Loads the list items from SharedPreferences.
     * If nothing is saved yet, we create an empty ArrayList.
     */
    private void loadListItemsFromPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_LIST_ITEMS, null);

        if (json != null) {
            // We have some saved data
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            listItems = gson.fromJson(json, type);
        } else {
            // No saved data yet => start with an empty list
            listItems = new ArrayList<>();
        }
    }

    /**
     * Saves the current list to SharedPreferences as JSON.
     */
    public void saveListItemsToPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(listItems);

        editor.putString(KEY_LIST_ITEMS, json);
        editor.apply(); // or commit(), but apply() is async and usually preferred
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
