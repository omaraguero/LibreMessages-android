package com.roa.libremessagesapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.roa.libremessagesapp.adapter.ChatRecyclerAdapter
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

    private var adapter: ChatRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otherUser = AndroidUtil.getUserModelFromIntent(intent)
        chatroomId = FirebaseUtil.getChatroomId(
            FirebaseUtil.currentUserId().toString(), otherUser!!.userId.toString()
        )

        val imageView: ImageView = findViewById(R.id.profile_pic_image_view)

        FirebaseUtil.getOtherProfilePicStorageRef(otherUser!!.userId).downloadUrl
            .addOnCompleteListener { t ->
                if (t.isSuccessful) {
                    val uri = t.result
                    AndroidUtil.setProfilePic(this, uri, imageView)
                }
            }

        binding.backBtn.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        binding.otherUsername.text = otherUser!!.username

        binding.messageSendBtn.setOnClickListener {
            val message = binding.chatMessageInput.text.toString().trim()
            if (message.isEmpty()) {
                return@setOnClickListener
            }
            sendMessageToUser(message)
        }


        getOrCreateChatroomModel()
        setupChatRecyclerView()
    }

    private fun setupChatRecyclerView() {
        val query = FirebaseUtil.getChatroomMessageReference(chatroomId)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<ChatMessageModel>()
            .setQuery(query, ChatMessageModel::class.java).build()

        adapter = ChatRecyclerAdapter(options, applicationContext)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true

        binding.chatRecyclerView.layoutManager = manager
        binding.chatRecyclerView.adapter = adapter
        adapter!!.startListening()
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.chatRecyclerView.smoothScrollToPosition(0)
            }
        })

    }

    private fun sendMessageToUser(message: String) {

        chatroomModel!!.lastMessageTimestamp = Timestamp.now()
        chatroomModel!!.lastMessageSenderId = FirebaseUtil.currentUserId()
        chatroomModel!!.lastMessage = message
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel!!)

        val chatMessageModel =
            ChatMessageModel(message, FirebaseUtil.currentUserId(), Timestamp.now())
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.chatMessageInput.setText("")
                    sendNotification(message)
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
                        Arrays.asList(
                            FirebaseUtil.currentUserId(),
                            otherUser!!.userId
                        ) as List<String>?,
                        Timestamp.now(),
                        ""
                    )
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel!!)
                }
            }
        }
    }

    private fun sendNotification(message: String) {
        val otherUserId = otherUser?.userId ?: return
        val otherUserFcmToken = otherUser?.fcmToken ?: return
        val otherUserName = otherUser?.username ?: return

        val title = "New Message from $otherUserName"
        val body = message

        val sendNotification = SendNotification(otherUserFcmToken, title, body, this)
        sendNotification.sendNotifications()
    }


}