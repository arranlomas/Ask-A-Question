package com.arran.askaquestion.views.main.mvp

import android.os.Bundle
import com.arran.askaquestion.AskAQuestion
import com.arran.askaquestion.R
import com.arran.askaquestion.views.base.BaseActivity
import com.arran.askaquestion.views.main.MainActivityModule
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AskAQuestion.presenterComponent.add(MainActivityModule()).inject(this)
        presenter.attachView(this)
    }
}
