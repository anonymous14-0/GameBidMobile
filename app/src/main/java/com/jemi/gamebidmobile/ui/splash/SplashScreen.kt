package com.jemi.gamebidmobile.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen() {
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "splash_animation")

    val logoPulse by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_pulse"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.28f,
        targetValue = 0.62f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val loadingRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing)
        ),
        label = "loading_rotation"
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF16002F),
                        Color(0xFF4B0DA7),
                        Color(0xFF7B2CFF),
                        Color(0xFF2A0A58)
                    ),
                    start = Offset.Zero,
                    end = Offset(900f, 1600f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        DecorativeGradientOrbs(glowAlpha = glowAlpha)

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 900)) +
                scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
                )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GameBidLogo(
                    modifier = Modifier.scale(logoPulse),
                    glowAlpha = glowAlpha
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "GameBid Mobile",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Secure Game Trading Platform",
                    color = Color.White.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.6.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(44.dp))

                PremiumLoadingIndicator(rotation = loadingRotation)
            }
        }
    }
}

@Composable
private fun GameBidLogo(
    modifier: Modifier = Modifier,
    glowAlpha: Float
) {
    Box(
        modifier = modifier.size(132.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(24.dp)
                .alpha(glowAlpha)
                .background(Color(0xFFE7D6FF), CircleShape)
        )

        Surface(
            modifier = Modifier.size(104.dp),
            shape = RoundedCornerShape(30.dp),
            color = Color.White.copy(alpha = 0.14f),
            shadowElevation = 18.dp,
            tonalElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.34f),
                                Color.White.copy(alpha = 0.08f)
                            )
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(62.dp),
                    shape = CircleShape,
                    color = Color(0xFFFFD86B),
                    shadowElevation = 10.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.SportsEsports,
                            contentDescription = "GameBid Logo",
                            tint = Color(0xFF421080),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumLoadingIndicator(rotation: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(34.dp)
                .graphicsLayer { rotationZ = rotation },
            color = Color(0xFFFFD86B),
            strokeWidth = 3.dp,
            trackColor = Color.White.copy(alpha = 0.16f)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            repeat(3) { index ->
                val dotAlpha = 0.35f + (index * 0.2f)
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(Color.White.copy(alpha = dotAlpha), CircleShape)
                )
            }
        }
    }
}

@Composable
private fun DecorativeGradientOrbs(glowAlpha: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp, start = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .blur(56.dp)
                .alpha(glowAlpha)
                .background(Color(0xFFFFD86B), CircleShape)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 12.dp, bottom = 80.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .blur(68.dp)
                .alpha(0.42f)
                .background(Color(0xFF00D4FF), CircleShape)
        )
    }
}
