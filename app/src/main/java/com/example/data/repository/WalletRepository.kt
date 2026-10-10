package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.RedemptionOrder
import com.example.data.model.TransactionEntity
import com.example.data.model.UserWallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class WalletRepository(private val database: AppDatabase) {
    private val walletDao = database.walletDao()
    private val transactionDao = database.transactionDao()
    private val redemptionDao = database.redemptionDao()
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    val walletFlow: Flow<UserWallet?> = walletDao.getWalletFlow()
    val transactionsFlow: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val ordersFlow: Flow<List<RedemptionOrder>> = redemptionDao.getAllOrders()

    suspend fun getOrCreateWallet(): UserWallet {
        val existing = walletDao.getWallet()
        if (existing != null) {
            return checkAndResetDailyTasks(existing)
        }
        val initial = UserWallet(
            id = 1,
            points = 250,
            totalEarned = 250,
            diamondsRedeemed = 0,
            streakDays = 1,
            lastClaimDate = "",
            referralCode = "FF-" + (10000 + (Math.random() * 89999).toInt()),
            referredBy = null,
            redeemedPromoCodes = "",
            savedUid = "",
            savedRegion = "Global",
            adsWatchedToday = 0,
            captchasSolvedToday = 0,
            lastTaskDate = getTodayDateString()
        )
        walletDao.insertOrUpdate(initial)
        transactionDao.insert(
            TransactionEntity(
                type = "WELCOME_BONUS",
                title = "Welcome Reward",
                detail = "New Gamer Welcome Bonus Credited",
                pointsDelta = 250
            )
        )
        return initial
    }

    private suspend fun checkAndResetDailyTasks(wallet: UserWallet): UserWallet {
        val today = getTodayDateString()
        if (wallet.lastTaskDate != today) {
            val updated = wallet.copy(
                adsWatchedToday = 0,
                captchasSolvedToday = 0,
                lastTaskDate = today
            )
            walletDao.update(updated)
            return updated
        }
        return wallet
    }

    suspend fun recordAdWatched(adTitle: String): Int {
        val wallet = getOrCreateWallet()
        val reward = 100
        val updated = wallet.copy(
            points = wallet.points + reward,
            totalEarned = wallet.totalEarned + reward,
            adsWatchedToday = wallet.adsWatchedToday + 1
        )
        walletDao.update(updated)
        transactionDao.insert(
            TransactionEntity(
                type = "AD_WATCH",
                title = "Ad Reward: 100 Points",
                detail = "Sponsored Video Ad: $adTitle",
                pointsDelta = reward
            )
        )
        return reward
    }

    suspend fun recordCaptchaSolved(type: String): Int {
        val wallet = getOrCreateWallet()
        val reward = 100
        val updated = wallet.copy(
            points = wallet.points + reward,
            totalEarned = wallet.totalEarned + reward,
            captchasSolvedToday = wallet.captchasSolvedToday + 1
        )
        walletDao.update(updated)
        transactionDao.insert(
            TransactionEntity(
                type = "CAPTCHA_SOLVE",
                title = "Captcha Solved: 100 Points",
                detail = "Verified Security Check ($type)",
                pointsDelta = reward
            )
        )
        return reward
    }

    suspend fun redeemPromoCode(inputCode: String): Result<Int> {
        val code = inputCode.trim().uppercase()
        val wallet = getOrCreateWallet()
        val usedCodes = wallet.redeemedPromoCodes.split(",").filter { it.isNotBlank() }

        if (usedCodes.contains(code)) {
            return Result.failure(Exception("Promo code '$code' has already been redeemed on this device!"))
        }

        val reward = when (code) {
            "MG69" -> 10000 // Specified in prompt: gives 10,000 points!
            "BOOYAH2026" -> 500
            "FIREPASS" -> 300
            "DIAMONDKING" -> 1000
            else -> return Result.failure(Exception("Invalid Promo Code! Check spelling or use creator code 'MG69'."))
        }

        val updatedCodes = if (wallet.redeemedPromoCodes.isBlank()) code else "${wallet.redeemedPromoCodes},$code"
        val updatedWallet = wallet.copy(
            points = wallet.points + reward,
            totalEarned = wallet.totalEarned + reward,
            redeemedPromoCodes = updatedCodes
        )
        walletDao.update(updatedWallet)
        transactionDao.insert(
            TransactionEntity(
                type = "PROMO_CODE",
                title = "Creator Code Redeemed: $code",
                detail = if (code == "MG69") "Special Creator VIP Code (+10,000 PTS)" else "Bonus Promo Reward",
                pointsDelta = reward
            )
        )
        return Result.success(reward)
    }

    suspend fun claimDailyStreak(): Result<Int> {
        val wallet = getOrCreateWallet()
        val today = getTodayDateString()
        if (wallet.lastClaimDate == today) {
            return Result.failure(Exception("You have already claimed today's daily streak bonus!"))
        }

        val streak = (wallet.streakDays % 7) + 1
        val reward = when (streak) {
            1 -> 50
            2 -> 100
            3 -> 150
            4 -> 200
            5 -> 250
            6 -> 350
            else -> 500
        }

        val updated = wallet.copy(
            points = wallet.points + reward,
            totalEarned = wallet.totalEarned + reward,
            streakDays = streak,
            lastClaimDate = today
        )
        walletDao.update(updated)
        transactionDao.insert(
            TransactionEntity(
                type = "DAILY_STREAK",
                title = "Daily Streak Day $streak",
                detail = "Daily Login Streak Bonus (+${reward} PTS)",
                pointsDelta = reward
            )
        )
        return Result.success(reward)
    }

    suspend fun applyReferralCode(code: String): Result<Int> {
        val trimmed = code.trim().uppercase()
        val wallet = getOrCreateWallet()
        if (wallet.referredBy != null) {
            return Result.failure(Exception("You have already used a referral code!"))
        }
        if (trimmed == wallet.referralCode) {
            return Result.failure(Exception("You cannot use your own referral code!"))
        }
        if (!trimmed.startsWith("FF-") || trimmed.length < 6) {
            return Result.failure(Exception("Invalid referral code format (e.g. FF-XXXXX)"))
        }

        val reward = 250
        val updated = wallet.copy(
            points = wallet.points + reward,
            totalEarned = wallet.totalEarned + reward,
            referredBy = trimmed
        )
        walletDao.update(updated)
        transactionDao.insert(
            TransactionEntity(
                type = "REFERRAL_BONUS",
                title = "Referral Bonus Claimed",
                detail = "Joined via friend's code: $trimmed",
                pointsDelta = reward
            )
        )
        return Result.success(reward)
    }

    suspend fun createRedemptionOrder(
        uid: String,
        region: String,
        diamonds: Int,
        pointsCost: Int
    ): Result<RedemptionOrder> {
        val cleanUid = uid.trim()
        if (cleanUid.length < 8 || !cleanUid.all { it.isDigit() }) {
            return Result.failure(Exception("Please enter a valid 8 to 11 digit Free Fire numeric UID!"))
        }
        val wallet = getOrCreateWallet()
        if (wallet.points < pointsCost) {
            return Result.failure(Exception("Insufficient points! You need $pointsCost points (Balance: ${wallet.points})"))
        }

        val orderId = "ORD-" + (100000 + (Math.random() * 899999).toInt())
        val txHash = "FF-TX-" + UUID.randomUUID().toString().take(10).uppercase()

        val updated = wallet.copy(
            points = wallet.points - pointsCost,
            diamondsRedeemed = wallet.diamondsRedeemed + diamonds,
            savedUid = cleanUid,
            savedRegion = region
        )
        walletDao.update(updated)

        val order = RedemptionOrder(
            id = orderId,
            uid = cleanUid,
            region = region,
            diamonds = diamonds,
            pointsCost = pointsCost,
            status = "PROCESSING",
            timestamp = System.currentTimeMillis(),
            txHash = txHash,
            serverNote = "Garena Official Top-Up Gateway in Queue"
        )
        redemptionDao.insert(order)

        transactionDao.insert(
            TransactionEntity(
                type = "DIAMOND_WITHDRAW",
                title = "Redeem $diamonds FF Diamonds",
                detail = "Dispatched to UID: $cleanUid ($region)",
                pointsDelta = -pointsCost,
                status = "PROCESSING"
            )
        )

        // Launch background server dispatch simulation
        repositoryScope.launch {
            delay(3500)
            redemptionDao.update(
                order.copy(
                    status = "VERIFIED",
                    serverNote = "UID Account Verified: Status Active & Region Matched"
                )
            )
            delay(4000)
            redemptionDao.update(
                order.copy(
                    status = "COMPLETED",
                    serverNote = "Diamonds Credited Successfully via Official UID Gateway!"
                )
            )
        }

        return Result.success(order)
    }

    suspend fun savePlayerDetails(uid: String, region: String) {
        val wallet = getOrCreateWallet()
        walletDao.update(wallet.copy(savedUid = uid.trim(), savedRegion = region))
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }
}
