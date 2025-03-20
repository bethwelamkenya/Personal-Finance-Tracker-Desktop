package models

data class LoadingStates(
    val loadingAccounts: Boolean = false,
    val loadingSavings: Boolean = false,
    val loadingTransactions: Boolean = false,
    val loadingUser: Boolean = false,
    val loadingAll: Boolean = false
)