package com.google.android.apps.photos.cameraassistant;

import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.os.IBinder;
import android.content.res.Configuration;

public class CameraAssistantService extends Service {
  @Override
  public IBinder onBind(Intent intent) {
    Log.i("DEBUGCAM", "BOUND: " + intent + " with " + intent.getFlags());

    return null;
  }
}
