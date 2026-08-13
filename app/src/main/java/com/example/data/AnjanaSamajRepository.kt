package com.example.data

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.firebase.FirebaseAuthService
import com.example.firebase.FirestoreService
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnjanaSamajRepository(private val context: Context) {

    private val authService = FirebaseAuthService()
    private val firestoreService = FirestoreService()
    private val database = AppDatabase.getDatabase(context)

    // Current logged-in user state
    private val _currentUser = MutableStateFlow<UserProfile?>(
        UserProfile(
            id = "user_101",
            phone = "+91 98290 12345",
            fullName = "Vikram Patel Anjana",
            photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
            dob = "15/08/1996",
            age = 28,
            gender = "Male",
            community = "Anjana Choudhary",
            education = "B.Tech Computer Science",
            occupation = "Senior Software Engineer",
            city = "Jodhpur",
            state = "Rajasthan",
            aboutMe = "Proud member of Anjana Samaj. Passionate about community social work, photography & tech.",
            marriagePreference = "Seeking an educated, warm-hearted bride from Anjana Samaj who respects family values.",
            isVerified = "VERIFIED",
            isAdmin = true
        )
    )
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Posts Feed
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // Comments map: postId -> list of comments
    private val _comments = MutableStateFlow<Map<String, List<PostComment>>>(emptyMap())
    val comments: StateFlow<Map<String, List<PostComment>>> = _comments.asStateFlow()

    // Statuses
    private val _statuses = MutableStateFlow<List<StatusItem>>(emptyList())
    val statuses: StateFlow<List<StatusItem>> = _statuses.asStateFlow()

    // Nearby Members
    private val _nearbyMembers = MutableStateFlow<List<NearbyMember>>(emptyList())
    val nearbyMembers: StateFlow<List<NearbyMember>> = _nearbyMembers.asStateFlow()

    // Matrimonial Profiles
    private val _matrimonialProfiles = MutableStateFlow<List<MatrimonialProfile>>(emptyList())
    val matrimonialProfiles: StateFlow<List<MatrimonialProfile>> = _matrimonialProfiles.asStateFlow()

    // Interests
    private val _interests = MutableStateFlow<List<InterestMatch>>(emptyList())
    val interests: StateFlow<List<InterestMatch>> = _interests.asStateFlow()

    // Chats
    private val _chatConversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val chatConversations: StateFlow<List<ChatConversation>> = _chatConversations.asStateFlow()

    private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

    // Video Call Sessions & Call History
    private val _activeCall = MutableStateFlow<VideoCallSession?>(null)
    val activeCall: StateFlow<VideoCallSession?> = _activeCall.asStateFlow()

    private val _callHistory = MutableStateFlow<List<VideoCallSession>>(emptyList())
    val callHistory: StateFlow<List<VideoCallSession>> = _callHistory.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // Community Announcements
    private val _announcements = MutableStateFlow<List<CommunityAnnouncement>>(emptyList())
    val announcements: StateFlow<List<CommunityAnnouncement>> = _announcements.asStateFlow()

    // Verification Requests
    private val _verificationRequests = MutableStateFlow<List<VerificationRequest>>(emptyList())
    val verificationRequests: StateFlow<List<VerificationRequest>> = _verificationRequests.asStateFlow()

    // Reports
    private val _reports = MutableStateFlow<List<UserReport>>(emptyList())
    val reports: StateFlow<List<UserReport>> = _reports.asStateFlow()

    // Blocked Users
    private val _blockedUserIds = MutableStateFlow<Set<String>>(emptySet())
    val blockedUserIds: StateFlow<Set<String>> = _blockedUserIds.asStateFlow()

    // Admin Stats
    private val _adminStats = MutableStateFlow(AdminStats())
    val adminStats: StateFlow<AdminStats> = _adminStats.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        // Initial Posts
        _posts.value = listOf(
            Post(
                id = "post_1",
                authorId = "user_102",
                authorName = "Ramesh Choudhary Anjana",
                authorPhoto = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                text = "Jay Arbuda Devi! Warm invitation to all Anjana Samaj members for our annual Youth Talent & Cultural Gathering in Samdari, Barmer next Sunday. Let us come together to celebrate our rich heritage!",
                mediaUrl = "https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&w=800&q=80",
                mediaType = "IMAGE",
                createdAt = System.currentTimeMillis() - 2 * 3600 * 1000L,
                likeCount = 142,
                commentCount = 18,
                likedByUsers = listOf("user_101")
            ),
            Post(
                id = "post_2",
                authorId = "user_103",
                authorName = "Dr. Anita Patel",
                authorPhoto = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=300&q=80",
                text = "Proud moment! Our Anjana Samaj Educational Trust has awarded scholarships to 45 deserving students in Jaipur today. Education is the key to our community's bright future. 🙏✨",
                mediaUrl = "https://images.unsplash.com/photo-1523240795612-9a054b0db644?auto=format&fit=crop&w=800&q=80",
                mediaType = "IMAGE",
                createdAt = System.currentTimeMillis() - 8 * 3600 * 1000L,
                likeCount = 289,
                commentCount = 34
            ),
            Post(
                id = "post_3",
                authorId = "user_104",
                authorName = "Suresh Patel (Barmer)",
                authorPhoto = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80",
                text = "Anjana Samaj Blood Donation Camp collected 150 units of blood in Jodhpur. Special thanks to all young donors who participated with enthusiasm!",
                mediaType = "NONE",
                createdAt = System.currentTimeMillis() - 24 * 3600 * 1000L,
                likeCount = 98,
                commentCount = 12
            )
        )

        _comments.value = mapOf(
            "post_1" to listOf(
                PostComment("c1", "post_1", "user_105", "Priya Anjana", "", "Wonderful initiative! We will definitely come from Ahmedabad."),
                PostComment("c2", "post_1", "user_106", "Mahendra Choudhary", "", "Jay Arbuda Devi! Best wishes for the event.")
            )
        )

        // Statuses
        _statuses.value = listOf(
            StatusItem(
                id = "s1",
                userId = "user_105",
                userName = "Priya Anjana",
                userPhoto = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80",
                text = "Jay Arbuda Devi! Blessed morning in Mt. Abu 🌄",
                backgroundColorHex = "#E65100",
                createdAt = System.currentTimeMillis() - 3 * 3600 * 1000L
            ),
            StatusItem(
                id = "s2",
                userId = "user_102",
                userName = "Ramesh Choudhary",
                userPhoto = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
                text = "Preparations in full swing for Samaj Sammelan! 🎉",
                backgroundColorHex = "#1E88E5",
                createdAt = System.currentTimeMillis() - 5 * 3600 * 1000L
            )
        )

        // Nearby Members (Privacy safe distance)
        _nearbyMembers.value = listOf(
            NearbyMember(
                id = "user_102",
                fullName = "Rahul Anjana",
                age = 29,
                gender = "Male",
                city = "Jodhpur",
                distanceKm = 3.4,
                approximateArea = "Shastri Nagar",
                occupation = "Chartered Accountant",
                education = "CA, B.Com",
                isVerified = true,
                photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80"
            ),
            NearbyMember(
                id = "user_105",
                fullName = "Priya Anjana",
                age = 26,
                gender = "Female",
                city = "Jodhpur",
                distanceKm = 5.8,
                approximateArea = "Ratanada",
                occupation = "Assistant Professor",
                education = "M.Sc Mathematics, NET",
                isVerified = true,
                photoUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80"
            ),
            NearbyMember(
                id = "user_107",
                fullName = "Gopal Choudhary",
                age = 32,
                gender = "Male",
                city = "Pali",
                distanceKm = 14.2,
                approximateArea = "Industrial Area",
                occupation = "Textile Business Owner",
                education = "MBA Marketing",
                isVerified = true,
                photoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80"
            )
        )

        // Matrimonial Profiles
        _matrimonialProfiles.value = listOf(
            MatrimonialProfile(
                id = "mat_1",
                userId = "user_201",
                name = "Pooja Patel Anjana",
                age = 25,
                gender = "Female",
                height = "5' 5\"",
                education = "B.Tech Computer Engineering",
                occupation = "Software Engineer (TCS)",
                annualIncome = "10 - 12 LPA",
                city = "Jaipur",
                state = "Rajasthan",
                photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                familyDetails = "Respectable Anjana family in Jaipur. Father Govt Officer (PWD), Mother Teacher.",
                marriagePreference = "Seeking well-educated, cultured groom from Anjana Samaj residing in India/Abroad.",
                isVerified = true
            ),
            MatrimonialProfile(
                id = "mat_2",
                userId = "user_202",
                name = "Deepak Choudhary",
                age = 28,
                gender = "Male",
                height = "5' 11\"",
                education = "MBA Finance",
                occupation = "Assistant Manager (HDFC Bank)",
                annualIncome = "14 - 16 LPA",
                city = "Ahmedabad",
                state = "Gujarat",
                photoUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=400&q=80",
                familyDetails = "Traditional Anjana family from Mehsana, currently settled in Ahmedabad. Business background.",
                marriagePreference = "Seeking graduate, family-oriented bride in Anjana Samaj.",
                isVerified = true
            ),
            MatrimonialProfile(
                id = "mat_3",
                userId = "user_203",
                name = "Sunita Anjana",
                age = 26,
                gender = "Female",
                height = "5' 4\"",
                education = "MBBS",
                occupation = "Resident Doctor (SMS Hospital)",
                annualIncome = "12 - 15 LPA",
                city = "Jaipur",
                state = "Rajasthan",
                photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80",
                familyDetails = "Highly educated family. Father Doctor, Mother Professor.",
                marriagePreference = "Seeking Doctor or Engineer groom with high values.",
                isVerified = true
            )
        )

        // Interests
        _interests.value = listOf(
            InterestMatch(
                id = "int_1",
                senderId = "user_201",
                senderName = "Pooja Patel Anjana",
                senderPhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                receiverId = "user_101",
                receiverName = "Vikram Patel Anjana",
                status = "PENDING",
                timestamp = System.currentTimeMillis() - 2 * 3600 * 1000L
            )
        )

        // Chats
        _chatConversations.value = listOf(
            ChatConversation(
                id = "chat_101_202",
                user1Id = "user_101",
                user2Id = "user_202",
                otherUserId = "user_202",
                otherUserName = "Deepak Choudhary",
                otherUserPhoto = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=400&q=80",
                lastMessage = "Jay Arbuda Devi! Are you coming to Ahmedabad next week?",
                lastTimestamp = System.currentTimeMillis() - 15 * 60 * 1000L,
                unreadCount = 1,
                isOnline = true
            )
        )

        _chatMessages.value = mapOf(
            "chat_101_202" to listOf(
                ChatMessage("m1", "chat_101_202", "user_202", "user_101", "Jay Arbuda Devi Vikram bhai!", timestamp = System.currentTimeMillis() - 30 * 60 * 1000L),
                ChatMessage("m2", "chat_101_202", "user_101", "user_202", "Jay Arbuda Devi Deepak! How are you?", timestamp = System.currentTimeMillis() - 25 * 60 * 1000L),
                ChatMessage("m3", "chat_101_202", "user_202", "user_101", "Jay Arbuda Devi! Are you coming to Ahmedabad next week?", timestamp = System.currentTimeMillis() - 15 * 60 * 1000L)
            )
        )

        // Call History
        _callHistory.value = listOf(
            VideoCallSession(
                id = "call_1",
                callerId = "user_202",
                callerName = "Deepak Choudhary",
                callerPhoto = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=400&q=80",
                receiverId = "user_101",
                receiverName = "Vikram Patel Anjana",
                status = "ENDED",
                channelId = "chan_101",
                durationSeconds = 245,
                timestamp = System.currentTimeMillis() - 24 * 3600 * 1000L
            )
        )

        // Notifications
        _notifications.value = listOf(
            AppNotification("n1", "user_101", "New Matrimonial Interest", "Pooja Patel sent an interest in your matrimonial profile.", "INTEREST", timestamp = System.currentTimeMillis() - 2 * 3600 * 1000L),
            AppNotification("n2", "user_101", "Community Announcement", "Samaj meeting scheduled for Sunday in Barmer.", "ANNOUNCEMENT", timestamp = System.currentTimeMillis() - 5 * 3600 * 1000L),
            AppNotification("n3", "user_101", "Profile Verified", "Your Anjana Samaj ID verification request has been APPROVED!", "VERIFICATION", timestamp = System.currentTimeMillis() - 24 * 3600 * 1000L)
        )

        // Community Announcements
        _announcements.value = listOf(
            CommunityAnnouncement(
                id = "ann_1",
                title = "📢 Shri Arbuda Devi Temple Annual Festival & Mahaprasad",
                content = "All Anjana Samaj brothers and sisters are cordially invited to the grand annual Puja and Mahaprasad at Mount Abu Arbuda Devi Temple. Free accommodation provided for community families.",
                imageUrl = "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?auto=format&fit=crop&w=800&q=80",
                eventDate = "Sunday, 25th October 2026",
                location = "Mt. Abu, Rajasthan",
                rsvpCount = 342
            ),
            CommunityAnnouncement(
                id = "ann_2",
                title = "🎓 Anjana Samaj Career Guidance & Civil Services Workshop",
                content = "Free mentoring webinar and classroom workshop for IAS/RAS/GATE aspirants from our community by senior officers.",
                imageUrl = "https://images.unsplash.com/photo-1524178232363-1fb2b075b655?auto=format&fit=crop&w=800&q=80",
                eventDate = "Saturday, 5th September 2026",
                location = "Anjana Samaj Bhawan, Jodhpur",
                rsvpCount = 189
            )
        )

        // Verification Requests
        _verificationRequests.value = listOf(
            VerificationRequest(
                id = "vr_1",
                userId = "user_301",
                userName = "Manish Patel",
                userPhone = "+91 94140 88776",
                documentType = "Aadhaar Card",
                documentUrl = "restricted_docs/user_301_aadhaar.pdf",
                status = "PENDING"
            )
        )
    }

    // AUTH ACTIONS
    fun loginWithPhone(phone: String, otp: String): Boolean {
        _isLoggedIn.value = true
        return true
    }

    fun logout() {
        authService.logout()
        _isLoggedIn.value = false
    }

    fun updateProfile(profile: UserProfile) {
        _currentUser.value = profile
    }

    // POST ACTIONS
    fun addPost(text: String, mediaUrl: String = "", mediaType: String = "NONE") {
        val user = currentUser.value ?: return
        val newPost = Post(
            id = "post_${System.currentTimeMillis()}",
            authorId = user.id,
            authorName = user.fullName,
            authorPhoto = user.photoUrl,
            text = text,
            mediaUrl = mediaUrl,
            mediaType = mediaType,
            createdAt = System.currentTimeMillis()
        )
        _posts.value = listOf(newPost) + _posts.value
    }

    fun deletePost(postId: String) {
        _posts.value = _posts.value.filter { it.id != postId }
    }

    fun toggleLikePost(postId: String) {
        val userId = currentUser.value?.id ?: "user_101"
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                val hasLiked = post.likedByUsers.contains(userId)
                val newLikedList = if (hasLiked) post.likedByUsers - userId else post.likedByUsers + userId
                val newCount = if (hasLiked) (post.likeCount - 1).coerceAtLeast(0) else post.likeCount + 1
                post.copy(likeCount = newCount, likedByUsers = newLikedList)
            } else post
        }
    }

    fun addComment(postId: String, commentText: String) {
        val user = currentUser.value ?: return
        val newComment = PostComment(
            id = "c_${System.currentTimeMillis()}",
            postId = postId,
            authorId = user.id,
            authorName = user.fullName,
            authorPhoto = user.photoUrl,
            commentText = commentText
        )
        val currentList = _comments.value[postId] ?: emptyList()
        _comments.value = _comments.value + (postId to (currentList + newComment))
        
        // increment comment count
        _posts.value = _posts.value.map { p ->
            if (p.id == postId) p.copy(commentCount = p.commentCount + 1) else p
        }
    }

    // STATUS ACTIONS
    fun addStatus(text: String, mediaUrl: String = "", mediaType: String = "TEXT", bgHex: String = "#E65100") {
        val user = currentUser.value ?: return
        val newStatus = StatusItem(
            id = "s_${System.currentTimeMillis()}",
            userId = user.id,
            userName = user.fullName,
            userPhoto = user.photoUrl,
            mediaUrl = mediaUrl,
            mediaType = mediaType,
            text = text,
            backgroundColorHex = bgHex
        )
        _statuses.value = listOf(newStatus) + _statuses.value
    }

    fun deleteStatus(statusId: String) {
        _statuses.value = _statuses.value.filter { it.id != statusId }
    }

    // MATRIMONY & INTEREST ACTIONS
    fun sendInterest(profile: MatrimonialProfile) {
        val user = currentUser.value ?: return
        val newInterest = InterestMatch(
            id = "int_${System.currentTimeMillis()}",
            senderId = user.id,
            senderName = user.fullName,
            senderPhoto = user.photoUrl,
            receiverId = profile.userId,
            receiverName = profile.name,
            receiverPhoto = profile.photoUrl,
            status = "PENDING"
        )
        _interests.value = listOf(newInterest) + _interests.value
    }

    fun acceptInterest(interestId: String) {
        _interests.value = _interests.value.map { interest ->
            if (interest.id == interestId) {
                interest.copy(status = "ACCEPTED")
            } else interest
        }
        val interest = _interests.value.find { it.id == interestId } ?: return
        // Enable Chat
        val existingChat = _chatConversations.value.find { 
            (it.user1Id == interest.senderId && it.user2Id == interest.receiverId) ||
            (it.user1Id == interest.receiverId && it.user2Id == interest.senderId)
        }
        if (existingChat == null) {
            val newChat = ChatConversation(
                id = "chat_${interest.senderId}_${interest.receiverId}",
                user1Id = interest.senderId,
                user2Id = interest.receiverId,
                otherUserId = interest.senderId,
                otherUserName = interest.senderName,
                otherUserPhoto = interest.senderPhoto,
                lastMessage = "Mutual Match established! You can now chat & video call.",
                unreadCount = 0
            )
            _chatConversations.value = listOf(newChat) + _chatConversations.value
        }
    }

    fun rejectInterest(interestId: String) {
        _interests.value = _interests.value.map { interest ->
            if (interest.id == interestId) interest.copy(status = "REJECTED") else interest
        }
    }

    // CHAT ACTIONS
    fun sendMessage(chatId: String, text: String, mediaUrl: String = "") {
        val user = currentUser.value ?: return
        val conversation = _chatConversations.value.find { it.id == chatId } ?: return
        val receiverId = if (conversation.user1Id == user.id) conversation.user2Id else conversation.user1Id

        val msg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            chatId = chatId,
            senderId = user.id,
            receiverId = receiverId,
            text = text,
            mediaUrl = mediaUrl
        )

        val currentList = _chatMessages.value[chatId] ?: emptyList()
        _chatMessages.value = _chatMessages.value + (chatId to (currentList + msg))

        // Update conversation last message
        _chatConversations.value = _chatConversations.value.map {
            if (it.id == chatId) {
                it.copy(lastMessage = text, lastTimestamp = System.currentTimeMillis())
            } else it
        }
    }

    // CALL ACTIONS
    fun startVideoCall(receiverId: String, receiverName: String, receiverPhoto: String) {
        val user = currentUser.value ?: return
        val call = VideoCallSession(
            id = "call_${System.currentTimeMillis()}",
            callerId = user.id,
            callerName = user.fullName,
            callerPhoto = user.photoUrl,
            receiverId = receiverId,
            receiverName = receiverName,
            receiverPhoto = receiverPhoto,
            status = "RINGING",
            channelId = "room_${user.id}_$receiverId"
        )
        _activeCall.value = call
    }

    fun acceptVideoCall() {
        _activeCall.value = _activeCall.value?.copy(status = "CONNECTED")
    }

    fun endVideoCall() {
        val currentCall = _activeCall.value
        if (currentCall != null) {
            val ended = currentCall.copy(status = "ENDED")
            _callHistory.value = listOf(ended) + _callHistory.value
        }
        _activeCall.value = null
    }

    // REPORT & BLOCK
    fun reportUser(targetType: String, targetId: String, targetAuthorId: String, reason: String, details: String) {
        val user = currentUser.value ?: return
        val rep = UserReport(
            id = "rep_${System.currentTimeMillis()}",
            reporterId = user.id,
            reporterName = user.fullName,
            targetType = targetType,
            targetId = targetId,
            targetAuthorId = targetAuthorId,
            reason = reason,
            details = details
        )
        _reports.value = listOf(rep) + _reports.value
    }

    fun blockUser(userId: String) {
        _blockedUserIds.value = _blockedUserIds.value + userId
        // Filter out from feeds
        _posts.value = _posts.value.filter { it.authorId != userId }
    }

    fun unblockUser(userId: String) {
        _blockedUserIds.value = _blockedUserIds.value - userId
    }

    // VERIFICATION SUBMISSION
    fun submitVerificationRequest(documentType: String, notes: String) {
        val user = currentUser.value ?: return
        val req = VerificationRequest(
            id = "vr_${System.currentTimeMillis()}",
            userId = user.id,
            userName = user.fullName,
            userPhone = user.phone,
            documentType = documentType,
            documentUrl = "restricted_storage/docs_${user.id}.pdf",
            notes = notes,
            status = "PENDING"
        )
        _verificationRequests.value = listOf(req) + _verificationRequests.value
        _currentUser.value = user.copy(isVerified = "PENDING")
    }

    // ADMIN ACTIONS
    fun approveVerification(requestId: String) {
        _verificationRequests.value = _verificationRequests.value.map { req ->
            if (req.id == requestId) req.copy(status = "VERIFIED") else req
        }
    }

    fun rejectVerification(requestId: String, reason: String) {
        _verificationRequests.value = _verificationRequests.value.map { req ->
            if (req.id == requestId) req.copy(status = "REJECTED", rejectionReason = reason) else req
        }
    }

    fun addAnnouncement(title: String, content: String, imageUrl: String, eventDate: String, location: String) {
        val newAnn = CommunityAnnouncement(
            id = "ann_${System.currentTimeMillis()}",
            title = title,
            content = content,
            imageUrl = imageUrl,
            eventDate = eventDate,
            location = location
        )
        _announcements.value = listOf(newAnn) + _announcements.value
    }
}
