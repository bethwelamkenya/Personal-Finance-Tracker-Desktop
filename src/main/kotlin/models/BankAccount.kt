package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class BankAccount(
    val id: String? = "",
    var accountNumber: String? = "",
    var holderName: String? = "",
    var bankName: String? = "",
    var balance: Double? = 0.0,
    var currency: String? = CurrencyType.USD.name,
    val createdAt: LocalDate? = LocalDate.now()
) {
    fun getTheCurrency(): CurrencyType {
        return currency?.let { CurrencyType.find(it) } ?: CurrencyType.USD
    }

    fun getCurrencySymbol(): String {
        return getTheCurrency().symbol
    }

    fun formatBalance() = "${getCurrencySymbol()}${"%,.2f".format(balance)}"
//    fun formatBalance() = "%.2f %s".format(balance, getCurrencySymbol())

    fun formatDate() = createdAt?.format(DateTimeFormatter.ISO_DATE) ?: ""

    fun formatDateTime(): String {
        return try {
            createdAt?.format(DateTimeFormatter.ISO_DATE_TIME) ?: ""
        } catch (_: Exception){
            ""
        }

    }
}
