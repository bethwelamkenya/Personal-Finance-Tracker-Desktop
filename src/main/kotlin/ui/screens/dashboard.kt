package ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import models.*
import ui.components.Loading
import ui.components.ScrollBar
import ui.theme.LocalDimens
import viewModels.MainViewModel
import viewModels.ViewModelStore

@Composable
fun DashBoard(
    viewModel: MainViewModel,
) {
    val dimens = LocalDimens.current
    val user by viewModel.user
    val accounts by viewModel.accounts
    val savings by viewModel.goals
    val transactions by viewModel.transactions
    val loadingStates by viewModel.loadingStates
    val scrollState = rememberScrollState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)

    Box {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(dimens.mediumPadding)
        ) {
            // Header Section
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Welcome to BeKa Personal Finance Tracker",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    user?.let {
                        Text(text = it.name!!, style = MaterialTheme.typography.bodyMedium)
                        Text(text = it.email!!, style = MaterialTheme.typography.bodySmall)
                    }
                }
                IconButton(onClick = { viewModel.refreshData() }) {
                    Icon(
                        Icons.Default.Refresh, contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.largePadding))

            // Bank Accounts Section
            SectionHeader(
                title = "Bank Accounts",
                icon = Icons.Default.AccountBalance,
                action = {
                    OutlinedButton(
                        onClick = { viewModel.addGeneratedBankAccount() }
                    ) {
                        Text("Add Account")
                        Icon(Icons.Default.Add, null)
                    }
                }
            )

            when {
                loadingStates.loadingAccounts || loadingStates.loadingAll -> Loading()
                accounts.isEmpty() -> EmptyState("No Bank Accounts Found")
                else -> LazyVerticalGrid(
                    modifier = Modifier.heightIn(max = dimens.breakpointDesktop), // Fix 2: Add height constraint
                    columns = GridCells.Adaptive(dimens.breakpointMobile / 2),
                    verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding),
                    horizontalArrangement = Arrangement.spacedBy(dimens.mediumPadding)
                ) {
                    items(accounts.sortedByDescending { it.createdAt }.take(6)) { account ->
                        BankAccountCard(
//                            modifier = Modifier
//                                .width(200.dp)
//                                .padding(end = 8.dp),
                            account = account,
                            onClick = {
                                viewModel.setActiveAccount(it)
                                viewModel.navigateTo(Screen.AccountDetails)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.largePadding))

            // Savings Goals Section
            SectionHeader(
                title = "Savings Goals",
                icon = Icons.Default.Savings,
                action = {
                    OutlinedButton(
                        onClick = { viewModel.addGeneratedSavingsGoal() }
                    ) {
                        Text("Add Goal")
                        Icon(Icons.Default.Add, null)
                    }
                }
            )

            when {
                loadingStates.loadingSavings || loadingStates.loadingAll -> Loading()
                savings.isEmpty() -> EmptyState("No Savings Goals Found")
                else -> LazyVerticalGrid(
                    modifier = Modifier.heightIn(max = dimens.breakpointDesktop / 2), // Fix 3: Constrain height
                    columns = GridCells.Adaptive(dimens.breakpointMobile / 2),
                    verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding),
                    horizontalArrangement = Arrangement.spacedBy(dimens.mediumPadding)
                ) {
                    items(savings.sortedByDescending { it.createdAt }.take(6)) { goal ->
                        SavingsGoalCard(
                            goal = goal,
                            onClick = {
                                viewModel.setActiveGoal(it)
                                viewModel.navigateTo(Screen.SavingsGoalDetails)
                            }
                        )
//                        SavingsGoalCard(goal)
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.largePadding))

            // Transactions Section
            SectionHeader(
                title = "Transaction History",
                icon = Icons.Default.SwapHoriz,
                action = {
                    OutlinedButton(
                        onClick = { viewModel.addGeneratedTransaction() },
                    ) {
                        Text("Create Transaction")
                        Icon(Icons.Default.Add, null)
                    }
                }
            )

            when {
                loadingStates.loadingTransactions || loadingStates.loadingAll -> Loading()
                transactions.isEmpty() -> EmptyState("No Transactions Found")
                else -> LazyColumn(
                    modifier = Modifier.heightIn(max = dimens.breakpointMobile), // Fix 3: Constrain height
                    verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)
                ) {
                    items(transactions.sortedByDescending { it.timestamp }.take(20)) { transaction ->
                        TransactionItem(transaction, onClick = {
                            viewModel.setActiveTransaction(it)
                            viewModel.navigateTo(Screen.TransactionDetails)
                        })
                    }
                }
            }
        }

        ScrollBar(modifier = Modifier.align(Alignment.CenterEnd), adapter = scrollbarAdapter)
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector, action: @Composable () -> Unit) {
    val dimens = LocalDimens.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimens.iconSize)
            )
            Spacer(modifier = Modifier.width(dimens.smallPadding))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        action()
    }
    Spacer(modifier = Modifier.height(dimens.mediumPadding))
}

@Composable
private fun BankAccountCard(modifier: Modifier = Modifier, account: BankAccount, onClick: (BankAccount) -> Unit) {
    val dimens = LocalDimens.current

    Card(
        onClick = { onClick(account) },
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(dimens.mediumPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(dimens.avatarSizeSmall)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        .padding(dimens.smallPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = account.bankName!!.first().uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(dimens.mediumPadding))
                Column {
                    Text(
                        text = account.bankName!!,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = account.accountNumber!!,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.mediumPadding))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = account.holderName!!,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = remember { account.formatBalance() },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Created: ${account.formatDate()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SavingsGoalCard(modifier: Modifier = Modifier, goal: SavingsGoal, onClick: (SavingsGoal) -> Unit) {
    val dimens = LocalDimens.current
    val progress = (goal.savedAmount!! / goal.targetAmount!!).toFloat().coerceIn(0f..1f)

    Card(
        modifier = modifier,
        onClick = { onClick(goal) }

    ) {
        Column(modifier = Modifier.padding(dimens.mediumPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Savings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(dimens.smallPadding))
                Text(
                    text = goal.goalName!!,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(dimens.smallPadding))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.smallPadding),
                color = if (progress >= 1f) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary,
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
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction, onClick: (Transaction) -> Unit) {
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
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {onClick(transaction)}
//            .border(2.dp, color, MaterialTheme.shapes.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimens.mediumPadding)
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.avatarSizeSmall)
                    .background(color.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color)
            }

            Spacer(modifier = Modifier.width(dimens.mediumPadding))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.type!!.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = transaction.formatDateTime(),
                    style = MaterialTheme.typography.labelSmall
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
                style = MaterialTheme.typography.titleMedium,
                color = color
            )
        }
    }
}

@Composable
private fun EmptyState(text: String) {
    val dimens = LocalDimens.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.extraLargePadding)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun HomePreview() {
    val viewModel = ViewModelStore.getViewModel<MainViewModel>()
    viewModel.setUser(
        User(
            name = "name",
            email = "email@email.email",
            passwordHash = "rdf6tdc5er"
        )
    )
    viewModel.refreshData()
    DashBoard(
        viewModel
    )
}