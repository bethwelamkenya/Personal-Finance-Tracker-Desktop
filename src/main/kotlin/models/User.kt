package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class User(
    var id: String? = "",
    var name: String? = "",
    var email: String? = "",
    var passwordHash: String? = "",
    var salt: String? = "",
    val createdAt: LocalDate? = LocalDate.now()
) {
    fun formatDate() = createdAt?.format(DateTimeFormatter.ISO_DATE) ?: ""

    fun formatDateTime() = createdAt?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")) ?: ""
}


