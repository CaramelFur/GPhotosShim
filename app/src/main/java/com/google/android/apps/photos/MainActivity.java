package com.google.android.apps.photos;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;
import android.content.ContentResolver;
import java.io.InputStream;
import java.net.URI;

import android.content.ContentProviderClient;
import android.os.ParcelFileDescriptor;
import java.io.File;
import android.database.Cursor;
import android.provider.MediaStore;
import android.content.Context;
import android.app.PendingIntent;

public class MainActivity extends Activity {

  private static final int waitMS = 100;
  private static final int maximumSeconds = 30000;
  private static final int iterations = maximumSeconds * 1000 / waitMS;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView(R.layout.activity_main);

    Intent receivedIntent = getIntent();

    String SessionId = receivedIntent.getStringExtra("external_session_id");
    Uri uri = Uri.parse(receivedIntent.getData().toString() + "?requireOriginal=1");
    String type = receivedIntent.getType();

    Uri processingURI = (Uri) receivedIntent.getParcelableExtra("processing_uri_intent_extra");

    PendingIntent cameraRelaunchIntent = (PendingIntent) receivedIntent.getParcelableExtra("CAMERA_RELAUNCH_INTENT_EXTRA");

    Log.i("PhotosShim", "SessionId: '" + SessionId + "', URI:'" + uri + "', type: '" + type + "', processingURI: '"
        + processingURI + "'");

    boolean success = false;

    for (int i = 0; i < iterations; i++) {
      String absolut = MainActivity.getRealPathFromURI(getApplicationContext(), uri);
      if (absolut == "")
        break;

      Log.i("PhotosShim", "URL: " + absolut);

      String name = "";
      try {
        name = new File(absolut).getName();
      } catch (Exception e) {
        Log.e("PhotosShim", "Error creating file obj", e);
        break;
      }

      if (name == "")
        break;

      if (!name.startsWith(".pending")) {
        success = true;
        break;
      }

      try {
        Thread.sleep(waitMS);
      } catch (Exception e) {
        Log.e("PhotosShim", "Error sleeping", e);
        break;
      }
    }

    if (!success) {
      try {
        cameraRelaunchIntent.send();
      } catch (Exception e) {
        Log.e("PhotosShim", "Error sending intent", e);
      }
      finish();
      return;
    }

    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setDataAndType(uri, "image/*");
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }

  public static String getRealPathFromURI(Context context, Uri contentUri) {
    Cursor cursor = null;
    String result = "";
    try {
      String[] proj = { MediaStore.Images.Media.DATA };
      cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
      cursor.moveToFirst();
      int column_index = cursor.getColumnIndex(proj[0]);
      result = cursor.getString(column_index);
    } finally {
      if (cursor != null)
        cursor.close();
    }
    return result;
  }
}