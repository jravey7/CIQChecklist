using Toybox.Communications as Comms;
using Toybox.WatchUi as Ui;

module PhoneMessageHandler
{
	// stores the last uploaded checklist from phone (overwritten each upload)
	var lastUploadedChecklist = new Checklist("", new[1]);
	var checklistIsNew = false;
	
	function getLastChecklist()
	{
		checklistIsNew = false;
		return lastUploadedChecklist;
	}
	
	function hasNewChecklist()
	{
		return checklistIsNew;
	}
	
	// remove this function after testing
	function setChecklist(name, items)
	{
		checklistIsNew = true;
		lastUploadedChecklist = new Checklist(name, items);
	}

	function registerForMessages()
	{
		Comms.registerForPhoneAppMessages(onReceiveMessage);
	}
	
	function onReceiveMessage(data)
	{
		// the message passed representing a checklist shall be a list of strings in the format:
		// list name
		// number of list items
		// list item 1
		// list item 2
		// ...
		// list item n
	
		// first element is the list name
		var listName = data[0];
		
		// seconds element is the number of list items
		var nItems = data[1].toNumber();
		
		// remaining elements are the list items
		var listItems = new[nItems];
		for( var i = 0; i < nItems; i++)
		{
			listItems[i] = data[i + 2];
		}
		
		checklistIsNew = true;
		lastUploadedChecklist = new CIQChecklist(listName, nItems, listItems);
		
		Ui.requestUpdate();
	}

}