package com.orionedwards.azsmediabrowser

import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.StorageCredentials

class AzureBlobService(connectionString: String) {
    val account = CloudStorageAccount(StorageCredentials.tryParseCredentials(connectionString))
}