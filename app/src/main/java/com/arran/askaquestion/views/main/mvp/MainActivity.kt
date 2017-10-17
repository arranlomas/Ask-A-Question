package com.arran.askaquestion.views.main.mvp

import android.os.Bundle
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.arran.askaquestion.AskAQuestion
import com.arran.askaquestion.R
import com.arran.askaquestion.models.Channel
import com.arran.askaquestion.views.base.BaseActivity
import com.arran.askaquestion.views.main.MainActivityModule
import com.arran.askaquestion.views.question.QuestionsFragment
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.View {
    private val DEFAULT_DRAWER_ITEMS = 2

    @Inject
    lateinit var presenter: MainContract.Presenter

    private lateinit var nawDrawer: Drawer

    val drawerItemsMap = mutableMapOf<SecondaryDrawerItem, Channel?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AskAQuestion.presenterComponent.add(MainActivityModule()).inject(this)
        presenter.attachView(this)
        refreshQuestionsFragment()
        setupNavigationDrawer(emptyList())
    }

    fun setupNavigationDrawer(channels: List<Channel>) {
        drawerItemsMap.clear()
        val item1 = SecondaryDrawerItem().withIdentifier(0).withName(R.string.drawer_item_new_channel)
        val item2 = SecondaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_join_channel)
        drawerItemsMap.put(item1, null)
        drawerItemsMap.put(item2, null)
        channels.forEachIndexed { index, channel ->
            val channelDrawerItem = SecondaryDrawerItem().withIdentifier((index + DEFAULT_DRAWER_ITEMS).toLong()).withName(channel.channelName)
            drawerItemsMap.put(channelDrawerItem, channel)
        }
        nawDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(main_toolbar)
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    onDrawerItemSelected(drawerItem)
                    true
                }
                .build()

        drawerItemsMap.forEach { secondaryDrawerItem, _ ->
            nawDrawer.addItem(secondaryDrawerItem)
        }
    }

    private fun onDrawerItemSelected(drawerItem: IDrawerItem<*, *>) {
        when (drawerItem.identifier) {
            0L -> showCreateChannelDialog()
            1L -> showJoinChannelDialog()
            else -> {
                drawerItemsMap[drawerItem]?.let {
                    refreshQuestionsFragment(it.firebaseKey)
                } ?: showError()
            }
        }

    }

    private fun showCreateChannelDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.create_new_channel_dialog_title)
                .customView(R.layout.dialog_channel_details, true)
                .positiveText(R.string.create_new_channel_dialog_positive)
                .onPositive { dialog, _ ->
                    val channelName = dialog.customView?.findViewById<EditText>(R.id.channel_name_input)?.text?.toString()
                    val channelPassword = dialog.customView?.findViewById<EditText>(R.id.channel_password_input)?.text?.toString()
                    if (channelName != null && channelPassword != null) presenter.createNewChannel(channelName, channelPassword)
                    else showError()
                }
                .show()
    }

    private fun showJoinChannelDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.join_channel_dialog_title)
                .customView(R.layout.dialog_channel_details, true)
                .positiveText(R.string.join_channel_dialog_positive)
                .onPositive { dialog, _ ->
                    val channelName = dialog.customView?.findViewById<EditText>(R.id.channel_name_input)?.text?.toString()
                    val channelPassword = dialog.customView?.findViewById<EditText>(R.id.channel_password_input)?.text?.toString()
                    if (channelName != null && channelPassword != null) presenter.joinChannel(channelName, channelPassword)
                    else showError()
                }
                .show()
    }

    private fun refreshQuestionsFragment(channelKey: String? = null) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, QuestionsFragment.newInstance(channelKey))
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun updateChannels(channels: List<Channel>) {
        setupNavigationDrawer(channels)
    }
}
