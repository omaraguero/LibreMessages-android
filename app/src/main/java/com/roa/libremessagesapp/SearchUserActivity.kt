package com.roa.libremessagesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.roa.libremessagesapp.adapter.SearchUserRecyclerAdapter
import com.roa.libremessagesapp.databinding.ActivitySearchUserBinding
import com.roa.libremessagesapp.model.UserModel
import com.roa.libremessagesapp.utils.FirebaseUtil


class SearchUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchUserBinding
    private var adapter: SearchUserRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seachUsernameInput.requestFocus()

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.searchUserBtn.setOnClickListener {
            var searchTerm = binding.seachUsernameInput.text.toString()
            if(searchTerm.isEmpty() || searchTerm.length < 3){
               binding.seachUsernameInput.error = "Invalid user"
                return@setOnClickListener
            }
            setupSearchRecyclerView(searchTerm)

        }
    }

    fun setupSearchRecyclerView(searchTerm: String){

        var query = FirebaseUtil.allUserCollectionReference()
            .whereGreaterThanOrEqualTo("username", searchTerm)

        val options = FirestoreRecyclerOptions.Builder<UserModel>()
            .setQuery(query, UserModel::class.java).build()

        adapter = SearchUserRecyclerAdapter(options ,applicationContext)
        binding.searchUserRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchUserRecyclerView.adapter = adapter
        adapter!!.startListening()
    }

    override fun onStart() {
        super.onStart()
        if(adapter!=null){
            adapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if(adapter!=null){
            adapter!!.stopListening()
        }
    }


    override fun onResume() {
        super.onResume()
        if(adapter!=null){
            adapter!!.startListening()
        }
    }

}