package com.penys.fi.movemaster95.ble_connection

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BleConnService : Service() {


    override fun onCreate() {
        super.onCreate()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}