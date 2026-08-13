package com.example.model

data class UserProfile(
    val id: String,
    val phone: String,
    val fullName: String,
    val photoUrl: String = "",
    val dob: String = "",
    val age: Int = 25,
    val gender: String = "Male", // "Male", "Female", "Other"
    val community: String = "Anjana Choudhary",
    val subCommunity: String = "Pawar / Patel",
    val education: String = "B.Tech / MCA",
    val occupation: String = "Software Engineer",
    val city: String = "Jodhpur",
    val state: String = "Rajasthan",
    val aboutMe: String = "Proud member of Anjana Samaj. Looking to connect with family and friends.",
    val marriagePreference: String = "Looking for educated, cultured matches in Anjana Samaj",
    val contactPrivacy: String = "CONNECTIONS_ONLY", // "EVERYONE", "CONNECTIONS_ONLY", "PRIVATE"
    val isVerified: String = "VERIFIED", // "PENDING", "VERIFIED", "REJECTED", "NOT_APPLIED"
    val isAdmin: Boolean = false,
    val isBlocked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class NearbyMember(
    val id: String,
    val fullName: String,
    val age: Int,
    val gender: String,
    val city: String,
    val distanceKm: Double,
    val approximateArea: String,
    val occupation: String,
    val education: String,
    val isVerified: Boolean,
    val photoUrl: String
)

data class VerificationRequest(
    val id: String,
    val userId: String,
    val userName: String,
    val userPhone: String,
    val documentType: String = "Aadhaar Card / Samaj ID",
    val documentUrl: String, // Secure restricted URL
    val notes: String = "",
    val status: String = "PENDING", // "PENDING", "VERIFIED", "REJECTED"
    val submittedAt: Long = System.currentTimeMillis(),
    val rejectionReason: String = ""
)
