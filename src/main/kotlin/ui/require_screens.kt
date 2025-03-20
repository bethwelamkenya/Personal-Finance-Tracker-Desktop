package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import models.BankAccount
import models.SavingsGoal
import models.Transaction
import models.User
import viewModels.MainViewModel

@Composable
fun RequireUser(viewModel: MainViewModel, content: @Composable (User) -> Unit) {
    val user by viewModel.user

    if (user == null) {
        LaunchedEffect(Unit) {
            viewModel.navigateBack()
        }
        return
    }

    content(user!!)
}

@Composable
fun RequireAccount(viewModel: MainViewModel, content: @Composable (BankAccount) -> Unit) {
    val account by viewModel.activeAccount

    if (account == null) {
        LaunchedEffect(Unit) {
            viewModel.navigateBack()
        }
        return
    }

    content(account!!)
}

@Composable
fun RequireGoal(viewModel: MainViewModel, content: @Composable (SavingsGoal) -> Unit) {
    val goal by viewModel.activeGoal

    if (goal == null) {
        LaunchedEffect(Unit) {
            viewModel.navigateBack()
        }
        return
    }

    content(goal!!)
}

@Composable
fun RequireTransaction(viewModel: MainViewModel, content: @Composable (Transaction) -> Unit) {
    val transaction by viewModel.activeTransaction

    if (transaction == null) {
        LaunchedEffect(Unit) {
            viewModel.navigateBack()
        }
        return
    }

    content(transaction!!)
}
