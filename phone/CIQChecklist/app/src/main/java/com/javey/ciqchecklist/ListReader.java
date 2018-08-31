package com.javey.ciqchecklist;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ListReader {
    public static ArrayList<String> readListFromFile(Context context, String listName)
    {
        ArrayList<String> list = new ArrayList<>();

        try {
            FileInputStream inFile = context.openFileInput(listName + ".txt");
            Scanner scanner = new Scanner(inFile);

            // first line specifies the number of list items
            int nItems = Integer.getInteger(scanner.next());

            // read each list item into the list
            for(int i = 0; i < nItems; i++)
            {
                list.add(scanner.next());
            }

            inFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;
    }
}
