package com.arran.askaquestion

import android.support.multidex.MultiDexApplication
import android.util.Log
import com.google.firebase.auth.FirebaseAuth


/**
 * Created by arran on 10/07/2017.
 */
class MainApplication : MultiDexApplication() {
    lateinit var auth: FirebaseAuth

    override fun onCreate() {
        AskAQuestion.install()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { AskAQuestion.currentUser = it } ?:
                auth.signInAnonymously().addOnCompleteListener {
                    if (it.isSuccessful) {
                        auth.currentUser?.let { AskAQuestion.currentUser }
                        Log.v("Sign in", "SUCCESS")
                    } else Log.v("Sign in", "ERROR: ${it.exception}")
                }
        super.onCreate()
    }

}