package com.arran.askaquestion.models

/**
 * Created by arran on 14/10/2017.
 */
data class Question(
        var questionText: String = "",
        var votes: Int = 0,
        var voters: HashMap<String, Boolean> = hashMapOf()
) : FirebaseObject()