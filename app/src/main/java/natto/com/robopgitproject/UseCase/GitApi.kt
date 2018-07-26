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

    fun getProjectCommits(key:String,projectName: String,userId:String, handler: Handler<Json>){
        val header: HashMap<String, String> = hashMapOf("Accept" to "application/vnd.github.cloak-preview")
        val url = "https://api.github.com/search/commits?q=org:$projectName+committer:$userId&access_token=$key"

        url.httpGet().header(header).responseJson(handler)
    }

    fun getUserData(json: Json): User {
        val user = User()
        val obj = json.obj()
        val uName=obj.getString("name")
        val uId=obj.getString("login")
        if (uName != "null") {
            user.uName = uName
        }else{
            user.uName=uId
        }
        user.uId = uId
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

    fun getCommitNum(json: Json):Int{
        val obj = json.obj()
        return obj.getInt("total_count")
    }
}