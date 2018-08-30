package com.javey.ciqchecklist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class CreateListActivity extends AppCompatActivity {

    String[] defaultListItems = {"List Item #1"};

    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        // not used atm
        Intent intent = getIntent();

        Collections.addAll(listItems, defaultListItems);
//        for(int i = 0; i < defaultListItems.length; i++)
//        {
//            listItems.add(defaultListItems[i]);
//        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        ListView listView = findViewById(R.id.checklistItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listItemClickedListener);
        listView.setOnItemLongClickListener(listItemLongClickedListener);
    }

    public void onClickAddListItem(View view)
    {
        // create a dialog to ask the user for the list item's label
        AlertDialog.Builder textInputDialog = new AlertDialog.Builder(this);
        textInputDialog.setTitle("Add list item");

        // add an editable view to the dialog to accept user input
        final EditText textInput = new EditText(this );
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
        textInputDialog.setView(textInput);

        textInputDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // add user input to the list of items
                       listItems.add(textInput.getText().toString());
                       adapter.notifyDataSetChanged();
                   }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user cancelled
                    }
                });

        textInputDialog.show();
    }

    public void onClickDoneCreateList(View view)
    {

    }

    // implement OnItemClick() in the adapter listener class for editing a list item when it's short clicked
    private AdapterView.OnItemClickListener listItemClickedListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView parent, View v, int pos, long id)
        {
            // create a dialog to ask the user for the list item's label
            AlertDialog.Builder textInputDialog = new AlertDialog.Builder(parent.getContext());
            textInputDialog.setTitle("Edit list item");

            // add an editable view to the dialog to accept user input
            final EditText textInput = new EditText(parent.getContext() );
            textInput.setInputType(InputType.TYPE_CLASS_TEXT);
            textInput.setText(listItems.get(pos));
            textInputDialog.setView(textInput);

            final int constPos = pos;

            textInputDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listItems.set(constPos, textInput.getText().toString());
                    adapter.notifyDataSetChanged();
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // user cancelled
                        }
                    });

            textInputDialog.show();
        }
    };

    // implement OnItemClick() in the adapter listener class for editing a list item when it's long clicked
    private AdapterView.OnItemLongClickListener listItemLongClickedListener = new AdapterView.OnItemLongClickListener() {

        boolean confirmedDelete = false;

        @Override
        public boolean onItemLongClick(AdapterView parent, View v, int pos, long id)
        {
            // create a dialog to ask the user for the list item's label
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(parent.getContext());
            confirmDialog.setTitle("Remove list item?")
                    .setMessage(listItems.get(pos));

            final int constPos = pos;

            confirmDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // user confirmed to delete this list item
                    confirmedDelete = true;
                    listItems.remove(constPos);
                    adapter.notifyDataSetChanged();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // user cancelled the delete
                    confirmedDelete = false;
                }
            });
            confirmDialog.show();

            return true;
        }
    };
}
