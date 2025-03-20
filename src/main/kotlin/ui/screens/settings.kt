package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import ui.components.CurrencySelector
import ui.components.DropdownSelector
import ui.components.ScrollBar
import ui.theme.AppColorScheme
import ui.theme.AppColorTheme
import ui.theme.LocalDimens
import viewModels.MainViewModel
import viewModels.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val uiTheme by settingsViewModel.appColorTheme
    val colorScheme by settingsViewModel.appColorScheme
    val compactLayout by settingsViewModel.compactLayout
    val defaultCurrency by settingsViewModel.defaultCurrency
    val dimens = LocalDimens.current
    var analyticsEnabled by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)

    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(dimens.largePadding),
            verticalArrangement = Arrangement.spacedBy(dimens.largePadding)
        ) {
            // Appearance Section
            SettingsSection(title = "Appearance", icon = Icons.Default.Palette) {
                DropdownSelector(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Theme",
                    items = AppColorTheme.entries,
                    selectedItem = uiTheme,
                    onItemSelected = { settingsViewModel.setAppColorTheme(it) },
                    itemToString = { it.value }
                )

                DropdownSelector(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Color Scheme",
                    items = AppColorScheme.entries,
                    selectedItem = colorScheme,
                    onItemSelected = { settingsViewModel.setAppColorScheme(it) },
                    itemToString = { it.displayName }
                )
                SettingsSwitch(
                    label = "Compact Ui",
                    checked = compactLayout,
                    onCheckedChange = { settingsViewModel.setCompactLayout(it) }
                )

                CurrencySelector(
                    label = "Default Currency",
                    modifier = Modifier.fillMaxWidth(),
                    onCurrencySelected = {
                        settingsViewModel.setDefaultCurrency(it)
                        viewModel.setDefaultCurrency(it)
                    },
                    currency = defaultCurrency,
                )
            }

            // Data Management Section
            SettingsSection(title = "Data & Privacy", icon = Icons.Default.Security) {
                SettingsSwitch(
                    label = "Enable Analytics",
                    checked = analyticsEnabled,
                    onCheckedChange = { analyticsEnabled = it }
                )
                SettingsSwitch(
                    label = "Other Privacy Policy",
                    checked = analyticsEnabled,
                    onCheckedChange = { analyticsEnabled = it }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimens.smallPadding)
                ) {
                    SettingsButton(
                        text = "Export Data",
                        icon = Icons.Default.Download,
                        onClick = { /* Handle export */ }
                    )
                    SettingsButton(
                        text = "Import Data",
                        icon = Icons.Default.Upload,
                        onClick = { /* Handle import */ }
                    )
                }
            }

            // About Section
            SettingsSection(title = "About", icon = Icons.Default.Info) {
                Column(verticalArrangement = Arrangement.spacedBy(dimens.smallPadding)) {
                    SettingItem(
                        label = "Version",
                        value = "1.0.0"
                    )
                    SettingItem(
                        label = "License",
                        value = "MIT License"
                    )
                    SettingItem(
                        label = "Developer",
                        value = "Beka"
                    )
                }
            }
        }

        ScrollBar(modifier = Modifier.align(Alignment.CenterEnd), adapter = scrollbarAdapter)
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    val dimens = LocalDimens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(dimens.mediumPadding),
            verticalArrangement = Arrangement.spacedBy(dimens.mediumPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.smallPadding)
            ) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
                Text(title, style = MaterialTheme.typography.titleLarge)
            }
            content()
        }
    }
}

@Composable
private fun SettingsSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
        Icon(icon, null, modifier = Modifier.size(ButtonDefaults.IconSize))
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}

@Composable
private fun SettingItem(
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}