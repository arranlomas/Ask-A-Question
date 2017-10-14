package com.arran.askaquestion

import com.arran.askaquestion.dagger.DaggerFirebaseRepositoryComponent
import com.arran.askaquestion.dagger.DaggerPresenterComponent
import com.arran.askaquestion.dagger.PresenterComponent
import com.google.firebase.auth.FirebaseUser

/**
 * Created by arran on 14/10/2017.
 */
object AskAQuestion {
    lateinit var presenterComponent: PresenterComponent
    var currentUser: FirebaseUser? = null

    fun install() {
        val firebaseRepositoryComponent = DaggerFirebaseRepositoryComponent.builder().build()
        presenterComponent = DaggerPresenterComponent.builder()
                .firebaseRepositoryComponent(firebaseRepositoryComponent)
                .build()
    }
}