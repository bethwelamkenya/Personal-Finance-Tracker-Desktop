package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import models.Screen
import ui.components.CustomButton
import ui.components.FinanceTextField
import ui.components.PasswordTextField
import ui.theme.LocalDimens
import viewModels.LoginViewModel
import viewModels.MainViewModel
import viewModels.ViewModelStore

@Composable
fun LoginScreen(
    viewModel: MainViewModel,
) {
    // Create a coroutine scope tied to the composition.
    val logInViewModel = ViewModelStore.getViewModel<LoginViewModel>()
    val dimens = LocalDimens.current
    val email by logInViewModel.email
    val password by logInViewModel.password
    var rememberMe by remember { mutableStateOf(false) }
    val isLoading by logInViewModel.isLoading
    val errorMessage by logInViewModel.errorMessage
    val errors by logInViewModel.errors
    val successMessage by logInViewModel.successMessage
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (viewModel.hasBack()) IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(dimens.iconSize)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.mediumPadding)
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimens.mediumPadding)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = dimens.mediumPadding)
                    )

                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = dimens.smallPadding)
                        )
                    }

                    successMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = dimens.smallPadding)
                        )
                    }

                    FinanceTextField(
                        value = email,
                        onValueChange = { logInViewModel.updateEmail(it) },
                        label = "Email",
                        modifier = Modifier.fillMaxWidth(),
                        error = errors["email"],
                        leadingIcon = Icons.Default.Email,
                    )

                    PasswordTextField(
                        value = password,
                        onValueChange = { logInViewModel.updatePassword(it) },
                        label = "Password",
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        error = errors["password"],
                        leadingIcon = Icons.Default.Lock
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimens.smallPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        )
                        Text(
                            text = "Remember me",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    CustomButton(
                        onClick = {
                            logInViewModel.login {
                                viewModel.setUser(user = it)
                                viewModel.refreshData()
                                viewModel.clearNavigationStack(Screen.Dashboard)
                            }
                        },
                        modifier = Modifier
                            .height(dimens.buttonHeight)
                            .fillMaxWidth(),
                        text = "Login",
                        isLoading = isLoading,
                    )

                    Spacer(modifier = Modifier.size(dimens.mediumPadding))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ClickableText(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append("Forgot password?")
                                }
                            },
                            onClick = { }
                        )

                        ClickableText(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append("Sign up")
                                }
                            },
                            onClick = { viewModel.navigateTo(Screen.Signup) }
                        )
                    }
                }
            }
        }
    }
}