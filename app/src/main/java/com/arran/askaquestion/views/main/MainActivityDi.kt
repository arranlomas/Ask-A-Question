package com.arran.askaquestion.views.main

import com.arran.askaquestion.firebase.IFirebaseRepository
import com.arran.askaquestion.views.main.mvp.MainActivity
import com.arran.askaquestion.views.main.mvp.MainContract
import com.arran.askaquestion.views.main.mvp.MainPresenter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by arran on 6/09/2017.
 */

@Subcomponent(modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}

@Module
class MainActivityModule {
    @Provides
    fun providesMainPresenter(firebaseRepository: IFirebaseRepository): MainContract.Presenter {
        return MainPresenter(firebaseRepository)
    }
}