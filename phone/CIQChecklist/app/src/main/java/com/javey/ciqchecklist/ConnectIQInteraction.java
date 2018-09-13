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

                        IQDevice watch = paired.get(0);
                        if( watch.getStatus() == IQDevice.IQDeviceStatus.CONNECTED) {

                            connectIQ.getApplicationInfo(appID, watch, new ConnectIQ.IQApplicationInfoListener() {
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
                            System.out.print("VívoActive 3 not connected");
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
                }
            });

            initialized = true;
        }
    }

    public void writeListToWatch(Checklist checklist) throws InvalidStateException, ServiceUnavailableException
    {

        if(sdkReady) {

            List<IQDevice> paired = null;
            try {
                paired = connectIQ.getKnownDevices();
            } catch (InvalidStateException e) {
                e.printStackTrace();
            } catch (ServiceUnavailableException e) {
                e.printStackTrace();
            }

            IQDevice watch = paired.get(0);

            System.out.println(watch.getStatus());

            // todo: support other device types (currently just vivoactive 3)
            if(watch.getStatus() == IQDevice.IQDeviceStatus.CONNECTED)
            {
                List<String> message = new ArrayList<>();
                message.add(checklist.getListName()); // name
                message.add(Integer.toString(checklist.getNumListItems())); // num items
                message.addAll(checklist.getListItems()); // items

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
