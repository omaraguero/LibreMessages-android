package com.roa.libremessagesapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.roa.libremessagesapp.model.UserModel


public class AndroidUtil{

    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun passUserModelAsIntent(intent: Intent, model: UserModel) {
            intent.putExtra("username", model.username)
            intent.putExtra("phone", model.phone)
            intent.putExtra("userId", model.userId)
            //intent.putExtra("fcmToken", model.getFcmToken())
        }

        fun getUserModelFromIntent(intent: Intent): UserModel {
            val userModel = UserModel()
            userModel.username = intent.getStringExtra("username")
            userModel.phone = intent.getStringExtra("phone")
            userModel.userId = intent.getStringExtra("userId")
            //userModel.setFcmToken(intent.getStringExtra("fcmToken"))
            return userModel
        }

        /*
        fun setProfilePic(context: Context?, imageUri: Uri?, imageView: ImageView?) {
            Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform())
                .into(imageView)
        }

         */

    }
}