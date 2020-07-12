package com.orionedwards.azsmediabrowser

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.azure.storage.blob.models.BlobListDetails
import com.azure.storage.blob.models.ListBlobsOptions
import reactor.core.scheduler.Schedulers

/**
 * Loads [MainFragment].
 */
class MainActivity : Activity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
