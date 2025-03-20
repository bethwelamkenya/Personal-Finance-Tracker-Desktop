package viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.*
import utils.DataSimulator

class MainViewModel : MyBaseViewModel() {
    private val _accounts: MutableState<List<BankAccount>> = mutableStateOf(listOf())
    val accounts: State<List<BankAccount>> = _accounts

    private val _goals: MutableState<List<SavingsGoal>> = mutableStateOf(listOf())
    val goals: State<List<SavingsGoal>> = _goals

    private val _transactions: MutableState<List<Transaction>> = mutableStateOf(listOf())
    val transactions: State<List<Transaction>> = _transactions

    private val _user: MutableState<User?> = mutableStateOf(null)
    val user: State<User?> = _user

    private val _activeAccount: MutableState<BankAccount?> = mutableStateOf(null)
    val activeAccount: State<BankAccount?> = _activeAccount

    private val _activeGoal: MutableState<SavingsGoal?> = mutableStateOf(null)
    val activeGoal: State<SavingsGoal?> = _activeGoal

    private val _activeTransaction: MutableState<Transaction?> = mutableStateOf(null)
    val activeTransaction: State<Transaction?> = _activeTransaction

    private val _defaultCurrency: MutableState<CurrencyType> = mutableStateOf(CurrencyType.USD)
    val defaultCurrency: State<CurrencyType> = _defaultCurrency

    // Use a mutableStateListOf to hold the navigation stack.
    private val _navStack: MutableState<List<Screen>> = mutableStateOf(listOf())

    val currentScreen: State<Screen> = derivedStateOf {
        _navStack.value.lastOrNull() ?: Screen.Login
    }

    private val _loadingStates: MutableState<LoadingStates> = mutableStateOf(LoadingStates())
    val loadingStates: State<LoadingStates> = _loadingStates

    init {
        // Initialize the stack if empty.
        if (_navStack.value.isEmpty()) {
            _navStack.value += Screen.Login
        }
    }

    // Navigation helper functions.
    fun navigateTo(screen: Screen) {
        _navStack.value += screen
    }

    fun navigateBack() {
        if (_navStack.value.size > 1) {
            _navStack.value -= _navStack.value.last()
        }
    }

    fun clearNavigationStack(screen: Screen) {
        _navStack.value = listOf(screen)
    }

    fun hasBack(): Boolean {
        return _navStack.value.size > 1
    }

    fun logout() {
        clearNavigationStack(Screen.Login)
        _user.value = null
    }

    fun refreshData() {
        generateData()
    }

    fun addAccount(account: BankAccount, onResult: (Result<BankAccount>) -> Unit) {
        getViewModelScope().launch {
            delay(1000)
            _accounts.value += account
            onResult(Result.success(account))
        }
    }

    fun addTransaction(transaction: Transaction, onResult: (Result<Transaction>) -> Unit) {
        getViewModelScope().launch {
            try {
                delay(1000)
                when (TransactionType.valueOf(transaction.type!!)) {
                    TransactionType.DEPOSIT -> {
                        _accounts.value = _accounts.value.map {
                            if (it.accountNumber == transaction.accountNumber) {
                                it.balance = it.balance!! + transaction.amount!!
                                it
                            } else it
                        }
                    }

                    TransactionType.WITHDRAW, TransactionType.TRANSFER_OUT_TO -> {
                        _accounts.value = _accounts.value.map {
                            if (it.accountNumber == transaction.accountNumber) {
                                if (it.balance!! < transaction.amount!!){
                                    onResult(Result.failure(Exception("Invalid balance to complete transaction")))
                                    return@launch
                                }
                                it.copy(
                                    balance = it.balance!! - transaction.amount!!
                                )
                            } else it
                        }
                    }

                    TransactionType.TRANSFER_OUT -> {
                        _accounts.value = _accounts.value.map {
                            when (it.accountNumber) {
                                transaction.accountNumber -> {
                                    if (it.balance!! < transaction.amount!!){
                                        onResult(Result.failure(Exception("Invalid balance to complete transaction")))
                                        return@launch
                                    }
                                    it.copy(
                                        balance = it.balance!! - transaction.amount!!
                                    )
                                }

                                transaction.targetAccountNumber -> {
                                    it.copy(
                                        balance = it.balance!! + transaction.amount!!
                                    )
                                }

                                else -> it
                            }
                        }
                    }

                    TransactionType.DEPOSIT_GOAL -> {
                        _accounts.value = _accounts.value.map {
                            if (it.accountNumber == transaction.accountNumber) {
                                if (it.balance!! < transaction.amount!!){
                                    onResult(Result.failure(Exception("Invalid balance to complete transaction")))
                                    return@launch
                                }
                                it.copy(
                                    balance = it.balance!! - transaction.amount!!
                                )
                            } else it
                        }
                        _goals.value = _goals.value.map {
                            if (it.goalName == transaction.goalName) {
                                it.copy(
                                    savedAmount = it.savedAmount!! + transaction.amount!!
                                )
                            } else it
                        }
                    }

                    TransactionType.WITHDRAW_GOAL -> {
                        _accounts.value = _accounts.value.map {
                            if (it.accountNumber == transaction.accountNumber) {
                                it.copy(
                                    balance = it.balance!! + transaction.amount!!
                                )
                            } else it
                        }
                        _goals.value = _goals.value.map {
                            if (it.goalName == transaction.goalName) {
                                if (it.savedAmount!! < transaction.amount!!){
                                    onResult(Result.failure(Exception("Invalid balance to complete transaction")))
                                    return@launch
                                }
                                it.copy(
                                    savedAmount = it.savedAmount!! - transaction.amount!!
                                )
                            } else it
                        }
                    }

                    TransactionType.TRANSFER_GOAL_OUT -> {
                        _goals.value = _goals.value.map {
                            when (it.goalName) {
                                transaction.goalName -> {
                                    if (it.savedAmount!! < transaction.amount!!){
                                        onResult(Result.failure(Exception("Invalid balance to complete transaction")))
                                        return@launch
                                    }
                                    it.copy(
                                        savedAmount = it.savedAmount!! - transaction.amount!!
                                    )
                                }

                                transaction.targetGoalName -> {
                                    it.copy(
                                        savedAmount = it.savedAmount!! + transaction.amount!!
                                    )
                                }

                                else -> it
                            }
                        }
                    }

                    TransactionType.TRANSFER_GOAL_OUT_TO -> {
                        _goals.value = _goals.value.map {
                            if (it.goalName == transaction.goalName) {
                                if (it.savedAmount!! < transaction.amount!!){
                                    onResult(Result.failure(Exception("Invalid balance to complete transaction")))
                                    return@launch
                                }
                                it.copy(
                                    savedAmount = it.savedAmount!! - transaction.amount!!
                                )
                            } else it
                        }
                    }
                }
                _transactions.value += transaction
                onResult(Result.success(transaction))
            } catch (e: Exception){
                onResult(Result.failure(e))
            }
        }
    }

    fun addSavingsGoal(goal: SavingsGoal, onResult: (Result<SavingsGoal>) -> Unit) {
        getViewModelScope().launch {
            delay(1000)
            _goals.value += goal
            onResult(Result.success(goal))
        }
    }

    fun addGeneratedTransaction() {
        _transactions.value += DataSimulator.generateTransactions(_accounts.value, _goals.value, 1)
    }

    fun addGeneratedSavingsGoal() {
        _goals.value += DataSimulator.generateSavingsGoals(_accounts.value, 1)
    }

    fun addGeneratedBankAccount() {
        _accounts.value += DataSimulator.generateBankAccounts(_defaultCurrency.value, 1)
    }

    fun setActiveAccount(account: BankAccount) {
        _activeAccount.value = account
    }

    fun removeActiveAccount() {
        _activeAccount.value = null
    }

    fun setActiveGoal(goal: SavingsGoal) {
        _activeGoal.value = goal
    }

    fun removeActiveGoal() {
        _activeGoal.value = null
    }

    fun deleteAccount(account: BankAccount) {
        _accounts.value = _accounts.value.filter { it != account }
    }

    fun deleteSavingsGoal(goal: SavingsGoal) {
        _goals.value = _goals.value.filter { it != goal }
    }

    fun deleteTransaction(transaction: Transaction) {
        _transactions.value = _transactions.value.filter { it != transaction }
    }

    fun setActiveTransaction(transaction: Transaction) {
        _activeTransaction.value = transaction
    }

    fun removeActiveTransaction() {
        _activeTransaction.value = null
    }

    fun setUser(user: User) {
        _user.value = user
    }

    fun updateUser(user: User) {
        _user.value = user
    }

    fun setDefaultCurrency(currency: CurrencyType) {
        _defaultCurrency.value = currency
    }

    private fun generateData() {
        getViewModelScope().launch {
            _loadingStates.value = _loadingStates.value.copy(
                loadingAccounts = true
            )
            _loadingStates.value = _loadingStates.value.copy(
                loadingSavings = true
            )
            _loadingStates.value = _loadingStates.value.copy(
                loadingTransactions = true
            )
            delay(500)
            _accounts.value = DataSimulator.generateBankAccounts(_defaultCurrency.value, 4)
            _loadingStates.value = _loadingStates.value.copy(
                loadingAccounts = false
            )
            delay(500)
            _goals.value = DataSimulator.generateSavingsGoals(_accounts.value, 3)
            _loadingStates.value = _loadingStates.value.copy(
                loadingSavings = false
            )
            delay(500)
            _transactions.value = DataSimulator.generateTransactions(_accounts.value, _goals.value, 10)
            _loadingStates.value = _loadingStates.value.copy(
                loadingTransactions = false
            )
        }
    }

    fun getAccountsForEmail(email: String, onResult: (Result<List<BankAccount>>) -> Unit) {
        if (!isValidEmail(email)) {
            onResult(Result.failure(Exception("Invalid Email Address")))
            return
        }
        getViewModelScope().launch {
            delay(1000)
            val generatedAccounts = DataSimulator.generateBankAccounts(_defaultCurrency.value, 4)
            onResult(Result.success(generatedAccounts))
        }
    }

    fun getGoalsForEmail(email: String, onResult: (Result<List<SavingsGoal>>) -> Unit) {
        if (!isValidEmail(email)) {
            onResult(Result.failure(Exception("Invalid Email Address")))
            return
        }
        getViewModelScope().launch {
            delay(1000)
            val generatedAccounts = DataSimulator.generateBankAccounts(_defaultCurrency.value, 4)
            val generatedGoals = DataSimulator.generateSavingsGoals(generatedAccounts, 3)
            onResult(Result.success(generatedGoals))
        }
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
