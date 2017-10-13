package com.arran.askaquestion.views.base

import android.app.Activity
import android.content.Context

/**
 * Created by arran on 11/07/2017.
 */
interface BaseContract {
    interface MvpView{
        fun showError(msg: String)
        fun showError(stringRes: Int)
        fun getActivity(): Activity?
    }

    interface Presenter<in V : MvpView> {

        fun attachView(mvpView: V)

        fun detachView()
    }
}