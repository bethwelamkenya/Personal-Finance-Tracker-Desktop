package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import ui.components.CustomButton
import ui.components.FinanceTextField
import ui.theme.LocalDimens
import viewModels.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel
) {
    val dimens = LocalDimens.current

    // State for user profile data
    val vmUser by viewModel.user
    val user = vmUser!!
    var name by remember { mutableStateOf(user.name!!) }
    var email by remember { mutableStateOf(user.email!!) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    // State for edit mode
    var isEditMode by remember { mutableStateOf(false) }
    var isChangingPassword by remember { mutableStateOf(false) }

    // Error handling
    val errors = remember { mutableStateMapOf<String, String>() }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Loading state
    var isLoading by remember { mutableStateOf(false) }

    fun validatePassword(password: String): Boolean {
        val pattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$".toRegex()
        return pattern.matches(password)
    }

    // Form validation function
    fun validateForm(): Boolean {
        val newErrors = mutableMapOf<String, String>().apply {
            if (name.isEmpty()) put("name", "Name is required")
            if (!viewModel.isValidEmail(email)) put("email", "Email is required")
            if (currentPassword.isEmpty()) put("currentPassword", "Current password is required")
            if (currentPassword != user.passwordHash) put("currentPassword", "Current password is incorrect")
            if (newPassword.isEmpty()) put("newPassword", "New password is required")
            if (!validatePassword(newPassword)) put(
                "newPassword",
                "Must be 8+ chars with 1 uppercase and 1 number"
            )
            if (confirmNewPassword.isEmpty()) put("confirmNewPassword", "Confirm new password is required")
            if (newPassword != confirmNewPassword) put("confirmNewPassword", "Passwords do not match")
        }
        errors.clear()
        errors.putAll(newErrors)
        return errors.isEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimens.mediumPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile header
        Spacer(modifier = Modifier.height(dimens.largePadding))

        // Profile avatar placeholder
        Surface(
            modifier = Modifier.size(dimens.sideBarWidth / 2),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = name.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.mediumPadding))

        // User name display
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(dimens.largePadding))

        // Success message
        successMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = dimens.smallPadding)
            )
        }

        OutlinedButton(
            onClick = {
                isEditMode = true
            }
        ) {
            Text(text = "Edit Profile")
        }

        // Profile details section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.mediumPadding)
            ) {
                Text(
                    text = "Personal Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = dimens.smallPadding)
                )

                Spacer(modifier = Modifier.height(dimens.smallPadding))

                // Name field
                FinanceTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    readOnly = !isEditMode,
                    error = errors["name"]
                )

                Spacer(modifier = Modifier.height(dimens.smallPadding))

                // Email field
                FinanceTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    readOnly = !isEditMode,
                    error = errors["email"]
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.mediumPadding))

        // Password change section
        if (isEditMode) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimens.mediumPadding)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Change Password",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Switch(
                            checked = isChangingPassword,
                            onCheckedChange = { isChangingPassword = it }
                        )
                    }

                    if (isChangingPassword) {
                        Spacer(modifier = Modifier.height(dimens.smallPadding))

                        // Current password
                        FinanceTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = "Current Password",
                            visualTransformation = PasswordVisualTransformation(),
                            error = errors["currentPassword"]
                        )

                        Spacer(modifier = Modifier.height(dimens.smallPadding))

                        // New password
                        FinanceTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = "New Password",
                            visualTransformation = PasswordVisualTransformation(),
                            error = errors["newPassword"]
                        )

                        Spacer(modifier = Modifier.height(dimens.smallPadding))

                        // Confirm new password
                        FinanceTextField(
                            value = confirmNewPassword,
                            onValueChange = { confirmNewPassword = it },
                            label = "Confirm New Password",
                            visualTransformation = PasswordVisualTransformation(),
                            error = errors["confirmNewPassword"]
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(dimens.mediumPadding))

        // Action buttons
        if (isEditMode) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.smallPadding)
            ) {
                OutlinedButton(
                    onClick = {
                        isEditMode = false
                        isChangingPassword = false
                        // Reset form
                        currentPassword = ""
                        newPassword = ""
                        confirmNewPassword = ""
                        errors.clear()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                CustomButton(
                    onClick = {
                        // Validate and save profile changes
                        if (validateForm()) {
                            isLoading = true
                            // TODO: Implement actual profile update logic
                            viewModel.updateUser(
                                user.copy(
                                    name = name,
                                    email = email,
                                    passwordHash = newPassword
                                )
                            )
                            // Simulate API call
                            isLoading = false
                            successMessage = "Profile updated successfully!"
                            isEditMode = false
                            isChangingPassword = false
                            currentPassword = ""
                            newPassword = ""
                            confirmNewPassword = ""
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    text = "Save",
                    isLoading = isLoading,
                )
            }
        } else {
            // Logout button when not in edit mode
            OutlinedButton(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(dimens.largePadding))
    }
}

