package com.arran.askaquestion.views.main.mvp

import com.arran.askaquestion.R
import com.arran.askaquestion.firebase.IFirebaseRepository
import com.arran.askaquestion.views.base.BasePresenter

/**
 * Created by arran on 11/07/2017.
 */
class MainPresenter(private val firebaseRepository: IFirebaseRepository) : BasePresenter<MainContract.View>(), MainContract.Presenter {
    override fun createNewChannel(channelName: String, channelPassword: String) {
        firebaseRepository.createChannel(channelName, channelPassword)
                .subscribe(object : BaseSubscriber<String>(){
                    override fun onNext(t: String?) {
                        mvpView.showSuccess(R.string.channel_create)
                    }
                })
    }
}