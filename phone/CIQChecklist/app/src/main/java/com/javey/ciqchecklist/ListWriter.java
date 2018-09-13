package com.javey.ciqchecklist;

import android.content.Context;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.ArrayList;

import com.javey.ciqchecklist.Checklist;

public class ListWriter {

    public static void writeListToFile(Context context, Checklist checklist)
    {
        String listName = checklist.getListName();
        ArrayList<String> list = checklist.getListItems();

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
