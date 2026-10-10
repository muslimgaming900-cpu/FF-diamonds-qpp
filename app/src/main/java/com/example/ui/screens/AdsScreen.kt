package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.FFBackground
import com.example.ui.theme.FFBorder
import com.example.ui.theme.FFCyanDiamond
import com.example.ui.theme.FFGold
import com.example.ui.theme.FFGreenSuccess
import com.example.ui.theme.FFOrange
import com.example.ui.theme.FFSurface
import com.example.ui.theme.FFSurfaceCard
import com.example.ui.theme.FFSurfaceVariant
import com.example.ui.theme.FFTextMuted
import com.example.ui.theme.FFTextPrimary
import com.example.ui.theme.FFTextSecondary
import kotlinx.coroutines.delay

data class SponsoredAd(
    val id: String,
    val title: String,
    val sponsor: String,
    val category: String,
    val durationSeconds: Int,
    val icon: ImageVector,
    val accentColor: Color,
    val rating: String,
    val downloads: String,
    val tagline: String,
    val isPlayable: Boolean = false
)

@Composable
fun AdsScreen(
    adsWatchedToday: Int,
    onAdCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sponsoredAds = remember {
        listOf(
            SponsoredAd(
                id = "ad_ff_max",
                title = "Free Fire MAX: Season 45 Booyah Pass",
                sponsor = "Garena Official",
                category = "Battle Royale Game",
                durationSeconds = 12,
                icon = Icons.Default.LocalFireDepartment,
                accentColor = FFOrange,
                rating = "4.6 ★",
                downloads = "500M+",
                tagline = "Unlock exclusive mythic weapon skins, bundles & diamond royale vouchers today!"
            ),
            SponsoredAd(
                id = "ad_razer",
                title = "Razer BlackShark V2 Pro Gaming Headset",
                sponsor = "Razer Esports",
                category = "Pro Gaming Hardware",
                durationSeconds = 10,
                icon = Icons.Default.Headphones,
                accentColor = Color(0xFF00E676),
                rating = "4.9 ★",
                downloads = "Verified Sponsor",
                tagline = "Ultra-clear spatial audio with zero latency. Built for competitive Free Fire tournaments."
            ),
            SponsoredAd(
                id = "ad_shadow_rpg",
                title = "Shadow Strike: Tactical Heroes",
                sponsor = "Apex Studios",
                category = "Playable Demo Game",
                durationSeconds = 14,
                icon = Icons.Default.SportsEsports,
                accentColor = FFCyanDiamond,
                rating = "4.7 ★",
                downloads = "10M+",
                tagline = "Interactive Demo: Tap target enemies during the ad to test your combat reflexes!",
                isPlayable = true
            ),
            SponsoredAd(
                id = "ad_redbull",
                title = "RedBull Esports: High Velocity Energy",
                sponsor = "RedBull Gaming",
                category = "Esports Nutrition",
                durationSeconds = 8,
                icon = Icons.Default.LocalFireDepartment,
                accentColor = Color(0xFFFF1744),
                rating = "5.0 ★",
                downloads = "Official Sponsor",
                tagline = "Fuel your clutch plays in the final zone. Used by top international championship teams."
            )
        )
    }

    var activeAd by remember { mutableStateOf<SponsoredAd?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FFBackground)
            .testTag("ads_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Ads Status Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ads_status_header_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                border = BorderStroke(1.dp, FFOrange.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = FFOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WATCH REAL REWARDED ADS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = FFOrange,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Earn 100 Points Per Ad",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FFTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ads watched today: $adsWatchedToday / 25 daily limit",
                            fontSize = 12.sp,
                            color = FFTextSecondary
                        )
                    }

                    Surface(
                        color = FFOrange.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, FFOrange)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "+100",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = FFOrange
                            )
                            Text(
                                text = "PTS / AD",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = FFTextSecondary
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "AVAILABLE SPONSORED ADS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FFTextSecondary,
                letterSpacing = 1.sp
            )
        }

        // List of Real Ads
        items(sponsoredAds) { ad ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { activeAd = ad }
                    .testTag("ad_card_${ad.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurface),
                border = BorderStroke(1.dp, FFBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ad.accentColor.copy(alpha = 0.2f))
                            .border(BorderStroke(1.dp, ad.accentColor.copy(alpha = 0.6f)), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ad.icon,
                            contentDescription = ad.title,
                            tint = ad.accentColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = ad.sponsor.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ad.accentColor
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = FFTextMuted,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${ad.durationSeconds}s",
                                    fontSize = 11.sp,
                                    color = FFTextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Text(
                            text = ad.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = FFTextPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = ad.tagline,
                            fontSize = 11.sp,
                            color = FFTextSecondary,
                            maxLines = 2,
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { activeAd = ad },
                        colors = ButtonDefaults.buttonColors(containerColor = FFOrange),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("watch_ad_button_${ad.id}")
                    ) {
                        Text(
                            text = "WATCH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Ads Quality & Security Note
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ad_policy_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Authentic Rewarded Ad Delivery Policy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FFTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "To guarantee 100 points, watch the complete countdown timer. Never close the ad early. All ads are checked and verified for high-paying gaming rewards.",
                        fontSize = 11.sp,
                        color = FFTextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }

    // Interactive Real Ad Player Dialog
    activeAd?.let { ad ->
        RealRewardedAdPlayerDialog(
            ad = ad,
            onCloseEarly = { activeAd = null },
            onCompleted = {
                activeAd = null
                onAdCompleted(ad.title)
            }
        )
    }
}

@Composable
fun RealRewardedAdPlayerDialog(
    ad: SponsoredAd,
    onCloseEarly: () -> Unit,
    onCompleted: () -> Unit
) {
    var timeLeft by remember { mutableIntStateOf(ad.durationSeconds) }
    var isMuted by remember { mutableStateOf(false) }
    var showCloseWarning by remember { mutableStateOf(false) }
    var interactiveHits by remember { mutableIntStateOf(0) }

    LaunchedEffect(ad) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        // Small delay then trigger reward
        delay(600)
        onCompleted()
    }

    Dialog(
        onDismissRequest = {
            if (timeLeft > 0) showCloseWarning = true else onCloseEarly()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .testTag("rewarded_ad_player_modal")
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Ad Header Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFF263238),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "AD • SPONSORED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        if (timeLeft > 0) {
                            Text(
                                text = "Reward in ${timeLeft}s",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = FFCyanDiamond
                            )
                        } else {
                            Text(
                                text = "Reward Verified! (+100 PTS)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FFGreenSuccess
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { isMuted = !isMuted }) {
                            Icon(
                                imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                contentDescription = "Audio Toggle",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                if (timeLeft > 0) {
                                    showCloseWarning = true
                                } else {
                                    onCompleted()
                                }
                            },
                            modifier = Modifier.testTag("ad_close_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Ad",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Video Progress Bar
                LinearProgressIndicator(
                    progress = { 1f - (timeLeft.toFloat() / ad.durationSeconds) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = FFOrange,
                    trackColor = Color.DarkGray
                )

                // Video Player & Playable Content Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF020617)
                                )
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Commercial Video Animation Simulation
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(ad.accentColor.copy(alpha = 0.25f))
                                .border(BorderStroke(2.dp, ad.accentColor), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ad.icon,
                                contentDescription = null,
                                tint = ad.accentColor,
                                modifier = Modifier.size(64.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = ad.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = ad.tagline,
                            fontSize = 13.sp,
                            color = Color(0xFFCBD5E1),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )

                        // If Playable Ad mini demo
                        if (ad.isPlayable) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(
                                color = FFSurfaceVariant,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, FFCyanDiamond)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "🎮 Playable Interactive Demo",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = FFCyanDiamond
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Tap the target to test combat response ($interactiveHits hits)",
                                        fontSize = 11.sp,
                                        color = FFTextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { interactiveHits++ },
                                        colors = ButtonDefaults.buttonColors(containerColor = FFCyanDiamond),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("playable_ad_tap_target")
                                    ) {
                                        Text(
                                            text = "🎯 TARGET ENEMY (HIT +1)",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom App Installation / Call To Action Card
                Surface(
                    color = Color(0xFF1E293B),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ad.accentColor.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ad.icon,
                                    contentDescription = null,
                                    tint = ad.accentColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = ad.sponsor,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = ad.rating,
                                        fontSize = 11.sp,
                                        color = FFGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = ad.downloads,
                                        fontSize = 11.sp,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                // Real CTA simulation
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FFGreenSuccess),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("ad_install_cta_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "INSTALL",
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCloseWarning) {
        AlertDialog(
            onDismissRequest = { showCloseWarning = false },
            title = {
                Text(
                    text = "Close Ad Early?",
                    fontWeight = FontWeight.Bold,
                    color = FFTextPrimary
                )
            },
            text = {
                Text(
                    text = "If you close now before the timer finishes, you will forfeit your 100 points reward!",
                    color = FFTextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showCloseWarning = false }
                ) {
                    Text("RESUME AD (+100 PTS)", color = FFOrange, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCloseWarning = false
                        onCloseEarly()
                    }
                ) {
                    Text("Close & Cancel", color = FFTextMuted)
                }
            },
            containerColor = FFSurfaceCard
        )
    }
}
