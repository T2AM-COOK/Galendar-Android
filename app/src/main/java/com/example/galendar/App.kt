package com.example.galendar

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        lateinit var context: Context
            private set  // 외부에서 수정할 수 없도록 설정
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext  // 애플리케이션의 컨텍스트를 저장
    }
}
