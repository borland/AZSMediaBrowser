package com.orionedwards.azsmediabrowser

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.Executors


class ThumbnailService(private val context: Context) {

    private val thumbnailsDir = context.noBackupFilesDir.resolve("thumbnails")

    data class ThumbnailRequest(val movie: Movie, val localFile: File, val intoImageView: ImageView)

    // this isn't remotely threadsafe but it ends up being only called on one thread so it's fine for my hacky test app
    private val queue: Queue<ThumbnailRequest> = LinkedList<ThumbnailRequest>()
    private var isQueueProcessing = false

    init {
        thumbnailsDir.mkdirs()
    }

    fun loadMovieThumb(movie: Movie, intoImageView: ImageView) {
        fun loadPlaceholder() {
            Glide.with(context)
                .load(R.drawable.movie_placeholder)
                .centerCrop()
                .into(intoImageView)
        }

        val md5 = movie.blob.properties?.contentMd5?.toHexString() // this probably crashes
        if(md5 != null) { // we got the md5 from azure, is there a local thumbnail?
            val file = thumbnailsDir.resolve(md5)
            if(file.exists()) {
                Glide.with(context)
                    .load(file)
                    .centerCrop()
                    .into(intoImageView)
            } else { // doesn't exist
                loadPlaceholder()

                // glide loads things asynchronously so we must manually manage the queue
                queue.add(ThumbnailRequest(movie, file, intoImageView))
                processNext()
            }

        } else { // load the placeholder
            loadPlaceholder()
        }
    }

    fun processNext() {
        if(isQueueProcessing) { // suppress spurious calls from the loader
            return
        }

        val item = queue.poll() ?: return // queue is empty

        isQueueProcessing = true
        val thumb: Long = 900*1000 // 90 seconds
            val options = RequestOptions().frame(thumb)
            Glide.with(context)
                .asBitmap()
                .load(item.movie.authenticatedUrl())
                .apply(options)
                .into(object: SimpleTarget<Bitmap>(300, 300) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val out = FileOutputStream(item.localFile)
                        resource.compress(Bitmap.CompressFormat.JPEG, 75, out)
                        out.close()

                        Glide.with(context)
                            .load(item.localFile)
                            .centerCrop()
                            .into(item.intoImageView)

                        // glide always comes back on the main thread so this is ok
                        isQueueProcessing = false
                        processNext()
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        isQueueProcessing = false
                        processNext()
                    }
                })
    }
}

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

fun ByteArray.toHexString(): String {
    val hexChars = CharArray(this.size * 2)
    for (i in this.indices) {
        // note: Kotlin values are signed, so we'll need to "AND" with 0xFF to ensure that we ignore
        // the signed byte.
        val v = this[i].toInt() and 0xFF
        hexChars[i * 2] = HEX_ARRAY[v ushr 4]
        hexChars[i * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }
    return String(hexChars)
}