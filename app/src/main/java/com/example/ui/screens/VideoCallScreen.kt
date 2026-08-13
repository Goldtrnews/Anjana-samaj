package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.AnjanaSamajRepository
import com.example.model.VideoCallSession
import com.example.ui.theme.LoveRed
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.delay

@Composable
fun ActiveVideoCallOverlay(
    callSession: VideoCallSession,
    onAccept: () -> Unit,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var callDurationSeconds by remember { mutableIntStateOf(0) }

    // Call duration timer
    LaunchedEffect(callSession.status) {
        if (callSession.status == "CONNECTED") {
            while (true) {
                delay(1000L)
                callDurationSeconds += 1
            }
        }
    }

    // Pulse animation for ringing
    val infiniteTransition = rememberInfiniteTransition(label = "ring_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        if (callSession.status == "CONNECTED") {
            // Simulated Remote Video Feed
            if (!isCameraOff) {
                AsyncImage(
                    model = callSession.receiverPhoto.ifEmpty { callSession.callerPhoto },
                    contentDescription = "Video Feed",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Camera Off", color = Color.White)
                }
            }

            // Self Camera Pip (Top Right)
            Surface(
                modifier = Modifier
                    .size(110.dp, 160.dp)
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = Color.Black
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("You", color = Color.White, fontSize = 12.sp)
                }
            }
        } else {
            // RINGING / OUTGOING SCREEN
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(SaffronPrimary.copy(alpha = 0.3f))
                    )
                    Surface(
                        modifier = Modifier.size(110.dp).clip(CircleShape),
                        color = SaffronPrimary
                    ) {
                        if (callSession.receiverPhoto.isNotEmpty()) {
                            AsyncImage(model = callSession.receiverPhoto, contentDescription = null, contentScale = ContentScale.Crop)
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Text(callSession.receiverName.take(1), color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(callSession.receiverName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    text = if (callSession.status == "RINGING") "Ringing... Anjana Samaj Call" else "Connecting WebRTC...",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // CALL TIMER OVERLAY
        if (callSession.status == "CONNECTED") {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp),
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = String.format("%02d:%02d", callDurationSeconds / 60, callDurationSeconds % 60),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // BOTTOM CONTROLS BAR
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            color = Color.Black.copy(alpha = 0.7f)
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (callSession.status == "RINGING" && callSession.callerId != "user_101") {
                    // ACCEPT BUTTON FOR INCOMING
                    IconButton(
                        onClick = onAccept,
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF2E7D32), CircleShape)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Accept", tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                }

                // MIC MUTE
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(50.dp)
                        .background(if (isMuted) Color.White else Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = if (isMuted) Color.Black else Color.White
                    )
                }

                // CAMERA TOGGLE
                IconButton(
                    onClick = { isCameraOff = !isCameraOff },
                    modifier = Modifier
                        .size(50.dp)
                        .background(if (isCameraOff) Color.White else Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Camera",
                        tint = if (isCameraOff) Color.Black else Color.White
                    )
                }

                // END CALL BUTTON
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .size(60.dp)
                        .background(LoveRed, CircleShape)
                        .testTag("end_call_btn")
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
fun CallHistoryTab(
    callHistory: List<VideoCallSession>,
    onStartCall: (userId: String, name: String, photo: String) -> Unit
) {
    if (callHistory.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No call history yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(callHistory) { call ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(modifier = Modifier.size(44.dp).clip(CircleShape), color = SaffronPrimary) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(call.receiverName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(call.receiverName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Duration: ${call.durationSeconds}s • Anjana Video Call", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        IconButton(onClick = { onStartCall(call.receiverId, call.receiverName, call.receiverPhoto) }) {
                            Icon(Icons.Default.Videocam, contentDescription = "Call Again", tint = SaffronPrimary)
                        }
                    }
                }
            }
        }
    }
}
