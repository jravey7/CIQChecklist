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
    	var list = new [2];
    	list[0] = "Connect IQ";
    	list[1] = "Garmin";
    	PhoneMessageHandler.setChecklist("Test", list);
    	Ui.requestUpdate();
    }

}