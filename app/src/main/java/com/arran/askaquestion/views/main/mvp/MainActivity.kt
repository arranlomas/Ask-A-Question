package com.arran.askaquestion.views.main.mvp

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.arran.askaquestion.AskAQuestion
import com.arran.askaquestion.R
import com.arran.askaquestion.models.Question
import com.arran.askaquestion.views.base.BaseActivity
import com.arran.askaquestion.views.main.MainActivityModule
import com.arran.askaquestion.views.main.list.QuestionsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    lateinit var adapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AskAQuestion.presenterComponent.add(MainActivityModule()).inject(this)
        presenter.attachView(this)

        fab.setOnClickListener { showAddQuestionDialog() }

        adapter = QuestionsAdapter({ question, action -> presenter.onListItemAction(question, action) })

        mainRecyclerView.setHasFixedSize(true)
        val mLayoutManager = GridLayoutManager(this, 1, OrientationHelper.VERTICAL, false)
        mainRecyclerView.layoutManager = mLayoutManager
        mainRecyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun showAddQuestionDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.add_new_question_dialog_title)
                .customView(R.layout.dialog_add_new_question, true)
                .positiveText(R.string.add_new_question_dialog_positive)
                .onPositive { dialog, _ ->
                    dialog.customView?.findViewById<EditText>(R.id.question_input)?.text?.toString()?.let {
                        presenter.sendNewQuestion(it)
                    } ?: showError()
                }
                .show()

    }

    override fun showQuestions(questions: List<Question>) {
        adapter.items = questions
        adapter.notifyDataSetChanged()
    }
}
