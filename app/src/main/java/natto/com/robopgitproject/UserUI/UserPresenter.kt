package natto.com.robopgitproject.UserUI

import android.os.Message
import natto.com.robopgitproject.UseCase.GitApi
import android.util.Log
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import natto.com.robopgitproject.model.User
import org.json.JSONException
import java.util.*

class UserPresenter(userView: UserContract.View) : UserContract.Presenter {

    private var mUserView: UserContract.View = userView

    private var mHandler: LoadHandler? = null

    private var GIT_API_KEY=""

    var userNum=0

    var userList:ArrayList<User>?=null

    init {
        mUserView.setPresenter(this)
        userList=ArrayList()
        mHandler=LoadHandler()
        userNum=0
    }

    override fun start() {

    }

    override fun reloadData() {
        GitApi().getUsers(this.GIT_API_KEY,"t-robop", object: Handler<Json> {
            override fun success(request: Request, response: Response, value: Json) {
                try {
                    val idList=GitApi().getLoginIdList(value)
                    userNum=idList.size
                    for(id in idList) {
                        val msg = Message.obtain()
                        msg.obj = id
                        mHandler?.sendMessage(msg)
                        Thread(Runnable {
                            GitApi().getUser(this@UserPresenter.GIT_API_KEY,id,object: Handler<Json>{
                                override fun success(request: Request, response: Response, uValue: Json) {
                                    val userData=GitApi().getUserData(uValue)
                                    GitApi().getProjectCommits(this@UserPresenter.GIT_API_KEY,"t-robop",userData.uId, object: Handler<Json>{
                                        override fun success(request: Request, response: Response, cValue: Json) {
                                            userData.uCommitNum=GitApi().getCommitNum(cValue)
                                            userList?.add(userData)
                                            val msg = Message.obtain()
                                            msg.obj = "success"
                                            mHandler?.sendMessage(msg)
                                        }
                                        override fun failure(request: Request, response: Response, error: FuelError) {
                                            Log.d("COMMIT_TEST","しっぱい:${error.response}")
                                            userList?.add(userData)
                                            val msg = Message.obtain()
                                            msg.obj = "success"
                                            mHandler?.sendMessage(msg)
                                        }
                                    })
                                }
                                override fun failure(request: Request, response: Response, error: FuelError) {
                                    mUserView.connectFailure()
                                }
                            })
                        }).start()
                    }
                    //mUserView.connectSuccess()
                } catch (e: JSONException) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString())
                }
            }

            override fun failure(request: Request, response: Response, error: FuelError) {
                mUserView.connectFailure()
            }
        })
    }

    override fun setApiKey(key: String) {
        this.GIT_API_KEY=key
    }

    internal inner class LoadHandler : android.os.Handler() {
        override fun handleMessage(msg: Message) {
            val text = msg.obj.toString()
            mUserView.connectStatus(userList?.size.toString()+"/"+userNum.toString())
            if(userList?.size==userNum) {
                userList?.sortWith(Comparator { p0, p1 -> if (p0!!.uCommitNum < p1!!.uCommitNum) 1 else -1 })
                mUserView.setAdapter(userList!!)
            }
        }
    }
}