package com.miftahjuanda.movies.Reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.miftahjuanda.movies.API.ApiService;
import com.miftahjuanda.movies.Model.Movie;
import com.miftahjuanda.movies.Model.MovieData;
import com.miftahjuanda.movies.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.miftahjuanda.movies.Fragment.FragmentSettings.CHANNEL_NAME;

public class ReleaseTodayReminder extends BroadcastReceiver {

    private static final int NOTIF_ID_REPEATING = 102;
    private static int notifId;
    private static final String CHANNEL_ID = "todayremainder" ;
    private Movie movie;
    private List<MovieData> ReleaseToday = new ArrayList<>();

    public ReleaseTodayReminder() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        notifId = 1;
        final String title = "Release Today";

        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.txt_pattern_date), Locale.getDefault());
        Date date = new Date();
        final String currentDate = sdf.format(date);

        ApiService apiService = new ApiService();
        apiService.getUpcoming(currentDate, currentDate, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                movie = (Movie) response.body();
                if (movie != null) {
                    ReleaseToday = movie.getResults();
                    for (int i = 0; i < ReleaseToday.size(); i++) {
                        MovieData movie = ReleaseToday.get(i);
                        showAlarmNotification(context, title,movie.getTitle(), i);
                    }

                }else{
                    Toast.makeText(context, R.string.toast_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(context, R.string.toast_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlarmNotification(Context context, String title, String content, int notifId) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                .setContentTitle(title)
                .setContentText(new StringBuilder().append(content).append(R.string.reales_todat).toString())
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = mBuilder.build();

        if (mNotificationManager != null) {
            mNotificationManager.notify(notifId, notification);
        }
    }
    public void setRepeatingAlarm(Context context) {

        cancelAlarm(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(context));
        Toast.makeText(context, context.getString(R.string.repeatingalarmtoast), Toast.LENGTH_SHORT).show();

    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getPendingIntent(context));
        }
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, ReleaseTodayReminder.class);
        return PendingIntent.getBroadcast(context, NOTIF_ID_REPEATING, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    }
}
