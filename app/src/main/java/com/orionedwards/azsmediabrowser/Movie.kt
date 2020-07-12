package com.orionedwards.azsmediabrowser

import com.azure.storage.blob.models.BlobItem
import java.io.Serializable

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class Movie(
        val id: Long = 0,
        val title: String? = null,
        val description: String? = null,
        val backgroundImageUrl: String? = null,
        val cardImageUrl: String? = null,
        val videoUrl: String? = null,
        val studio: String? = null
) : Serializable {

    constructor(blob: BlobItem): this(nextId(), blob.name)

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", backgroundImageUrl='" + backgroundImageUrl + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                '}'
    }

    companion object {
        internal const val serialVersionUID = 727566175075960653L

        var lastId: Long = 0
        fun nextId() : Long {
            lastId += 1
            return lastId
        }
    }
}
