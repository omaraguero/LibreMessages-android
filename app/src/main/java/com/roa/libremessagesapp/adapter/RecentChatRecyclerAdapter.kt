package com.roa.libremessagesapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.roa.libremessagesapp.ChatActivity
import com.roa.libremessagesapp.R
import com.roa.libremessagesapp.model.ChatroomModel
import com.roa.libremessagesapp.model.UserModel
import com.roa.libremessagesapp.utils.AndroidUtil
import com.roa.libremessagesapp.utils.FirebaseUtil

class RecentChatRecyclerAdapter(
    options: FirestoreRecyclerOptions<ChatroomModel>,
    private val context: Context
) : FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder>(options) {

    override fun onBindViewHolder(holder: ChatroomModelViewHolder, position: Int, model: ChatroomModel) {
        FirebaseUtil.getOtherUserFromChatroom(model.userIds!!)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val lastMessageSentByMe = model.lastMessageSenderId == FirebaseUtil.currentUserId()
                    val otherUserModel = task.result?.toObject(UserModel::class.java)


                    otherUserModel?.let {
                        /*
                        FirebaseUtil.getOtherProfilePicStorageRef(it.userId).downloadUrl
                            .addOnCompleteListener { t ->
                                if (t.isSuccessful) {
                                    val uri = t.result
                                    AndroidUtil.setProfilePic(context, uri, holder.profilePic)
                                }
                            }

                         */

                        holder.usernameText.text = it.username
                        holder.lastMessageText.text = if (lastMessageSentByMe) {
                            "You : ${model.lastMessage}"
                        } else {
                            model.lastMessage
                        }
                        holder.lastMessageTime.text = FirebaseUtil.timestampToString(model.lastMessageTimestamp!!)

                        holder.itemView.setOnClickListener { _ ->
                            // Navigate to chat activity
                            val intent = Intent(context, ChatActivity::class.java).apply {
                                AndroidUtil.passUserModelAsIntent(this, it)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomModelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false)
        return ChatroomModelViewHolder(view)
    }

    class ChatroomModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.user_name_text)
        val lastMessageText: TextView = itemView.findViewById(R.id.last_message_text)
        val lastMessageTime: TextView = itemView.findViewById(R.id.last_message_time_text)
        val profilePic: ImageView = itemView.findViewById(R.id.profile_pic_image_view)
    }
}
