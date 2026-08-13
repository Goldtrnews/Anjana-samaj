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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnjanaSamajRepository
import com.example.model.AppNotification
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterScreen(
    repository: AnjanaSamajRepository,
    onBack: () -> Unit
) {
    val notifications by repository.notifications.collectAsState()
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = when (selectedFilter) {
        "UNREAD" -> notifications.filter { !it.isRead }
        "MATRIMONY" -> notifications.filter { it.type == "INTEREST" || it.type == "MATCH" }
        "COMMUNITY" -> notifications.filter { it.type == "ANNOUNCEMENT" }
        else -> notifications
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Center", fontWeight = FontWeight.Bold) },
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
            // Filter Pills
            ScrollableTabRow(
                selectedTabIndex = listOf("ALL", "UNREAD", "MATRIMONY", "COMMUNITY").indexOf(selectedFilter),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(selected = selectedFilter == "ALL", onClick = { selectedFilter = "ALL" }, text = { Text("All") })
                Tab(selected = selectedFilter == "UNREAD", onClick = { selectedFilter = "UNREAD" }, text = { Text("Unread") })
                Tab(selected = selectedFilter == "MATRIMONY", onClick = { selectedFilter = "MATRIMONY" }, text = { Text("Matrimony") })
                Tab(selected = selectedFilter == "COMMUNITY", onClick = { selectedFilter = "COMMUNITY" }, text = { Text("Community") })
            }

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No notifications found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { notif ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp).clip(CircleShape),
                                    color = SaffronPrimary.copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            when (notif.type) {
                                                "INTEREST", "MATCH" -> Icons.Default.Favorite
                                                "ANNOUNCEMENT" -> Icons.Default.Campaign
                                                "VERIFICATION" -> Icons.Default.Verified
                                                else -> Icons.Default.Notifications
                                            },
                                            contentDescription = null,
                                            tint = SaffronPrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(notif.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
