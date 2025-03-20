package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import viewModels.MainViewModel
import viewModels.SignupViewModel
import viewModels.ViewModelStore

@Composable
fun SignupScreen(
    viewModel: MainViewModel
) {
    val signupViewModel = ViewModelStore.getViewModel<SignupViewModel>()
    val dimens = LocalDimens.current
    val name by signupViewModel.name
    val email by signupViewModel.email
    val password by signupViewModel.password
    val confirmPassword by signupViewModel.rePassword
    val termsAccepted by signupViewModel.termsAccepted

    val errors by signupViewModel.errors
    val generalError by signupViewModel.errorMessage
    val successMessage by signupViewModel.successMessage
    val isLoading by signupViewModel.isLoading

    Scaffold(
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
                    .padding(dimens.smallPadding)
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimens.mediumPadding)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = dimens.mediumPadding)
                    )

                    generalError?.let {
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
                        value = name,
                        onValueChange = { signupViewModel.updateName(it) },
                        label = "Full Name",
                        error = errors["name"],
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = Icons.Default.Person,
                    )

                    FinanceTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = email,
                        onValueChange = { signupViewModel.updateEmail(it) },
                        label = "Email",
                        error = errors["email"],
                        leadingIcon = Icons.Default.Email,
                    )

                    PasswordTextField(
                        value = password,
                        onValueChange = { signupViewModel.updatePassword(it) },
                        label = "Password",
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        error = errors["password"],
                        leadingIcon = Icons.Default.Lock,
                    )

                    PasswordTextField(
                        value = confirmPassword,
                        onValueChange = { signupViewModel.updateRePassword(it) },
                        label = "Confirm Password",
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        error = errors["confirmPassword"],
                        leadingIcon = Icons.Default.Lock,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimens.smallPadding)
                    ) {
                        Checkbox(
                            checked = termsAccepted,
                            onCheckedChange = { signupViewModel.updateTermsAccepted(it) }
                        )
                        Text(
                            text = "I agree to the Terms & Conditions",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    errors["terms"]?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = dimens.smallPadding)
                        )
                    }

                    CustomButton(
                        onClick = {
                            signupViewModel.signup {
                                viewModel.setUser(it)
                                viewModel.refreshData()
                                viewModel.clearNavigationStack(Screen.Dashboard)
                            }
                        },
                        modifier = Modifier
                            .height(dimens.buttonHeight)
                            .fillMaxWidth(),
                        text = "Sign Up",
                        isLoading = isLoading,
                    )

                    Spacer(modifier = Modifier.size(dimens.mediumPadding))

                    ClickableText(
                        text = buildAnnotatedString {
                            append("Already have an account? ")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                append("Log in")
                            }
                        },
                        onClick = { viewModel.navigateTo(Screen.Login) }
                    )
                }
            }
        }
    }
}