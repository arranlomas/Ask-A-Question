package com.arran.askaquestion.dagger

import com.arran.askaquestion.views.main.MainActivityComponent
import com.arran.askaquestion.views.main.MainActivityModule
import dagger.Component

/**
 * Created by arran on 6/09/2017.
 */

@Component(dependencies = arrayOf(FirebaseRepositoryComponent::class))
interface PresenterComponent {
    fun add(mainActivityModule: MainActivityModule): MainActivityComponent
}