package natto.com.robopgitproject

import natto.com.robopgitproject.UserUI.UserFragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import natto.com.robopgitproject.UserUI.UserPresenter

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fragmentを作成します
        val fragment = UserFragment()
        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
        val transaction = supportFragmentManager.beginTransaction()
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        transaction.add(R.id.container,fragment)
        // 最後にcommitを使用することで変更を反映します
        transaction.commit()

        UserPresenter(fragment)
    }
}
