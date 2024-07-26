package com.roa.libremessagesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.roa.libremessagesapp.databinding.ActivityLoginUsernameBinding
import com.roa.libremessagesapp.model.UserModel
import com.roa.libremessagesapp.utils.FirebaseUtil


class LoginUsernameActivity : AppCompatActivity() {

    private lateinit var phoneNumber: String
    private lateinit var binding: ActivityLoginUsernameBinding
    private var userModel: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        phoneNumber = intent.extras?.getString("phone").toString()
        getUsername()

        binding.loginLetMeInBtn.setOnClickListener {
            setUsername()
        }
    }

    fun getUsername() {
        setInProgress(true)
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val userModel = task.result.toObject(UserModel::class.java)
                if (userModel != null) {
                    binding.loginUsername.setText(userModel.username)
                }
            }
        }
    }

    fun setUsername(){
        val username = binding.loginUsername.text.toString()
        if(username.isEmpty() || username.length < 3){
            binding.loginUsername.error = "Username length sould be at least 3 chars"
            return
        }
        setInProgress(true)
        if(userModel!=null){
            userModel!!.username = username
        }else{
            userModel = UserModel(phoneNumber, username, Timestamp.now())
        }

        FirebaseUtil.currentUserDetails().set(userModel!!).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

    }


    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.loginProgressBar.visibility = View.VISIBLE
            binding.loginLetMeInBtn.visibility = View.GONE
        } else {
            binding.loginProgressBar.visibility = View.GONE
            binding.loginLetMeInBtn.visibility = View.VISIBLE
        }
    }
}