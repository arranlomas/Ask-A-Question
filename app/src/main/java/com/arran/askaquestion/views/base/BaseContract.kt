package com.arran.askaquestion.views.base

import android.app.Activity
import android.content.Context
import com.arran.askaquestion.R

/**
 * Created by arran on 11/07/2017.
 */
interface BaseContract {
    interface MvpView{
        fun showError(msg: String)
        fun showError(stringRes: Int = R.string.unexpected_error)
        fun showInfo(msg: String)
        fun showInfo(stringRes: Int)
        fun showSuccess(msg: String)
        fun showSuccess(stringRes: Int)
        fun getActivity(): Activity?
    }

    interface Presenter<in V : MvpView> {

        fun attachView(mvpView: V)

        fun detachView()
    }
}