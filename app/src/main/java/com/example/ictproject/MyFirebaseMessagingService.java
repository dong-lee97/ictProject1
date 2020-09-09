package com.example.ictproject;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.ictproject.activity.AcceptActivity;
import com.example.ictproject.upload.CompanyUpload;;
import com.example.ictproject.upload.Upload;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    final static String companyInformation = "companyInformation";
    final static String uploadInformation = "uploadInformation";
    private String channelId;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String dataJson = remoteMessage.getData().get("data");
        String type = remoteMessage.getData().get("type");
        String click_action = remoteMessage.getData().get("click_action");
        String messageData = remoteMessage.getData().get("body");
        String titleData = remoteMessage.getData().get("title");
        Gson gson = new Gson();
        if (type.equals("companyUpload")){
            CompanyUpload companyUpload = gson.fromJson(dataJson, CompanyUpload.class);
            sendNotification(titleData, messageData, click_action,  companyUpload);
        } else if (type.equals("upload")){
            Upload upload = gson.fromJson(dataJson, Upload.class);
            sendNotification(titleData, messageData, click_action,  upload);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {

    }

    public void sendNotification(String title, String msg, String click_action, Object object){
        PendingIntent pendingIntent = null;
        if (object instanceof CompanyUpload){
            Intent intent = new Intent(click_action);
            CompanyUpload companyUpload = (CompanyUpload) object;
            intent.putExtra(companyInformation, companyUpload.getuId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        } else if(object instanceof Upload){
            Intent intent = new Intent(this, AcceptActivity.class);
            Upload upload = (Upload) object;
            intent.putExtra(uploadInformation, upload.getUid()); // 연락을 한 사람(개인)의 UID 를 넘겨주는 부분
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        channelId = getResources().getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_loading)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("fcm_default_channel",
                    "fcm_default_channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0,notificationBuilder.build());
    }

}
