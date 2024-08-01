package com.roa.libremessagesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import com.roa.libremessagesapp.databinding.ActivitySplashBinding
import com.roa.libremessagesapp.model.UserModel
import com.roa.libremessagesapp.utils.AndroidUtil
import com.roa.libremessagesapp.utils.FirebaseUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (intent.extras != null) {

            val userId = intent.extras?.getString("userId")
            if (userId != null) {
                FirebaseUtil.allUserCollectionReference().document(userId).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val model = task.result.toObject(UserModel::class.java)

                            val mainIntent = Intent(this, MainActivity::class.java)
                            mainIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                            startActivity(mainIntent)

                            val intent = Intent(this, ChatActivity::class.java)
                            AndroidUtil.passUserModelAsIntent(intent, model!!)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                if (FirebaseUtil.isLoggedIn()) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginPhoneNumberActivity::class.java))
                }
                finish()
            }, 1000)
            ////
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
    }
}
