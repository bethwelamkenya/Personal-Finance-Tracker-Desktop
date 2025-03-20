package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import models.CurrencyType
import ui.theme.LocalDimens

@Composable
fun FinanceTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onIconClick: (() -> Unit)? = null,
) {
    val dimens = LocalDimens.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .heightIn(min = dimens.textFieldHeight),
        textStyle = MaterialTheme.typography.bodyMedium,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
        },
        supportingText = {
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = dimens.smallPadding)
                )
            }
        },
        isError = error != null,
        colors = OutlinedTextFieldDefaults.colors().copy(
            // Use outlinedTextFieldColors
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        shape = MaterialTheme.shapes.small,
        leadingIcon = leadingIcon?.let {
            {
                onIconClick?.let { click ->
                    IconButton(
                        onClick = click
                    ) {
                        Icon(it, contentDescription = label)
                    }
                } ?: Icon(it, contentDescription = label)
            }
        },
        trailingIcon = trailingIcon?.let {
            {
                onIconClick?.let { click ->
                    IconButton(
                        onClick = click
                    ) {
                        Icon(it, contentDescription = label)
                    }
                } ?: Icon(it, contentDescription = label)
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        enabled = enabled
    )
}

@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    error: String? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
) {
    FinanceTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        error = error,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        readOnly = readOnly,
        enabled = enabled
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: ImageVector? = null,
    leadingIcon: ImageVector? = null
) {
    FinanceTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        error = error,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun DateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    readOnly: Boolean = true,
) {
    FinanceTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        error = error,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        readOnly = readOnly,
        trailingIcon = Icons.Default.DateRange
    )
}

@Composable
fun CurrencySelector(
    modifier: Modifier = Modifier,
    currency: CurrencyType,
    onCurrencySelected: (CurrencyType) -> Unit,
    label: String,
    error: String? = null,
    readOnly: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        FinanceTextField(
            value = currency.fullName,
            onValueChange = { },
//            onValueChange = { onCurrencySelected(CurrencyType.find(it) ?: currency) },
            label = label,
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (isFocused && readOnly) expanded = true
                },
            error = error,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            readOnly = readOnly,
            onIconClick = { expanded = true },
            leadingIcon = leadingIcon,
            trailingIcon = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            },
        ) {
            CurrencyType.entries.forEach { currency ->
                DropdownMenuItem(
                    text = { Text("${currency.symbol}  ${currency.fullName}") },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

@Composable
fun <T> DropdownSelector(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    label: String,
    error: String? = null,
    readOnly: Boolean = true,
    leadingIcon: ImageVector? = null,
    itemToString: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(selectedItem?.let(itemToString) ?: "") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        FinanceTextField(
            value = text,
            onValueChange = { text = it },
            label = label,
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (isFocused && readOnly) expanded = true
                },
            error = error,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            readOnly = readOnly,
            onIconClick = { expanded = true },
            leadingIcon = leadingIcon,
            trailingIcon = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus() // Clear focus when closing menu
            },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemToString(item)) },
                    onClick = {
                        text = itemToString(item)
                        expanded = false
                        onItemSelected(item)
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

