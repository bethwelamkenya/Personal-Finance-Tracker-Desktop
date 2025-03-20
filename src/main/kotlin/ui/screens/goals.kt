package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import models.SavingsGoal
import models.Screen
import ui.components.ScrollBar
import ui.theme.LocalDimens
import viewModels.MainViewModel

@Composable
fun SavingsScreen(
    viewModel: MainViewModel
) {
    val goals by viewModel.goals
    val dimens = LocalDimens.current
    val scrollState = rememberLazyListState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)
    Box {
        // Accounts Overview
        if (goals.isNotEmpty()) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.padding(horizontal = dimens.mediumPadding).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)
            ) {
                items(goals.sortedByDescending { it.createdAt }) { goal ->
                    SavingsCardView(modifier = Modifier.fillMaxWidth(), goal = goal) {
                        viewModel.setActiveGoal(goal)
                        viewModel.navigateTo(Screen.SavingsGoalDetails)
                    }
                }
            }
        } else {
            Text("Not savings goals found", style = MaterialTheme.typography.displayMedium, modifier = Modifier.padding(horizontal = dimens.mediumPadding))
        }
        ScrollBar(modifier = Modifier.align(Alignment.CenterEnd), adapter = scrollbarAdapter)
    }
}

@Composable
fun SavingsCardView(
    modifier: Modifier = Modifier,
    goal: SavingsGoal,
    onClick: () -> Unit = {}
) {
    val dimens = LocalDimens.current
    val progress = remember(goal) {
        val calculatedProgress = if (goal.targetAmount == 0.0) {
            0f // Avoid division by zero
        } else {
            (goal.savedAmount!! / goal.targetAmount!!).toFloat()
        }
        calculatedProgress.coerceIn(0f, 1f)
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.mediumPadding, vertical = dimens.smallPadding),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationLow),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(dimens.mediumPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding)
        ) {
            // Header with Goal Name and Progress
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                goal.goalName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                ProgressChip(progress = progress)
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.smallPadding)
                    .clip(MaterialTheme.shapes.small),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
                strokeCap = StrokeCap.Butt,
            )

            // Amounts Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)) {
                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    goal.accountNumber?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)) {
                    Text(
                        text = "Saved",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = goal.formatSaved(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)) {
                    Text(
                        text = "Target",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = goal.formatTarget(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Creation Date
            Text(
                text = goal.formatDate(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun ProgressChip(progress: Float) {
    val dimens = LocalDimens.current
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = dimens.mediumPadding, vertical = dimens.smallPadding)
    ) {
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}