package com.example.marianolopez.bluetest;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Created by MarianoLopez on 4/4/2017.
 */

public class ConnectBT {//extends Thread {
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private Context context;

    public ConnectBT(Context context,BluetoothDevice device) throws IOException{
        mmDevice = device;
        this.context = context;
    }

   public Boolean connect() throws IOException{
       mmSocket = mmDevice.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
       Log.i("bt","init");
       mmSocket.connect();
       return mmSocket.isConnected();
   }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(String string) throws IOException{
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8); // Java 7+ only
        this.mmSocket.getOutputStream().write(bytes);
    }
}
