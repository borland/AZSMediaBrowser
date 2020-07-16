package com.orionedwards.azsmediabrowser

import com.azure.storage.blob.BlobAsyncClient
import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.models.BlobItem
import com.azure.storage.blob.sas.BlobSasPermission
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues
import java.io.Serializable
import java.time.OffsetDateTime

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
class Movie(
        val id: Long,
        val blob: BlobItem,
        val blobClient: BlobAsyncClient
) : Comparable<Movie> {
    constructor(blob: BlobItem, client: BlobAsyncClient): this(
        nextId(),
        blob,
        client)

    fun authenticatedUrl() : String {
        return blobClient.blobUrl + "?" + generateSas()
    }

    val showName: String by lazy {
        blob.name.split('/').first()
    }

    // the name of this particular file. All movies must be .mp4 or we don't even get them
    val title: String by lazy {
        blob.name.split('/').last().removeSuffix(".mp4")
    }

    val backgroundImageUrl: String?
        get() = null

    val cardImageUrl: String?
        get() = "https://lh3.googleusercontent.com/HAGDHQduy3JrUiCcjROHMZPjoKF_M3_FutliAUyWskPImePUeC0e1J75DCirXEGl1iOcx9Z9BBjJJuKyf-Fd9w=w1004"

    private fun generateSas(): String {
        val sasValues = BlobServiceSasSignatureValues(
            OffsetDateTime.now().plusDays(1),
            BlobSasPermission().setReadPermission(true))
        return blobClient.generateSas(sasValues)
    }

    companion object {
        internal const val serialVersionUID = 727566175075960653L

        var lastId: Long = 0
        fun nextId() : Long {
            lastId += 1
            return lastId
        }
    }

    override fun compareTo(other: Movie): Int {
        // first compare by title, then by id
        val titleCmp = title.compareTo(other.title, ignoreCase = true)
        return if (titleCmp != 0)
            titleCmp else
            id.compareTo(other.id)
    }
}
