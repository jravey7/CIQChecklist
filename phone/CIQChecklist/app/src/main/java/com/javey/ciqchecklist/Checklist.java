package com.javey.ciqchecklist;

import java.util.ArrayList;

public class Checklist {
    private String listName;
    private ArrayList<String> listItems;

    public Checklist(String name, ArrayList<String> items)
    {
        listName = name;
        listItems = items;
    }

    public String getListName()
    {
        return listName;
    }

    public void setListName(String newName)
    {
        listName = newName;
    }

    public ArrayList<String> getListItems()
    {
        return listItems;
    }

    public void setListItems(ArrayList<String> newItems, boolean clearList)
    {
        if(clearList)
        {
            listItems.clear();
        }

        listItems.addAll(newItems);
    }

    public int getNumListItems()
    {
        return listItems.size();
    }
}
