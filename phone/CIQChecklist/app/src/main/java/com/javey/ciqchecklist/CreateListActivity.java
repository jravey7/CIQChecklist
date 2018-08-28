package com.javey.ciqchecklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateListActivity extends AppCompatActivity {

    private Integer addCount = 0;
    private Integer doneCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        Intent intent = getIntent();
    }

    public void onClickAddListItem(View view)
    {
        this.addCount++;
        EditText editTextItem = findViewById(R.id.editListItem);
        String displayText = "Add count: " + addCount;
        editTextItem.setText(displayText);
    }

    public void onClickDoneCreateList(View view)
    {
        this.doneCount++;
        EditText editListName = findViewById(R.id.editListName);
        String displayText = "Done count: " + doneCount;
        editListName.setText(displayText);
    }
}
