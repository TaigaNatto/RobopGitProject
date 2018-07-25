package natto.com.robopgitproject.UseCase

import android.util.Log
import com.bumptech.glide.load.engine.Resource
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import natto.com.robopgitproject.R
import natto.com.robopgitproject.model.User
import org.json.JSONException
import org.json.JSONObject

class GitApi() {

    fun getUsers(key:String,projectName: String, handler: Handler<Json>) {
        val header: HashMap<String, String> = hashMapOf("Accept" to "application/vnd.github.inertia-preview+json")
        val url = "https://api.github.com/orgs/$projectName/members?access_token=$key"

        url.httpGet().header(header).responseJson(handler)
    }

    fun getUser(key:String,loginId: String, handler: Handler<Json>) {
        val url = "https://api.github.com/users/$loginId?access_token=$key"

        url.httpGet().responseJson(handler)
    }

    fun getUserData(json: Json): User {
        val list = ArrayList<User>()
        val user = User()
        val obj = json.obj()
        user.uName = obj.getString("name")
        user.uId = obj.getString("login")
        user.uImgUrl = obj.getString("avatar_url")
        return user
    }

    fun getLoginIdList(json: Json): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0..(json.array().length() - 1)) {
            val obj = json.array()[i] as JSONObject
            list.add(obj.getString("login"))
        }
        return list
    }
}