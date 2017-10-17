package com.arran.askaquestion.dagger

import com.arran.askaquestion.views.main.MainActivityComponent
import com.arran.askaquestion.views.main.MainActivityModule
import com.arran.askaquestion.views.question.QuestionsComponent
import com.arran.askaquestion.views.question.QuestionsModule
import dagger.Component

/**
 * Created by arran on 6/09/2017.
 */

@Component(dependencies = arrayOf(FirebaseRepositoryComponent::class))
interface PresenterComponent {
    fun add(mainActivityModule: MainActivityModule): MainActivityComponent
    fun add(questionsModule: QuestionsModule): QuestionsComponent
}