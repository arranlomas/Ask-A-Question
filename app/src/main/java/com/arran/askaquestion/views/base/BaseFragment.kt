package com.arran.askaquestion.views.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Created by arran on 11/07/2017.
 */
open class BaseFragment : Fragment(), BaseContract.MvpView {

    fun getActivityContext(): Context = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun showError(msg: String) {
        Toasty.error(activity, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showError(stringRes: Int) {
        Toasty.error(activity, getString(stringRes), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringRes))
    }

    override fun showInfo(msg: String) {
        Toasty.info(activity, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showInfo(stringRes: Int) {
        Toasty.info(activity, getString(stringRes), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringRes))
    }

    override fun showSuccess(msg: String) {
        Toasty.success(activity, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showSuccess(stringRes: Int) {
        Toasty.success(activity, getString(stringRes), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringRes))
    }
}