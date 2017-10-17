package com.arran.askaquestion.models

/**
 * Created by arran on 14/10/2017.
 */
data class Channel(
        val channelName: String,
        val channelPassword: String
) : FirebaseObject()