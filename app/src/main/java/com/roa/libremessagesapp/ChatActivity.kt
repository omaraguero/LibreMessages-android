package com.roa.libremessagesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.roa.libremessagesapp.databinding.ActivityChatBinding
import com.roa.libremessagesapp.model.UserModel
import com.roa.libremessagesapp.utils.AndroidUtil

class ChatActivity : AppCompatActivity() {


    private var otherUser: UserModel? = null
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otherUser = AndroidUtil.getUserModelFromIntent(intent)

        binding.backBtn.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        binding.otherUsername.text = otherUser!!.username
    }
}