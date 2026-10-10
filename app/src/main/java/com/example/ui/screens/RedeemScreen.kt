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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RedemptionOrder
import com.example.data.model.UserWallet
import com.example.ui.theme.FFBackground
import com.example.ui.theme.FFBorder
import com.example.ui.theme.FFCyanDiamond
import com.example.ui.theme.FFGold
import com.example.ui.theme.FFGreenSuccess
import com.example.ui.theme.FFOrange
import com.example.ui.theme.FFRedError
import com.example.ui.theme.FFSurface
import com.example.ui.theme.FFSurfaceCard
import com.example.ui.theme.FFSurfaceVariant
import com.example.ui.theme.FFTextMuted
import com.example.ui.theme.FFTextPrimary
import com.example.ui.theme.FFTextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class DiamondPackage(
    val diamonds: Int,
    val pointsCost: Int,
    val popularTag: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(
    wallet: UserWallet?,
    orders: List<RedemptionOrder>,
    onSubmitRedemption: (uid: String, region: String, diamonds: Int, pointsCost: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var uidInput by remember { mutableStateOf(wallet?.savedUid ?: "") }
    var selectedRegion by remember { mutableStateOf(wallet?.savedRegion ?: "Global") }
    var regionDropdownExpanded by remember { mutableStateOf(false) }

    val regions = listOf("Global", "India", "Indonesia", "Brazil", "MENA", "North America", "Europe", "Singapore")

    val packages = remember {
        listOf(
            DiamondPackage(100, 1000),
            DiamondPackage(310, 3000),
            DiamondPackage(520, 5000),
            DiamondPackage(1060, 10000, "POPULAR - MG69 PACK"),
            DiamondPackage(2180, 20000),
            DiamondPackage(5600, 50000)
        )
    }

    var selectedDiamonds by remember { mutableIntStateOf(1060) }
    var isVerifyingUid by remember { mutableStateOf(false) }
    var verifiedNickName by remember { mutableStateOf<String?>(null) }
    var uidError by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val pointsBalance = wallet?.points ?: 0
    val selectedCost = selectedDiamonds * 10
    val hasEnoughPoints = pointsBalance >= selectedCost

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FFBackground)
            .testTag("redeem_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Overview Top Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("redeem_balance_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                border = BorderStroke(1.2.dp, FFCyanDiamond.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AVAILABLE FOR REDEMPTION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FFTextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "%,d".format(pointsBalance),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = FFTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PTS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = FFOrange,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }

                    Surface(
                        color = FFCyanDiamond.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, FFCyanDiamond)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = null,
                                tint = FFCyanDiamond,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "≈ %,d 💎".format(pointsBalance / 10),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = FFCyanDiamond
                            )
                        }
                    }
                }
            }
        }

        // Free Fire Gamer ID Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("player_details_input_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurface),
                border = BorderStroke(1.dp, FFBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = FFOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FREE FIRE PLAYER ACCOUNT",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = FFTextPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Player UID field
                    OutlinedTextField(
                        value = uidInput,
                        onValueChange = {
                            if (it.length <= 12 && it.all { ch -> ch.isDigit() }) {
                                uidInput = it
                                uidError = null
                                verifiedNickName = null
                            }
                        },
                        label = { Text("Main Free Fire Player UID (8-11 Digits)") },
                        placeholder = { Text("e.g. 1928374650", color = FFTextMuted) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uidError != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FFCyanDiamond,
                            unfocusedBorderColor = FFBorder,
                            focusedTextColor = FFTextPrimary,
                            unfocusedTextColor = FFTextPrimary
                        ),
                        trailingIcon = {
                            if (isVerifyingUid) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = FFCyanDiamond,
                                    strokeWidth = 2.dp
                                )
                            } else if (verifiedNickName != null) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = FFGreenSuccess
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("player_uid_input")
                    )

                    uidError?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = it, fontSize = 11.sp, color = FFRedError)
                    }

                    // Region Selector Dropdown
                    Spacer(modifier = Modifier.height(10.dp))
                    ExposedDropdownMenuBox(
                        expanded = regionDropdownExpanded,
                        onExpandedChange = { regionDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedRegion,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Game Server / Region") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FFCyanDiamond,
                                unfocusedBorderColor = FFBorder,
                                focusedTextColor = FFTextPrimary,
                                unfocusedTextColor = FFTextPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("region_dropdown_selector")
                        )
                        ExposedDropdownMenu(
                            expanded = regionDropdownExpanded,
                            onDismissRequest = { regionDropdownExpanded = false },
                            modifier = Modifier.background(FFSurfaceCard)
                        ) {
                            regions.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region, color = FFTextPrimary) },
                                    onClick = {
                                        selectedRegion = region
                                        regionDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Verify Account Preview Button
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (uidInput.length < 8) {
                                    uidError = "Please enter valid 8-11 digit numeric UID"
                                } else {
                                    isVerifyingUid = true
                                    coroutineScope.launch {
                                        delay(1400)
                                        isVerifyingUid = false
                                        verifiedNickName = "ProGamer_${uidInput.takeLast(4)}"
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FFSurfaceVariant),
                            border = BorderStroke(1.dp, FFCyanDiamond.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("verify_uid_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = FFCyanDiamond,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Verify UID on Garena",
                                color = FFCyanDiamond,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        verifiedNickName?.let { nick ->
                            Surface(
                                color = FFGreenSuccess.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Found: $nick ✓",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FFGreenSuccess,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Diamond Package Selection
        item {
            Text(
                text = "SELECT DIAMONDS AMOUNT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FFTextSecondary,
                letterSpacing = 1.sp
            )
        }

        // Diamond Packages Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                packages.chunked(2).forEach { rowPackages ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowPackages.forEach { pkg ->
                            val isSelected = selectedDiamonds == pkg.diamonds
                            val canAfford = pointsBalance >= pkg.pointsCost

                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedDiamonds = pkg.diamonds }
                                    .testTag("diamond_package_${pkg.diamonds}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) FFSurfaceVariant else FFSurfaceCard
                                ),
                                border = BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) FFCyanDiamond else FFBorder
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    pkg.popularTag?.let { tag ->
                                        Surface(
                                            color = FFGold,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = tag,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Diamond,
                                            contentDescription = null,
                                            tint = FFCyanDiamond,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${pkg.diamonds}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = FFTextPrimary
                                        )
                                    }

                                    Text(
                                        text = "Diamonds",
                                        fontSize = 11.sp,
                                        color = FFTextSecondary
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Surface(
                                        color = if (canAfford) FFOrange.copy(alpha = 0.2f) else Color(0xFF334155),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "%,d PTS".format(pkg.pointsCost),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (canAfford) FFOrange else FFTextMuted,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Security Guarantee Box
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("security_policy_box"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FFSurface)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = FFGreenSuccess,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "100% Safe: Diamonds are dispatched via official direct UID top-up API. Zero ban risk, no password required.",
                        fontSize = 11.sp,
                        color = FFGreenSuccess,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Primary Submit Redeem Button
        item {
            Button(
                onClick = {
                    if (uidInput.length < 8) {
                        uidError = "Please enter valid 8-11 digit numeric UID"
                    } else if (!hasEnoughPoints) {
                        uidError = "Insufficient points! Earn more via Ads, Captcha or Promo Code."
                    } else {
                        showConfirmDialog = true
                    }
                },
                enabled = hasEnoughPoints,
                colors = ButtonDefaults.buttonColors(
                    containerColor = FFOrange,
                    disabledContainerColor = FFSurfaceVariant
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_redeem_request_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = if (hasEnoughPoints) Color.White else FFTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasEnoughPoints)
                            "REDEEM $selectedDiamonds DIAMONDS (%,d PTS)".format(selectedCost)
                        else
                            "NEED %,d MORE POINTS".format(selectedCost - pointsBalance),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = if (hasEnoughPoints) Color.White else FFTextMuted
                    )
                }
            }
        }

        // Recent Orders List (Live Status Tracker)
        if (orders.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "LIVE TOP-UP ORDER TRACKER",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FFTextSecondary,
                    letterSpacing = 1.sp
                )
            }

            items(orders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("redemption_order_item_${order.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                    border = BorderStroke(
                        1.dp,
                        if (order.status == "COMPLETED") FFGreenSuccess.copy(alpha = 0.5f) else FFOrange.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ORDER #${order.id}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = FFTextPrimary
                            )
                            Surface(
                                color = when (order.status) {
                                    "COMPLETED" -> FFGreenSuccess.copy(alpha = 0.2f)
                                    "VERIFIED" -> FFCyanDiamond.copy(alpha = 0.2f)
                                    else -> FFOrange.copy(alpha = 0.2f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = order.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = when (order.status) {
                                        "COMPLETED" -> FFGreenSuccess
                                        "VERIFIED" -> FFCyanDiamond
                                        else -> FFOrange
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "UID: ${order.uid} (${order.region})",
                                fontSize = 12.sp,
                                color = FFTextSecondary
                            )
                            Text(
                                text = "+${order.diamonds} 💎",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = FFCyanDiamond
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = order.serverNote,
                            fontSize = 11.sp,
                            color = FFTextMuted
                        )
                        Text(
                            text = "Tx: ${order.txHash}",
                            fontSize = 10.sp,
                            color = FFTextMuted
                        )
                    }
                }
            }
        }
    }

    // Confirmation Alert
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "Confirm Free Fire Top-Up",
                    fontWeight = FontWeight.Black,
                    color = FFTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to redeem $selectedDiamonds Diamonds for Free Fire UID: $uidInput ($selectedRegion)?",
                        color = FFTextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cost: %,d Points will be deducted from your balance.".format(selectedCost),
                        color = FFOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onSubmitRedemption(uidInput, selectedRegion, selectedDiamonds, selectedCost)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FFOrange)
                ) {
                    Text("CONFIRM TOP-UP", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel", color = FFTextMuted)
                }
            },
            containerColor = FFSurfaceCard
        )
    }
}
