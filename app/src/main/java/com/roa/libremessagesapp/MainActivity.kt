package com.roa.libremessagesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.roa.libremessagesapp.databinding.ActivityLoginPhoneNumberBinding
import com.roa.libremessagesapp.databinding.ActivityMainBinding

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

    }
}