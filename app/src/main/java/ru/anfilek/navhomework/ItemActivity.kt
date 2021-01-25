package ru.anfilek.navhomework

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.anfilek.navhomework.ListActivity.Companion.SOME_RESOURCE_ID
import ru.anfilek.navhomework.databinding.ActivityItemBinding

class ItemActivity : AppCompatActivity() {

    private val userLogin: UserLogin by lazy { UserLogin(this) }
    private lateinit var binding: ActivityItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startAgainButton.setOnClickListener { startMeAgain() }
        binding.logout.setOnClickListener { logout() }
        
        renderItemId()
    }

    private fun renderItemId() {
        binding.tvItemId.text = intent.extras?.getString(SOME_RESOURCE_ID)
    }

    private fun startMeAgain() {
        finish()
        startActivity(intent)

    }

    private fun logout() {
        userLogin.setUserLoggedOut()
        val fromTheBeginningIntent = Intent(this,LoginActivity::class.java)
        fromTheBeginningIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(fromTheBeginningIntent)
        finish()
    }
}