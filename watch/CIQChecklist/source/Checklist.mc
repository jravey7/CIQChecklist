
class Checklist
{
	var listName;
	var nItems;
	var listItems;
	
	function initialize(name, numItems, arrayOfItems)
	{
		listName = name;
		nItems = numItems;
		listItems = arrayOfItems;
	}
	
	function getListName()
    {
        return listName;
    }

    function setListName(newName)
    {
        listName = newName;
    }

    function getListItems()
    {
        return listItems;
    }

    function setListItems(newItems, clearList)
    {
        if(clearList)
        {
            listItems.clear();
        }

        listItems.addAll(newItems);
    }

    function getNumListItems()
    {
        return listItems.size();
    }
}