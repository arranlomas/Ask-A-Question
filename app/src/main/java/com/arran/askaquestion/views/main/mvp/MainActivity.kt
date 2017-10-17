package com.arran.askaquestion.views.main.mvp

import android.os.Bundle
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
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
        val item1 = SecondaryDrawerItem().withIdentifier(0).withName(R.string.drawer_item_new_channel)
        val item2 = SecondaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_join_channel)
        nawDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(main_toolbar)
                .addDrawerItems(
                        item1,
                        item2
                )
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    when (drawerItem.identifier) {
                        0L -> {
                            showCreateChannelDialog()
                        }
                        1L -> {

                        }
                    }
                    true
                }
                .build()
    }

    private fun showCreateChannelDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.create_new_channel_dialog_title)
                .customView(R.layout.dialog_create_new_channel, true)
                .positiveText(R.string.create_new_channel_dialog_positive)
                .onPositive { dialog, _ ->
                    val channelName = dialog.customView?.findViewById<EditText>(R.id.channel_name_input)?.text?.toString()
                    val channelPassword = dialog.customView?.findViewById<EditText>(R.id.channel_password_input)?.text?.toString()
                    if (channelName != null && channelPassword != null) presenter.createNewChannel(channelName, channelPassword)
                    else showError()
                }
                .show()

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
