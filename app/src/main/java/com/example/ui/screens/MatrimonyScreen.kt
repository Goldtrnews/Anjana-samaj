package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.AnjanaSamajRepository
import com.example.model.MatrimonialProfile
import com.example.ui.theme.LoveRed
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.VerifiedBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatrimonyScreen(
    repository: AnjanaSamajRepository,
    onOpenMatches: () -> Unit
) {
    val profiles by repository.matrimonialProfiles.collectAsState()
    val interests by repository.interests.collectAsState()

    var selectedGender by remember { mutableStateOf("Female") } // "Male", "Female"
    var selectedProfileDetail by remember { mutableStateOf<MatrimonialProfile?>(null) }
    var filterVerifiedOnly by remember { mutableStateOf(false) }

    val filteredProfiles = profiles.filter { profile ->
        (selectedGender.isEmpty() || profile.gender == selectedGender) &&
        (!filterVerifiedOnly || profile.isVerified)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anjana Matrimony", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronPrimary,
                    titleContentColor = Color.White
                ),
                actions = {
                    TextButton(
                        onClick = onOpenMatches,
                        modifier = Modifier.testTag("my_matches_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.People, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Interests", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Gender Switcher Bar
            TabRow(
                selectedTabIndex = if (selectedGender == "Female") 0 else 1,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedGender == "Female",
                    onClick = { selectedGender = "Female" },
                    text = { Text("Brides (Kanya)", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedGender == "Male",
                    onClick = { selectedGender = "Male" },
                    text = { Text("Grooms (Var)", fontWeight = FontWeight.Bold) }
                )
            }

            if (filteredProfiles.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No matrimonial profiles available for selected filter.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredProfiles) { profile ->
                        MatrimonialCardItem(
                            profile = profile,
                            onCardClick = { selectedProfileDetail = profile },
                            onSendInterest = { repository.sendInterest(profile) }
                        )
                    }
                }
            }
        }
    }

    // PROFILE DETAIL SHEET
    selectedProfileDetail?.let { profile ->
        MatrimonialDetailDialog(
            profile = profile,
            onDismiss = { selectedProfileDetail = null },
            onSendInterest = {
                repository.sendInterest(profile)
                selectedProfileDetail = null
            }
        )
    }
}

@Composable
private fun MatrimonialCardItem(
    profile: MatrimonialProfile,
    onCardClick: () -> Unit,
    onSendInterest: () -> Unit
) {
    var isShortlisted by remember { mutableStateOf(false) }
    var isInterestSent by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (profile.photoUrl.isNotEmpty()) {
                    AsyncImage(
                        model = profile.photoUrl,
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(SaffronPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
                    }
                }

                if (profile.isVerified) {
                    Surface(
                        modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                        color = VerifiedBlue,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Verified Samaj Candidate", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                IconButton(
                    onClick = { isShortlisted = !isShortlisted },
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                ) {
                    Icon(
                        if (isShortlisted) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Shortlist",
                        tint = if (isShortlisted) Color(0xFFFFD700) else Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("${profile.age} yrs • ${profile.height}", fontWeight = FontWeight.Bold, color = SaffronPrimary, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text("🎓 ${profile.education} • 💼 ${profile.occupation}", fontSize = 13.sp)
                Text("📍 ${profile.city}, ${profile.state} (Income: ${profile.annualIncome})", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            isInterestSent = true
                            onSendInterest()
                        },
                        enabled = !isInterestSent,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = LoveRed)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isInterestSent) "Interest Sent ❤️" else "Send Interest")
                    }

                    OutlinedButton(
                        onClick = onCardClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("View Profile")
                    }
                }
            }
        }
    }
}

@Composable
private fun MatrimonialDetailDialog(
    profile: MatrimonialProfile,
    onDismiss: () -> Unit,
    onSendInterest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(profile.name, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Age & Height: ${profile.age} yrs, ${profile.height}", fontSize = 13.sp)
                Text("Education: ${profile.education}", fontSize = 13.sp)
                Text("Occupation: ${profile.occupation}", fontSize = 13.sp)
                Text("Location: ${profile.city}, ${profile.state}", fontSize = 13.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Family Details:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(profile.familyDetails, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Partner Preferences:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(profile.marriagePreference, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = {
            Button(
                onClick = onSendInterest,
                colors = ButtonDefaults.buttonColors(containerColor = LoveRed)
            ) {
                Text("Send Interest ❤️")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}
