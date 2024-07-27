package com.roa.libremessagesapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


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

    }

}