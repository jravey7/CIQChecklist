using Toybox.WatchUi as Ui;
using Toybox.Communications;

class CIQChecklistDelegate extends Ui.BehaviorDelegate {

	// class unused for now

    function initialize() {
        BehaviorDelegate.initialize();
    }

	// vivoactive3: hold screen (long touch)
    function onMenu() {
        Ui.popView();
    }
    
    // vivoactive3: tap screen (short touch)
    function onSelect() {
    	
    	var newMessage = new Communications.PhoneAppMessage();
    	newMessage.data = new [5];
    	newMessage.data[0] = "Test list";
    	newMessage.data[1] = 3;
    	newMessage.data[2] = "item 1";
    	newMessage.data[3] = "item 2";
    	newMessage.data[4] = "item 3";
    	
    	PhoneMessageHandler.setLastMessage(newMessage);
    	
    	Ui.requestUpdate();
    }

}