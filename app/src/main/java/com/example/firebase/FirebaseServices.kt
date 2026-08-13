package com.example.firebase

import android.content.Context
import android.util.Log
import com.example.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthService {
    private var auth: FirebaseAuth? = null

    init {
        try {
            auth = FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.w("FirebaseAuthService", "Firebase Auth not initialized yet. Operating in standalone demo mode.")
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth?.currentUser != null
    }

    fun getCurrentUserId(): String {
        return auth?.currentUser?.uid ?: "user_101"
    }

    fun logout() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            Log.e("FirebaseAuthService", "Sign out error: ${e.message}")
        }
    }
}

class FirestoreService {
    private var db: FirebaseFirestore? = null

    init {
        try {
            db = FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w("FirestoreService", "Firestore DB not initialized yet. Operating in local repository mode.")
        }
    }

    val isLiveFirebaseAvailable: Boolean
        get() = db != null

    suspend fun saveUserProfile(user: UserProfile): Boolean {
        return try {
            db?.collection("users")?.document(user.id)?.set(user)?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error saving user: ${e.message}")
            false
        }
    }

    suspend fun createPost(post: Post): Boolean {
        return try {
            db?.collection("posts")?.document(post.id)?.set(post)?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error creating post: ${e.message}")
            false
        }
    }

    suspend fun createStatus(status: StatusItem): Boolean {
        return try {
            db?.collection("statuses")?.document(status.id)?.set(status)?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error creating status: ${e.message}")
            false
        }
    }

    suspend fun sendInterest(interest: InterestMatch): Boolean {
        return try {
            db?.collection("interests")?.document(interest.id)?.set(interest)?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error sending interest: ${e.message}")
            false
        }
    }

    suspend fun sendChatMessage(message: ChatMessage): Boolean {
        return try {
            db?.collection("messages")?.document(message.id)?.set(message)?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error sending message: ${e.message}")
            false
        }
    }

    suspend fun submitReport(report: UserReport): Boolean {
        return try {
            db?.collection("reports")?.document(report.id)?.set(report)?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error submitting report: ${e.message}")
            false
        }
    }
}
