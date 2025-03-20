import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import models.Screen
import ui.*
import ui.screens.*
import ui.theme.AppTheme
import viewModels.MainViewModel
import viewModels.SettingsViewModel
import viewModels.ViewModelStore

@Composable
fun App(
    viewModel: MainViewModel,
    settingsViewModel: SettingsViewModel
) {
    val screen by viewModel.currentScreen
    when (screen) {
        Screen.Dashboard -> DashBoard(viewModel = viewModel)

        Screen.AccountDetails -> RequireAccount(
            viewModel = viewModel,
            content = {
                BankAccountDetailsScreen(
                    mainViewModel = viewModel,
                    account = it,
                )
            }
        )

        Screen.SavingsGoalDetails -> RequireGoal(
            viewModel = viewModel,
            content = {
                SavingsGoalDetailsScreen(
                    viewModel = viewModel,
                    goal = it,
                )
            }
        )

        Screen.TransactionDetails -> RequireTransaction(
            viewModel = viewModel,
            content = {
                TransactionDetailsScreen(
                    viewModel = viewModel,
                    transaction = it,
                )
            }
        )

        Screen.Accounts -> AccountsScreen(viewModel = viewModel)
        Screen.Transactions -> TransactionsScreen(viewModel = viewModel)
        Screen.Budgets -> BudgetsScreen()
        Screen.Reports -> ReportsScreen()
        Screen.Settings -> SettingsScreen(viewModel = viewModel, settingsViewModel = settingsViewModel)

        Screen.SavingsGoals -> SavingsScreen(viewModel = viewModel)
        Screen.Notifications -> {}
        Screen.Profile -> {
            ProfileScreen(viewModel = viewModel)
        }

        Screen.AddBankAccount -> AddBankAccountScreen(viewModel = viewModel)

        Screen.AddSavingsGoal -> AddSavingsGoalScreen(viewModel = viewModel)

        Screen.AddTransaction -> AddTransactionScreen(viewModel = viewModel)
        Screen.CreateNew -> {}
        else -> {}
    }
}

fun main() = application {
    // Set up global exception handler
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        println("Uncaught exception in thread $thread")
        ErrorLogger.logError(throwable)
    }
    // Handle window state
    val windowState = rememberWindowState(
        size = DpSize(1280.dp, 800.dp),
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center)
    )

    // Setup error handling for Compose
    DisposableEffect(Unit) {
        val originalHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            ErrorLogger.logError(throwable)
            originalHandler?.uncaughtException(thread, throwable)
        }

        onDispose {
            // Restore original handler when the composable is disposed
            Thread.setDefaultUncaughtExceptionHandler(originalHandler)
        }
    }

    val viewModel = ViewModelStore.getViewModel<MainViewModel>()

// Add this near the top of your main function
    val settingsViewModel = ViewModelStore.getViewModel<SettingsViewModel>()
    val uiTheme by settingsViewModel.appColorTheme
    val colorScheme by settingsViewModel.appColorScheme
    val compactLayout by settingsViewModel.compactLayout
    val defaultCurrency by settingsViewModel.defaultCurrency

    viewModel.setDefaultCurrency(defaultCurrency)

    Window(
        state = windowState,
        title = "Finance Tracker",
        icon = painterResource("/app_image.ico"),
        onCloseRequest = ::exitApplication,
        onKeyEvent = {
            if (it.isCtrlPressed && it.key == Key.N) {
                // Handle Ctrl+N shortcut
                viewModel.navigateTo(Screen.AddBankAccount)
                true
            } else {
                false
            }
        }
    ) {
        AppTheme(uiTheme = uiTheme, compactLayout = compactLayout, colorScheme = colorScheme) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val screen by viewModel.currentScreen
                when (screen) {
                    Screen.Login -> LoginScreen(viewModel = viewModel)

                    Screen.Signup -> SignupScreen(viewModel = viewModel)

                    else -> {
                        RequireUser(
                            viewModel = viewModel,
                            content = {
                                DesktopLayout(
                                    viewModel = viewModel,
                                    content = {
                                        App(
                                            viewModel = viewModel,
                                            settingsViewModel = settingsViewModel
                                        )
                                    }
                                )
                            }
                        )

                    }
                }
            }
        }
    }
}

