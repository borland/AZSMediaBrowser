package com.orionedwards.azsmediabrowser

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.azure.storage.blob.models.BlobListDetails
import com.azure.storage.blob.models.ListBlobsOptions
import reactor.core.scheduler.Schedulers
import java.lang.IllegalStateException

/**
 * Loads [MainFragment].
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext

        setContentView(R.layout.activity_main)
    }

    companion object {
        var context: Context? = null

        val thumbnailService: ThumbnailService by lazy {
            ThumbnailService(context ?: throw IllegalStateException("Can't ask for thumbnail service before mainActivity onCreate"))
        }
    }
}
