package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import models.BankAccount
import models.SavingsGoal
import models.Transaction
import models.TransactionType
import ui.components.*
import ui.theme.LocalDimens
import viewModels.MainViewModel

@Composable
fun AddTransactionScreen(viewModel: MainViewModel) {
    val accounts by viewModel.accounts
    val goals by viewModel.goals
    val dimens = LocalDimens.current
    val currencyType by viewModel.defaultCurrency

    var type: TransactionType by remember { mutableStateOf(TransactionType.DEPOSIT) }
    var toUserAccounts by remember { mutableStateOf(listOf<BankAccount>()) }
    var toUserGoals by remember { mutableStateOf(emptyList<SavingsGoal>()) }

    var amount by remember { mutableStateOf("") }
    var account by remember { mutableStateOf(accounts.firstOrNull()) }
    val toAccounts by derivedStateOf {
        accounts.filter { it != account }
    }
    var toAccount by remember { mutableStateOf(toAccounts.firstOrNull()) }
    var goal by remember { mutableStateOf(goals.firstOrNull()) }
    val toGoals by derivedStateOf {
        goals.filter { it != goal }
    }
    var toGoal by remember { mutableStateOf(toGoals.firstOrNull()) }
    var recipientEmail by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isLoadingUser by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }
    val scrollState = rememberLazyListState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)

    LaunchedEffect(type) {
        if (type == TransactionType.TRANSFER_OUT_TO) {
            toUserAccounts = emptyList()
        }
        if (type == TransactionType.TRANSFER_GOAL_OUT_TO) {
            toUserGoals = emptyList()
        }
    }

    LaunchedEffect(toAccounts) {
        toAccount = toAccounts.firstOrNull()
    }

    LaunchedEffect(toGoals) {
        toGoal = toGoals.firstOrNull()
    }

    fun validateInputs(): Boolean {
        val newErrors = mutableMapOf<String, String>()

        if (amount.isBlank()) newErrors["amount"] = "Amount is required"
        if (account == null) newErrors["account"] = "Account number is required"
        if (type.name.contains("transfer", true)) {
            if (toAccount == null) newErrors["toAccount"] = "Target Account number is required"
        }
        if (type.name.contains("goal", true)) {
            if (goal == null) newErrors["goalName"] = "Goal Name is required"
            if (type.name.contains("transfer", true)) {
                if (toGoal == null) newErrors["toGoalName"] = "Target Goal Name is required"
            }
        }
        if (type.name.contains("to", true)) {
            if (!viewModel.isValidEmail(recipientEmail)) newErrors["recipientEmail"] = "Recipient Email is required"
        }
        errors = newErrors
        return errors.isEmpty()
    }

    fun createTransaction() = Transaction(
        accountNumber = account?.accountNumber,
        type = type.name,
        amount = amount.toDouble(),
        goalName = goal?.goalName,
        targetAccountNumber = toAccount?.accountNumber,
        targetGoalName = toGoal?.goalName,
        targetUserEmail = recipientEmail,
        currency = currencyType.code
    )

    AlertDialog(
        onDismissRequest = { viewModel.navigateBack() },
        title = {
            Text(
                text = "Financial Transactions",
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(dimens.largePadding),
                verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding, Alignment.CenterVertically)
            ) {
                // Transaction Type Selector
                LazyRow(
                    state = scrollState,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(TransactionType.entries.toTypedArray()) {
                        FilterChip(
                            selected = type == it,
                            onClick = { type = it },
                            label = { Text(it.name.replace('_', ' ')) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.2f
                                )
                            )
                        )
                    }
                }
                HScrollBar(
                    adapter = scrollbarAdapter
                )

                // Input Fields
                NumberTextField(
                    value = amount,
                    onValueChange = { amount = it.filter { c -> c.isDigit() || c == '.' } },
                    label = "Amount",
                    modifier = Modifier.fillMaxWidth(),
                    error = errors["amount"]
                )

                if (!type.name.contains("goal", true)) {
                    DropdownSelector(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Account",
                        items = accounts,
                        selectedItem = account,
                        onItemSelected = { account = it },
                        itemToString = {
                            it.accountNumber!!
                        },
                        error = errors["goalName"]
                    )
                } else {
                    DropdownSelector(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Goal Name",
                        items = goals,
                        selectedItem = goal,
                        onItemSelected = {
                            toAccount = accounts.find { acc -> acc.accountNumber == it.accountNumber }
                            goal = it
                        },
                        itemToString = {
                            it.goalName!!
                        },
                        error = errors["account"]
                    )
                }

                when (type) {
                    TransactionType.TRANSFER_OUT -> {
                        DropdownSelector(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Target Account",
                            items = toAccounts,
                            selectedItem = toAccount,
                            onItemSelected = { toAccount = it },
                            itemToString = {
                                it.accountNumber!!
                            },
                            error = errors["toAccount"]
                        )
                    }

                    TransactionType.TRANSFER_OUT_TO -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.smallPadding)
                        ) {
                            FinanceTextField(
                                modifier = Modifier.weight(0.8F),
                                value = recipientEmail,
                                onValueChange = { recipientEmail = it },
                                label = "Recipient Email",
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Email
                                ),
                                error = errors["recipientEmail"]
                            )
                            CustomButton(
                                modifier = Modifier.weight(0.2F),
                                icon = if (isLoadingUser) Icons.Default.Refresh else Icons.Default.Approval,
                                isLoading = isLoadingUser,
                            ) {
                                if (recipientEmail.isEmpty()) {
                                    return@CustomButton
                                }
                                isLoadingUser = true
                                viewModel.getAccountsForEmail(recipientEmail) { accountsResult ->
                                    accountsResult.onSuccess { accountsList ->
                                        errorMessage = null
                                        toUserAccounts = accountsList.toList()
                                        isLoadingUser = false
                                    }.onFailure { err ->
                                        errorMessage = err.message
                                        toUserAccounts = listOf()
                                        isLoadingUser = false
                                    }
                                }
                            }
                        }
                        if (toUserAccounts.isNotEmpty()) {
                            DropdownSelector(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Target Account",
                                items = toUserAccounts,
                                selectedItem = toAccount,
                                onItemSelected = { toAccount = it },
                                itemToString = {
                                    it.accountNumber!!
                                },
                                error = errors["toAccount"]
                            )
                        }
                    }

                    TransactionType.TRANSFER_GOAL_OUT -> {
                        DropdownSelector(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Target Goal",
                            items = toGoals,
                            selectedItem = toGoal,
                            onItemSelected = {
                                toAccount = accounts.find {acc-> acc.accountNumber == it.accountNumber }
                                toGoal = it
                            },
                            itemToString = {
                                it.goalName!!
                            },
                            error = errors["toGoalName"]
                        )
                    }

                    TransactionType.TRANSFER_GOAL_OUT_TO -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.smallPadding)
                        ) {
                            FinanceTextField(
                                modifier = Modifier.weight(0.8F),
                                value = recipientEmail,
                                onValueChange = { recipientEmail = it },
                                label = "Recipient Email",
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Email
                                ),
                                error = errors["recipientEmail"]
                            )
                            CustomButton(
                                modifier = Modifier.weight(0.2F),
                                icon = if (isLoadingUser) Icons.Default.Refresh else Icons.Default.Approval,
                                isLoading = isLoadingUser,
                                onClick = {
                                    if (recipientEmail.isEmpty()) {
                                        return@CustomButton
                                    }
                                    isLoadingUser = true
                                    viewModel.getGoalsForEmail(recipientEmail) { goalsResult ->
                                        goalsResult.onSuccess { goalList ->
                                            errorMessage = null
                                            toUserGoals = goalList.toList() // ✅ Update StateFlow
                                            isLoadingUser = false
                                        }.onFailure { err ->
                                            errorMessage = err.message
                                            toUserGoals = emptyList() // ✅ Clear list on failure
                                            isLoadingUser = false
                                        }
                                    }
                                }
                            )
                        }
                        if (toUserGoals.isNotEmpty()) {
                            DropdownSelector(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Target Goal",
                                items = toUserGoals,
                                selectedItem = toGoal,
                                onItemSelected = {
                                    toAccount = BankAccount(accountNumber = it.accountNumber)
                                    toGoal = it
                                },
                                itemToString = {
                                    it.goalName!!
                                },
                                error = errors["toGoalName"]
                            )
                        }
                    }

                    else -> {}
                }

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
                text = type.name.replace('_', ' '),
                icon = if (isLoading) Icons.Default.LocalHospital else Icons.Default.Check,
                isLoading = isLoading,
                onClick = {
                    if (validateInputs()) {
                        val transaction = createTransaction()
                        isLoading = true
                        viewModel.addTransaction(transaction) {
                            it.onSuccess {
                                isLoading = false
                                successMessage = "Deposit successful"
                                errorMessage = null
                                viewModel.navigateBack()
                            }.onFailure { err ->
                                successMessage = null
                                isLoading = false
                                errorMessage = "Deposit failed: ${err.message}"
                            }
                        }
                    }
                }
            )
        },
        dismissButton = {
            TextButton(onClick = { viewModel.navigateBack() }) {
                Text("Close")
            }
        }
    )
}