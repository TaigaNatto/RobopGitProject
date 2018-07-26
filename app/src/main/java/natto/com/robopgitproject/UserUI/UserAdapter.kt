package natto.com.robopgitproject.UserUI

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import natto.com.robopgitproject.model.User
import natto.com.robopgitproject.R

class UserAdapter(var context: Context, var items: ArrayList<User>) : BaseAdapter() {
    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = convertView
        var holder: CustomViewHolder? = null

        v?.let {
            holder = it.tag as CustomViewHolder?
        } ?: run {
            v = inflater.inflate(R.layout.item_user, null)
            holder = CustomViewHolder(
                    v?.findViewById(R.id.text_user_name) as TextView,
                    v?.findViewById(R.id.text_user_id) as TextView,
                    v?.findViewById(R.id.img_user) as ImageView,
                    v?.findViewById(R.id.text_commit_num) as TextView,
                    v?.findViewById(R.id.container_commit_num) as LinearLayout)
            v?.tag = holder
        }

        holder?.let {
            it.nameText.text=items[position].uName
            it.idText.text=items[position].uId
            val commitNum=items[position].uCommitNum
            it.commitNumText.text=commitNum.toString()
            if (commitNum>100){
                it.commitNumContainer.setBackgroundResource(R.color.colorTop)
            }else if(commitNum>50){
                it.commitNumContainer.setBackgroundResource(R.color.colorSecond)
            }else if(commitNum>30){
                it.commitNumContainer.setBackgroundResource(R.color.colorThird)
            }
            Glide.with(v?.context)
                    .load(items[position].uImgUrl)
                    .into(it.avaterImage)
        }

        return v as View
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    class CustomViewHolder(
            var nameText: TextView,
            var idText:TextView,
            var avaterImage:ImageView,
            var commitNumText:TextView,
            var commitNumContainer:LinearLayout
    )
}