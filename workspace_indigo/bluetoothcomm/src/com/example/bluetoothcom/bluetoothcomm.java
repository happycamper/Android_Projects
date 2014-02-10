package com.example.bluetoothcom;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class bluetoothcomm extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final int REQUEST_ENABLE_BT = 0;

        Context context = getApplicationContext();
        CharSequence text = "Bluetooth is not supported or is not turned on";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
     
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
        	   toast.show();
        }
        
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
     // If there are paired devices
     if (pairedDevices.size() > 0) {
    	 Set<BluetoothDevice> mArrayAdapter;
         // Loop through paired devices
         for (BluetoothDevice device : pairedDevices) {
             // Add the name and address to an array adapter to show in a ListView
             mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
         }
     }
    }
}