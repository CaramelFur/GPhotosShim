package com.google.android.apps.photos.cameraassistant;

import android.os.IInterface;
import android.os.IBinder;
import android.os.Binder;
import android.os.Parcel;
import android.util.Log;

public abstract class ICameraAssistantServiceBind extends Binder implements IInterface {
  protected ICameraAssistantServiceBind(String str) {
    attachInterface(this, str);
  }

  public IBinder asBinder() {
    return this;
  }

  protected boolean gh(int i, Parcel a, Parcel b) {
    return false;
  }

  @Override
  public boolean onTransact(int i1, Parcel a, Parcel b, int i2) {
    Log.i("DEBUGCAM", "TRANSACTING");
    try {
      if (i1 <= 16777215) {
        a.enforceInterface(getInterfaceDescriptor());
      } else if (super.onTransact(i1, a, b, i2)) {
        return true;
      }
      return gh(i1, a, b);
    } catch (Exception e) {
      Log.e("DEBUGCAM", e.toString());
      return false;
    }
  }

}
