package com.example.gyth.notifyme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;

    private Button mNotifyButton;
    private Button mUpdateButton;
    private Button mCancelButton;

    private static final String NOTIFICATION_GUIDE_URL = "https://developer.android.com/design/patterns/notifications.html";

    // Constant variable to represent the update notification
    // for the broadcast receiver
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.gyth.notifyme.ACTION_UPDATE_NOTIFICATION";
    
    // Initialize the broadcast receiver using the default constructor
    private NotificationReceiver mReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiating the notify button
        mNotifyButton = (Button) findViewById(R.id.notify);
        // Setting an onClickListener to the button to
        // call sendNotification method()
        mNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        mNotifyButton.setEnabled(true);

        // Registering the Broadcast Receiver to receive the
        // ACTION_UPDATE_NOTIFICATION intent
        registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        // Instatiating update button
        mUpdateButton = (Button) findViewById(R.id.update);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });
        mUpdateButton.setEnabled(false);

        // Instatiating cancel button
        mCancelButton = (Button) findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });
        mCancelButton.setEnabled(false);

        // Instantiating the NotificationManager
        // using getSystemService()
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void sendNotification(){
        // Intent to launch the main activity class
        Intent notifificationIntent = new Intent(this, MainActivity.class);

        // Getting a PendingIntent using getActivity()
        // pass the notification ID
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notifificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(NOTIFICATION_GUIDE_URL));
        PendingIntent learnMorePendingIntent = PendingIntent.getActivity(
                this, NOTIFICATION_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);

        // Create a broadcast intent using the custom update action
        // Get a PendingIntent using getBroadcast()
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(
                this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT
        );

        // Creating and instantiating the NotificationBuilder
        // Setting the notification title
        // Setting the notification text
        // Setting the notification icon
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("You have been notified")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_learn, getString(R.string.learn_more), learnMorePendingIntent)
                .addAction(R.drawable.ic_update, "Update", updatePendingIntent);

        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);

        mNotifyButton.setEnabled(false);
        mUpdateButton.setEnabled(true);
        mCancelButton.setEnabled(true);
    }

    public void updateNotification(){
        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(), R.drawable.mascot_1);

        // Intent to launch the main activity class
        Intent notifificationIntent = new Intent(this, MainActivity.class);

        // Getting a PendingIntent using getActivity()
        // pass the notification ID
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notifificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        // Creating and instantiating the NotificationBuilder
        // Setting the notification title
        // Setting the notification text
        // Setting the notification icon
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("You have been notified")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(androidImage)
                        .setBigContentTitle("Notification Updated!"));

        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);

        mNotifyButton.setEnabled(false);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(true);
    }

    public void cancelNotification(){
        mNotifyManager.cancel(NOTIFICATION_ID);

        mNotifyButton.setEnabled(true);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(false);
    }

    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver(){

        }
        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }

}
