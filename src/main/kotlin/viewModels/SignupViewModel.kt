package viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import kotlinx.coroutines.launch
import models.User

class SignupViewModel : MyBaseViewModel() {
    private val _name: MutableState<String> = mutableStateOf("")
    val name: State<String> = _name

    private val _email: MutableState<String> = mutableStateOf("")
    val email: State<String> = _email

    private val _password: MutableState<String> = mutableStateOf("")
    val password: State<String> = _password

    private val _rePassword: MutableState<String> = mutableStateOf("")
    val rePassword: State<String> = _rePassword

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage: MutableState<String?> = mutableStateOf(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage: MutableState<String?> = mutableStateOf(null)
    val successMessage: State<String?> = _successMessage

    private val _termsAccepted: MutableState<Boolean> = mutableStateOf(false)
    val termsAccepted: State<Boolean> = _termsAccepted

    private val _errors: MutableState<Map<String, String>> = mutableStateOf(mapOf())
    val errors: State<Map<String, String>> = _errors

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun updateRePassword(rePassword: String) {
        _rePassword.value = rePassword
    }

    fun updateTermsAccepted(termsAccepted: Boolean) {
        _termsAccepted.value = termsAccepted
    }

    private fun validatePassword(password: String): Boolean {
        val pattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$".toRegex()
        return pattern.matches(password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    private fun validateForm(): Boolean {
        val newErrors = mutableMapOf<String, String>().apply {
            if (_name.value.isEmpty()) put("name", "Name is required")
            if (!isValidEmail(_email.value)) put("email", "Invalid email address")
            if (!validatePassword(_password.value)) put(
                "password",
                "Must be 8+ chars with 1 uppercase and 1 number"
            )
            if (_password.value != _rePassword.value) put(
                "confirmPassword",
                "Passwords don't match"
            )
            if (!_termsAccepted.value) put("terms", "You must accept the terms")
        }

        _errors.value = newErrors
        return newErrors.isEmpty()
    }

    fun signup(onSuccess: (User) -> Unit) {
        if (!validateForm()) return
        _isLoading.value = true
        _errorMessage.value = null

        getViewModelScope().launch {
            try {
                // Perform login logic here
                // For example: authRepository.login(email.value, password.value)

                // Simulate network delay
                kotlinx.coroutines.delay(1000)

                // If successful
                _isLoading.value = false
                _successMessage.value = "Signup successful!"
                onSuccess(
                    User(
                        name = _name.value,
                        email = _email.value,
                        passwordHash = _password.value
                    )
                )
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}
