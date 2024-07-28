package com.roa.libremessagesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.roa.libremessagesapp.databinding.ActivityChatBinding
import com.roa.libremessagesapp.model.ChatMessageModel
import com.roa.libremessagesapp.model.ChatroomModel
import com.roa.libremessagesapp.model.UserModel
import com.roa.libremessagesapp.utils.AndroidUtil
import com.roa.libremessagesapp.utils.FirebaseUtil
import java.util.Arrays


class ChatActivity : AppCompatActivity() {


    private var otherUser: UserModel? = null
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatroomId: String
    private var chatroomModel: ChatroomModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otherUser = AndroidUtil.getUserModelFromIntent(intent)
        chatroomId = FirebaseUtil.getChatroomId(
            FirebaseUtil.currentUserId().toString(), otherUser!!.userId.toString()
        )

        binding.backBtn.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        binding.otherUsername.text = otherUser!!.username

        binding.messageSendBtn.setOnClickListener {
            val message = binding.chatMessageInput.text.toString().trim()
            if(message.isEmpty()){
                return@setOnClickListener
            }
            sendMessageToUser(message)
        }


        getOrCreateChatroomModel()
    }

    private fun sendMessageToUser(message: String) {

        chatroomModel!!.lastMessageTimestamp = Timestamp.now()
        chatroomModel!!.lastMessageSenderId = FirebaseUtil.currentUserId()
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel!!)

        val chatMessageModel = ChatMessageModel(message, FirebaseUtil.currentUserId(), Timestamp.now())
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.chatMessageInput.setText("")
                    //sendNotification(message)
                }
            }

    }

    fun getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                chatroomModel = task.result.toObject(ChatroomModel::class.java)
                if (chatroomModel == null) {
                    //first time chat
                    chatroomModel = ChatroomModel(
                        chatroomId,
                        Arrays.asList(FirebaseUtil.currentUserId(), otherUser!!.userId) as List<String>?,
                        Timestamp.now(),
                        ""
                    )
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel!!)
                }
            }
        }
    }
}