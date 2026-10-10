package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Stars
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
fun PromoCodeScreen(
    wallet: UserWallet?,
    onRedeemPromo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var codeInput by remember { mutableStateOf("") }
    val claimedCodes = remember(wallet?.redeemedPromoCodes) {
        wallet?.redeemedPromoCodes?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FFBackground)
            .testTag("promo_code_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("promo_header_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                border = BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(FFGold, FFOrange)))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(FFGold.copy(alpha = 0.15f), FFSurfaceCard)
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
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(FFGold.copy(alpha = 0.2f))
                                .border(BorderStroke(2.dp, FFGold), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ConfirmationNumber,
                                contentDescription = null,
                                tint = FFGold,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "REDEEM PROMO CODES",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = FFTextPrimary,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Enter exclusive creator & community promo codes to claim free bonus points!",
                            fontSize = 12.sp,
                            color = FFTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }

        // Highlighted Creator Code MG69 Card
        item {
            val isMg69Claimed = claimedCodes.contains("MG69")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mg69_highlight_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isMg69Claimed) FFSurfaceVariant else Color(0xFF2E1C0A)
                ),
                border = BorderStroke(
                    1.5.dp,
                    if (isMg69Claimed) FFBorder else FFGold
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (isMg69Claimed) FFGreenSuccess else FFGold,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isMg69Claimed) "ALREADY CLAIMED" else "OFFICIAL CREATOR CODE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = "+10,000 PTS",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isMg69Claimed) FFTextMuted else FFGold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Code: MG69",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = FFTextPrimary,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "Grants 10,000 Points (≈ 1,000 Free Diamonds)",
                                fontSize = 12.sp,
                                color = FFTextSecondary
                            )
                        }

                        if (!isMg69Claimed) {
                            Button(
                                onClick = {
                                    codeInput = "MG69"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FFGold),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("apply_mg69_quick_button")
                            ) {
                                Text(
                                    text = "APPLY CODE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        // Promo Code Input Box
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("promo_input_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurface),
                border = BorderStroke(1.dp, FFBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Enter Promo Code",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = FFTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = { codeInput = it.uppercase() },
                        placeholder = { Text("Type code here (e.g. MG69)", color = FFTextMuted) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (codeInput.isNotBlank()) {
                                    onRedeemPromo(codeInput)
                                    codeInput = ""
                                }
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FFCyanDiamond,
                            unfocusedBorderColor = FFBorder,
                            focusedTextColor = FFTextPrimary,
                            unfocusedTextColor = FFTextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("promo_code_input_field")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (codeInput.isNotBlank()) {
                                onRedeemPromo(codeInput)
                                codeInput = ""
                            }
                        },
                        enabled = codeInput.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FFOrange,
                            disabledContainerColor = FFSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("redeem_promo_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ConfirmationNumber,
                                contentDescription = null,
                                tint = if (codeInput.isNotBlank()) Color.White else FFTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "REDEEM PROMO CODE",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = if (codeInput.isNotBlank()) Color.White else FFTextMuted
                            )
                        }
                    }
                }
            }
        }

        // Active Community Codes List
        item {
            Text(
                text = "COMMUNITY CODES AVAILABLE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FFTextSecondary,
                letterSpacing = 1.sp
            )
        }

        val availableCodes = listOf(
            Triple("BOOYAH2026", 500, "Official Season Welcome"),
            Triple("FIREPASS", 300, "Battle Pass Launch"),
            Triple("DIAMONDKING", 1000, "Gamer Milestone Reward")
        )

        items(availableCodes.size) { index ->
            val (code, pts, desc) = availableCodes[index]
            val isClaimed = claimedCodes.contains(code)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (!isClaimed) codeInput = code
                    }
                    .testTag("community_code_$code"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                border = BorderStroke(1.dp, if (isClaimed) FFGreenSuccess.copy(alpha = 0.4f) else FFBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = code,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isClaimed) FFTextMuted else FFCyanDiamond,
                                letterSpacing = 1.sp
                            )
                            if (isClaimed) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "✓ Claimed",
                                    fontSize = 11.sp,
                                    color = FFGreenSuccess,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = desc,
                            fontSize = 11.sp,
                            color = FFTextSecondary
                        )
                    }

                    Surface(
                        color = if (isClaimed) FFSurfaceVariant else FFOrange.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "+$pts PTS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isClaimed) FFTextMuted else FFOrange,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
