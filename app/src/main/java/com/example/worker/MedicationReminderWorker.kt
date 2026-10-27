package com.example.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.MainActivity
import com.example.R

class MedicationReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "medication_reminders"
        const val KEY_MEDICATION_TITLE = "medication_title"
        const val KEY_MEDICATION_DETAILS = "medication_details"
        const val KEY_NOTIFICATION_ID = "notification_id"
        const val KEY_EVENT_ID = "event_id"
    }

    override suspend fun doWork(): Result {
        val title = inputData.getString(KEY_MEDICATION_TITLE) ?: "Rappel Médication FIV"
        val details = inputData.getString(KEY_MEDICATION_DETAILS) ?: "C'est l'heure de votre injection ou prise de médicament."
        val notificationId = inputData.getInt(KEY_NOTIFICATION_ID, System.currentTimeMillis().toInt())

        createNotificationChannel()

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(details)
            .setStyle(NotificationCompat.BigTextStyle().bigText(details))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 400, 250, 400))
            .setContentIntent(pendingIntent)
            .build()

        try {
            val notificationManager = NotificationManagerCompat.from(applicationContext)
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(notificationId, notification)
            }
        } catch (_: SecurityException) {
            // Handled safely if permission revoked
        }

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Rappels Injections & Traitement FIV"
            val descriptionText = "Rappels programmés pour les prises d'injections et médicaments du protocole"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400, 250, 400)
            }
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
