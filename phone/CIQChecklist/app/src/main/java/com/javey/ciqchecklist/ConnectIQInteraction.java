package com.javey.ciqchecklist;

import android.content.Context;
import android.widget.Toast;

import com.garmin.android.connectiq.ConnectIQ;
import com.garmin.android.connectiq.IQApp;
import com.garmin.android.connectiq.IQDevice;
import com.garmin.android.connectiq.exception.InvalidStateException;
import com.garmin.android.connectiq.exception.ServiceUnavailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConnectIQInteraction {

    // unique identifier representing the ConnectIQ watch app (generated when creating watch app)
    private static String appID = new String("cca6574cd2264a7daa2a92ac3ab32d6f"); // CIQChecklist UUID
    private static IQApp app;

    private static ConnectIQ connectIQ;
    private static volatile boolean sdkReady = false;
    private static boolean initialized = false;

    private static Context appContext;

    public ConnectIQInteraction(Context context) throws InterruptedException
    {
        if(!initialized)
        {
            appContext = context;

            connectIQ = ConnectIQ.getInstance(appContext, ConnectIQ.IQConnectType.WIRELESS);

            connectIQ.initialize(appContext, true, new ConnectIQ.ConnectIQListener() {

                @Override
                public void onSdkReady()
                {
                    Toast.makeText(appContext,"ConnectIQ SDK ready.", Toast.LENGTH_LONG).show();

                    sdkReady = true;

                    List<IQDevice> paired = null;
                    try {
                        paired = connectIQ.getKnownDevices();

                        IQDevice firstPaired = paired.get(0);

                        // register for device status updates
//                        connectIQ.registerForDeviceEvents(firstPaired, new ConnectIQ.IQDeviceEventListener()
//                        {
//                           @Override
//                           public void onDeviceStatusChanged(IQDevice device, IQDevice.IQDeviceStatus newStatus)
//                           {
//                               System.out.println(device.getFriendlyName() + " status: " + newStatus);
//                           }
//                        });

                        if( connectIQ.getDeviceStatus(firstPaired) == IQDevice.IQDeviceStatus.CONNECTED)
                        {
                            Toast.makeText(appContext, firstPaired.getFriendlyName() + " is connected.", Toast.LENGTH_LONG).show();

                            connectIQ.getApplicationInfo(appID, firstPaired, new ConnectIQ.IQApplicationInfoListener() {
                                @Override
                                public void onApplicationInfoReceived(IQApp iqApp) {
                                    if( iqApp != null)
                                    {
                                        app = iqApp;
                                        System.out.print("App info retrieved (");

                                        if( app.getStatus() == IQApp.IQAppStatus.INSTALLED)
                                        {
                                            System.out.println("installed: " + app.version() + ").");
                                        }
                                        else
                                        {
                                            System.out.println("not installed).");
                                        }
                                    }
                                }

                                @Override
                                public void onApplicationNotInstalled(String s) {
                                    System.out.println("App not installed.");
                                }
                            });
                        }
                        else
                        {
                            System.out.print("device not connected (" + firstPaired.getStatus() + ")");
                            if( firstPaired.getFriendlyName().equals("vívoactive 3"))
                            {
                                System.out.println(" (but is paired to phone).");
                            }
                            else
                            {
                                System.out.println(" (not paired).");
                            }
                        }
                    } catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onInitializeError(ConnectIQ.IQSdkErrorStatus var1)
                {
                    System.out.println("ConnectIQ initialization error: " + var1);
                }

                @Override
                public void onSdkShutDown()
                {
                    sdkReady = false;
                    initialized = false;

                    System.out.println("ConnectIQ SDK shutdown.");
                }
            });
        }
        else
        {
            sdkReady = true;
        }

        initialized = true;
    }

    public void writeListToWatch(Checklist checklist) throws InvalidStateException, ServiceUnavailableException
    {
        IQDevice watch = getFirstConnected();
        connectIQ.openApplication(watch, app, new ConnectIQ.IQOpenApplicationListener() {
            @Override
            public void onOpenApplicationResponse(IQDevice iqDevice, IQApp iqApp, ConnectIQ.IQOpenApplicationStatus iqOpenApplicationStatus) {
                System.out.println("Open application response: " + iqOpenApplicationStatus + " (app version: " + iqApp.version() + ")");
            }
        });

        // todo: support other device types (currently just vivoactive 3)
        if(sdkReady && watch.getFriendlyName().equals("vívoactive3")) {

            IQDevice.IQDeviceStatus status = connectIQ.getDeviceStatus(watch);

            System.out.println(watch.getFriendlyName() + " status: " + status);

            if(status == IQDevice.IQDeviceStatus.CONNECTED)
            {
                List<String> message = new ArrayList<>();
                message.add(checklist.getListName()); // name
                message.add(Integer.toString(checklist.getNumListItems())); // num items
                message.addAll(checklist.getListItems()); // items

                System.out.println("Attempting to send list to watch now.");
                connectIQ.sendMessage(watch, app, message, new ConnectIQ.IQSendMessageListener() {
                    @Override
                    public void onMessageStatus(IQDevice iqDevice, IQApp iqApp, ConnectIQ.IQMessageStatus iqMessageStatus) {
                        Toast.makeText(appContext, iqMessageStatus.name(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                System.out.println("Watch not connected.");
            }
        }
    }

    private IQDevice getFirstConnected()
    {
        List<IQDevice> connectedList;
        IQDevice firstConnected = new IQDevice(0, "");
        try {
            connectedList = connectIQ.getConnectedDevices();
            firstConnected = connectedList.get(0);
        } catch( Exception e)
        {
            e.printStackTrace();
        }

        return firstConnected;
    }

    public static boolean isInitialized()
    {
        return initialized;
    }
}
