package com.roa.libremessagesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.roa.libremessagesapp.databinding.ActivityLoginOtpBinding
import com.roa.libremessagesapp.databinding.ActivityLoginUsernameBinding

class LoginUsernameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginUsernameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}