package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.VerifiedBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserVerificationScreen(
    repository: AnjanaSamajRepository,
    onBack: () -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val verificationRequests by repository.verificationRequests.collectAsState()

    var docType by remember { mutableStateOf("Aadhaar Card / Samaj ID") }
    var notes by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anjana Samaj ID Verification", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (currentUser?.isVerified) {
                        "VERIFIED" -> VerifiedBlue.copy(alpha = 0.1f)
                        "PENDING" -> Color(0xFFFFB300).copy(alpha = 0.1f)
                        else -> MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        when (currentUser?.isVerified) {
                            "VERIFIED" -> Icons.Default.Verified
                            "PENDING" -> Icons.Default.HourglassTop
                            else -> Icons.Default.Shield
                        },
                        contentDescription = null,
                        tint = when (currentUser?.isVerified) {
                            "VERIFIED" -> VerifiedBlue
                            "PENDING" -> Color(0xFFF57C00)
                            else -> SaffronPrimary
                        },
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = when (currentUser?.isVerified) {
                                "VERIFIED" -> "Verified Anjana Member Badge Active"
                                "PENDING" -> "Verification Request Pending Review"
                                else -> "Get Your Verified Member Badge"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = when (currentUser?.isVerified) {
                                "VERIFIED" -> "Your profile displays the official blue verification badge in Matrimony & Social feed."
                                "PENDING" -> "Our Samaj Committee administrators are verifying your documents."
                                else -> "Submit your document to get verified and build trust in the community."
                            },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Security Notice Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = SaffronPrimary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🔒 Security Mandate: Your submitted identity documents are stored in restricted, encrypted Firebase Storage and are accessible ONLY to authorized Samaj administrators.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (currentUser?.isVerified != "VERIFIED" && currentUser?.isVerified != "PENDING") {
                Text("Submit Verification Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = docType,
                    onValueChange = { docType = it },
                    label = { Text("Document Type") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Native Village / Family Gotra details") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        repository.submitVerificationRequest(docType, notes)
                        isSubmitted = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_verification_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload Document & Request Verification", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
