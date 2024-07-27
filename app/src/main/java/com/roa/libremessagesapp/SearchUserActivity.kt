package com.roa.libremessagesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.roa.libremessagesapp.databinding.ActivityMainBinding
import com.roa.libremessagesapp.databinding.ActivitySearchUserBinding

class SearchUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seachUsernameInput.requestFocus()

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.searchUserBtn.setOnClickListener {
            var searchTerm = binding.seachUsernameInput.text.toString()
            if(searchTerm.isEmpty() || searchTerm.length < 3){
               binding.seachUsernameInput.error = "Invalid user"
                return@setOnClickListener
            }
            setupSearchRecyclerView(searchTerm)

        }
    }

    fun setupSearchRecyclerView(searchTerm: String){


    }

}