package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnjanaSamajRepository
import com.example.model.StatusItem
import com.example.model.VideoCallSession
import com.example.ui.screens.*
import com.example.ui.theme.AnjanaSamajTheme
import com.example.ui.theme.SaffronPrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnjanaSamajApp()
        }
    }
}

@Composable
fun AnjanaSamajApp() {
    val context = LocalContext.current
    val repository = remember { AnjanaSamajRepository(context) }

    var showSplash by remember { mutableStateOf(true) }
    var isLoggedIn by remember { mutableStateOf(true) } // Seeded user logged in by default
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Home, 1: Nearby, 2: Matrimony, 3: Feed, 4: Profile/Chats

    // Navigation sub-view states
    var activeSubScreen by remember { mutableStateOf<SubScreen?>(null) }
    var activeChatId by remember { mutableStateOf<String?>(null) }
    var activeChatUserName by remember { mutableStateOf("") }
    var activeChatUserPhoto by remember { mutableStateOf("") }

    var activeStatusViewer by remember { mutableStateOf<StatusItem?>(null) }
    var showCreateStatusDialog by remember { mutableStateOf(false) }

    var activeCallSession by remember { mutableStateOf<VideoCallSession?>(null) }
    var showFirebaseGuideModal by remember { mutableStateOf(false) }

    val notifications by repository.notifications.collectAsState()
    val unreadNotifCount = notifications.count { !it.isRead }

    AnjanaSamajTheme {
        if (showSplash) {
            SplashScreen(
                onSplashFinished = {
                    showSplash = false
                }
            )
        } else if (!isLoggedIn) {
            AuthScreen(
                onLoginSuccess = { phone ->
                    isLoggedIn = true
                }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            NavigationBarItem(
                                selected = selectedTab == 0 && activeSubScreen == null,
                                onClick = {
                                    selectedTab = 0
                                    activeSubScreen = null
                                },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                modifier = Modifier.testTag("nav_tab_home")
                            )

                            NavigationBarItem(
                                selected = selectedTab == 1 && activeSubScreen == null,
                                onClick = {
                                    selectedTab = 1
                                    activeSubScreen = null
                                },
                                icon = { Icon(Icons.Default.LocationOn, contentDescription = "Nearby") },
                                label = { Text("Nearby", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                modifier = Modifier.testTag("nav_tab_nearby")
                            )

                            NavigationBarItem(
                                selected = selectedTab == 2 && activeSubScreen == null,
                                onClick = {
                                    selectedTab = 2
                                    activeSubScreen = null
                                },
                                icon = { Icon(Icons.Default.Favorite, contentDescription = "Matrimony") },
                                label = { Text("Matrimony", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                modifier = Modifier.testTag("nav_tab_matrimony")
                            )

                            NavigationBarItem(
                                selected = selectedTab == 3 && activeSubScreen == null,
                                onClick = {
                                    selectedTab = 3
                                    activeSubScreen = null
                                },
                                icon = { Icon(Icons.Default.DynamicFeed, contentDescription = "Feed") },
                                label = { Text("Feed", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                modifier = Modifier.testTag("nav_tab_feed")
                            )

                            NavigationBarItem(
                                selected = selectedTab == 4 && activeSubScreen == null,
                                onClick = {
                                    selectedTab = 4
                                    activeSubScreen = null
                                },
                                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                modifier = Modifier.testTag("nav_tab_profile")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (activeSubScreen != null) {
                            when (activeSubScreen) {
                                SubScreen.MATCHES -> MatchesScreen(
                                    repository = repository,
                                    onStartChat = { id, name ->
                                        activeChatId = "chat_$id"
                                        activeChatUserName = name
                                        activeSubScreen = SubScreen.CHAT_DETAIL
                                    },
                                    onStartCall = { id, name, photo ->
                                        repository.startVideoCall(id, name, photo)
                                        activeCallSession = repository.activeCall.value
                                    },
                                    onBack = { activeSubScreen = null }
                                )

                                SubScreen.CHAT_LIST -> ChatListScreen(
                                    repository = repository,
                                    onSelectChat = { id, name, photo ->
                                        activeChatId = id
                                        activeChatUserName = name
                                        activeChatUserPhoto = photo
                                        activeSubScreen = SubScreen.CHAT_DETAIL
                                    },
                                    onStartCall = { id, name, photo ->
                                        repository.startVideoCall(id, name, photo)
                                        activeCallSession = repository.activeCall.value
                                    }
                                )

                                SubScreen.CHAT_DETAIL -> activeChatId?.let { chatId ->
                                    ChatDetailScreen(
                                        chatId = chatId,
                                        otherUserName = activeChatUserName,
                                        otherUserPhoto = activeChatUserPhoto,
                                        repository = repository,
                                        onBack = { activeSubScreen = null },
                                        onStartCall = {
                                            repository.startVideoCall(chatId, activeChatUserName, activeChatUserPhoto)
                                            activeCallSession = repository.activeCall.value
                                        }
                                    )
                                }

                                SubScreen.NOTIFICATIONS -> NotificationCenterScreen(
                                    repository = repository,
                                    onBack = { activeSubScreen = null }
                                )

                                SubScreen.VERIFICATION -> UserVerificationScreen(
                                    repository = repository,
                                    onBack = { activeSubScreen = null }
                                )

                                SubScreen.ADMIN_DASHBOARD -> AdminDashboardScreen(
                                    repository = repository,
                                    onBack = { activeSubScreen = null }
                                )

                                null -> {}
                            }
                        } else {
                            when (selectedTab) {
                                0 -> HomeDashboardScreen(
                                    repository = repository,
                                    onNavigateTab = { selectedTab = it },
                                    onOpenSearch = { selectedTab = 1 },
                                    onOpenNotifications = { activeSubScreen = SubScreen.NOTIFICATIONS },
                                    onOpenStatusViewer = { status -> activeStatusViewer = status },
                                    onOpenCreateStatus = { showCreateStatusDialog = true },
                                    onOpenAdminPanel = { activeSubScreen = SubScreen.ADMIN_DASHBOARD },
                                    onOpenFirebaseGuide = { showFirebaseGuideModal = true }
                                )

                                1 -> NearbyPeopleScreen(
                                    repository = repository,
                                    onStartChat = { id, name ->
                                        activeChatId = "chat_$id"
                                        activeChatUserName = name
                                        activeSubScreen = SubScreen.CHAT_DETAIL
                                    }
                                )

                                2 -> MatrimonyScreen(
                                    repository = repository,
                                    onOpenMatches = { activeSubScreen = SubScreen.MATCHES }
                                )

                                3 -> DailyPostsScreen(
                                    repository = repository
                                )

                                4 -> ProfileScreen(
                                    repository = repository,
                                    onOpenVerification = { activeSubScreen = SubScreen.VERIFICATION },
                                    onOpenAdminDashboard = { activeSubScreen = SubScreen.ADMIN_DASHBOARD },
                                    onOpenFirebaseGuide = { showFirebaseGuideModal = true },
                                    onLogout = { isLoggedIn = false }
                                )
                            }
                        }
                    }
                }

                // STATUS VIEWER OVERLAY
                activeStatusViewer?.let { status ->
                    StatusViewerScreen(
                        status = status,
                        currentUserId = repository.currentUser.value?.id ?: "user_101",
                        onClose = { activeStatusViewer = null },
                        onDelete = { statusId ->
                            repository.deleteStatus(statusId)
                            activeStatusViewer = null
                        },
                        onReply = { recipientId, reply ->
                            repository.sendMessage("chat_$recipientId", reply)
                        }
                    )
                }

                // CREATE STATUS DIALOG
                if (showCreateStatusDialog) {
                    CreateStatusDialog(
                        onDismiss = { showCreateStatusDialog = false },
                        onSubmit = { text, bgHex ->
                            repository.addStatus(text = text, bgHex = bgHex)
                            showCreateStatusDialog = false
                        }
                    )
                }

                // ACTIVE VIDEO CALL OVERLAY
                activeCallSession?.let { call ->
                    ActiveVideoCallOverlay(
                        callSession = call,
                        onAccept = {
                            repository.acceptVideoCall()
                            activeCallSession = repository.activeCall.value ?: call.copy(status = "CONNECTED")
                        },
                        onEndCall = {
                            repository.endVideoCall()
                            activeCallSession = null
                        }
                    )
                }

                // FIREBASE GUIDE DIALOG
                if (showFirebaseGuideModal) {
                    FirebaseSetupGuideModal(
                        onDismiss = { showFirebaseGuideModal = false }
                    )
                }
            }
        }
    }
}

enum class SubScreen {
    MATCHES,
    CHAT_LIST,
    CHAT_DETAIL,
    NOTIFICATIONS,
    VERIFICATION,
    ADMIN_DASHBOARD
}
