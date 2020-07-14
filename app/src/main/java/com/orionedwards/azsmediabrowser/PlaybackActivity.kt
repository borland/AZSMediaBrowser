package com.orionedwards.azsmediabrowser

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import java.net.URI

/** Loads [PlaybackVideoFragment]. */
class PlaybackActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, PlaybackVideoFragment())
                    .commit()
        }
    }

    companion object {
        const val MOVIE_TITLE = "MOVIE_TITLE"
        const val MOVIE_DESCRIPTION = "MOVIE_DESCRIPTION"
        const val MOVIE_URL = "MOVIE_URL"
    }
}
