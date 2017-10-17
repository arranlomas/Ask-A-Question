package com.arran.askaquestion.views.main.mvp

import com.arran.askaquestion.views.base.BaseContract

/**
 * Created by arran on 11/07/2017.
 */
interface MainContract {
    interface View : BaseContract.MvpView {
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun createNewChannel(channelName: String, channelPassword: String)
    }
}