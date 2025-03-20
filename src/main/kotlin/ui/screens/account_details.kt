package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import models.BankAccount
import models.SavingsGoal
import models.Transaction
import ui.components.AccountCardView
import ui.components.ScrollBar
import ui.theme.LocalDimens
import viewModels.MainViewModel

@Composable
fun BankAccountDetailsScreen(
    mainViewModel: MainViewModel,
    account: BankAccount
) {
    val dimens = LocalDimens.current
    var currentTab by remember { mutableStateOf(0) }
    val theSavingsGoals by mainViewModel.goals
    val theTransactions by mainViewModel.transactions
    val savingsGoals = theSavingsGoals.filter { it.accountNumber == account.accountNumber }.sortedBy { it.createdAt }
    val transactions =
        theTransactions.filter { it.accountNumber == account.accountNumber || it.targetAccountNumber == account.accountNumber }
            .sortedBy { it.timestamp }
    val scrollState = rememberScrollState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)

    Box {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth()
                .padding(dimens.mediumPadding)
        ) {
            account.let { acc ->
                // Account Overview Card

                AccountCardView(modifier = Modifier.fillMaxWidth(), account = acc) { }

                Spacer(modifier = Modifier.height(dimens.largePadding))

                // Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(dimens.largePadding))
                ) {
                    TabItem(
                        Modifier.weight(1f),
                        text = "Savings Goals (${savingsGoals.size})",
                        icon = Icons.Default.Savings,
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 }
                    )
                    TabItem(
                        Modifier.weight(1f),
                        text = "Transactions (${transactions.size})",
                        icon = Icons.AutoMirrored.Filled.ListAlt,
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 }
                    )
                }

                Spacer(modifier = Modifier.height(dimens.mediumPadding))

                when (currentTab) {
                    0 -> SavingsGoalsList(savingsGoals.sortedByDescending { it.createdAt })
                    1 -> TransactionsList(transactions.sortedByDescending { it.timestamp }.take(20))
                }
            }
        }

        ScrollBar(modifier = Modifier.align(Alignment.CenterEnd), adapter = scrollbarAdapter)
    }
}

@Composable
private fun DetailItem(modifier: Modifier, label: String, value: String, content: (@Composable () -> Unit)? = null) {
    val dimensions = LocalDimens.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.largePadding)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content?.invoke() ?: Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun TabItem(modifier: Modifier, text: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val dimens = LocalDimens.current
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(vertical = dimens.mediumPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(dimens.smallPadding))
            Text(
                text = text,
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SavingsGoalsList(goals: List<SavingsGoal>) {
    val dimens = LocalDimens.current
    LazyVerticalGrid(
        modifier = Modifier.heightIn(max = dimens.breakpointDesktop /2),
        columns = GridCells.Adaptive(dimens.breakpointMobile / 2),
        verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding),
        horizontalArrangement = Arrangement.spacedBy(dimens.mediumPadding)
    ) {
        items(goals) { goal ->
            SavingsGoalItem(goal)
        }
    }
}

@Composable
private fun SavingsGoalItem(goal: SavingsGoal) {
    val dimens = LocalDimens.current
    val progress = (goal.savedAmount!! / goal.targetAmount!!).toFloat().coerceIn(0f..1f)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(dimens.mediumPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Savings,
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimens.avatarSizeSmall)
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                        .padding(dimens.smallPadding),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(dimens.mediumPadding))
                Text(
                    text = goal.goalName!!,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(dimens.mediumPadding))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.smallPadding),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(dimens.smallPadding))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Saved: ${goal.formatSaved()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Target: ${goal.formatTarget()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Created: ${goal.formatDate()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = dimens.smallPadding)
            )
        }
    }
}

@Composable
private fun TransactionsList(transactions: List<Transaction>) {
    val dimens = LocalDimens.current
    LazyColumn(
        modifier = Modifier.heightIn(max = dimens.breakpointDesktop / 2),
        verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)
    ) {
        items(transactions) { transaction ->
            TransactionItem(transaction)
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    val dimens = LocalDimens.current
    val (icon, color) = when {
        transaction.type!!.contains("WITHDRAW", true) ->
            Icons.Default.ArrowUpward to MaterialTheme.colorScheme.error

        transaction.type!!.contains("TRANSFER", true) ->
            Icons.Default.SwapHoriz to MaterialTheme.colorScheme.secondary

        else -> Icons.Default.ArrowDownward to MaterialTheme.colorScheme.primary
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimens.mediumPadding)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(dimens.avatarSizeSmall)
                    .background(color.copy(alpha = 0.1f), CircleShape)
                    .padding(dimens.smallPadding),
                tint = color
            )

            Spacer(modifier = Modifier.width(dimens.mediumPadding))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.type!!.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = transaction.formatDateTime(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${
                    if (transaction.type!!.contains(
                            "withdraw",
                            true
                        )
                    ) "-" else "+"
                }${transaction.formatAmount()}",
                style = MaterialTheme.typography.bodyLarge,
                color = color
            )
        }
    }
}