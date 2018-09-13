using Toybox.WatchUi as Ui;

class CIQChecklistDelegate extends Ui.BehaviorDelegate {

	// class unused for now

    function initialize() {
        BehaviorDelegate.initialize();
    }

	// vivoactive3: hold screen (long touch)
    function onMenu() {
        
    }
    
    // vivoactive3: tap screen (short touch)
    function onSelect() {
    	if( PhoneMessageHandler.hasNewChecklist())
    	{
	    	// todo: shouldn't be a menu
			var checklistMenu = new Ui.Menu();
			checklistMenu.setTitle(listName);
			for( var i = 0; i < nItems; i++)
			{
				checklistMenu.addItem(listItems[i], i);
			}
			Ui.pushView( checkListMenu, new CIQChecklistMenuDelegate(), Ui.SLIDE_UP);
    	}
    }

}