package com.android.jerroldelayre.portfolio.smsobserver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.util.Log;

import com.android.jerroldelayre.portfolio.R;

public class SMSSendService extends Service {
    private static final String TAG = "SMSSendService";

    public SMSSendService() {

    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate called");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called");
        /*ContentObserver co = new SMSObserver(new Handler(), getApplicationContext());
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms"), true, co);*/

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
