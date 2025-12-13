package com.xtrapay.commons

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import com.tegritech.commons.R
import kotlin.math.ln

interface Beeper {
    fun play(volume: Float)
}