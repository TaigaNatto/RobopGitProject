package natto.com.robopgitproject.UserUI

import natto.com.robopgitproject.UseCase.GitApi
import android.util.Log
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import natto.com.robopgitproject.model.User
import org.json.JSONException

class UserPresenter(userView: UserContract.View) : UserContract.Presenter {

    private var mUserView: UserContract.View = userView

    var userList:ArrayList<User>?=null

    init {
        mUserView.setPresenter(this)
    }

    override fun start() {

    }

    override fun reloadData() {
        GitApi().getUsers("t-robop", object: Handler<Json> {
            override fun success(request: Request, response: Response, value: Json) {
                try {
                    userList= GitApi().jsonToList(value)
                    mUserView.setAdapter(userList!!)
                } catch (e: JSONException) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString())
                }
            }

            override fun failure(request: Request, response: Response, error: FuelError) {
                mUserView.connectFailure()
            }
        })
    }
}