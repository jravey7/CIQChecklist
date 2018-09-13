package com.javey.ciqchecklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.garmin.android.connectiq.ConnectIQ;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void createNewList(View view)
    {
        Intent intent = new Intent(this, CreateListActivity.class);
        startActivity(intent);
    }

    public void viewEditLists(View view)
    {
        Intent intent = new Intent(this, ViewEditListsActivity.class);
        startActivity(intent);
    }
}
