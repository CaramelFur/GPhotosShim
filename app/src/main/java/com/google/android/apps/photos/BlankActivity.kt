package com.google.android.apps.photos

import android.app.Activity
import android.os.Bundle

class BlankActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}