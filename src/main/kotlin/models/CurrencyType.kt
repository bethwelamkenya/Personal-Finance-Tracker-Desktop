package models

// Enum for major currencies with full names
enum class CurrencyType(val code: String, val symbol: String, val fullName: String) {
    USD("USD", "$", "United States Dollar"),
    EUR("EUR", "€", "Euro"),
    GBP("GBP", "£", "British Pound Sterling"),
    JPY("JPY", "¥", "Japanese Yen"),
    AUD("AUD", "A$", "Australian Dollar"),
    CAD("CAD", "C$", "Canadian Dollar"),
    CHF("CHF", "Fr.", "Swiss Franc"),
    CNY("CNY", "¥", "Chinese Yuan"),
    SEK("SEK", "kr", "Swedish Krona"),
    NZD("NZD", "NZ$", "New Zealand Dollar");

    companion object {
        fun find(code: String): CurrencyType {
            return entries.find { it.code == code || it.fullName == code } ?: USD
        }
    }
}
