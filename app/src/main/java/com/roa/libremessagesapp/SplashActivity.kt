package com.roa.libremessagesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.roa.libremessagesapp.databinding.ActivitySplashBinding
import com.roa.libremessagesapp.utils.FirebaseUtil

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            if(FirebaseUtil.isLoggedIn()){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, LoginPhoneNumberActivity::class.java))
            }
            finish()
        }, 1000)
    }
}