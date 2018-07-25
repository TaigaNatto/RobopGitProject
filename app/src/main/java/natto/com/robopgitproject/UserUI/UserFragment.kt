package natto.com.robopgitproject.UserUI

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import natto.com.robopgitproject.model.User

import natto.com.robopgitproject.R

class UserFragment : Fragment(), UserContract.View {

    private lateinit var mPresenter: UserContract.Presenter

    private lateinit var mContext: Context

    var textV: TextView? = null
    var listV: ListView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textV = view.findViewById(R.id.text)
        listV=view.findViewById(R.id.list_user)
        mContext=view.context
        mPresenter.setApiKey(mContext.getString(R.string.GIT_API_SECRET_KEY))
        mPresenter.reloadData()
    }

    override fun setPresenter(presenter: UserContract.Presenter) {
        mPresenter=presenter
    }

    override fun setAdapter(list: ArrayList<User>) {
        listV?.adapter= UserAdapter(mContext, list)
    }

    override fun connectFailure() {
        textV?.text="通信失敗"
    }

    override fun connectSuccess() {
        textV?.text="通信成功"
    }

    override fun connectStatus(status: String) {
        textV?.text=status
    }

}
