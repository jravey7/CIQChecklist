package com.javey.ciqchecklist;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.ArrayList;

public class ListWriter {

    public static void writeListToFile(Context context, String listName, ArrayList<String> list)
    {
        try {

            FileOutputStream outFile = context.openFileOutput(listName + ".txt", Context.MODE_PRIVATE);
            String newLine = "\n";

            ///// format:
            // number of list items
            // list item 1
            // list item 2
            // ...
            // list item n

            // write number of list items
            outFile.write(Integer.toString(list.size()).getBytes());

            // write each list item
            for(int i = 0; i < list.size(); i++)
            {
                outFile.write( newLine.getBytes() );
                outFile.write( list.get(i).getBytes() );
            }

            outFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
