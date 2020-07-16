package com.orionedwards.azsmediabrowser

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_playback.*
import java.net.URI

/** Loads [PlaybackVideoFragment]. */
class PlaybackActivity : FragmentActivity() {

    lateinit var exoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)

        val intent = this.intent ?: throw IllegalArgumentException("Can't load PlaybackVideoFragment without an intent")

        val title = intent.getStringExtra(PlaybackActivity.MOVIE_TITLE)
        val description = intent.getStringExtra(PlaybackActivity.MOVIE_DESCRIPTION)
        val videoUrl = intent.getStringExtra(PlaybackActivity.MOVIE_URL)

        val userAgent = "AZSMediaBrowser/0.1"

        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoUrl))

        exoPlayer = SimpleExoPlayer.Builder(this).build()
        exo_player_view.player = exoPlayer
        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource)
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    companion object {
        const val MOVIE_TITLE = "MOVIE_TITLE"
        const val MOVIE_DESCRIPTION = "MOVIE_DESCRIPTION"
        const val MOVIE_URL = "MOVIE_URL"
    }
}
