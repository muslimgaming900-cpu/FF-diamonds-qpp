package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RedemptionOrder
import com.example.data.model.TransactionEntity
import com.example.ui.theme.FFBackground
import com.example.ui.theme.FFBorder
import com.example.ui.theme.FFCyanDiamond
import com.example.ui.theme.FFGreenSuccess
import com.example.ui.theme.FFOrange
import com.example.ui.theme.FFRedError
import com.example.ui.theme.FFSurface
import com.example.ui.theme.FFSurfaceCard
import com.example.ui.theme.FFSurfaceVariant
import com.example.ui.theme.FFTextMuted
import com.example.ui.theme.FFTextPrimary
import com.example.ui.theme.FFTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    transactions: List<TransactionEntity>,
    orders: List<RedemptionOrder>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Points History", "UID Top-Ups")

    val dateFormatter = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.US) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FFBackground)
            .testTag("history_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = FFSurface,
                contentColor = FFCyanDiamond,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = FFCyanDiamond
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) FFCyanDiamond else FFTextSecondary
                            )
                        }
                    )
                }
            }
        }

        if (selectedTab == 0) {
            if (transactions.isEmpty()) {
                item {
                    EmptyStateNotice("No points activity yet. Complete tasks or ads to start earning!")
                }
            } else {
                items(transactions) { tx ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transaction_item_${tx.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                        border = BorderStroke(1.dp, FFBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val isPositive = tx.pointsDelta >= 0
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isPositive) FFGreenSuccess.copy(alpha = 0.15f) else FFOrange.copy(alpha = 0.15f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (tx.type) {
                                        "AD_WATCH" -> Icons.Default.PlayCircle
                                        "CAPTCHA_SOLVE" -> Icons.Default.Security
                                        "PROMO_CODE" -> Icons.Default.ConfirmationNumber
                                        "DIAMOND_WITHDRAW" -> Icons.Default.Diamond
                                        else -> if (isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
                                    },
                                    contentDescription = null,
                                    tint = if (isPositive) FFGreenSuccess else FFOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tx.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = FFTextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = tx.detail,
                                    fontSize = 11.sp,
                                    color = FFTextSecondary
                                )
                                Text(
                                    text = dateFormatter.format(Date(tx.timestamp)),
                                    fontSize = 10.sp,
                                    color = FFTextMuted
                                )
                            }

                            Text(
                                text = (if (isPositive) "+" else "") + "%,d PTS".format(tx.pointsDelta),
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = if (isPositive) FFGreenSuccess else FFOrange
                            )
                        }
                    }
                }
            }
        } else {
            if (orders.isEmpty()) {
                item {
                    EmptyStateNotice("No withdrawal orders yet. Redeem your points for Free Fire diamonds!")
                }
            } else {
                items(orders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("order_history_item_${order.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FFSurfaceCard),
                        border = BorderStroke(1.dp, FFBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ORDER #${order.id}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    color = FFTextPrimary
                                )
                                Surface(
                                    color = if (order.status == "COMPLETED") FFGreenSuccess.copy(alpha = 0.2f) else FFOrange.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = order.status,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (order.status == "COMPLETED") FFGreenSuccess else FFOrange,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "UID: ${order.uid} • ${order.region}",
                                fontSize = 12.sp,
                                color = FFTextSecondary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Credit: +${order.diamonds} Diamonds",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FFCyanDiamond
                                )
                                Text(
                                    text = "Cost: %,d PTS".format(order.pointsCost),
                                    fontSize = 11.sp,
                                    color = FFTextMuted
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = order.serverNote,
                                fontSize = 11.sp,
                                color = FFTextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateNotice(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FFSurface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = FFTextMuted,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 12.sp,
                color = FFTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
