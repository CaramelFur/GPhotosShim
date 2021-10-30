package com.google.android.apps.photos.cameraassistant

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class CameraAssistantService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        Log.i("DEBUGCAM", "BOUND: " + intent + " with " + intent.flags)
        return null
    }
}