package com.dicoding.courseschedule.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.ID_REPEATING
import com.dicoding.courseschedule.util.NOTIFICATION_CHANNEL_ID
import com.dicoding.courseschedule.util.NOTIFICATION_CHANNEL_NAME
import com.dicoding.courseschedule.util.NOTIFICATION_ID
import com.dicoding.courseschedule.util.executeThread
import java.util.Calendar

class DailyReminder : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        executeThread {
            val repository = DataRepository.getInstance(context)
            val courses = repository?.getTodaySchedule()

            courses?.let {
                if (it.isNotEmpty()) showNotification(context, it)
            }
        }
    }

    //TODO 12 : Implement daily reminder for every 06.00 a.m using AlarmManager
    fun setDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_IMMUTABLE)

        val calender = Calendar.getInstance()
        calender.set(Calendar.SECOND, 0)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.HOUR_OF_DAY, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, content: List<Course>) {
        //TODO 13 : Show today schedules in inbox style notification & open HomeActivity when notification tapped
        val notificationStyle = NotificationCompat.InboxStyle()
        val timeString = context.resources.getString(R.string.notification_message_format)
        content.forEach {
            val courseData = String.format(timeString, it.startTime, it.endTime, it.courseName)
            notificationStyle.addLine(courseData)
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.today_schedule))
            .setSmallIcon(R.drawable.ic_notifications)
            .setStyle(notificationStyle)
            .setContentText(context.getString(R.string.notification_message_format))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getIntentNotify(context))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
//        with(NotificationManagerCompat.from(context)){
//            notify(NOTIFICATION_ID, builder.build())
//        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID,notification)

    }

    private fun getIntentNotify(context: Context): PendingIntent? {
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }
}