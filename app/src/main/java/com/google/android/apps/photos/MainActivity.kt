package com.google.android.apps.photos;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;

import android.database.Cursor;
import android.provider.MediaStore;
import android.content.Context;
import android.app.PendingIntent;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {
  // Polling settings for checking if the URI is finsihed yet
  private static final int waitMS = 100;
  private static final int maximumSeconds = 30000;
  private static final int iterations = maximumSeconds * 1000 / waitMS;

  // Random integer to identify app in permission callback
  private static final int PERMISSIONS_INT = 755009201;

  private PendingIntent cameraRelaunchIntent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent receivedIntent = getIntent();

    // Extract all necessary information from the intent
    // We store the relaunch intent in this class so it can be used in callbacks
    // (In this case that is just the permission callback)
    String SessionId = receivedIntent.getStringExtra("external_session_id");
    Uri uri = Uri.parse(receivedIntent.getData().toString());
    String type = receivedIntent.getType();
    Uri processingURI = (Uri) receivedIntent.getParcelableExtra("processing_uri_intent_extra");
    this.cameraRelaunchIntent = (PendingIntent) receivedIntent.getParcelableExtra("CAMERA_RELAUNCH_INTENT_EXTRA");

    // Make sure there is even a session id, the call is probably invalid if this is gone
    if (SessionId == null) {
      finish();
      return;
    }

    // Make sure that we have the READ_EXTERNAL_STORAGE permission,
    // we sadly need this permission to check if a URI is pending
    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      Log.i("PhotosShim", "No permission, requesting them");

      requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_INT);

      quit();
      return;
    }

    Log.i("PhotosShim", "SessionId: '" + SessionId + "', URI:'" + uri + "', type: '" + type + "', processingURI: '"
        + processingURI + "'");

    // Keep polling the URI to see if it has stopped being pending
    boolean success = false;
    for (int i = 0; i < iterations; i++) {
      if (!isUriPending(getApplicationContext(), uri)) {
        success = true;
        break;
      }

      Log.i("PhotosShim", "Pending...");

      try {
        Thread.sleep(waitMS);
      } catch (Exception e) {
        Log.e("PhotosShim", "Error sleeping", e);
        break;
      }
    }

    if (!success) {
      quit();
      return;
    }

    // If the URI is not pending anymore we send an implicit intent
    // so you can now handle the image with any gallery you want

    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setDataAndType(uri, "image/*");
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }

  private void quit() {
    try {
      if (this.cameraRelaunchIntent != null)
        this.cameraRelaunchIntent.send();
    } catch (Exception e) {
      Log.e("PhotosShim", "Error sending intent", e);
    }
    finish();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode != PERMISSIONS_INT)
      return;
    if (permissions.length == 0 || grantResults.length == 0)
      return;

    if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE))
      quit();
  }

  public static boolean isUriPending(Context context, Uri contentUri) {
    Cursor cursor = null;
    boolean result;
    try {
      String[] proj = {MediaStore.Images.Media.IS_PENDING};
      cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
      cursor.moveToFirst();
      int column_index_data = cursor.getColumnIndex(proj[0]);
      result = cursor.getInt(column_index_data) != 0;
    } finally {
      if (cursor != null)
        cursor.close();
    }
    return result;
  }
}