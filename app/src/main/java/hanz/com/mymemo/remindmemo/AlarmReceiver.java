package hanz.com.mymemo.remindmemo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import hanz.com.mymemo.MainActivity;
import hanz.com.mymemo.R;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"you can do something ~~",Toast.LENGTH_SHORT).show();

        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //MainActivity是你点击通知时想要跳转的Activity
        Intent playIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Alarm").setContentText("LA la la ~").
                setSubText("Click to close").setSmallIcon(R.drawable.alarm).
                setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.alarm1)).
                setDefaults(Notification.DEFAULT_ALL).
                setContentIntent(pendingIntent).setAutoCancel(true).
                setWhen(System.currentTimeMillis());
        manager.notify(0, builder.build());
    }


}
