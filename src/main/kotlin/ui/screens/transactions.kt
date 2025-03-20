package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import models.Screen
import models.Transaction
import ui.components.ScrollBar
import ui.theme.LocalDimens
import viewModels.MainViewModel

@Composable
fun TransactionsScreen(
    viewModel: MainViewModel
) {
    val dimens = LocalDimens.current
    val transactions by viewModel.transactions
    val scrollState = rememberLazyListState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)
    Box {
        // Accounts Overview
        if (transactions.isNotEmpty()) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(horizontal = dimens.mediumPadding)
                    .fillMaxWidth(),
            ) {
                items(transactions.sortedByDescending { it.timestamp }) { transaction ->
                    TransactionCardView(
                        modifier = Modifier.fillMaxWidth(),
                        transaction = transaction
                    ) {
                        viewModel.setActiveTransaction(transaction)
                        viewModel.navigateTo(Screen.TransactionDetails)
                    }
                }
            }
        } else {
            Text(
                "Not accounts found",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(horizontal = dimens.mediumPadding)
            )
        }
        ScrollBar(modifier = Modifier.align(Alignment.CenterEnd), adapter = scrollbarAdapter)
    }
}

@Composable
fun TransactionCardView(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: () -> Unit = {},
) {
    val dimens = LocalDimens.current
    val transactionType = transaction.getTheTransactionType()
    val currencySymbol = remember { transaction.getTheCurrency().symbol }

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
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimens.smallPadding),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationMedium),
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
            // Transaction Icon and Details
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon with background
                Box(
                    modifier = Modifier
                        .size(dimens.avatarSizeSmall)
                        .background(
                            color = color.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = transaction.type,
                        tint = color
                    )
                }

                Spacer(modifier = Modifier.width(dimens.mediumPadding))

                // Transaction Details
                Column(verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimens.smallPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = transactionType.name.replace("_", " "),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        transaction.accountNumber?.let {
                            Text(
                                text = " •••• ${it.takeLast(4)}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Target account/email if available
                    transaction.targetAccountNumber?.let {
                        Text(
                            text = "To: •••• ${it.takeLast(4)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                    transaction.targetUserEmail?.let {
                        Text(
                            text = "To: ${it.replaceBefore("@", "••••")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                    Text(
                        text = transaction.formatDateTime(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            // Amount Display
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = transaction.formatAmount(),
                    style = MaterialTheme.typography.titleMedium,
                    color = color,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Balance: ${currencySymbol}${"%,.2f".format(transaction.amount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}