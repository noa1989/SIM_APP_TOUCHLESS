/*
Copyright 2011-2013 Pieter Pareit

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
*/

package cl.genesys.appchofer.gui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import net.vrallev.android.cat.Cat;

import java.net.InetAddress;

import cl.genesys.appchofer.FsService;
import cl.genesys.appchofer.FsSettings;
import cl.genesys.appchofer.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class FsNotification {

    public static final int NOTIFICATION_ID = 7890;

    public static Notification setupNotification(Context context) {
        Cat.d("Setting up the notification");
        // Get NotificationManager reference
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // get ip address
        InetAddress address = FsService.getLocalInetAddress();
        String ipText;
        if (address == null) {
            ipText = "-";
        } else {
            ipText = "ftp://" + address.getHostAddress() + ":"
                    + FsSettings.getPortNumber() + "/";
        }

        // Instantiate a Notification
        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = String.format(context.getString(R.string.notification_server_starting), ipText);
        long when = System.currentTimeMillis();

        // Define Notification's message and Intent
        CharSequence contentTitle = "ConvectorOT";
        CharSequence contentText = "Transmisión activa por QR";

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
/*
        int stopIcon = android.R.drawable.ic_menu_close_clear_cancel;
        CharSequence stopText = context.getString(R.string.notification_stop_text);
        Intent stopIntent = new Intent(context, FsService.class);
        stopIntent.setAction(FsService.ACTION_REQUEST_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(context, 0,
                stopIntent, PendingIntent.FLAG_ONE_SHOT);*/

        /*int preferenceIcon = android.R.drawable.ic_menu_preferences;
        CharSequence preferenceText = context.getString(R.string.notif_settings_text);
        Intent preferenceIntent = new Intent(context, MainActivity.class);
        preferenceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent preferencePendingIntent = PendingIntent.getActivity(context, 0, preferenceIntent, 0);*/

        int priority = FsSettings.showNotificationIcon() ? Notification.PRIORITY_DEFAULT
                : Notification.PRIORITY_MIN;

        String channelId = "cl.genesys.appchofer.notification.channelId";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Show FTP Server state";
            String description = "Transmitiendo archivos";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            nm.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_patente)
                .setWhen(when)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(priority)
                /*.addAction(stopIcon, stopText, stopPendingIntent)
                .addAction(preferenceIcon, preferenceText, preferencePendingIntent)*/
                .setShowWhen(false)
                .setChannelId(channelId)
                .build();

        // Pass Notification to NotificationManager
        return notification;

        /*Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(contentIntent)
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setWhen(when)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(priority)
                *//*.addAction(stopIcon, stopText, stopPendingIntent)
                .addAction(preferenceIcon, preferenceText, preferencePendingIntent)*//*
                .setShowWhen(false)
                .setChannelId(channelId)
                .build();

        // Pass Notification to NotificationManager
        return notification;*/
    }

}
