package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class SavingsGoal(
    var id: String? = "",
    var accountNumber: String? = "",
    var goalName: String? = "",
    var targetAmount: Double? = 0.0,
    var savedAmount: Double? = 0.0,
    var currency: String? = CurrencyType.USD.code,
    val createdAt: LocalDate? = LocalDate.now()
) {
    fun getTheCurrency(): CurrencyType {
        return currency?.let { CurrencyType.find(it) } ?: CurrencyType.USD
    }

    fun getCurrencySymbol(): String {
        return getTheCurrency().symbol
    }

    fun formatSaved() = "${getCurrencySymbol()}${"%,.2f".format(savedAmount)}"
    fun formatTarget() = "${getCurrencySymbol()}${"%,.2f".format(targetAmount)}"

//    fun formatSaved() = "%.2f %s".format(savedAmount, getCurrencySymbol())
//    fun formatTarget() = "%.2f %s".format(targetAmount, getCurrencySymbol())

    fun formatDate() = createdAt?.format(DateTimeFormatter.ISO_DATE) ?: ""
    fun formatDateTime(): String {
        return try {
            createdAt?.format(DateTimeFormatter.ISO_DATE_TIME) ?: ""
        } catch (_: Exception){
            ""
        }

    }

}

