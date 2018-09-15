package com.javey.ciqchecklist;

import android.content.Context;
import android.widget.Toast;

import com.garmin.android.connectiq.ConnectIQ;
import com.garmin.android.connectiq.ConnectIQAdbStrategy;
import com.garmin.android.connectiq.IQApp;
import com.garmin.android.connectiq.IQDevice;
import com.garmin.android.connectiq.exception.InvalidStateException;
import com.garmin.android.connectiq.exception.ServiceUnavailableException;

import java.util.ArrayList;
import java.util.List;

public class ConnectIQInteraction {

    // unique identifier representing the ConnectIQ watch app (generated when creating watch app)
    private static String appID = "cca6574cd2264a7daa2a92ac3ab32d6f"; // CIQChecklist UUID
    private static IQApp app;

    private static ConnectIQ connectIQ;
    private static volatile boolean sdkReady = false;
    private static boolean initialized = false;

    private static Context appContext;

    private static IQDevice watch;

    public static void initialize(Context context) throws InterruptedException
    {
        if(!initialized)
        {
            appContext = context;

//            connectIQ = ConnectIQ.getInstance(appContext, ConnectIQ.IQConnectType.TETHERED);
//            connectIQ.setAdbPort(7381);

            connectIQ = ConnectIQ.getInstance(appContext, ConnectIQ.IQConnectType.WIRELESS);

            connectIQ.initialize(appContext, true, new ConnectIQ.ConnectIQListener() {

                @Override
                public void onSdkReady()
                {
                    Toast.makeText(appContext,"ConnectIQ SDK ready.", Toast.LENGTH_LONG).show();

                    sdkReady = true;

                    try {

                        watch = getFirstPaired();

                        // register for device status updates
                        connectIQ.registerForDeviceEvents(watch, new ConnectIQ.IQDeviceEventListener()
                        {
                           @Override
                           public void onDeviceStatusChanged(IQDevice device, IQDevice.IQDeviceStatus newStatus)
                           {
                               watch.setStatus(newStatus);

                               if( newStatus == IQDevice.IQDeviceStatus.CONNECTED )
                               {
                                   Toast.makeText(appContext, watch.getFriendlyName() + " connected.", Toast.LENGTH_SHORT).show();
                               }
                               else if( newStatus == IQDevice.IQDeviceStatus.NOT_CONNECTED )
                               {
                                   Toast.makeText(appContext, watch.getFriendlyName() + " not connected.", Toast.LENGTH_SHORT).show();
                               }
                               else
                               {
                                   Toast.makeText(appContext, "Watch status unknown. Please try reconnecting the watch to your phone.", Toast.LENGTH_LONG).show();
                               }
                           }
                        });

                        if( connectIQ.getDeviceStatus(watch) == IQDevice.IQDeviceStatus.CONNECTED)
                        {
                            connectIQ.getApplicationInfo(appID, watch, new ConnectIQ.IQApplicationInfoListener() {
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
                                    Toast.makeText(appContext, "App not installed on watch.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            System.out.print("device not connected (" + watch.getStatus() + ")");
                            if( watch.getFriendlyName().equals("vívoactive 3"))
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

    public static void writeListToWatch(Checklist checklist) throws InvalidStateException, ServiceUnavailableException
    {
        if(sdkReady)
        {
            System.out.println(watch.getFriendlyName() + " status: " + watch.getStatus());

            // todo: support other device types (currently just vivoactive 3)
            if((watch.getStatus() == IQDevice.IQDeviceStatus.CONNECTED) && watch.getFriendlyName().equals("vívoactive 3"))
            {
                connectIQ.openApplication(watch, app, new ConnectIQ.IQOpenApplicationListener() {
                    @Override
                    public void onOpenApplicationResponse(IQDevice iqDevice, IQApp iqApp, ConnectIQ.IQOpenApplicationStatus iqOpenApplicationStatus) {
                        System.out.println("Open application response: " + iqOpenApplicationStatus + " (app version: " + iqApp.version() + ")");
                    }
                });

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
                 Toast.makeText(appContext, "Watch not connected", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            System.out.println("Connect IQ SDK not initialized");
        }
    }

    public static boolean isInitialized()
    {
        return initialized;
    }

    public static IQDevice.IQDeviceStatus getWatchStatus()
    {
        if( watch != null) {
            return watch.getStatus();
        }
        return IQDevice.IQDeviceStatus.UNKNOWN;
    }

    public static boolean watchIsConnected()
    {
        if( watch != null) {
            return watch.getStatus() == IQDevice.IQDeviceStatus.CONNECTED;
        }
        return false;
    }

    private static IQDevice getFirstConnected()
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

    private static IQDevice getFirstPaired()
    {
        List<IQDevice> connectedList;
        IQDevice firstPaired = new IQDevice(0, "");
        try {
            connectedList = connectIQ.getKnownDevices();
            firstPaired = connectedList.get(0);
        } catch( Exception e)
        {
            e.printStackTrace();
        }

        return firstPaired;
    }
}
