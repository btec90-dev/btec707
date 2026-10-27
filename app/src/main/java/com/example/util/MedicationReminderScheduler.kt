package com.example.util

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.worker.MedicationReminderWorker
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ScheduledReminderInfo(
    val eventId: String,
    val title: String,
    val details: String,
    val timeLabel: String,
    val triggerTimestampMillis: Long
)

object MedicationReminderScheduler {

    private const val PREFS_NAME = "fiv_medication_reminders"
    private const val KEY_REMINDERS = "saved_reminders"

    fun scheduleReminder(
        context: Context,
        eventId: String,
        title: String,
        details: String,
        delaySeconds: Long,
        timeLabel: String
    ) {
        val uniqueWorkName = "med_reminder_$eventId"
        val notificationId = (eventId.hashCode() and 0x7FFFFFFF)

        val data = workDataOf(
            MedicationReminderWorker.KEY_MEDICATION_TITLE to title,
            MedicationReminderWorker.KEY_MEDICATION_DETAILS to details,
            MedicationReminderWorker.KEY_NOTIFICATION_ID to notificationId,
            MedicationReminderWorker.KEY_EVENT_ID to eventId
        )

        val workRequest = OneTimeWorkRequestBuilder<MedicationReminderWorker>()
            .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
            .setInputData(data)
            .addTag("medication_reminder")
            .addTag("event_$eventId")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueWorkName,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )

        // Save persistent record in SharedPreferences
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentList = getAllScheduledReminders(context).toMutableList()
        currentList.removeAll { it.eventId == eventId }
        val triggerTime = System.currentTimeMillis() + (delaySeconds * 1000L)
        currentList.add(
            ScheduledReminderInfo(
                eventId = eventId,
                title = title,
                details = details,
                timeLabel = timeLabel,
                triggerTimestampMillis = triggerTime
            )
        )
        saveRemindersList(prefs, currentList)
    }

    fun cancelReminder(context: Context, eventId: String) {
        val uniqueWorkName = "med_reminder_$eventId"
        WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentList = getAllScheduledReminders(context).toMutableList()
        currentList.removeAll { it.eventId == eventId }
        saveRemindersList(prefs, currentList)
    }

    fun isReminderScheduled(context: Context, eventId: String): Boolean {
        val reminders = getAllScheduledReminders(context)
        return reminders.any { it.eventId == eventId }
    }

    fun getReminderDetails(context: Context, eventId: String): ScheduledReminderInfo? {
        val reminders = getAllScheduledReminders(context)
        return reminders.find { it.eventId == eventId }
    }

    fun getAllScheduledReminders(context: Context): List<ScheduledReminderInfo> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val rawJson = prefs.getString(KEY_REMINDERS, null) ?: return emptyList()
        val result = mutableListOf<ScheduledReminderInfo>()
        try {
            val jsonArray = JSONArray(rawJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                result.add(
                    ScheduledReminderInfo(
                        eventId = obj.getString("eventId"),
                        title = obj.getString("title"),
                        details = obj.getString("details"),
                        timeLabel = obj.getString("timeLabel"),
                        triggerTimestampMillis = obj.optLong("triggerTimestampMillis", 0L)
                    )
                )
            }
        } catch (_: Exception) {
            // Return empty if parse fails
        }
        return result
    }

    fun cancelAllReminders(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("medication_reminder")
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    private fun saveRemindersList(
        prefs: android.content.SharedPreferences,
        list: List<ScheduledReminderInfo>
    ) {
        val jsonArray = JSONArray()
        list.forEach { item ->
            val obj = JSONObject().apply {
                put("eventId", item.eventId)
                put("title", item.title)
                put("details", item.details)
                put("timeLabel", item.timeLabel)
                put("triggerTimestampMillis", item.triggerTimestampMillis)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_REMINDERS, jsonArray.toString()).apply()
    }
}
