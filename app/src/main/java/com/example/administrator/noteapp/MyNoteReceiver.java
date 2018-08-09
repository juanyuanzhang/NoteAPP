package com.example.administrator.noteapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNoteReceiver extends BroadcastReceiver {

    //利用廣播開啟提醒通知
    @Override
    public void onReceive(Context context, Intent intent) {
        //設定通知的附加動作
        Intent i =new Intent(context,MainActivity.class);
        PendingIntent  pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        String top = intent.getStringExtra("msg");
        int id = intent.getIntExtra("id",0);
        Log.i("id", String.valueOf(id));
        Calendar c =Calendar.getInstance();
        //設定Notification
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.note2)
                .setContentTitle("ToDoList")
                .setContentText(top)
                .setContentIntent(pendingIntent)
                .setWhen(c.getTimeInMillis()).build();//calendar.getTimeInMillis()
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //執行NotificationManager的方法notify()
        assert manager != null;
        manager.notify(id,notification);
    }
}
