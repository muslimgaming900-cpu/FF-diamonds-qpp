package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DailyChallengeItem
import com.example.data.model.UserWallet
import com.example.ui.components.BalanceOverviewCard
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

@Composable
fun HomeScreen(
    wallet: UserWallet?,
    dailyChallenges: List<DailyChallengeItem>,
    onNavigateToAds: () -> Unit,
    onNavigateToCaptcha: () -> Unit,
    onNavigateToRedeem: () -> Unit,
    onNavigateToPromo: () -> Unit,
    onClaimStreak: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FFBackground)
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Gaming Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, FFBorder),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.ff_hero_banner_1791629531082),
                        contentDescription = "FF Championship Diamond Rewards",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(18.dp))
                    )

                    // Gradient overlay with call to action
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        FFBackground.copy(alpha = 0.5f),
                                        FFBackground.copy(alpha = 0.95f)
                                    ),
                                    startY = 100f
                                )
                            )
                            .padding(14.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = FFOrange,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "SEASON REWARDS ACTIVE",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "100 PTS per Task",
                                    color = FFCyanDiamond,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Earn Free Fire Diamonds",
                                color = FFTextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // Wallet Balance Card
        item {
            BalanceOverviewCard(
                points = wallet?.points ?: 0,
                onRedeemClick = onNavigateToRedeem
            )
        }

        // Special Creator Code MG69 Teaser Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToPromo() }
                    .testTag("promo_teaser_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceVariant),
                border = BorderStroke(1.2.dp, FFGold.copy(alpha = 0.8f))
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
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(FFGold.copy(alpha = 0.2f))
                                .border(BorderStroke(1.dp, FFGold), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ConfirmationNumber,
                                contentDescription = "Promo Code",
                                tint = FFGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CREATOR CODE: ",
                                    color = FFTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "MG69",
                                    color = FFGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Text(
                                text = "Redeem for 10,000 Free Points!",
                                color = FFTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToPromo,
                        colors = ButtonDefaults.buttonColors(containerColor = FFGold),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "CLAIM",
                            color = FFBackground,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Quick Earn Grid (Real Ads & Captcha)
        item {
            Text(
                text = "EARN GAMING CREDITS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FFTextSecondary,
                letterSpacing = 1.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Watch Ads Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToAds() }
                        .testTag("task_watch_ads_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                    border = BorderStroke(1.dp, FFOrange.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(FFOrange.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Ads",
                                tint = FFOrange,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Watch Real Ads",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = FFTextPrimary
                        )
                        Text(
                            text = "High-paying video ads",
                            fontSize = 11.sp,
                            color = FFTextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = FFOrange.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "+100 PTS / Ad",
                                color = FFOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Solve Captcha Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToCaptcha() }
                        .testTag("task_solve_captcha_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                    border = BorderStroke(1.dp, FFCyanDiamond.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(FFCyanDiamond.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Captcha",
                                tint = FFCyanDiamond,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Solve Captcha",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = FFTextPrimary
                        )
                        Text(
                            text = "Security challenges",
                            fontSize = 11.sp,
                            color = FFTextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = FFCyanDiamond.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "+100 PTS / Task",
                                color = FFCyanDiamond,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Daily Streak Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_streak_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurface),
                border = BorderStroke(1.dp, FFBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "7-DAY LOGIN STREAK",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FFTextSecondary
                            )
                            Text(
                                text = "Current Streak: Day ${wallet?.streakDays ?: 1}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = FFTextPrimary
                            )
                        }

                        Button(
                            onClick = onClaimStreak,
                            colors = ButtonDefaults.buttonColors(containerColor = FFOrange),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("claim_daily_streak_button")
                        ) {
                            Text(
                                text = "CLAIM DAILY",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val streakRewards = listOf(50, 100, 150, 200, 250, 350, 500)
                        items(streakRewards.size) { index ->
                            val dayNum = index + 1
                            val isPassed = dayNum <= (wallet?.streakDays ?: 1)
                            val isCurrent = dayNum == (wallet?.streakDays ?: 1)

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isCurrent) FFOrange.copy(alpha = 0.2f) else FFSurfaceVariant,
                                border = BorderStroke(
                                    1.dp,
                                    if (isCurrent) FFOrange else if (isPassed) FFGreenSuccess else FFBorder
                                ),
                                modifier = Modifier.width(46.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "D$dayNum",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPassed) FFGreenSuccess else FFTextMuted
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "+${streakRewards[index]}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isCurrent) FFOrange else FFTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Daily Challenges Quests
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DAILY CHALLENGES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FFTextSecondary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Resets in 14h 22m",
                    fontSize = 11.sp,
                    color = FFTextMuted
                )
            }
        }

        items(dailyChallenges) { quest ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_challenge_item_${quest.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                border = BorderStroke(1.dp, if (quest.isCompleted) FFGreenSuccess.copy(alpha = 0.4f) else FFBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (quest.isCompleted) FFGreenSuccess.copy(alpha = 0.2f) else FFSurfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (quest.isCompleted) Icons.Default.CheckCircle else Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = if (quest.isCompleted) FFGreenSuccess else FFTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = quest.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = FFTextPrimary
                            )
                            Text(
                                text = "+${quest.rewardPoints} PTS",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = FFCyanDiamond
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = quest.description,
                            fontSize = 11.sp,
                            color = FFTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LinearProgressIndicator(
                                progress = { (quest.current.toFloat() / quest.target).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (quest.isCompleted) FFGreenSuccess else FFOrange,
                                trackColor = FFSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${quest.current}/${quest.target}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FFTextMuted
                            )
                        }
                    }
                }
            }
        }

        // 100% Safe & Secure Guarantee Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("security_guarantee_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurface),
                border = BorderStroke(1.dp, FFGreenSuccess.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(FFGreenSuccess.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Safe",
                            tint = FFGreenSuccess,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "100% Safe For Your Main Free Fire ID",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = FFTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Only your player UID is needed. Never asks for password or game login credentials. 100% compliant with Garena policies.",
                            fontSize = 11.sp,
                            color = FFTextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
