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

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun showError(msg: String) {
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showError(stringRes: Int) {
        Toasty.error(this,getString(stringRes), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringRes))
    }
}