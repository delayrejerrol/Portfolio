package com.android.jerroldelayre.portfolio.smsobserver;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    public static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        /*try {
            Log.d(TAG, "SMSReceive Trigger");
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Log.d(TAG, "Incoming Message");
            } else if (intent.getAction().equals("android.provider.Telephony.SMS_SENT")) {
                Log.d(TAG, "Outgoing SMS");
            }
        } catch (Exception e) {
            Log.e(TAG, "onReceiver method cannot be processed");
        }*/
        //Intent i = new Intent(context, SMSSendService.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addCategory(Intent.CATEGORY_LAUNCHER);
        //context.startActivity(i);
        //context.startService(i);
    }
}
