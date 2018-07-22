package natto.com.robopgitproject.UseCase

import android.util.Log
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import natto.com.robopgitproject.model.User
import org.json.JSONException
import org.json.JSONObject

class GitApi() {

    fun getUsers(projectName: String, handler: Handler<Json>) {
        val header: HashMap<String, String> = hashMapOf("Accept" to "application/vnd.github.inertia-preview+json")
        val url = "https://api.github.com/orgs/$projectName/members?access_token=7de2dca888f3b64b765087060aa237b8de8471f9"

        url.httpGet().header(header).responseJson(handler)
    }

    fun getUsersTest(projectName: String, handler: Handler<Json>) {
        val header: HashMap<String, String> = hashMapOf("Accept" to "application/vnd.github.inertia-preview+json")
        val url = "https://api.github.com/orgs/$projectName/members?access_token=7de2dca888f3b64b765087060aa237b8de8471f9"

        url.httpGet().header(header).responseJson(object:Handler<Json>{
            override fun success(request: Request, response: Response, value: Json) {
                try {
                    for (i in 0..(value.array().length() - 1)) {
                        //ここでloginIdのみ分かればいい
                        val obj = value.array()[i] as JSONObject
                        val loginName=obj.getString("login")
                        getUser(loginName,object:Handler<Json>{
                            override fun success(request: Request, response: Response, value: Json) {
                                val obj=value.obj()
                                val user = User()
                                user.uName = obj.getString("name")
                                user.uId = obj.getString("login")
                                user.uImgUrl = obj.getString("avatar_url")
                                //userList.add(user)
                            }
                            override fun failure(request: Request, response: Response, error: FuelError) {
                            }
                        })
                    }
                } catch (e: JSONException) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString())
                }
            }

            override fun failure(request: Request, response: Response, error: FuelError) {
                //textV?.text = "通信失敗"
            }
        })
    }

    fun getUser(loginId:String, handler: Handler<Json>){
        val url = "https://api.github.com/users/$loginId?access_token=7de2dca888f3b64b765087060aa237b8de8471f9"

        url.httpGet().responseJson(handler)
    }

    fun jsonToList(json: Json): ArrayList<User> {
        val list = ArrayList<User>()
        for (i in 0..(json.array().length() - 1)) {
            val user = User()
            val obj = json.array()[i] as JSONObject
            user.uName = obj.getString("login")
            user.uId = obj.getString("id")
            user.uImgUrl = obj.getString("avatar_url")
            list.add(user)
        }
        return list
    }
}