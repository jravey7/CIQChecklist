package com.javey.ciqchecklist;

import android.content.Context;

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

    private ConnectIQ connectIQ;
    private static IQDevice watch;
    private volatile boolean sdkReady = false;
    private static boolean initialized = false;

    public ConnectIQInteraction(Context context) throws InterruptedException
    {
        connectIQ = ConnectIQ.getInstance(context, ConnectIQ.IQConnectType.WIRELESS);

        if(!initialized)
        {
            connectIQ.initialize(context, true, new ConnectIQ.ConnectIQListener() {

                @Override
                public void onSdkReady()
                {
                    sdkReady = true;

                    List<IQDevice> paired = null;
                    try {
                        paired = connectIQ.getKnownDevices();

                        IQDevice firstPaired = paired.get(0);
                        connectIQ.registerForDeviceEvents(firstPaired, new ConnectIQ.IQDeviceEventListener()
                        {
                           @Override
                           public void onDeviceStatusChanged(IQDevice device, IQDevice.IQDeviceStatus newStatus)
                           {
                               System.out.println(device.getFriendlyName() + " status: " + newStatus);

                               if(newStatus == IQDevice.IQDeviceStatus.CONNECTED)
                               {
                                   watch = new IQDevice(device.getDeviceIdentifier(), device.getFriendlyName());
                                   watch.setStatus(IQDevice.IQDeviceStatus.CONNECTED);
                               }
                           }
                        });

                        if( firstPaired.getStatus() == IQDevice.IQDeviceStatus.CONNECTED)
                        {

                            connectIQ.getApplicationInfo(appID, firstPaired, new ConnectIQ.IQApplicationInfoListener() {
                                @Override
                                public void onApplicationInfoReceived(IQApp iqApp) {
                                    app = iqApp;
                                    System.out.println("App info retrieved (" + app.getApplicationId() + ").");
                                }

                                @Override
                                public void onApplicationNotInstalled(String s) {
                                    System.out.println("App not installed.");
                                }
                            });
                        }
                        else
                        {
                            System.out.print("vívoactive 3 not connected");
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

        if(sdkReady && watch != null) {

            System.out.println(watch.getFriendlyName() + " status(2): " + watch.getStatus());

            // todo: support other device types (currently just vivoactive 3)
            if(watch.getStatus() == IQDevice.IQDeviceStatus.CONNECTED)
            {
                List<String> message = new ArrayList<>();
                message.add(checklist.getListName()); // name
                message.add(Integer.toString(checklist.getNumListItems())); // num items
                message.addAll(checklist.getListItems()); // items

                System.out.println("Attempting to send list to watch now.");
                connectIQ.sendMessage(watch, app, message, new ConnectIQ.IQSendMessageListener() {
                    @Override
                    public void onMessageStatus(IQDevice iqDevice, IQApp iqApp, ConnectIQ.IQMessageStatus iqMessageStatus) {
                        System.out.println(iqDevice.getFriendlyName() + ":" + iqApp.toString() + ": Status = " + iqMessageStatus.name());
                    }
                });
            }
            else {
                System.out.println("Watch not connected.");
            }
        }
    }

    public static boolean isInitialized()
    {
        return initialized;
    }
}
