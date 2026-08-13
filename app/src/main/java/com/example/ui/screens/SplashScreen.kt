package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val context = LocalContext.current

    // Synchronously decode bitmap from asset or resource safely on first composition
    val imageBitmap: ImageBitmap? = remember(context) {
        try {
            context.assets.open("gurudev_rajeshwar.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: Exception) {
            try {
                BitmapFactory.decodeResource(context.resources, R.drawable.img_gurudev_rajeshwar)?.asImageBitmap()
            } catch (ex: Exception) {
                null
            }
        }
    }

    // Animation states: 2.5 second duration
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.92f) }
    val progressAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Parallel animations for smooth fade in and gentle scale
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1.02f,
            animationSpec = tween(durationMillis = 2500, easing = LinearOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        progressAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2300, easing = LinearEasing)
        )
        delay(200) // Ensure full 2.5 seconds (2500ms total duration)
        onSplashFinished()
    }

    // Devotional Golden-Saffron Gradient Palette
    val goldenGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3E1200), // Rich Dark Golden Umber
            Color(0xFF6E2200), // Deep Devotional Saffron
            Color(0xFFB84500), // Warm Orange
            Color(0xFFE67E22), // Vibrant Saffron
            Color(0xFFD4AC0D), // Golden Sand
            Color(0xFF381000)  // Deep Golden Base
        )
    )

    val cardBorderGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFD700),
            Color(0xFFFFA500),
            Color(0xFFFFD700)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(goldenGradient)
            .testTag("splash_screen_root"),
        contentAlignment = Alignment.Center
    ) {
        // Decorative background aura
        Box(
            modifier = Modifier
                .size(360.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFDF00).copy(alpha = 0.25f),
                            Color(0xFFFF8C00).copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState())
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Main Devotional Image Container preserving image proportions
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .wrapContentHeight()
                    .padding(top = 8.dp)
                    .border(2.dp, cardBorderGradient, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E0800))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Gurudev Rajeshwar Bhagwan",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        AsyncImage(
                            model = R.drawable.img_gurudev_rajeshwar,
                            contentDescription = "Gurudev Rajeshwar Bhagwan",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text Typography Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // "राजेश्वर भगवान"
                Text(
                    text = "राजेश्वर भगवान",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFEAA7),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // "ANJANA SAMAJ"
                Text(
                    text = "ANJANA SAMAJ",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF),
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Golden Divider Line
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, Color(0xFFFFD700), Color.Transparent)
                            )
                        )
                        .padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // "समाज • संस्कार • सेवा • संबंध"
                Text(
                    text = "समाज • संस्कार • सेवा • संबंध",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFF3CD),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Loading Progress Bar & Copyright Footer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                // Circular Anjana Samaj Emblem Badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF8E7))
                        .border(1.5.dp, Color(0xFFFFD700), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Anjana Samaj Badge",
                        tint = Color(0xFFD35400),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Loading...",
                    fontSize = 12.sp,
                    color = Color(0xFFFFE0B2),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Progress Indicator
                LinearProgressIndicator(
                    progress = { progressAnim.value },
                    modifier = Modifier
                        .width(180.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color(0xFFFFD700),
                    trackColor = Color(0x44FFFFFF)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "© Anjana Samaj | All Rights Reserved",
                    fontSize = 11.sp,
                    color = Color(0xCCFFE0B2),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
