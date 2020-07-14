package com.orionedwards.azsmediabrowser

import android.content.Context
import androidx.core.content.ContextCompat
import com.azure.core.http.rest.PagedResponse
import com.azure.storage.blob.BlobAsyncClient
import com.azure.storage.blob.BlobContainerAsyncClient
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.models.BlobItem
import com.azure.storage.blob.models.ListBlobsOptions
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

/// Returns all its results on the android main thread beacuse we are lazy
class AzureBlobService(connectionString: String, private val context: Context) {
    val blobClient = BlobServiceClientBuilder()
        .connectionString(connectionString)
        .buildAsyncClient()

    private val BLOCK_SIZE = 100

    private val mainScheduler = Schedulers.fromExecutor(ContextCompat.getMainExecutor(context))

    fun clientForContainer(name: String) : BlobContainerAsyncClient {
        return blobClient.getBlobContainerAsyncClient(name)
    }

    fun listBlobs(container: BlobContainerAsyncClient, options: ListBlobsOptions? = null) : Flux<PagedResponse<BlobItem>> = container
            .listBlobs(options ?: ListBlobsOptions())
            .byPage()
            .publishOn(mainScheduler)
}