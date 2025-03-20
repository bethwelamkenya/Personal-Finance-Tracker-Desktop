package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import models.BankAccount
import ui.components.CurrencySelector
import ui.components.CustomButton
import ui.components.FinanceTextField
import ui.components.NumberTextField
import viewModels.MainViewModel
import java.time.LocalDate

@Composable
fun AddBankAccountScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var accountNumber by remember { mutableStateOf("") }
    var holderName by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    val defaultCurrency by viewModel.defaultCurrency
    var currency by remember { mutableStateOf(defaultCurrency) }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    var errors by remember { mutableStateOf(mapOf<String, String>()) }
    fun createAccount() = BankAccount(
        accountNumber = accountNumber,
        holderName = holderName,
        bankName = bankName,
        balance = balance.toDoubleOrNull() ?: 0.0,
        currency = currency.name,
        createdAt = LocalDate.now()
    )

    fun validateForm(): Boolean {
        val newErrors = mutableMapOf<String, String>()
        // Add validation logic

        if (holderName.isBlank()) newErrors["holderName"] = "Holder name is required"
        if (accountNumber.isBlank()) newErrors["accountNumber"] = "Account number is required"
        if (bankName.isBlank()) newErrors["bankName"] = "Bank name is required"

        balance.toDoubleOrNull()?.let {
            if (it <= 0) newErrors["balance"] = "Must be positive amount"
        } ?: run { newErrors["balance"] = "Invalid amount" }

        errors = newErrors
        return newErrors.isEmpty()
    }

    AlertDialog(
        onDismissRequest = { viewModel.navigateBack() },
        title = { Text("Create Bank Account") },
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
                        value = accountNumber,
                        label = "Account Number",
                        leadingIcon = Icons.Default.Numbers,
                        error = errors["accountNumber"],
                        onValueChange = { accountNumber = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    FinanceTextField(
                        value = holderName,
                        label = "Account Holder",
                        leadingIcon = Icons.Default.Person,
                        error = errors["holderName"],
                        onValueChange = { holderName = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    FinanceTextField(
                        value = bankName,
                        label = "Bank Name",
                        leadingIcon = Icons.Default.AccountBalance,
                        error = errors["bankName"],
                        onValueChange = { bankName = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    NumberTextField(
                        value = balance,
                        label = "Initial Balance",
                        leadingIcon = Icons.Default.AttachMoney,
                        error = errors["balance"],
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d*\$"))) balance = it
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
                        viewModel.addAccount(createAccount()) { result ->
                            result.onSuccess {
                                successMessage = "Account created successfully"
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
                            viewModel.addAccount(createAccount()) { result ->
                                result.onSuccess {
                                    successMessage = "Account created successfully"
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
                text = "Create Account"
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

