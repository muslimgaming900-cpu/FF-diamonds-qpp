package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_wallet")
data class UserWallet(
    @PrimaryKey
    val id: Int = 1,
    val points: Int = 250,
    val totalEarned: Int = 250,
    val diamondsRedeemed: Int = 0,
    val streakDays: Int = 1,
    val lastClaimDate: String = "",
    val referralCode: String = "FF-83921",
    val referredBy: String? = null,
    val redeemedPromoCodes: String = "", // Comma-separated list of codes
    val savedUid: String = "",
    val savedRegion: String = "Global",
    val adsWatchedToday: Int = 0,
    val captchasSolvedToday: Int = 0,
    val lastTaskDate: String = ""
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "AD_WATCH", "CAPTCHA_SOLVE", "PROMO_CODE", "DAILY_STREAK", "REFERRAL_BONUS", "DIAMOND_WITHDRAW"
    val title: String,
    val detail: String,
    val pointsDelta: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "COMPLETED"
)

@Entity(tableName = "redemption_orders")
data class RedemptionOrder(
    @PrimaryKey
    val id: String,
    val uid: String,
    val region: String,
    val diamonds: Int,
    val pointsCost: Int,
    val status: String = "PROCESSING", // "PROCESSING", "VERIFIED", "DISPATCHED", "COMPLETED"
    val timestamp: Long = System.currentTimeMillis(),
    val txHash: String,
    val serverNote: String = "Direct Garena Top-Up Gateway"
)

data class DailyChallengeItem(
    val id: String,
    val title: String,
    val description: String,
    val current: Int,
    val target: Int,
    val rewardPoints: Int,
    val isCompleted: Boolean,
    val isClaimed: Boolean
)
