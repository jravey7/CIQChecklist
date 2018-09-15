using Toybox.Application as App;
using Toybox.WatchUi as Ui;
using Toybox.Communications;
using Toybox.System;

class CIQChecklistApp extends App.AppBase {

    function initialize() {
        App.AppBase.initialize();
        
       	if( Communications has :registerForPhoneAppMessages)
        {
        	var phoneCallback = method(:onReceiveMessage);
        	Communications.registerForPhoneAppMessages(phoneCallback);
        }
        else
        {
        	// crash if registerForPhoneAppMessages interface isn't available
        	foo = bar;
        }
    }

	// callback function to receive a phone message
	function onReceiveMessage(msg)
	{
		PhoneMessageHandler.isNewMessage = true;
		PhoneMessageHandler.message = msg;
		WatchUi.requestUpdate();
	}

    // onStart() is called on application start up
    function onStart(state) {
    	    
    }

    // onStop() is called when your application is exiting
    function onStop(state) {
    }

    // Return the initial view of your application here
    function getInitialView() {
        return [ new InitialView(), new CIQChecklistDelegate() ];
    }

}
