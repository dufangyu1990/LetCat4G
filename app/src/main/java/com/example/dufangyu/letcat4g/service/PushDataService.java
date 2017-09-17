package com.example.dufangyu.letcat4g.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.dufangyu.letcat4g.R;
import com.example.dufangyu.letcat4g.utils.Constant;
import com.example.dufangyu.letcat4g.utils.LogUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dufangyu on 2017/9/15.
 */

public class PushDataService extends Service {

    private static ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    private static SendDataTask task;
    private static Messenger messenger;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        LogUtil.d("dfy", "service onCreate");
        super.onCreate();
        task = new SendDataTask();
        Intent intent = new Intent();
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("服务中")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setContentIntent(pi).build();
        notification.flags = NotificationCompat.FLAG_ONGOING_EVENT
                | NotificationCompat.FLAG_FOREGROUND_SERVICE
                | NotificationCompat.FLAG_NO_CLEAR;
        startForeground(1, notification);

        initExecutor();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("dfy", "service onStartCommand");
        LogUtil.d("dfy", "messenger = "+messenger);
        if (messenger == null) {
            if(intent!=null)
                messenger = (Messenger) intent.getExtras().get("messenger");
        }
        return START_STICKY;
    }


    private void initExecutor() {
        if (mScheduledThreadPoolExecutor == null)
            mScheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
        if (mScheduledThreadPoolExecutor != null && task != null) {
            mScheduledThreadPoolExecutor.scheduleAtFixedRate(task, 5, 10, TimeUnit.SECONDS);
        }
    }

   static class SendDataTask implements Runnable {

        @Override
        public void run() {
            LogUtil.d("dfy", "进行数据请求");
            Message message = Message.obtain();
            message.what = Constant.MSG_SENDDATA;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        LogUtil.d("dfy", "service onDestroy");
    }


}
