using Toybox.WatchUi as Ui;

class InitialView extends Ui.View {

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
        if(PhoneMessageHandler.hasNewChecklist())
        {
        	var checklist = PhoneMessageHandler.getLastChecklist();
        	
        	// todo: shouldn't be a menu
			var checklistMenu = new Ui.Menu();
			checklistMenu.setTitle(checklist.getListName());
			var listItems = checklist.getListItems();
			for( var i = 0; i < listItems.size(); i++)
			{
				checklistMenu.addItem(listItems[i], i);
			}
			Ui.pushView( checklistMenu, new CIQChecklistMenuDelegate(), Ui.SLIDE_UP);
        }    
        
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() {
    }

}
