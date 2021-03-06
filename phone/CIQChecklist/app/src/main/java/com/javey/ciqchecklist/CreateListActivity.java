package com.javey.ciqchecklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class CreateListActivity extends AppCompatActivity {

    //String[] defaultListItems = {"List Item #1"};

    String[] defaultListItems = {"Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato",
            "Lettuce",
            "Tomato ",
            "Lettuce",
            "Tomato",
            "Tomato ",
            "Lettuce"};

    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter adapter;

    // false if the list is being created (new)
    // true if the list is just being edited
    boolean editList = false;
    String editListName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        Intent intent = getIntent();
        editList = intent.getBooleanExtra("editList", false);

        if( editList )
        {
            // populate list with existing values
            editListName = intent.getStringExtra("listName");

            if( editListName.isEmpty() )
            {
                throw new IllegalArgumentException("Edit list: List name cannot be empty.");
            }
            listItems = ListReader.readListFromFile(this, editListName);

            EditText listNameEditText = findViewById(R.id.editListName);
            listNameEditText.setText(editListName);

            // change title to Edit List (instead of Create List)
            setTitle("Edit List");
        }
        else
        {
            // populate list with default values
            Collections.addAll(listItems, defaultListItems);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        ListView listView = findViewById(R.id.checklistItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listItemClickedListener);
        listView.setOnItemLongClickListener(listItemLongClickedListener);

        // initialize connect iq sdk
        try {
            ConnectIQInteraction.initialize(getApplicationContext());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onClickAddListItem(View view)
    {
        // create a dialog to ask the user for the list item's label
        AlertDialog.Builder textInputDialogBuilder = new AlertDialog.Builder(this);
        textInputDialogBuilder.setTitle("Add list item");

        // add an editable view to the dialog to accept user input
        final EditText textInput = new EditText(this );
        textInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        textInputDialogBuilder.setView(textInput);
        textInputDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

        final AlertDialog textInputDialog = textInputDialogBuilder.create();
        textInputDialog.show();

        textInputDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    textInputDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    textInputDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });
    }


    public void onClickDoneCreateList(View view)
    {
        // make sure the name is populated and unique, and the list is populated
        EditText editTextListName = (EditText) findViewById(R.id.editListName);
        final String newListName = editTextListName.getText().toString();
        if( !newListName.isEmpty() && !listItems.isEmpty()
                // can only overwrite lists if we are currently editing a list and the list's name is either unique or the same as it used to be
            && (isUniqueListName(newListName) || (editList && editListName.equals(newListName))))
        {

            final Checklist checklist = new Checklist(newListName, listItems);

            if( ConnectIQInteraction.watchIsConnected() ) {

                AlertDialog.Builder uploadToWatchAlert = new AlertDialog.Builder(this);
                uploadToWatchAlert.setMessage("Upload to watch?");
                uploadToWatchAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // save list
                        ListWriter.writeListToFile(CreateListActivity.this, checklist);

                        // upload to watch
                        try {
                            ConnectIQInteraction.writeListToWatch(checklist);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // end this activity and go back to landing page
                        finish();
                    }
                });
                uploadToWatchAlert.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // save list but do not upload to watch
                        ListWriter.writeListToFile(CreateListActivity.this, checklist);
                        finish();
                    }
                });
                uploadToWatchAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancelled, do nothing
                    }
                });

                uploadToWatchAlert.show();
            }
            else
            {
                AlertDialog.Builder savedListAlert = new AlertDialog.Builder(this);
                savedListAlert.setMessage("Save list for later? No compatible watches detected. " +
                        "(Please check that the watch is connected in your phone's settings and reconnect if needed.)");
                savedListAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // save list
                        ListWriter.writeListToFile(CreateListActivity.this, checklist);

                        // end this activity and go back to landing page
                        finish();
                    }
                });
                savedListAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancelled, do nothing
                    }
                });

                savedListAlert.show();
            }

        }
        else
        {
            if( newListName.isEmpty())
            {
                AlertDialog.Builder invalidListAlert = new AlertDialog.Builder(this);
                invalidListAlert.setMessage("Invalid list name (empty).");
                invalidListAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                invalidListAlert.show();
            }
            else if(listItems.isEmpty())
            {
                AlertDialog.Builder invalidListAlert = new AlertDialog.Builder(this);
                invalidListAlert.setMessage("List is empty.");
                invalidListAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                invalidListAlert.show();
            }
            else if(!isUniqueListName(newListName))
            {
                AlertDialog.Builder invalidListAlert = new AlertDialog.Builder(this);
                invalidListAlert.setMessage("Invalid list name (already exists).");
                invalidListAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                invalidListAlert.show();
            }
        }
    }

    private boolean isUniqueListName(String name)
    {
        File internalStorageDirectory = this.getFilesDir();
        File[] allFiles = internalStorageDirectory.listFiles();
        for(int i = 0;  i < allFiles.length; i++)
        {
            if(allFiles[i].getName().equals(name + ".txt"))
            {
                return false;
            }
        }
        return true;
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
