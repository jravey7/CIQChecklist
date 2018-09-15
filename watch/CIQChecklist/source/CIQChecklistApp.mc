using Toybox.Application as App;
using Toybox.WatchUi as Ui;
using Toybox.Communications;
using Toybox.System;

var phoneMethod;

class CIQChecklistApp extends App.AppBase {

    function initialize() {
        App.AppBase.initialize();
        
        phoneMethod = method(:onReceiveMessage);
        
        if( Communications has :registerForPhoneAppMessages)
        {
        	Communications.registerForPhoneAppMessages(phoneMethod);
        }
        else
        {
        	// crash if registerForPhoneAppMessages interface isn't available
        	foo = bar;
        }
    }
    
    function onReceiveMessage(msg)
	{
		// the message passed representing a checklist shall be a list of strings in the format:
		// list name
		// number of list items
		// list item 1
		// list item 2
		// ...
		// list item n
	
		// first element is the list name
		var listName = msg.data[0].toString();
		
		// seconds element is the number of list items
		var nItems = msg.data[1].toNumber();
		
		// remaining elements are the list items
		var listItems = new[nItems];
		for( var i = 0; i < nItems; i++)
		{
			listItems[i] = msg.data[i + 2].toString();
		}
		
		PhoneMessageHandler.setChecklist(listName, listItems);
		
		Ui.requestUpdate();
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
