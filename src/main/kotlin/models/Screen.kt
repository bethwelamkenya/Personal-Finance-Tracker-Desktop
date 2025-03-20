package models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val title: String,
    val icon: ImageVector
) {
    Dashboard("Dashboard", Icons.Default.Home),
    Accounts("Accounts", Icons.Default.AccountBalance),
    Transactions("Transactions", Icons.AutoMirrored.Filled.ListAlt),
    Settings("Settings", Icons.Default.Settings),
    Login("Login", Icons.AutoMirrored.Filled.Login),
    Signup("Signup", Icons.Default.PersonAdd),
    CreateNew("Create New", Icons.Default.GroupAdd),
    AccountDetails("Account Details", Icons.Default.AccountBalance),
    SavingsGoalDetails("Savings Goal Details", Icons.Default.Savings),
    TransactionDetails("Transaction Details", Icons.AutoMirrored.Filled.ListAlt),
    SavingsGoals("Savings Goals", Icons.Default.Savings),
    Budgets("Budgets", Icons.Default.Savings),
    Reports("Reports", Icons.Default.Analytics),
    Notifications("Notifications", Icons.Default.Notifications),
    Profile("Profile", Icons.Default.Person),
    AddBankAccount("Add Bank Account", Icons.Default.AccountBalance),
    AddSavingsGoal("Add Savings Goal", Icons.Default.Savings),
    AddTransaction("Add Transaction", Icons.Default.SwapHoriz),
    Logout("Logout", Icons.AutoMirrored.Filled.Logout)
}