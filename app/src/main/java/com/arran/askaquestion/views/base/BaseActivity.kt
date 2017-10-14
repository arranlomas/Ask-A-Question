package com.arran.askaquestion.views.base

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Created by arran on 11/07/2017.
 */
open class BaseActivity: AppCompatActivity(), BaseContract.MvpView {

    override fun getActivity(): Activity? {
        return this
    }

    override fun showError(msg: String) {
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showError(stringRes: Int) {
        Toasty.error(this,getString(stringRes), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringRes))
    }

    override fun showInfo(msg: String) {
        Toasty.info(this, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showInfo(stringRes: Int) {
        Toasty.info(this,getString(stringRes), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringRes))
    }

    override fun showSuccess(msg: String) {
        Toasty.success(this, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showSuccess(stringRes: Int) {
        Toasty.success(this,getString(stringRes), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringRes))
    }
}