package com.google.android.apps.photos;

import android.app.Activity;
import android.os.Bundle;

public class BlankActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    finish();
  }
}
