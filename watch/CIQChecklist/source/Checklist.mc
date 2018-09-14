
class Checklist
{
	var listName;
	var listItems;
	
	function initialize(name, arrayOfItems)
	{
		listName = name;
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