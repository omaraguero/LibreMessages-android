package com.roa.libremessagesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.roa.libremessagesapp.databinding.ActivityMainBinding
import com.roa.libremessagesapp.utils.FirebaseUtil


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatFragment = ChatFragment()
        val profileFragment = ProfileFragment()

        binding.mainSearchBtn.setOnClickListener {
            startActivity(Intent(this, SearchUserActivity::class.java))
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_chat -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, chatFragment).commit()
                    true
                }
                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, profileFragment).commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.menu_chat
        getFCMToken()

    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener { task: Task<String?> ->
            if (task.isSuccessful) {
                val token = task.result
                FirebaseUtil.currentUserDetails().update("fcmToken", token)
            }
        }
    }
}