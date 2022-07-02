package com.example.smartrestaurant.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.NotificationItemModel;
import com.example.smartrestaurant.model.NotificationResponseModel;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.example.smartrestaurant.ui.activity.MainActivity;
import com.example.smartrestaurant.ui.activity.Splash;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dell 5521 on 1/23/2018.
 */

public class NotificationService extends Service {

    private Timer timerFor15Sec;
    private Handler timerFor15SecNoActivityHandler;
    private TimerTask timerTaskFor15Sec;

    @Override
    public void onCreate() {
        super.onCreate();
        startTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
    }

    public void startTimer() {
        //set a new Timer
        timerFor15Sec = new Timer();
        timerFor15SecNoActivityHandler = new Handler();
        //initialize the TimerTask's job
        initializeTimerTask();
        timerFor15Sec.schedule(timerTaskFor15Sec, 1000, 60000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timerTaskFor15Sec != null) {
            timerTaskFor15Sec.cancel();
        }
    }

    public void initializeTimerTask() {
        timerTaskFor15Sec = new TimerTask() {
            @Override
            public void run() {
                timerFor15SecNoActivityHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getNotificationsFromServer();
                    }
                });
            }
        };
    }

    private void getNotificationsFromServer(){
        RequestHandler.getNotifications(new Callback<NotificationResponseModel>() {
            @Override
            public void invoke(NotificationResponseModel obj) {
                if(obj != null && obj.getData() != null && obj.getData().size() > 0){
                    List<NotificationItemModel> list = DbScript.getInstance().getAllNotifications();
                    if(list.size() < obj.getData().size()){
                        if(MainActivity.isMainActivityRef != null){
                            createInAppNotification(obj.getData().size() - list.size());
                            MainActivity.isMainActivityRef.setNotificationCount(obj.getData().size()-list.size());

                            com.example.smartrestaurant.ui.fragment.Notification notificationScreen =
                                    (com.example.smartrestaurant.ui.fragment.Notification) MainActivity.isMainActivityRef.getSupportFragmentManager().findFragmentByTag(
                                            com.example.smartrestaurant.ui.fragment.Notification.class.getName());

                            if(notificationScreen != null){
                                notificationScreen.getNotifications();
                            }
                        } else {
                            createNotification((obj.getData().size() - list.size()));
                        }
                    }
                }
            }

            @Override
            public void alreadyLogin() {
                if(MainActivity.isMainActivityRef != null){
                    AppLog.toast("Your session has been expired, please login to active.");
                }
            }

            @Override
            public void _422(String msg) {

            }
        });
    }

    private void createNotification(int count){
        Intent intent = new Intent(this, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = null;

        pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // If user logged out, do not show any notification
        if (AppClass.getAuthToken().equalsIgnoreCase("")) {
            return;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Smart Restaurant")
                .setContentText("You have "+count+" unread notifications")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }

    private void createInAppNotification(int count){
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Smart Restaurant")
                .setContentText("You have "+count+" unread notifications")
                .setAutoCancel(false)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
