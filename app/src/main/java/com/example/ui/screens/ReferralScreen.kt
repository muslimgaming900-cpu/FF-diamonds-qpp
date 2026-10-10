package com.example.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserWallet
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
fun ReferralScreen(
    wallet: UserWallet?,
    onApplyReferral: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var friendCodeInput by remember { mutableStateOf("") }
    var copiedNotice by remember { mutableStateOf(false) }

    val myCode = wallet?.referralCode ?: "FF-83921"
    val isAlreadyReferred = wallet?.referredBy != null

    fun shareReferralCode() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "🎮 Join me on FF Diamonds! Earn free Free Fire diamonds with real ads and captcha tasks. Use my referral code: $myCode to claim +250 points immediately!"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Referral Code")
        context.startActivity(shareIntent)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FFBackground)
            .testTag("referral_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Referral Header Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("referral_header_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                border = BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(FFCyanDiamond, FFOrange)))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(FFCyanDiamond.copy(alpha = 0.15f), FFSurfaceCard)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(FFCyanDiamond.copy(alpha = 0.2f))
                                .border(BorderStroke(2.dp, FFCyanDiamond), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GroupAdd,
                                contentDescription = null,
                                tint = FFCyanDiamond,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "REFERRAL SQUAD BONUSES",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = FFTextPrimary,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Invite fellow Free Fire gamers. You both get +250 points when they sign up with your code!",
                            fontSize = 12.sp,
                            color = FFTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }

        // Your Unique Code Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("my_referral_code_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurface),
                border = BorderStroke(1.dp, FFBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "YOUR UNIQUE SQUAD CODE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FFTextSecondary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = FFSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, FFCyanDiamond),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                clipboardManager.setText(AnnotatedString(myCode))
                                copiedNotice = true
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = myCode,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = FFCyanDiamond,
                                letterSpacing = 2.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = FFTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (copiedNotice) "Copied!" else "Copy",
                                    fontSize = 12.sp,
                                    color = if (copiedNotice) FFGreenSuccess else FFTextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { shareReferralCode() },
                        colors = ButtonDefaults.buttonColors(containerColor = FFOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("share_referral_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SHARE WITH SQUAD MATES",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Enter Friend's Code Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("enter_friend_code_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurface),
                border = BorderStroke(1.dp, FFBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Enter Squad Friend's Code",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = FFTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isAlreadyReferred)
                            "Referred by: ${wallet?.referredBy} ✓"
                        else
                            "Enter your friend's invite code to claim +250 free points.",
                        fontSize = 11.sp,
                        color = if (isAlreadyReferred) FFGreenSuccess else FFTextSecondary
                    )

                    if (!isAlreadyReferred) {
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = friendCodeInput,
                            onValueChange = { friendCodeInput = it.uppercase() },
                            placeholder = { Text("e.g. FF-12345", color = FFTextMuted) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FFCyanDiamond,
                                unfocusedBorderColor = FFBorder,
                                focusedTextColor = FFTextPrimary,
                                unfocusedTextColor = FFTextPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("friend_referral_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (friendCodeInput.isNotBlank()) {
                                    onApplyReferral(friendCodeInput)
                                    friendCodeInput = ""
                                }
                            },
                            enabled = friendCodeInput.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FFCyanDiamond,
                                disabledContainerColor = FFSurfaceVariant
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("apply_referral_button")
                        ) {
                            Text(
                                text = "CLAIM +250 POINTS",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        // Tier Rewards Showcase
        item {
            Text(
                text = "REFERRAL BADGE TIERS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FFTextSecondary,
                letterSpacing = 1.sp
            )
        }

        val tiers = listOf(
            Triple("Bronze Scout", "1 Friend", "+250 PTS"),
            Triple("Silver Raider", "5 Friends", "+1,500 PTS"),
            Triple("Gold Veteran", "15 Friends", "+5,000 PTS"),
            Triple("Diamond Grandmaster", "30 Friends", "+12,000 PTS")
        )

        items(tiers.size) { index ->
            val (name, req, reward) = tiers[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tier_item_$index"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                border = BorderStroke(1.dp, FFBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(FFSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = if (index == 3) FFCyanDiamond else FFGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = FFTextPrimary
                            )
                            Text(
                                text = req,
                                fontSize = 11.sp,
                                color = FFTextSecondary
                            )
                        }
                    }

                    Surface(
                        color = FFOrange.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = reward,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = FFOrange,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}
