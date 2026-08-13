package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.AnjanaSamajRepository
import com.example.model.InterestMatch
import com.example.ui.theme.LoveRed
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    repository: AnjanaSamajRepository,
    onStartChat: (userId: String, name: String) -> Unit,
    onStartCall: (userId: String, name: String, photo: String) -> Unit,
    onBack: () -> Unit
) {
    val interests by repository.interests.collectAsState()
    val currentUser by repository.currentUser.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Received, 1: Mutual Matches, 2: Sent

    val currentUserId = currentUser?.id ?: "user_101"

    val receivedInterests = interests.filter { it.receiverId == currentUserId && it.status == "PENDING" }
    val mutualMatches = interests.filter { (it.senderId == currentUserId || it.receiverId == currentUserId) && it.status == "ACCEPTED" }
    val sentInterests = interests.filter { it.senderId == currentUserId && it.status == "PENDING" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interests & Mutual Matches", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronPrimary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Received (${receivedInterests.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Matches (${mutualMatches.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Sent (${sentInterests.size})", fontWeight = FontWeight.Bold) }
                )
            }

            when (selectedTab) {
                0 -> InterestList(
                    items = receivedInterests,
                    isReceived = true,
                    onAccept = { repository.acceptInterest(it.id) },
                    onReject = { repository.rejectInterest(it.id) }
                )

                1 -> MutualMatchesList(
                    items = mutualMatches,
                    currentUserId = currentUserId,
                    onChat = { id, name -> onStartChat(id, name) },
                    onCall = { id, name, photo -> onStartCall(id, name, photo) }
                )

                2 -> InterestList(
                    items = sentInterests,
                    isReceived = false,
                    onAccept = {},
                    onReject = {}
                )
            }
        }
    }
}

@Composable
private fun InterestList(
    items: List<InterestMatch>,
    isReceived: Boolean,
    onAccept: (InterestMatch) -> Unit,
    onReject: (InterestMatch) -> Unit
) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No interest requests here.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { interest ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(54.dp).clip(CircleShape),
                            color = SaffronPrimary
                        ) {
                            if (interest.senderPhoto.isNotEmpty()) {
                                AsyncImage(model = interest.senderPhoto, contentDescription = null, contentScale = ContentScale.Crop)
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(interest.senderName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(interest.senderName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Expressed interest in your profile", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        if (isReceived) {
                            Row {
                                IconButton(onClick = { onAccept(interest) }) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Accept", tint = Color(0xFF2E7D32))
                                }
                                IconButton(onClick = { onReject(interest) }) {
                                    Icon(Icons.Default.Cancel, contentDescription = "Reject", tint = LoveRed)
                                }
                            }
                        } else {
                            Text("Pending", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MutualMatchesList(
    items: List<InterestMatch>,
    currentUserId: String,
    onChat: (String, String) -> Unit,
    onCall: (String, String, String) -> Unit
) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No mutual matches yet. Accept received interests to match!", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { match ->
                val otherId = if (match.senderId == currentUserId) match.receiverId else match.senderId
                val otherName = if (match.senderId == currentUserId) match.receiverName else match.senderName
                val otherPhoto = if (match.senderId == currentUserId) match.receiverPhoto else match.senderPhoto

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(56.dp).clip(CircleShape),
                            color = SaffronPrimary
                        ) {
                            if (otherPhoto.isNotEmpty()) {
                                AsyncImage(model = otherPhoto, contentDescription = null, contentScale = ContentScale.Crop)
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(otherName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(otherName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("❤️ Match!", fontSize = 11.sp, color = LoveRed, fontWeight = FontWeight.Bold)
                            }
                            Text("Chat & Video Calling enabled", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row {
                            IconButton(onClick = { onChat(otherId, otherName) }) {
                                Icon(Icons.Default.Chat, contentDescription = "Chat", tint = SaffronPrimary)
                            }
                            IconButton(onClick = { onCall(otherId, otherName, otherPhoto) }) {
                                Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = Color(0xFF2E7D32))
                            }
                        }
                    }
                }
            }
        }
    }
}
