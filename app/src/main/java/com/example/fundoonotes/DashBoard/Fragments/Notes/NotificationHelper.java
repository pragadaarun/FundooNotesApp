package com.example.fundoonotes.DashBoard.Fragments.Notes;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.example.fundoonotes.DashBoard.Activity.HomeActivity;
import com.example.fundoonotes.R;
import com.example.fundoonotes.UI.Activity.SharedPreferenceHelper;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);

    private String title = sharedPreferenceHelper.getNoteTitle();
    private String description = sharedPreferenceHelper.getNoteDescription();

    private NotificationManager mManager;


    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_fundoo_icon);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setContentIntent(PendingIntent.getActivity(this, // Context from onReceive method.
                0, new Intent(this, HomeActivity.class), 0));
        return builder;
    }
}