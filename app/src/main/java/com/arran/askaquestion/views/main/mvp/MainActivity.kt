package com.arran.askaquestion.views.main.mvp

import android.os.Bundle
import com.arran.askaquestion.AskAQuestion
import com.arran.askaquestion.R
import com.arran.askaquestion.views.base.BaseActivity
import com.arran.askaquestion.views.main.MainActivityModule
import com.arran.askaquestion.views.question.QuestionsFragment
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    private lateinit var nawDrawer: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AskAQuestion.presenterComponent.add(MainActivityModule()).inject(this)
        presenter.attachView(this)
        refreshFragment()
        setupNavigationDrawer()
    }

    fun setupNavigationDrawer() {
        val item2 = SecondaryDrawerItem().withIdentifier(2).withName("channels")
        nawDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(main_toolbar)
                .addDrawerItems(
                        item2,
                        SecondaryDrawerItem().withName("channel 1")
                )
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    true
                }
                .build()
    }

    private fun refreshFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, QuestionsFragment.newInstance())
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
