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
import com.example.model.NearbyMember
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.VerifiedBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyPeopleScreen(
    repository: AnjanaSamajRepository,
    onStartChat: (userId: String, name: String) -> Unit
) {
    val nearbyMembers by repository.nearbyMembers.collectAsState()

    var maxDistanceKm by remember { mutableFloatStateOf(20f) }
    var verifiedOnly by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("All") } // "All", "Male", "Female"
    var showFilterModal by remember { mutableStateOf(false) }

    val filteredMembers = nearbyMembers.filter { member ->
        member.distanceKm <= maxDistanceKm &&
        (!verifiedOnly || member.isVerified) &&
        (selectedGender == "All" || member.gender == selectedGender)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paas ke Members (Nearby)", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronPrimary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = { showFilterModal = true },
                        modifier = Modifier.testTag("nearby_filter_btn")
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color.White)
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
            // Privacy Security Notice Banner
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Privacy Shield: Exact GPS & addresses are never shared. Distances shown are approximate.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }

            // Distance Slider Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Search Radius", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${maxDistanceKm.toInt()} km", fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    }
                    Slider(
                        value = maxDistanceKm,
                        onValueChange = { maxDistanceKm = it },
                        valueRange = 1f..50f,
                        colors = SliderDefaults.colors(thumbColor = SaffronPrimary, activeTrackColor = SaffronPrimary)
                    )
                }
            }

            if (filteredMembers.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LocationOff, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No members found within ${maxDistanceKm.toInt()} km", fontWeight = FontWeight.Bold)
                        Text("Try increasing the distance radius.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredMembers) { member ->
                        NearbyMemberCard(member = member, onStartChat = { onStartChat(member.id, member.fullName) })
                    }
                }
            }
        }
    }

    // FILTER MODAL
    if (showFilterModal) {
        AlertDialog(
            onDismissRequest = { showFilterModal = false },
            title = { Text("Filter Nearby Members") },
            text = {
                Column {
                    Text("Gender Filter:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All", "Male", "Female").forEach { g ->
                            FilterChip(
                                selected = selectedGender == g,
                                onClick = { selectedGender = g },
                                label = { Text(g) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Verified Profiles Only", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Switch(
                            checked = verifiedOnly,
                            onCheckedChange = { verifiedOnly = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary)
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showFilterModal = false }, colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)) {
                    Text("Apply Filters")
                }
            }
        )
    }
}

@Composable
private fun NearbyMemberCard(
    member: NearbyMember,
    onStartChat: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Surface(
                    modifier = Modifier.size(64.dp).clip(CircleShape),
                    color = SaffronPrimary
                ) {
                    if (member.photoUrl.isNotEmpty()) {
                        AsyncImage(model = member.photoUrl, contentDescription = member.fullName, contentScale = ContentScale.Crop)
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            Text(member.fullName.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                }
                if (member.isVerified) {
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        color = VerifiedBlue,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color.White, modifier = Modifier.size(18.dp).padding(2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(member.fullName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Text(
                    text = "${member.age} yrs • ${member.approximateArea}, ${member.city}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${member.occupation} (${member.education})",
                    fontSize = 11.sp,
                    color = SaffronPrimary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NearMe, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${member.distanceKm} km away",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            IconButton(
                onClick = onStartChat,
                modifier = Modifier
                    .background(SaffronPrimary.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.Chat, contentDescription = "Message", tint = SaffronPrimary)
            }
        }
    }
}
