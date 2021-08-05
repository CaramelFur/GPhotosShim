package com.google.android.apps.photos.cameraassistant;

import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.os.IBinder;
import android.content.res.Configuration;

public class CameraAssistantService extends Service {

  private final CameraAssistantServiceBind b = new CameraAssistantServiceBind(this);

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i("DEBUGCAM", "DEBUGCAM: created");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.i("DEBUGCAM", "DEBUGCAM: start flags " + flags + " id " + startId + " intent " + intent);
    return Service.START_NOT_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    Log.i("DEBUGCAM", "BOUND: " + intent + " with " + intent.getFlags());

    return b;
  }

  @Override
  public void onConfigurationChanged(Configuration config) {
    Log.i("DEBUGCAM", "CONFIG CHANGE: " + config);
    super.onConfigurationChanged(config);
  }

  @Override
  public void onStart(Intent intent, int id) {
    Log.i("DEBUGCAM", "START: " + intent + " with " + id);
    super.onStart(intent, id);
  }

  @Override
  public boolean onUnbind(Intent intent) {

    Log.i("DEBUGCAM", "UNBIND: " + intent);
    return false;
  }

  @Override
  public void onTaskRemoved(Intent intent) {
    Log.i("DEBUGCAM", "TASKREMOVE: " + intent);
    super.onTaskRemoved(intent);
  }

  @Override
  public void onDestroy() {
    Log.i("DEBUGCAM", "DEBUGCAM: destroyed");
  }
}
