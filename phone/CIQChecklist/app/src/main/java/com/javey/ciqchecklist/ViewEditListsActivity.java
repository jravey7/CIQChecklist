package com.javey.ciqchecklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ViewEditListsActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayList<String> listOfListnames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_lists);

        displayLists();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        displayLists();
    }

    private void displayLists()
    {
        listOfListnames.clear();

        File[] internalStorageFiles;
        internalStorageFiles = getFilesDir().listFiles();

        for(int i = 0; i < internalStorageFiles.length; i++)
        {
            String fileName = internalStorageFiles[i].getName();
            if(fileName.substring(fileName.length() - 4).equals(".txt"))
            {
                listOfListnames.add(fileName.substring(0, fileName.length() - 4));
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfListnames);

        ListView listView = (ListView) findViewById(R.id.listOfListnames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(listItemClickedListener);
        listView.setOnItemLongClickListener(listItemLongClickedListener);
    }

    // implement OnItemClick() in the adapter listener class for deleting a list when it's long clicked
    private AdapterView.OnItemLongClickListener listItemLongClickedListener = new AdapterView.OnItemLongClickListener() {


        @Override
        public boolean onItemLongClick(AdapterView parent, View v, int pos, long id)
        {
            // create a dialog to ask the user for the list item's label
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(parent.getContext());
            confirmDialog.setTitle("Delete list?")
                    .setMessage(listOfListnames.get(pos));

            final int constPos = pos;

            confirmDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // user confirmed to delete this list item
                    deleteFile(listOfListnames.get(constPos) + ".txt");
                    listOfListnames.remove(constPos);
                    adapter.notifyDataSetChanged();
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // user cancelled the delete, do nothing
                        }
                    });
            confirmDialog.show();

            return true;
        }
    };

    // implement OnItemClick() in the adapter listener class for editing a list when it's clicked
    private AdapterView.OnItemClickListener listItemClickedListener = new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView parent, View v, int pos, long id)
        {
            Intent intent = new Intent(ViewEditListsActivity.this, CreateListActivity.class);
            intent.putExtra("editList", true);
            intent.putExtra("listName", listOfListnames.get(pos));
            startActivity(intent);
        }
    };
}
