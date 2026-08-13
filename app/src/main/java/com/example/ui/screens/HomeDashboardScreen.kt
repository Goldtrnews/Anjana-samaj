package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.AnjanaSamajRepository
import com.example.model.CommunityAnnouncement
import com.example.model.MatrimonialProfile
import com.example.model.StatusItem
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SaffronSecondary
import com.example.ui.theme.VerifiedBlue

@Composable
fun HomeDashboardScreen(
    repository: AnjanaSamajRepository,
    onNavigateTab: (Int) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenStatusViewer: (StatusItem) -> Unit,
    onOpenCreateStatus: () -> Unit,
    onOpenAdminPanel: () -> Unit,
    onOpenFirebaseGuide: () -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val announcements by repository.announcements.collectAsState()
    val statuses by repository.statuses.collectAsState()
    val matrimonyProfiles by repository.matrimonialProfiles.collectAsState()
    val posts by repository.posts.collectAsState()
    val notifications by repository.notifications.collectAsState()

    val unreadNotifCount = notifications.count { !it.isRead }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR
        Surface(
            color = SaffronPrimary,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ANJANA SAMAJ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                            if (currentUser?.isAdmin == true) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = GoldAccent,
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.clickable { onOpenAdminPanel() }
                                ) {
                                    Text(
                                        text = "ADMIN",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = currentUser?.fullName ?: "Member Dashboard",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenFirebaseGuide,
                        modifier = Modifier.testTag("firebase_config_btn")
                    ) {
                        Icon(Icons.Default.Cloud, contentDescription = "Firebase Config", tint = Color.White)
                    }

                    IconButton(
                        onClick = onOpenSearch,
                        modifier = Modifier.testTag("home_search_btn")
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }

                    Box {
                        IconButton(
                            onClick = onOpenNotifications,
                            modifier = Modifier.testTag("home_notif_btn")
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                        }
                        if (unreadNotifCount > 0) {
                            Badge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp),
                                containerColor = Color.Red
                            ) {
                                Text(unreadNotifCount.toString(), color = Color.White, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // SCROLLABLE CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            // 1. STATUS STORY BAR (WhatsApp / Instagram Style)
            Text(
                text = "Status Updates",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Add My Status
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onOpenCreateStatus() }
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Surface(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                if (!currentUser?.photoUrl.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = currentUser?.photoUrl,
                                        contentDescription = "My Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = SaffronPrimary)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("My Status", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Community Statuses
                items(statuses) { status ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onOpenStatusViewer(status) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(SaffronPrimary, GoldAccent)))
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                color = Color.White
                            ) {
                                if (status.userPhoto.isNotEmpty()) {
                                    AsyncImage(
                                        model = status.userPhoto,
                                        contentDescription = status.userName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.background(SaffronSecondary)
                                    ) {
                                        Text(status.userName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = status.userName.split(" ").firstOrNull() ?: status.userName,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. QUICK ACTION SHORTCUT GRID
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Samaj Features",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FeatureShortcutItem("Daily Posts", Icons.Default.DynamicFeed, SaffronPrimary) { onNavigateTab(3) }
                        FeatureShortcutItem("Nearby", Icons.Default.LocationOn, Color(0xFF2E7D32)) { onNavigateTab(1) }
                        FeatureShortcutItem("Matrimony", Icons.Default.Favorite, Color(0xFFE53935)) { onNavigateTab(2) }
                        FeatureShortcutItem("Matches", Icons.Default.People, Color(0xFF7B1FA2)) { onNavigateTab(2) }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FeatureShortcutItem("Chats", Icons.Default.Chat, Color(0xFF0288D1)) { onNavigateTab(4) }
                        FeatureShortcutItem("Video Calls", Icons.Default.Videocam, Color(0xFFD84315)) { onNavigateTab(4) }
                        FeatureShortcutItem("Announcements", Icons.Default.Campaign, Color(0xFFF57C00)) { onNavigateTab(0) }
                        FeatureShortcutItem("Verification", Icons.Default.VerifiedUser, VerifiedBlue) { onNavigateTab(4) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. FEATURED COMMUNITY ANNOUNCEMENT
            if (announcements.isNotEmpty()) {
                val topAnn = announcements.first()
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📢 Community Announcement",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column {
                            if (topAnn.imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = topAnn.imageUrl,
                                    contentDescription = topAnn.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                )
                            }
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = topAnn.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = topAnn.content,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (topAnn.eventDate.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Event, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(topAnn.eventDate, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SaffronPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. FEATURED MATRIMONIAL MATCHES PREVIEW
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "❤️ Verified Matrimonial Matches",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { onNavigateTab(2) }) {
                        Text("View All", color = SaffronPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(matrimonyProfiles) { profile ->
                        MatrimonialPreviewCard(profile = profile, onClick = { onNavigateTab(2) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. RECENT POST PREVIEW
            if (posts.isNotEmpty()) {
                val latestPost = posts.first()
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📰 Recent Post from Feed",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { onNavigateTab(3) }) {
                            Text("Go to Feed", color = SaffronPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    modifier = Modifier.size(36.dp).clip(CircleShape),
                                    color = SaffronSecondary
                                ) {
                                    if (latestPost.authorPhoto.isNotEmpty()) {
                                        AsyncImage(model = latestPost.authorPhoto, contentDescription = null, contentScale = ContentScale.Crop)
                                    } else {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(latestPost.authorName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(latestPost.authorName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Anjana Samaj Member", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(latestPost.text, fontSize = 13.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureShortcutItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(14.dp),
            color = color.copy(alpha = 0.12f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun MatrimonialPreviewCard(
    profile: MatrimonialProfile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(modifier = Modifier.height(110.dp).fillMaxWidth()) {
                if (profile.photoUrl.isNotEmpty()) {
                    AsyncImage(
                        model = profile.photoUrl,
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(SaffronSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                    }
                }
                if (profile.isVerified) {
                    Surface(
                        modifier = Modifier.padding(6.dp).align(Alignment.TopEnd),
                        color = VerifiedBlue,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color.White, modifier = Modifier.size(16.dp).padding(2.dp))
                    }
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${profile.age} yrs • ${profile.city}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(profile.education, fontSize = 10.sp, color = SaffronPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
