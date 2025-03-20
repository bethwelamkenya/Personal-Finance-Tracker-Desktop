package viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import kotlinx.coroutines.launch
import models.User

class LoginViewModel : MyBaseViewModel() {
    private val _email: MutableState<String> = mutableStateOf("")
    val email: State<String> = _email

    private val _password: MutableState<String> = mutableStateOf("")
    val password: State<String> = _password

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage: MutableState<String?> = mutableStateOf(null)
    val errorMessage: State<String?> = _errorMessage

    private val _errors: MutableState<Map<String, String>> = mutableStateOf(mapOf())
    val errors: State<Map<String, String>> = _errors

    private val _successMessage: MutableState<String?> = mutableStateOf(null)
    val successMessage: State<String?> = _successMessage

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    private fun validateForm(): Boolean {
        val newErrors = mutableMapOf<String, String>().apply {
            if (!isValidEmail(_email.value)) put("email", "Invalid email address")
            if (_password.value.isEmpty()) put("password", "Invalid email address")
        }

        _errors.value = newErrors
        return newErrors.isEmpty()
    }

    fun login(onSuccess: (User) -> Unit) {
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
                _successMessage.value = "Login successful!"
                onSuccess(User(email = _email.value, passwordHash = _password.value))
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}
