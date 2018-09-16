using Toybox.WatchUi;
using Toybox.Communications;

class ChecklistDelegate extends WatchUi.Menu2InputDelegate {

	// class unused for now

    function initialize() {
        Menu2InputDelegate.initialize();
    }
    
    // vivoactive3: tap screen (short touch)
    function onSelect(item) {
    
        		
    	WatchUi.requestUpdate();
    }

}