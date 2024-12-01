package com.example.galendar.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message received: ${remoteMessage.data}")

        // 메시지 데이터 처리
        remoteMessage.notification?.let {
            NotificationHelper.showNotification(
                this, it.title ?: "Default Title", it.body ?: "Default Body"
            )
        }
    }

    private fun sendTokenToServer(token: String) {
        // 서버로 토큰을 보내는 로직 구현
    }
}
