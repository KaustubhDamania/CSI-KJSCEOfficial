package org.csikjsce.csi_kjsceofficial;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.csikjsce.csi_kjsceofficial.POJO.Notification;

import java.util.Map;

public class CSINotificationService extends FirebaseMessagingService {
    public static final String TAG = CSINotificationService.class.getSimpleName();
    public static final int REQUEST_CODE = 172;
    Intent intent;

    public CSINotificationService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Notification notification = parseNotification(remoteMessage);
        updateNotifDB(notification);

        intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(getBaseContext(),REQUEST_CODE, intent,PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this,"csi_channel");
        notifBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifManager.notify(0,notifBuilder.build());
    }

    private Notification parseNotification(RemoteMessage message){

        Map<String, String> data = message.getData();
        int eventId, eventType;
        try {
            eventId = Integer.parseInt(data.get(getResources().getString(R.string.notif_key_event_id)));
        } catch (NumberFormatException nfe) {
            eventId = -1;
        }
        try {
            eventType = Integer.parseInt(data.get(getString(R.string.notif_key_type)));
        } catch (NumberFormatException nfe){
            eventType = 0;
        }
        Notification notification = new Notification(
                Integer.parseInt(data.get(getString(R.string.notif_key_id))),
                data.get(getString(R.string.notif_key_time)),
                data.get(getString(R.string.notif_key_title)),
                data.get(getString(R.string.notif_key_desc)),
                data.get(getString(R.string.notif_key_extra_url)),
                eventType,
                eventId,
                Notification.NOT_READ
        );
        return notification;
    }

    private void updateNotifDB(Notification notification){
        Log.d(TAG,"start updateNotifDB()");
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        dbhelper.insertNotification(notification);
        Log.d(TAG,"end updateNotifDB()");
    }

}
