package natto.com.robopgitproject.UserUI

import natto.com.robopgitproject.BasePresenter
import natto.com.robopgitproject.BaseView
import natto.com.robopgitproject.model.User

interface UserContract {
    interface View : BaseView<Presenter> {
        fun setAdapter(list:ArrayList<User>)

        fun connectStatus(status:String)

        fun connectFailure()

        fun connectSuccess()
    }

    interface Presenter : BasePresenter {
        fun reloadData()
    }
}