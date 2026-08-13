package com.example.model

data class MatrimonialProfile(
    val id: String,
    val userId: String,
    val name: String,
    val age: Int,
    val gender: String, // "Male", "Female"
    val height: String = "5' 9\"",
    val education: String,
    val occupation: String,
    val annualIncome: String = "12 - 15 LPA",
    val city: String,
    val state: String,
    val photoUrl: String = "",
    val familyDetails: String = "Respected Anjana family, Father Business, Mother Homemaker",
    val marriagePreference: String = "Looking for a cultured, educated life partner from Anjana Samaj",
    val isVerified: Boolean = true,
    val shortlistedByUsers: List<String> = emptyList(),
    val ignoredByUsers: List<String> = emptyList()
)

data class InterestMatch(
    val id: String,
    val senderId: String,
    val senderName: String,
    val senderPhoto: String = "",
    val receiverId: String,
    val receiverName: String,
    val receiverPhoto: String = "",
    val status: String = "PENDING", // "PENDING", "ACCEPTED", "REJECTED"
    val timestamp: Long = System.currentTimeMillis()
)
