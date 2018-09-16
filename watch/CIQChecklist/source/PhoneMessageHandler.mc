using Toybox.Application as App;
using Toybox.WatchUi as Ui;
using Toybox.Communications;
using Toybox.System;
using Toybox.Lang;

module PhoneMessageHandler
{
	// stores the last sent message from phone (overwritten each upload)
	// to use: access the message's data field (i.e. message.data)
	// message.data can be a list, which is accessed as message.data[i]
	// which can then be converted to different types 
	// (i.e. message.data[i].toString(); message.data[i].toNumber())
	var message;
	
	// a flag field to identify when a new message is available 
	var isNewMessage = false; 
	
	// an optional user created callback function (todo: implemented functionality)
	var userCreatedCallback = null;
	
	var phoneCallback;
	
	function registerForMessages()
	{
	    if( Communications has :registerForPhoneAppMessages)
        {
        	phoneCallback = method(:onReceiveMessage);
        	Communications.registerForPhoneAppMessages(phoneCallback);
        }
        else
        {
        	// crash if registerForPhoneAppMessages interface isn't available
        	throw new Lang.Exception();
        }
	}
	
	// callback function to receive a phone message
	function onReceiveMessage(msg)
	{
		isNewMessage = true;
		message = msg;
		WatchUi.requestUpdate();
		
		if(userCreatedCallback != null)
		{
			userCreatedCallback.invoke();
		}
	}
	
	// returns the last message sent from the phone
	// see "var message" declaration above for usage 
	function getLastMessage()
	{
		isNewMessage = false;
		return message;
	}
	
	function hasNewMessage()
	{
		return isNewMessage;
	}
	
	function setLastMessage(message)
	{
		me.message = message;
		isNewMessage = true;
	}

}