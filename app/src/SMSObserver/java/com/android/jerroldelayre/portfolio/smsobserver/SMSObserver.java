package com.android.jerroldelayre.portfolio.smsobserver;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jerrol on 5/2/2017.
 */

public class SMSObserver extends ContentObserver {
    private static final String TAG = "SMSObserver";
    private Context context;

    private static final Uri uri = Uri.parse("content://sms");
    private static final String COLUMN_TYPE = "type";
    private static final int MESSAGE_TYPE_SENT = 2;
    private static final int MESSAGE_TYPE_RECEIVED = 1;

    public SMSObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        //Log.d(TAG, "onChange");
        //Toast.makeText(context, "TEST", Toast.LENGTH_LONG).show();
        Cursor cursor = null;

        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                    cursor = context.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                        Log.d(TAG, "Type: " + type);
                        if (type == MESSAGE_TYPE_SENT) {
                            // Sent message
                            Log.d(TAG, "selfChange " + selfChange);
                            Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))));
                            Log.d(TAG, "Message Sent body: " + cursor.getString(cursor.getColumnIndex("body")) );

                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                            Log.d(TAG, "Message sent date: " + sdf.format(date));
                        } else if (type == MESSAGE_TYPE_RECEIVED) {
                            Log.d(TAG, "selfChange " + selfChange);
                            Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))));
                            Log.d(TAG, "Message Receive body: " + cursor.getString(cursor.getColumnIndex("body")) );

                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
                            Log.d(TAG, "Message Receive date: " + sdf.format(date));
                        }
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
