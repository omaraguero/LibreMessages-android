package com.roa.libremessagesapp.utils

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat


public class FirebaseUtil {


    companion object {
        fun currentUserId(): String? {
            return FirebaseAuth.getInstance().uid
        }

        fun currentUserDetails(): DocumentReference {
            val userId = currentUserId()
            return FirebaseFirestore.getInstance().collection("users").document(userId ?: "")
        }

        fun isLoggedIn(): Boolean {
            if(currentUserId()!=null){
                return true
            }
            return false
        }

        fun allUserCollectionReference(): CollectionReference {
            return FirebaseFirestore.getInstance().collection("users")
        }

        fun getChatroomReference(chatroomId: String?): DocumentReference {
            return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId!!)
        }

        fun getChatroomId(userId1: String, userId2: String): String {
            return if (userId1.hashCode() < userId2.hashCode()) {
                userId1 + "_" + userId2
            } else {
                userId2 + "_" + userId1
            }
        }

        fun getChatroomMessageReference(chatroomId: String?): CollectionReference {
            return getChatroomReference(chatroomId).collection("chats")
        }

        fun allChatroomCollectionReference(): CollectionReference {
            return FirebaseFirestore.getInstance().collection("chatrooms")
        }

        fun getOtherUserFromChatroom(userIds: List<String>): DocumentReference {
            return if (userIds[0] == currentUserId()) {
                allUserCollectionReference().document(userIds[1])
            } else {
                allUserCollectionReference().document(userIds[0])
            }
        }

        fun timestampToString(timestamp: Timestamp): String {
            return SimpleDateFormat("HH:MM").format(timestamp.toDate())
        }


    }

}