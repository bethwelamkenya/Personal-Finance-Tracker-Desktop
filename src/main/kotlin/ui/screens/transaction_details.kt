package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import models.Transaction
import ui.components.CustomButton
import ui.components.InfoRow
import ui.theme.LocalDimens
import viewModels.MainViewModel

@Composable
fun TransactionDetailsScreen(
    viewModel: MainViewModel,
    transaction: Transaction
) {
    val goals by viewModel.goals
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val dimens = LocalDimens.current
    val icon: ImageVector
    val color: Color
    when {
        transaction.type!!.contains("deposit", true) -> {
            icon = Icons.Default.ArrowDownward
            color = MaterialTheme.colorScheme.primary
        }

        transaction.type!!.contains("withdraw", true) -> {
            icon = Icons.Default.ArrowUpward
            color = MaterialTheme.colorScheme.error
        }

        else -> {
            icon = Icons.Default.SwapHoriz
            color = MaterialTheme.colorScheme.secondary
        }
    }
    AlertDialog(
        onDismissRequest = { viewModel.navigateBack() },
        title = { Text("Transaction Details") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.mediumPadding)
            ) {
                // Transaction Type and Date
                InfoRow(
                    title = "Type",
                    value = transaction.getTheTransactionType().name.replace("_", " "),
                    isLarge = true,
                    icon = icon,
                    tint = color,
                )
                transaction.accountNumber?.let {
                    InfoRow(
                        title = "Account",
                        value = it,
                        isLarge = true,
                        icon = Icons.Default.AccountBalanceWallet,
                    )
                }

                if (transaction.type!!.contains("goal", true)) {
                    val goal = goals.find { it.accountNumber == transaction.accountNumber }
                    goal?.let {
                        InfoRow(
                            title = "Goal",
                            value = it.goalName!!,
                            isLarge = true,
                            icon = Icons.Default.Router
                        )
                    }
                }

                // Transaction Details
                transaction.targetAccountNumber?.let {
                    InfoRow(
                        title = "To",
                        value = it,
                        isLarge = true,
                        icon = Icons.Default.Expand
                    )
                }
                transaction.targetUserEmail?.let {
                    InfoRow(
                        title = "User",
                        value = it,
                        isLarge = true,
                        icon = Icons.Default.PersonOutline
                    )
                }

                // Date
                InfoRow(
                    title = "Date",
                    value = transaction.formatDateTime(),
                    isLarge = true,
                    icon = Icons.Default.DateRange
                )

                // Amount
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = transaction.formatAmount(),
                    style = MaterialTheme.typography.displayLarge,
                    color = color
                )

                // Status Indicators
                successMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            CustomButton(
                text = "Delete",
                icon = if (isLoading) Icons.Default.LocalHospital else Icons.Default.Delete,
                isLoading = isLoading,
            ) {
                isLoading = true
                viewModel.deleteTransaction(transaction)
                isLoading = false
                errorMessage = null
                successMessage = "Transaction deleted successfully"
                viewModel.navigateBack()
                viewModel.removeActiveTransaction()
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.navigateBack() }) {
                Text("Close")
            }
        }
    )
}