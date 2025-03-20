package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import models.Screen
import ui.components.AccountCardView
import ui.components.ScrollBar
import ui.theme.LocalDimens
import viewModels.MainViewModel

@Composable
fun AccountsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val accounts by viewModel.accounts
    val dimens = LocalDimens.current
    val scrollState = rememberLazyListState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)
    Box(modifier = modifier) {
        if (accounts.isNotEmpty()) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.padding(horizontal = dimens.mediumPadding).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)
            ) {
                items(accounts.sortedByDescending { it.createdAt }) { acc ->
                    AccountCardView(modifier = Modifier.fillMaxWidth(), account = acc) {
                        viewModel.setActiveAccount(acc)
                        viewModel.navigateTo(Screen.AccountDetails)
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