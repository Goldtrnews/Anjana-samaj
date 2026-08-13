package com.example.model

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val mediaUrl: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class ChatConversation(
    val id: String,
    val user1Id: String,
    val user2Id: String,
    val otherUserId: String,
    val otherUserName: String,
    val otherUserPhoto: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val isOnline: Boolean = true
)

data class VideoCallSession(
    val id: String,
    val callerId: String,
    val callerName: String,
    val callerPhoto: String = "",
    val receiverId: String,
    val receiverName: String,
    val receiverPhoto: String = "",
    val status: String = "RINGING", // "RINGING", "CONNECTED", "ENDED", "REJECTED", "MISSED"
    val channelId: String,
    val isVideo: Boolean = true,
    val durationSeconds: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class AppNotification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String, // "INTEREST", "MATCH", "MESSAGE", "CALL", "ANNOUNCEMENT", "VERIFICATION", "POST_LIKE"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val actionData: String = ""
)

data class CommunityAnnouncement(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String = "",
    val eventDate: String = "",
    val location: String = "",
    val authorName: String = "Anjana Samaj Central Committee",
    val timestamp: Long = System.currentTimeMillis(),
    val rsvpCount: Int = 0
)
