package com.arran.askaquestion.views.question

import com.arran.askaquestion.firebase.IFirebaseRepository
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by arran on 6/09/2017.
 */

@Subcomponent(modules = arrayOf(QuestionsModule::class))
interface QuestionsComponent {
    fun inject(questionsFragment: QuestionsFragment)
}

@Module
class QuestionsModule {
    @Provides
    fun provideQuestionsPresenter(firebaseRepository: IFirebaseRepository): QuestionsContract.Presenter {
        return QuestionsPresenter(firebaseRepository)
    }
}