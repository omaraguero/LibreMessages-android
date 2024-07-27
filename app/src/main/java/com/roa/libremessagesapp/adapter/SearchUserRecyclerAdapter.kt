package com.roa.libremessagesapp.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.roa.libremessagesapp.ChatActivity
import com.roa.libremessagesapp.R
import com.roa.libremessagesapp.model.UserModel
import com.roa.libremessagesapp.utils.AndroidUtil
import com.roa.libremessagesapp.utils.FirebaseUtil

class SearchUserRecyclerAdapter(
    options: FirestoreRecyclerOptions<UserModel>,
    private val context: Context
) : FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder>(options) {

    override fun onBindViewHolder(holder: UserModelViewHolder, position: Int, model: UserModel) {
        holder.usernameText.text = model.username
        holder.phoneText.text = model.phone
        if (model.userId == FirebaseUtil.currentUserId()) {
            holder.usernameText.text = "${model.username} (Me)"
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            AndroidUtil.passUserModelAsIntent(intent, model)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserModelViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false)
        return UserModelViewHolder(view)
    }

    inner class UserModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.user_name_text)
        val phoneText: TextView = itemView.findViewById(R.id.phone_text)
        val profilePic: ImageView = itemView.findViewById(R.id.profile_pic_image_view)
    }
}