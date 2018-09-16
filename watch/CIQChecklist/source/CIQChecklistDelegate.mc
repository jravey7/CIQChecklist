using Toybox.WatchUi;
using Toybox.Communications;

class CIQChecklistDelegate extends WatchUi.BehaviorDelegate {

	// class unused for now

    function initialize() {
        BehaviorDelegate.initialize();
    }

	// vivoactive3: hold screen (long touch)
    function onMenu() {
    
    	// test list
        var newMessage = new Communications.PhoneAppMessage();
    	newMessage.data = new [5];
    	newMessage.data[0] = "Test list";
    	newMessage.data[1] = 3;
    	newMessage.data[2] = "item 1";
    	newMessage.data[3] = "item 2";
    	newMessage.data[4] = "item 3";
    	
    	PhoneMessageHandler.setLastMessage(newMessage);
    }
    
    // vivoactive3: tap screen (short touch)
    function onSelect() {
		
		// first element is the list name
		var listName = "Vitamins";

		Application.Storage.keySet();

    	// get list items from storage
    	var listItems = Application.Storage.getValue(listName);
    	
    	// create checklist as a built-in CheckboxMenu (todo: custom menu? maybe based on user app settings)
		var checklistMenu = new WatchUi.CheckboxMenu({:title=>listName});
		for( var i = 0; i < listItems.size(); i++)
		{
			var item = new ChecklistItem(listItems[i], null, i, false); 
			checklistMenu.addItem(item);
		}
		WatchUi.pushView( checklistMenu, new ChecklistDelegate(), WatchUi.SLIDE_UP);
    }

}