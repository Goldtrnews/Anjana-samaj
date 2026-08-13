package com.example.model

data class Post(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorPhoto: String = "",
    val text: String,
    val mediaUrl: String = "",
    val mediaType: String = "NONE", // "NONE", "IMAGE", "VIDEO"
    val createdAt: Long = System.currentTimeMillis(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val likedByUsers: List<String> = emptyList(),
    val isAnnouncement: Boolean = false,
    val status: String = "ACTIVE" // "ACTIVE", "HIDDEN", "REPORTED"
)

data class PostComment(
    val id: String,
    val postId: String,
    val authorId: String,
    val authorName: String,
    val authorPhoto: String = "",
    val commentText: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class StatusItem(
    val id: String,
    val userId: String,
    val userName: String,
    val userPhoto: String = "",
    val mediaUrl: String = "",
    val mediaType: String = "TEXT", // "TEXT", "IMAGE", "VIDEO"
    val text: String = "",
    val backgroundColorHex: String = "#E65100",
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + 24 * 60 * 60 * 1000L,
    val privacy: String = "EVERYONE", // "EVERYONE", "CONNECTIONS", "SELECTED"
    val viewedByUsers: List<String> = emptyList(),
    val likedByUsers: List<String> = emptyList()
)
