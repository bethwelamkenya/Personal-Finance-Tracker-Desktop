package ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import models.BankAccount
import models.CurrencyType
import models.SavingsGoal
import ui.theme.LocalDimens

@Composable
fun ScrollBar(modifier: Modifier = Modifier, adapter: ScrollbarAdapter, changeColor: Boolean? = false) {
    val dimens = LocalDimens.current
    VerticalScrollbar(
        modifier = modifier,
        adapter = adapter,
        style = ScrollbarStyle(
            unhoverColor = if (changeColor == true) MaterialTheme.colorScheme.primary.copy(alpha = 0.35F) else MaterialTheme.colorScheme.surfaceVariant,
            hoverColor = MaterialTheme.colorScheme.primary,
            thickness = dimens.largePadding / 2,
            minimalHeight = dimens.mediumPadding,
            shape = MaterialTheme.shapes.small,
            hoverDurationMillis = 300
        )
    )
}

@Composable
fun HScrollBar(modifier: Modifier = Modifier, adapter: ScrollbarAdapter, changeColor: Boolean? = false) {
    val dimens = LocalDimens.current
    HorizontalScrollbar(
        modifier = modifier,
        adapter = adapter,
        style = ScrollbarStyle(
            unhoverColor = if (changeColor == true) MaterialTheme.colorScheme.primary.copy(alpha = 0.35F) else MaterialTheme.colorScheme.surfaceVariant,
            hoverColor = MaterialTheme.colorScheme.primary,
            thickness = dimens.mediumPadding,
            minimalHeight = dimens.mediumPadding,
            shape = MaterialTheme.shapes.small,
            hoverDurationMillis = 300
        )
    )
}

@Composable
fun AccountCardView(
    modifier: Modifier = Modifier,
    account: BankAccount,
    onAccountClick: () -> Unit
) {
    val dimens = LocalDimens.current
    Card(
        modifier = modifier.padding(dimens.mediumPadding),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = { onAccountClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(dimens.largePadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding)
        ) {
            // Bank Name & Currency
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Bank",
                        modifier = Modifier.size(dimens.iconSize)
                    )
                    Spacer(modifier = Modifier.width(dimens.smallPadding))
                    account.bankName?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                CurrencyChip(currency = account.getTheCurrency())
            }

            // Account Balance
            Text(
                text = "${account.getCurrencySymbol()}${account.balance}",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            // Account Details
            Column(verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)) {
                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.CreditCard,
                    title = "Account Number",
                    value = account.accountNumber ?: "N/A"
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Person,
                    title = "Account Holder",
                    value = account.holderName ?: "N/A"
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.DateRange,
                    title = "Created Date",
                    value = account.formatDate()
                )
            }
        }
    }

}

@Composable
fun CurrencyChip(modifier: Modifier = Modifier, currency: CurrencyType) {
    val dimens = LocalDimens.current
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = dimens.mediumPadding, vertical = dimens.smallPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.smallPadding)
        ) {
            Text(
                text = currency.symbol,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = currency.code.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    isLarge: Boolean = false,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
) {
    val dimens = LocalDimens.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(if (isLarge) dimens.avatarSizeSmall else dimens.largePadding),
            tint = tint
        )
        Spacer(modifier = Modifier.width(dimens.mediumPadding))
        Column {
            Text(
                text = title,
                style = if (isLarge) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = if (isLarge) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AccountCard(
    modifier: Modifier = Modifier,
    account: BankAccount,
    active: Boolean = false,
    onClick: (BankAccount) -> Unit
) {
    val dimensions = LocalDimens.current
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (active)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else
                MaterialTheme.colorScheme.surface
//                MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick(account) }
    ) {
        Column(
            modifier = Modifier.padding(dimensions.mediumPadding),
            verticalArrangement = Arrangement.spacedBy(dimensions.mediumPadding)
        ) {
            account.bankName?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            account.accountNumber?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            account.holderName?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Text(
                text = account.formatBalance(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            account.currency?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
fun SavingsCard(
    modifier: Modifier = Modifier,
    goal: SavingsGoal,
    active: Boolean = false,
    onClick: () -> Unit
) {
    val dimens = LocalDimens.current
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (active)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(dimens.mediumPadding)) {
            goal.goalName?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            goal.accountNumber?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Text(goal.targetAmount.toString(), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(dimens.smallPadding))
            Text(
                text = goal.formatSaved(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            goal.currency?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val dimens = LocalDimens.current
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.3f else 1f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "scale"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(dimens.buttonHeight)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                        )
                    ), CircleShape
                )
                .padding(dimens.mediumPadding),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.scale(scale),
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun CustomButton1(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val dimens = LocalDimens.current
    val interactionSource = remember { MutableInteractionSource() }
    val elevation by animateDpAsState(
        targetValue = when {
            !enabled -> dimens.noPadding
            interactionSource.collectIsPressedAsState().value -> dimens.smallPadding
            else -> dimens.smallPadding / 2
        },
        animationSpec = tween(200),
        label = "buttonElevation"
    )

    val buttonShape = ButtonDefaults.elevatedShape
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer
    )

    Surface(
        modifier = modifier
            .height(dimens.buttonHeight)
            .shadow(
                elevation = elevation,
                shape = buttonShape,
                spotColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
//                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
            .clip(buttonShape),
        color = Color.Transparent
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = buttonShape
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                    enabled = enabled,
                    onClick = onClick
                )
                .then(if (enabled) Modifier else Modifier.alpha(0.6f))
        ) {
            // Overlay for depth effect
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                        shape = buttonShape
                    )
            )

            // Content
            Row(
                modifier = Modifier.padding(horizontal = dimens.largePadding, vertical = dimens.mediumPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.largePadding)
            ) {
                if (isLoading) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val rotation = infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "loadingRotation"
                    )

                    val scale by animateFloatAsState(
                        targetValue = if (isLoading) 0.8f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "loadingScale"
                    )

                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
                        if (icon != null) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(dimens.extraLargePadding)
                                    .graphicsLayer {
                                        rotationZ = rotation.value
                                        scaleX = scale
                                        scaleY = scale
                                    }
                            )
                        } else {
                            Loading(large = false)
                        }

                        text?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                            )
                        }
                    }
                } else {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
                        text?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.size(dimens.extraLargePadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomButton2(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val dimens = LocalDimens.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val elevation by animateDpAsState(
        targetValue = when {
            !enabled -> dimens.noPadding
            isPressed -> dimens.smallPadding
            else -> dimens.smallPadding / 2
        },
        animationSpec = tween(200),
        label = "buttonElevation"
    )

    val buttonShape = ButtonDefaults.elevatedShape
    val isDark = isSystemInDarkTheme()

    // Use contrasting gradient colors based on the theme
    val gradientColors = if (isDark) {
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondaryContainer
        )
    }

    Surface(
        modifier = modifier
            .height(dimens.buttonHeight)
            .shadow(
                elevation = elevation,
                shape = buttonShape,
                spotColor = if (isDark) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
            )
            .clip(buttonShape)
            .border(width = dimens.borderWidth, color = MaterialTheme.colorScheme.outline, shape = buttonShape),
        color = Color.Transparent
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = buttonShape
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                    enabled = enabled,
                    onClick = onClick
                )
                .then(if (enabled) Modifier else Modifier.alpha(0.6f))
        ) {
            // Overlay for a subtle depth effect
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = if (isDark) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                        shape = buttonShape
                    )
            )

            // Button Content
            Row(
                modifier = Modifier.padding(
                    horizontal = dimens.largePadding,
                    vertical = dimens.mediumPadding
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.largePadding)
            ) {
                if (isLoading) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val rotation = infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "loadingRotation"
                    )

                    val scale by animateFloatAsState(
                        targetValue = if (isLoading) 0.8f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "loadingScale"
                    )

                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimary) {
                        if (icon != null) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(dimens.extraLargePadding)
                                    .graphicsLayer {
                                        rotationZ = rotation.value
                                        scaleX = scale
                                        scaleY = scale
                                    }
                            )
                        } else {
                            Loading(large = false)
                        }

                        text?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                            )
                        }
                    }
                } else {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimary) {
                        text?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.size(dimens.extraLargePadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val dimens = LocalDimens.current
    val interactionSource = remember { MutableInteractionSource() }
    val isDarkTheme = isSystemInDarkTheme()

    // Elevation animation
    val elevation by animateDpAsState(
        targetValue = when {
            !enabled -> dimens.noPadding
            interactionSource.collectIsPressedAsState().value -> dimens.smallPadding
            else -> dimens.smallPadding / 2
        },
        animationSpec = tween(200),
        label = "buttonElevation"
    )

    // Color scheme adjustments
    val containerColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.primary
    }

    val gradientColors = listOf(
        containerColor,
        if (isDarkTheme) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
    )

    // Shape and border
    val buttonShape = ButtonDefaults.outlinedShape
    val borderWidth = if (isDarkTheme) dimens.noPadding else dimens.borderWidth

    Surface(
        modifier = modifier
            .height(dimens.buttonHeight)
            .shadow(
                elevation = elevation,
                shape = buttonShape,
                ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                spotColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            ),
        color = Color.Transparent
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientColors,
                        startX = 0f,
                        endX = 1000f
                    ),
                    shape = buttonShape
                )
                .border(
                    width = borderWidth,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = buttonShape
                )
                .clip(buttonShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        bounded = true
                    ),
                    enabled = enabled,
                    onClick = onClick
                )
                .alpha(if (enabled) 1f else 0.6f)
        ) {
            // Interactive overlay
            val overlayAlpha by animateFloatAsState(
                targetValue = if (interactionSource.collectIsPressedAsState().value) 0.1f else 0f,
                label = "overlayAlpha"
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = overlayAlpha),
                        shape = buttonShape
                    )
            )

            // Content
            Row(
                modifier = Modifier.padding(horizontal = dimens.largePadding, vertical = dimens.mediumPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.mediumPadding)
            ) {
                if (isLoading) {
                    LoadingContent(icon = icon, text = text)
                } else {
                    StaticContent(icon = icon, text = text)
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(icon: ImageVector?, text: String?) {
    val dimens = LocalDimens.current
    val infiniteTransition = rememberInfiniteTransition()
    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loadingRotation"
    )

    val scale by animateFloatAsState(
        targetValue = 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "loadingScale"
    )

    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier
                    .size(dimens.iconSize)
                    .graphicsLayer {
                        rotationZ = rotation.value
                        scaleX = scale
                        scaleY = scale
                    }
            )
        } ?: run {
            Loading(large = false)
        }

        text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
            )
        }
    }
}

@Composable
private fun StaticContent(icon: ImageVector?, text: String?) {
    val dimens = LocalDimens.current

    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
        text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSize)
            )
        }
    }
}