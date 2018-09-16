
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
    
    
    // stores list in one array with the name as the first element
    // and the items following
    function toArray()
    {
    	var listSize = getNumListItems();
    	var array = new [listSize + 1];
    	array[0] = getListName();
    	for(var i = 0; i < listSize; i++)
    	{
    		array[i + 1] = listItems[i];
    	}
    	
    	return array;
    }
}