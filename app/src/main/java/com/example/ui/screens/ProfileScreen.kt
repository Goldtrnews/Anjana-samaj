package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.AnjanaSamajRepository
import com.example.model.UserProfile
import com.example.ui.theme.LoveRed
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.VerifiedBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    repository: AnjanaSamajRepository,
    onOpenVerification: () -> Unit,
    onOpenAdminDashboard: () -> Unit,
    onOpenFirebaseGuide: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    var showEditModal by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English / Hinglish") }

    val user = currentUser ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Samaj Profile", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Surface(
                            modifier = Modifier.size(90.dp).clip(CircleShape),
                            color = SaffronPrimary
                        ) {
                            if (user.photoUrl.isNotEmpty()) {
                                AsyncImage(model = user.photoUrl, contentDescription = user.fullName, contentScale = ContentScale.Crop)
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(user.fullName.take(1), color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        if (user.isVerified == "VERIFIED") {
                            Surface(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                color = VerifiedBlue,
                                shape = CircleShape
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color.White, modifier = Modifier.size(24.dp).padding(3.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("${user.occupation} • ${user.city}, ${user.state}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { showEditModal = true },
                        modifier = Modifier.testTag("edit_profile_btn")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Profile Details")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Sections List
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    ProfileOptionItem(
                        icon = Icons.Default.VerifiedUser,
                        title = "Anjana Samaj ID Verification",
                        subtitle = if (user.isVerified == "VERIFIED") "Verified Badge Active" else "Submit Govt ID for Verification",
                        onClick = onOpenVerification
                    )

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    ProfileOptionItem(
                        icon = Icons.Default.Cloud,
                        title = "Firebase & Backend Config Guide",
                        subtitle = "Instructions for live Firestore & FCM setup",
                        onClick = onOpenFirebaseGuide
                    )

                    if (user.isAdmin) {
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ProfileOptionItem(
                            icon = Icons.Default.AdminPanelSettings,
                            title = "Admin Dashboard",
                            subtitle = "Moderate posts, verifications & announcements",
                            onClick = onOpenAdminDashboard
                        )
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    ProfileOptionItem(
                        icon = Icons.Default.Language,
                        title = "Language / भाषा",
                        subtitle = selectedLanguage,
                        onClick = {}
                    )

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    ProfileOptionItem(
                        icon = Icons.Default.ExitToApp,
                        title = "Logout",
                        subtitle = "Sign out from this device",
                        titleColor = LoveRed,
                        onClick = onLogout
                    )
                }
            }
        }
    }

    if (showEditModal) {
        EditProfileDialog(
            user = user,
            onDismiss = { showEditModal = false },
            onSave = { updatedUser ->
                repository.updateProfile(updatedUser)
                showEditModal = false
            }
        )
    }
}

@Composable
private fun ProfileOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = SaffronPrimary)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = titleColor)
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun EditProfileDialog(
    user: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(user.fullName) }
    var education by remember { mutableStateOf(user.education) }
    var occupation by remember { mutableStateOf(user.occupation) }
    var city by remember { mutableStateOf(user.city) }
    var about by remember { mutableStateOf(user.aboutMe) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = education, onValueChange = { education = it }, label = { Text("Education") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = occupation, onValueChange = { occupation = it }, label = { Text("Occupation") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = about, onValueChange = { about = it }, label = { Text("About Me") }, modifier = Modifier.fillMaxWidth().height(90.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(user.copy(fullName = name, education = education, occupation = occupation, city = city, aboutMe = about))
                },
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
