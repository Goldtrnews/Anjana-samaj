package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SaffronPrimary

@Composable
fun FirebaseSetupGuideModal(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Cloud, contentDescription = null, tint = SaffronPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Firebase Integration Guide", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Follow these steps to connect your live Firebase project to Anjana Samaj:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                GuideStepItem("1. Create Firebase Project", "Go to console.firebase.google.com and create a project named 'Anjana Samaj'.")
                GuideStepItem("2. Add Android App", "Package name: com.aistudio.anjanasamaj.community. Download google-services.json and place it in app/ directory.")
                GuideStepItem("3. Enable Phone Authentication", "In Firebase Auth -> Sign-in method, enable 'Phone'. Add test phone numbers for local development.")
                GuideStepItem("4. Deploy Firestore Rules", "Create collections: users, posts, statuses, marriage_profiles, interests, matches, chats, messages, calls, notifications, announcements, reports, verification_requests.")
                GuideStepItem("5. Deploy Storage Rules", "Enable Firebase Storage with restricted access for verification documents (users/{uid}/verification/).")
                GuideStepItem("6. WebRTC Video Call Signaling", "The video call service uses Firestore signaling docs under calls/{callId} for WebRTC SDP exchange.")
                GuideStepItem("7. Production APK/AAB Build", "Run ./gradlew assembleRelease to generate release APK in app/build/outputs/apk/release/.")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)) {
                Text("Got It!")
            }
        }
    )
}

@Composable
private fun GuideStepItem(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SaffronPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
