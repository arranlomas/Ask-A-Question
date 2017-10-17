package com.arran.askaquestion.views.main.mvp

import com.arran.askaquestion.R
import com.arran.askaquestion.firebase.IFirebaseRepository
import com.arran.askaquestion.models.Channel
import com.arran.askaquestion.views.base.BasePresenter

/**
 * Created by arran on 11/07/2017.
 */
class MainPresenter(private val firebaseRepository: IFirebaseRepository) : BasePresenter<MainContract.View>(), MainContract.Presenter {


    override fun attachView(mvpView: MainContract.View) {
        super.attachView(mvpView)
        firebaseRepository.attachListenerToChannelsDatabase()
        firebaseRepository.channelsUpdateObservable
                .subscribe(object : BaseSubscriber<List<Channel>>() {
                    override fun onNext(channels: List<Channel>) {
                        mvpView.updateChannels(channels)
                    }
                })
    }

    override fun createNewChannel(channelName: String, channelPassword: String) {
        firebaseRepository.createChannel(channelName, channelPassword)
                .subscribe(object : BaseSubscriber<String>(){
                    override fun onNext(t: String?) {
                        mvpView.showSuccess(R.string.channel_create)
                    }
                })
    }

    override fun joinChannel(channelName: String, channelPassword: String) {
        firebaseRepository.joinChannel(channelName, channelPassword)
                .subscribe(object : BaseSubscriber<Channel>(){
                    override fun onNext(channel: Channel) {
                        mvpView.showSuccess(R.string.channel_joined)
                    }
                })
    }
}