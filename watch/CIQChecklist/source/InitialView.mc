using Toybox.WatchUi;

class InitialView extends WatchUi.View {

    function initialize() {
        View.initialize();
    }

    // Load your resources here
    function onLayout(dc) {
        setLayout(Rez.Layouts.MainLayout(dc));
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() {
    }

    // Update the view
    function onUpdate(dc) {
        // Call the parent onUpdate function to redraw the layout
        View.onUpdate(dc);
        
        // phone message handler requests for onUpdate() to arun when it gets a new checklist
        // if a new checklist has been received then start the list view
        if(PhoneMessageHandler.hasNewMessage())
        {
        	var phoneMessage = PhoneMessageHandler.getLastMessage();
        	
        	// the message passed representing a checklist shall be a list of strings in the format:
			// list name
			// number of list items
			// list item 1
			// list item 2
			// ...
			// list item n
		
			// first element is the list name
			var listName = phoneMessage.data[0].toString();
			
			// seconds element is the number of list items
			var nItems = phoneMessage.data[1].toNumber();
			
			// remaining elements are the list items
			var listItems = new[nItems];
			for( var i = 0; i < nItems; i++)
			{
				listItems[i] = phoneMessage.data[i + 2].toString();
			}
        	
        	// todo: shouldn't be a menu
			var checklistMenu = new WatchUi.CheckboxMenu({:title=>listName});
			for( var i = 0; i < listItems.size(); i++)
			{
				var item = new WatchUi.CheckboxMenuItem(listItems[i], null, i, false, {}); 
				checklistMenu.addItem(item);
			}
			WatchUi.pushView( checklistMenu, new ChecklistDelegate(), WatchUi.SLIDE_UP);
        }    
        
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() {
    }

}
