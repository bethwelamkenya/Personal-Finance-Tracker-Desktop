package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import models.SavingsGoal
import ui.components.*
import viewModels.MainViewModel
import java.time.LocalDate

@Composable
fun AddSavingsGoalScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val accounts by viewModel.accounts
    var goalName by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var savedAmount by remember { mutableStateOf("") }
    val defaultCurrency by viewModel.defaultCurrency
    var currency by remember { mutableStateOf(defaultCurrency) }
    var accountNumber by remember { mutableStateOf(accounts[0].accountNumber!!) }

    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }
    fun createSavingsGoal() = SavingsGoal(
        goalName = goalName,
        accountNumber = accountNumber,
        targetAmount = targetAmount.toDoubleOrNull() ?: 0.0,
        savedAmount = savedAmount.ifBlank { "0" }.toDoubleOrNull() ?: 0.0,
        currency = currency.name,
        createdAt = LocalDate.now()
    )

    fun validateForm(): Boolean {
        val newErrors = mutableMapOf<String, String>()

        if (goalName.isBlank()) newErrors["goalName"] = "Goal name is required"
        if (accountNumber.isBlank()) newErrors["accountNumber"] = "Account number is required"

        targetAmount.toDoubleOrNull()?.let {
            if (it <= 0) newErrors["targetAmount"] = "Must be positive amount"
        } ?: run { newErrors["targetAmount"] = "Invalid amount" }

        savedAmount.ifBlank { "0" }.toDoubleOrNull()?.let {
            if (it < 0) newErrors["savedAmount"] = "Cannot be negative"
        } ?: run { newErrors["savedAmount"] = "Invalid amount" }

        errors = newErrors
        return newErrors.isEmpty()
    }

    AlertDialog(
        onDismissRequest = { viewModel.navigateBack() },
        title = {
            Text(
                "Create Savings Goal",
                style = MaterialTheme.typography.headlineLarge
            )
        },
        text = {
            Surface(
                modifier = modifier.width(500.dp),
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FinanceTextField(
                        value = goalName,
                        label = "Goal Name",
                        leadingIcon = Icons.AutoMirrored.Filled.Label,
                        error = errors["goalName"],
                        onValueChange = { goalName = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownSelector(
                        items = accounts,
                        selectedItem = accounts.find { it.accountNumber == accountNumber } ?: accounts[0],
                        label = "Linked Account Number",
                        leadingIcon = Icons.Default.Numbers,
                        error = errors["accountNumber"],
                        onItemSelected = { accountNumber = it.accountNumber!! },
                        modifier = Modifier.fillMaxWidth(),
                        itemToString = { it.accountNumber!! },
                    )

                    NumberTextField(
                        value = targetAmount,
                        label = "Target Amount",
                        leadingIcon = Icons.AutoMirrored.Filled.TrendingUp,
                        error = errors["targetAmount"],
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d*\$"))) targetAmount = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    NumberTextField(
                        value = savedAmount,
                        label = "Initial Saved Amount (optional)",
                        leadingIcon = Icons.Default.Savings,
                        error = errors["savedAmount"],
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d*\$"))) savedAmount = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    CurrencySelector(
                        currency = currency,
                        onCurrencySelected = { currency = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Currency",
                        leadingIcon = Icons.Default.CurrencyExchange,
                    )

                    errors["general"]?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    successMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            CustomButton(
                isLoading = isLoading,
                onClick = {
                    if (validateForm()) {
                        isLoading = true
                        viewModel.addSavingsGoal(createSavingsGoal()) { result ->
                            result.onSuccess {
                                successMessage = "Goal created successfully"
                                isLoading = false
                                viewModel.navigateBack()
                            }.onFailure { err ->
                                val newErrors = mutableMapOf<String, String>()
                                err.message?.let { message -> newErrors["general"] = message }
                                errors += newErrors
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.onKeyEvent {
                    if (it.key == Key.Enter) {
                        if (validateForm()) {
                            isLoading = true
                            viewModel.addSavingsGoal(createSavingsGoal()) { result ->
                                result.onSuccess {
                                    successMessage = "Goal created successfully"
                                    isLoading = false
                                    viewModel.navigateBack()
                                }.onFailure { err ->
                                    val newErrors = mutableMapOf<String, String>()
                                    err.message?.let { message -> newErrors["general"] = message }
                                    errors += newErrors
                                    isLoading = false
                                }
                            }
                        }
                        true
                    } else false
                },
                text = "Create Goal",
            )

        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.onKeyEvent {
                    if (it.key == Key.Escape) {
                        viewModel.navigateBack()
                        true
                    } else false
                }
            ) {
                Text("Cancel")
            }
        }
    )
}