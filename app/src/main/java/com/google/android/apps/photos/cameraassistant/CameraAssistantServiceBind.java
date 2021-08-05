package com.google.android.apps.photos.cameraassistant;

import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;

public final class CameraAssistantServiceBind extends ICameraAssistantServiceBind {
  final CameraAssistantService a;


  public CameraAssistantServiceBind(CameraAssistantService cameraAssistantService) {
    super("com.google.android.apps.photos.cameraassistant.ICameraAssistantService");
    this.a = cameraAssistantService;
  }

  public CameraAssistantServiceBind() {
    super("com.google.android.apps.photos.cameraassistant.ICameraAssistantService");
    this.a = null;
  }

  @Override
  public final boolean gh(int i, Parcel a, Parcel b) {
    Log.i("DEBUGCAM", "Int: " + i + " Parcels: " + a + " " + b);

    return true;
  }
}
