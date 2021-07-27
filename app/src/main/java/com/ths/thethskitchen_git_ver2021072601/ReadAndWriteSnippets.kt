package com.ths.thethskitchen_git_ver2021072601

import android.util.Log
import com.google.api.services.drive.model.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ths.a20210713_sqllitehelper.DList

abstract class ReadAndWriteSnippets {
    private val TAG = "ReadAndWriteSnippets"

    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]

    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = Firebase.database.reference
        // [END initialize_database_ref]
    }

    private fun addPostEventListener(postReference: DatabaseReference) {
        // [START post_value_event_listener]
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue<DList>()
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
        // [END post_value_event_listener]
    }


    // [START post_stars_transaction]
    private fun onStarClicked(postRef: DatabaseReference) {
        // [START_EXCLUDE]
        val uid = ""
        // [END_EXCLUDE]
        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val p = mutableData.getValue(DList::class.java)
                    ?: return Transaction.success(mutableData)

//                if (p.stars.containsKey(uid)) {
//                    // Unstar the post and remove self from stars
//                    p.starCount = p.starCount - 1
//                    p.stars.remove(uid)
//                } else {
//                    // Star the post and add self to stars
//                    p.starCount = p.starCount + 1
//                    p.stars[uid] = true
//                }

                // Set value and report transaction success
                mutableData.value = p
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError!!)
            }
        })
    }
    // [END post_stars_transaction]

    // [START post_stars_increment]
//    private fun onStarClicked(uid: String, key: String) {
//        val updates: MutableMap<String, Any> = HashMap()
//        updates["posts/$key/stars/$uid"] = true
//        updates["posts/$key/starCount"] = ServerValue.increment(1)
//        updates["user-posts/$uid/$key/stars/$uid"] = true
//        updates["user-posts/$uid/$key/starCount"] = ServerValue.increment(1)
//        database.updateChildren(updates)
//    }
    // [END post_stars_increment]
}