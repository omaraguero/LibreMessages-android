package com.roa.libremessagesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.roa.libremessagesapp.adapter.RecentChatRecyclerAdapter
import com.roa.libremessagesapp.model.ChatroomModel
import com.roa.libremessagesapp.utils.FirebaseUtil

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecentChatRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerView = view.findViewById(R.id.recyler_view)
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        val query: Query = FirebaseUtil.allChatroomCollectionReference()
            .whereArrayContains("userIds", FirebaseUtil.currentUserId()!!)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<ChatroomModel>()
            .setQuery(query, ChatroomModel::class.java).build()

        adapter = RecentChatRecyclerAdapter(options, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.startListening()
    }


    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

}