package com.android.jerroldelayre.portfolio.bluetoothdisabler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BluetoothDisablerReceiver extends BroadcastReceiver {

    private static final String TAG = "BDReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, action);
        if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                Log.i(TAG, "Bluetooth disabled");
            } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_TURNING_ON) {
                Log.i(TAG, "Bluetooth is turning on");
            } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {
                Log.i(TAG, "Bluetooth is turned on");
                context.startService(new Intent(context, BluetoothDisablerService.class));
            }
            Log.i(TAG, "Bluetooth action state changed: " + action);
        } else if (action.equals("android.net.wifi.STATE_CHANGE")) {
            context.startService(new Intent(context, BluetoothDisablerService.class));
        }
    }
}