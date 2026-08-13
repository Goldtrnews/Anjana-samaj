package com.example.model

data class UserReport(
    val id: String,
    val reporterId: String,
    val reporterName: String = "",
    val targetType: String, // "PROFILE", "POST", "STATUS", "MESSAGE", "CALL"
    val targetId: String,
    val targetAuthorId: String,
    val reason: String, // "Fake Profile", "Harassment", "Spam", "Fraud", "Inappropriate Content", "Other"
    val details: String = "",
    val status: String = "PENDING", // "PENDING", "REVIEWED", "DISMISSED", "ACTIONED"
    val createdAt: Long = System.currentTimeMillis()
)

data class AdminStats(
    val totalUsers: Int = 12450,
    val verifiedUsers: Int = 9820,
    val pendingVerifications: Int = 34,
    val totalPosts: Int = 3840,
    val reportedPosts: Int = 8,
    val reportedUsers: Int = 5,
    val activeMatrimonialProfiles: Int = 4120,
    val totalMatches: Int = 1850,
    val totalCalls: Int = 6300
)
