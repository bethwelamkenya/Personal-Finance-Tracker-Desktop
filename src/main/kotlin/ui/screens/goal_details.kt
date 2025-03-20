package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import models.SavingsGoal
import models.Screen
import models.Transaction
import ui.components.QuickActionButton
import ui.theme.LocalDimens
import viewModels.MainViewModel

@Composable
fun SavingsGoalDetailsScreen(
    viewModel: MainViewModel,
    goal: SavingsGoal
) {
    val dimens = LocalDimens.current
    val transactions by viewModel.transactions
    val goalTransactions =
        transactions.filter { (it.accountNumber == goal.accountNumber && it.type!!.contains("goal", true)) }
            .sortedByDescending { it.timestamp }
    Column(verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding)) {
        SavingsCardView(goal = goal) {
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = dimens.smallPadding)
        ) {
            QuickActionButton(
                modifier = Modifier.weight(1f),
                label = "Edit Goal",
                icon = Icons.Default.Edit
            ) {
            }
            QuickActionButton(
                modifier = Modifier.weight(1f),
                label = "Delete Goal",
                icon = Icons.Default.Delete
            ) {
                viewModel.deleteSavingsGoal(goal)
                viewModel.navigateBack()
                viewModel.removeActiveGoal()
            }
            QuickActionButton(
                modifier = Modifier.weight(1f),
                label = "Deposit / Withdraw",
                icon = Icons.Default.BroadcastOnHome
            ) {
            }
        }

        Text(
            modifier = Modifier.padding(horizontal = dimens.mediumPadding),
            text = "Transactions",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        LazyColumn {
            items(goalTransactions.sortedByDescending { it.timestamp }.take(20)) { trans ->
                TransactionItem(
                    transaction = trans,
                    true,
                    onClick = {
                        viewModel.setActiveTransaction(it)
                        viewModel.navigateTo(Screen.TransactionDetails)
                    })
                Spacer(modifier = Modifier.height(dimens.smallPadding))
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, specific: Boolean = false, onClick: (Transaction) -> Unit) {
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
    Card(
        onClick = { onClick(transaction) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimens.smallPadding),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(dimens.mediumPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Transaction Type and Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = transaction.type,
                        tint = color,
                    )
                    if (!specific) {
                        Spacer(modifier = Modifier.width(dimens.smallPadding))
                        Text(
                            text = " •••• ${transaction.accountNumber!!.takeLast(4)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(dimens.smallPadding))
                    Text(
                        text = transaction.getTheTransactionType().name.replace("_", " "),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Transaction Details
                Row(horizontalArrangement = Arrangement.spacedBy(dimens.smallPadding)) {
                    transaction.targetAccountNumber?.let {
                        Text(
                            text = "To: •••• ${it.takeLast(4)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                    transaction.targetUserEmail?.let {
                        Text(
                            text = "User: ${it.replaceBefore("@", "••••")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }

                // Date
                Text(
                    text = transaction.formatDateTime(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            // Amount
            Text(
                text = transaction.formatAmount(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = color,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}