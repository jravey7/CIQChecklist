package com.javey.ciqchecklist;

import android.content.Context;

import com.garmin.android.connectiq.ConnectIQ;
import com.garmin.android.connectiq.IQDevice;
import com.garmin.android.connectiq.exception.InvalidStateException;
import com.garmin.android.connectiq.exception.ServiceUnavailableException;

import java.util.List;

public class ConnectIQInteraction {

    boolean sdkReady = false;

    public void writeListToWatch(Context context) throws InvalidStateException, ServiceUnavailableException, InterruptedException {


        ConnectIQ connectIQ = ConnectIQ.getInstance(context, ConnectIQ.IQConnectType.WIRELESS);
        connectIQ.initialize(context, true, new ConnectIQ.ConnectIQListener() {

            @Override
            public void onSdkReady()
            {
                System.out.println("ConnectIQ SDK ready");
                sdkReady = true;
            }

            @Override
            public void onInitializeError(ConnectIQ.IQSdkErrorStatus var1)
            {
                System.out.println("ConnectIQ initialization error: " + var1);
            }

            @Override
            public void onSdkShutDown()
            {
                System.out.println("ConnectIQ SDK shutdown");
            }
        });

        List<IQDevice> paired = connectIQ.getKnownDevices();

        if( paired != null && paired.size() > 0)
        {
            for( IQDevice device : paired ) {
                IQDevice.IQDeviceStatus status = connectIQ.getDeviceStatus(device);
                System.out.println(device.getFriendlyName());
            }
        }

    }
}
