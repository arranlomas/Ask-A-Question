package com.arran.askaquestion.views.main.mvp

import com.arran.askaquestion.firebase.IFirebaseRepository
import com.arran.askaquestion.views.base.BasePresenter

/**
 * Created by arran on 11/07/2017.
 */
class MainPresenter(val firebaseRepository: IFirebaseRepository) : BasePresenter<MainContract.View>(), MainContract.Presenter {

}