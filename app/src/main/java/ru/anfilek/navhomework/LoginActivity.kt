package ru.anfilek.navhomework

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val userLogin: UserLogin by lazy { UserLogin(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.button).setOnClickListener {
            performLogin()
        }
        checkLoginFlow()
    }

    private fun checkLoginFlow() {
        if (userLogin.isUserLoggedIn()) {
            openListActivity()
        }
    }

    private fun performLogin() {
        userLogin.setUserLoggedIn()
        openListActivity()
    }

    private fun openListActivity(){
        startActivity(Intent(this, ListActivity::class.java))
        finish()
    }
}