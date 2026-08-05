package sk.mkdigital.androidshowcase.domain.repository

import sk.mkdigital.androidshowcase.domain.model.Notification

interface LocalNotificationService {
    fun showNotification(notification: Notification)
    fun cancelNotification(id: String)
    fun cancelAllNotifications()
}
