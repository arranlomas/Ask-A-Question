package com.arran.askaquestion.views.question

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.arran.askaquestion.AskAQuestion
import com.arran.askaquestion.R
import com.arran.askaquestion.models.Question
import com.arran.askaquestion.views.base.BaseFragment
import com.arran.askaquestion.views.main.MainActivityModule
import com.arran.askaquestion.views.question.list.QuestionsAdapter
import kotlinx.android.synthetic.main.frag_questions.*
import javax.inject.Inject


class QuestionsFragment : BaseFragment(), QuestionsContract.View {

    @Inject
    lateinit var presenter: QuestionsContract.Presenter

    lateinit var adapter: QuestionsAdapter

    companion object{
        fun newInstance(): Fragment{
            return QuestionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        AskAQuestion.presenterComponent.add(QuestionsModule()).inject(this)
        return inflater?.inflate(R.layout.frag_questions, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        fab.setOnClickListener { showAddQuestionDialog() }

        adapter = QuestionsAdapter({ question, action -> presenter.onListItemAction(question, action) })

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = GridLayoutManager(context, 1, OrientationHelper.VERTICAL, false)
        recycler_view.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun showAddQuestionDialog() {
        MaterialDialog.Builder(context)
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
