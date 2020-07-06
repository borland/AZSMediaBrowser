package com.orionedwards.azsmediabrowser

import android.app.Activity
import android.os.Bundle

/**
 * Loads [MainFragment].
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectionString = ""

        val azs = AzureBlobService(connectionString)
    }
}
