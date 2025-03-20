package models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Transaction(
    var id: String? = "",
    var type: String? = TransactionType.DEPOSIT.name, // Store enum as a string
    var accountNumber: String? = "",
    var goalName: String? = "",
    var targetAccountNumber: String? = null, // Nullable for non-transfer transactions
    var targetGoalName: String? = null, // Nullable for non-transfer transactions
    var targetUserEmail: String? = null,    // Nullable, only needed for external transfers
    var amount: Double? = 0.0,            // Store transaction amount
    val timestamp: LocalDateTime? = LocalDateTime.now(),
    var currency: String? = CurrencyType.USD.code
) {
    fun getTheCurrency(): CurrencyType {
        return currency?.let { CurrencyType.find(it) } ?: CurrencyType.USD
    }

    fun getCurrencySymbol(): String {
        return getTheCurrency().symbol
    }
    fun formatAmount() = "${getCurrencySymbol()}${"%,.2f".format(amount)}"
//    fun formatAmount() = "%.2f %s".format(amount, getCurrencySymbol())

    fun getTheTransactionType(): TransactionType {
        return TransactionType.valueOf(type ?: TransactionType.DEPOSIT.name)
    }

    fun formatDate() = timestamp?.format(DateTimeFormatter.ISO_DATE) ?: ""

    fun formatDateTime(): String {
        return try {
            timestamp?.format(DateTimeFormatter.ISO_DATE_TIME) ?: ""
        } catch (_: Exception){
            ""
        }
    }
}