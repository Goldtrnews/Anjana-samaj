package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnjanaSamajRepository
import com.example.model.VerificationRequest
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LoveRed
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.VerifiedBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    repository: AnjanaSamajRepository,
    onBack: () -> Unit
) {
    val stats by repository.adminStats.collectAsState()
    val verificationRequests by repository.verificationRequests.collectAsState()
    val reports by repository.reports.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Overview, 1: Verifications, 2: Reports, 3: Announcements
    var showNewAnnouncementModal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Samaj Admin Dashboard", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF212121),
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
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF212121),
                contentColor = Color.White
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Overview") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Verify (${verificationRequests.count { it.status == "PENDING" }})") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Reports (${reports.size})") })
                Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Announce") })
            }

            when (selectedTab) {
                0 -> AdminStatsOverview(stats)
                1 -> AdminVerificationsList(verificationRequests, onApprove = { repository.approveVerification(it) }, onReject = { repository.rejectVerification(it, "Document invalid") })
                2 -> AdminReportsList(reports)
                3 -> AdminAnnouncementsSection(onNewAnnouncement = { showNewAnnouncementModal = true })
            }
        }
    }

    if (showNewAnnouncementModal) {
        CreateAnnouncementDialog(
            onDismiss = { showNewAnnouncementModal = false },
            onSubmit = { title, content, image, date, location ->
                repository.addAnnouncement(title, content, image, date, location)
                showNewAnnouncementModal = false
            }
        )
    }
}

@Composable
private fun AdminStatsOverview(stats: com.example.model.AdminStats) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Samaj Analytics & Moderation", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AdminStatCard("Total Users", stats.totalUsers.toString(), Icons.Default.People, SaffronPrimary, Modifier.weight(1f))
            AdminStatCard("Verified Members", stats.verifiedUsers.toString(), Icons.Default.Verified, VerifiedBlue, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AdminStatCard("Matrimonial Profiles", stats.activeMatrimonialProfiles.toString(), Icons.Default.Favorite, LoveRed, Modifier.weight(1f))
            AdminStatCard("Total Matches", stats.totalMatches.toString(), Icons.Default.ConnectWithoutContact, Color(0xFF7B1FA2), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AdminStatCard("Total Posts", stats.totalPosts.toString(), Icons.Default.DynamicFeed, Color(0xFF2E7D32), Modifier.weight(1f))
            AdminStatCard("Reported Content", stats.reportedPosts.toString(), Icons.Default.Flag, GoldAccent, Modifier.weight(1f))
        }
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AdminVerificationsList(
    requests: List<VerificationRequest>,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit
) {
    if (requests.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No pending verification requests.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(requests) { req ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(req.userName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Phone: ${req.userPhone} • Doc: ${req.documentType}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Doc URL: ${req.documentUrl} (Restricted)", fontSize = 11.sp, color = VerifiedBlue)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { onApprove(req.id) }, colors = ButtonDefaults.buttonColors(containerColor = VerifiedBlue)) {
                                Text("Approve")
                            }
                            OutlinedButton(onClick = { onReject(req.id) }) {
                                Text("Reject")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminReportsList(reports: List<com.example.model.UserReport>) {
    if (reports.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No reported items.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(reports) { rep ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Report Type: ${rep.targetType}", fontWeight = FontWeight.Bold)
                        Text("Reason: ${rep.reason}", color = LoveRed)
                        Text("Reporter: ${rep.reporterName}", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminAnnouncementsSection(onNewAnnouncement: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Button(
            onClick = onNewAnnouncement,
            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
            modifier = Modifier.testTag("admin_new_announcement_btn")
        ) {
            Icon(Icons.Default.Campaign, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Post New Samaj Announcement")
        }
    }
}

@Composable
private fun CreateAnnouncementDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, content: String, image: String, date: String, location: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post Community Announcement") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Announcement Title") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Content") }, modifier = Modifier.fillMaxWidth().height(100.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = eventDate, onValueChange = { eventDate = it }, label = { Text("Event Date (e.g. 25 Oct 2026)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Event Location (e.g. Barmer)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = { onSubmit(title, content, "", eventDate, location) }, colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)) {
                Text("Publish")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
