package com.hfad.antiplag.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Patterns

// Sealed классы для типизированных состояний
sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

class LoginSigninViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    // Основные поля
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // Состояния UI
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _dialogMessage = MutableStateFlow("")
    val dialogMessage: StateFlow<String> = _dialogMessage.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Единое состояние авторизации
    private val _authState = MutableStateFlow<AuthResult>(AuthResult.Success)
    val authState: StateFlow<AuthResult> = _authState.asStateFlow()

    // Инициализация Google Sign In
    fun initializeGoogleSignIn(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(com.hfad.antiplag.R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    // Запуск процесса входа через Google
    fun signInWithGoogle(launcher: ActivityResultLauncher<Intent>) {
        _isLoading.value = true
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    // Оптимизированная обработка результата входа через Google
    suspend fun handleGoogleSignInResult(data: Intent?): AuthResult {
        _authState.value = AuthResult.Loading
        _isLoading.value = true
        
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account)
            } else {
                val errorMessage = "Не удалось получить данные аккаунта Google"
                _authState.value = AuthResult.Error(errorMessage)
                _errorMessage.value = errorMessage
                _isLoading.value = false
                AuthResult.Error(errorMessage)
            }
        } catch (e: ApiException) {
            val errorMessage = "Ошибка входа через Google: ${e.statusCode}"
            Log.w("MyLog", "Google sign in failed", e)
            _authState.value = AuthResult.Error(errorMessage)
            _errorMessage.value = errorMessage
            _isLoading.value = false
            AuthResult.Error(errorMessage)
        }
    }
    
    // Обратная совместимость с callback'ами
    fun handleGoogleSignInResult(data: Intent?, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = handleGoogleSignInResult(data)
            onResult(result is AuthResult.Success)
        }
    }

    // Оптимизированная аутентификация в Firebase с учетными данными Google
    private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): AuthResult {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()

            if (result.user != null) {
                Log.d("MyLog", "signInWithGoogle: successful")
                _email.value = account.email ?: ""
                _authState.value = AuthResult.Success
                _errorMessage.value = ""
                _isLoading.value = false
                showStatusDialog("Успешный вход через Google")
                AuthResult.Success
            } else {
                val errorMessage = "Ошибка аутентификации через Google"
                _authState.value = AuthResult.Error(errorMessage)
                _isLoading.value = false
                AuthResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Ошибка аутентификации через Google"
            Log.d("MyLog", "signInWithGoogle: Failed", e)
            _authState.value = AuthResult.Error(errorMessage)
            _errorMessage.value = errorMessage
            _isLoading.value = false
            AuthResult.Error(errorMessage)
        }
    }
    
    // Обратная совместимость с callback'ами
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = firebaseAuthWithGoogle(account)
            onResult(result is AuthResult.Success)
        }
    }

    // Оптимизированные методы обновления полей с валидацией
    fun updateEmail(email: String) {
        _email.value = email
        _errorMessage.value = ""
        
        // Валидация в реальном времени
        val validation = validateEmail(email)
        if (validation is ValidationResult.Invalid) {
            _errorMessage.value = validation.message
        }
    }

    fun updatePassword(password: String) {
        _password.value = password
        _errorMessage.value = ""
        
        // Валидация в реальном времени
        val validation = validatePassword(password, false)
        if (validation is ValidationResult.Invalid) {
            _errorMessage.value = validation.message
        }
    }
    
    // Новый метод для проверки готовности к авторизации
    fun isReadyForAuth(): Boolean {
        val emailValidation = validateEmail(_email.value)
        val passwordValidation = validatePassword(_password.value, false)
        return emailValidation is ValidationResult.Valid && passwordValidation is ValidationResult.Valid
    }
    
    // Новый метод для проверки готовности к регистрации
    fun isReadyForRegistration(): Boolean {
        val emailValidation = validateEmail(_email.value)
        val passwordValidation = validatePassword(_password.value, true)
        return emailValidation is ValidationResult.Valid && passwordValidation is ValidationResult.Valid
    }

    fun showStatusDialog(message: String) {
        _dialogMessage.value = message
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    private fun setError(message: String) {
        _errorMessage.value = message
    }

    // Оптимизированный метод входа
    suspend fun logIn(): AuthResult {
        _authState.value = AuthResult.Loading
        _isLoading.value = true
        
        // Валидация данных
        val validation = validateCredentials(_email.value, _password.value, false)
        if (validation is ValidationResult.Invalid) {
            _authState.value = AuthResult.Error(validation.message)
            _errorMessage.value = validation.message
            _isLoading.value = false
            return AuthResult.Error(validation.message)
        }
        
        return try {
            val result = auth.signInWithEmailAndPassword(_email.value, _password.value).await()
            if (result.user != null) {
                Log.d("MyLog", "SignInUserWithEmail: successful")
                _authState.value = AuthResult.Success
                _errorMessage.value = ""
                _isLoading.value = false
                AuthResult.Success
            } else {
                _authState.value = AuthResult.Error("Ошибка входа")
                _isLoading.value = false
                AuthResult.Error("Ошибка входа")
            }
        } catch (e: FirebaseAuthException) {
            val errorMessage = getUserFriendlyError(e.errorCode)
            Log.d("MyLog", "SignInUserWithEmail: Failed", e)
            _authState.value = AuthResult.Error(errorMessage)
            _errorMessage.value = errorMessage
            _isLoading.value = false
            AuthResult.Error(errorMessage)
        }
    }
    
    // Обратная совместимость с callback'ами
    fun logIn(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = logIn()
            onResult(result is AuthResult.Success)
        }
    }

    // Оптимизированный метод регистрации
    suspend fun signUp(): AuthResult {
        _authState.value = AuthResult.Loading
        _isLoading.value = true
        
        // Валидация данных для регистрации
        val validation = validateCredentials(_email.value, _password.value, true)
        if (validation is ValidationResult.Invalid) {
            _authState.value = AuthResult.Error(validation.message)
            _errorMessage.value = validation.message
            _isLoading.value = false
            return AuthResult.Error(validation.message)
        }
        
        return try {
            val result = auth.createUserWithEmailAndPassword(_email.value, _password.value).await()
            if (result.user != null) {
                Log.d("MyLog", "createUserWithEmailAndPassword: successful")
                _authState.value = AuthResult.Success
                _errorMessage.value = ""
                _isLoading.value = false
                AuthResult.Success
            } else {
                _authState.value = AuthResult.Error("Ошибка регистрации")
                _isLoading.value = false
                AuthResult.Error("Ошибка регистрации")
            }
        } catch (e: FirebaseAuthException) {
            val errorMessage = getUserFriendlyError(e.errorCode)
            Log.d("MyLog", "createUserWithEmailAndPassword: Failed", e)
            _authState.value = AuthResult.Error(errorMessage)
            _errorMessage.value = errorMessage
            _isLoading.value = false
            AuthResult.Error(errorMessage)
        }
    }
    
    // Обратная совместимость с callback'ами
    fun signIn(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = signUp()
            onResult(result is AuthResult.Success)
        }
    }

    // Оптимизированная валидация данных
    private fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid("Email не может быть пустым")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult.Invalid("Введите корректный email адрес")
            else -> ValidationResult.Valid
        }
    }

    private fun validatePassword(password: String, isRegistration: Boolean = false): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("Пароль не может быть пустым")
            isRegistration && password.length < 6 -> ValidationResult.Invalid("Пароль должен содержать минимум 6 символов")
            else -> ValidationResult.Valid
        }
    }

    private fun validateCredentials(email: String, password: String, isRegistration: Boolean = false): ValidationResult {
        val emailValidation = validateEmail(email)
        if (emailValidation is ValidationResult.Invalid) return emailValidation
        
        val passwordValidation = validatePassword(password, isRegistration)
        if (passwordValidation is ValidationResult.Invalid) return passwordValidation
        
        return ValidationResult.Valid
    }

    private fun getUserFriendlyError(errorCode: String?): String {
        return when (errorCode) {
            "ERROR_INVALID_EMAIL" -> "Неверный формат email адреса"
            "ERROR_USER_NOT_FOUND" -> "Пользователь с таким email не найден"
            "ERROR_WRONG_PASSWORD" -> "Неверный пароль"
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Пользователь с таким email уже существует"
            "ERROR_WEAK_PASSWORD" -> "Пароль слишком слабый. Используйте не менее 6 символов"
            "ERROR_NETWORK_REQUEST_FAILED" -> "Ошибка сети. Проверьте подключение к интернету"
            "ERROR_TOO_MANY_REQUESTS" -> "Слишком много попыток. Попробуйте позже"
            "ERROR_USER_DISABLED" -> "Аккаунт заблокирован"
            "ERROR_OPERATION_NOT_ALLOWED" -> "Операция не разрешена"
            "ERROR_INVALID_CREDENTIAL" -> "Неверный email или пароль"
            null -> "Произошла неизвестная ошибка"
            else -> "Произошла ошибка: $errorCode"
        }
    }

    // Оптимизированные методы очистки и сброса состояния
    fun clearFields() {
        _email.value = ""
        _password.value = ""
        _errorMessage.value = ""
        _authState.value = AuthResult.Success
    }
    
    fun resetAuthState() {
        _authState.value = AuthResult.Success
        _errorMessage.value = ""
        _isLoading.value = false
    }
    
    // Метод для получения текущего состояния авторизации
    fun getCurrentAuthState(): AuthResult = _authState.value
    
    // Метод для проверки валидности email в реальном времени
    fun validateEmailRealtime(email: String): ValidationResult = validateEmail(email)
    
    // Метод для проверки валидности пароля в реальном времени
    fun validatePasswordRealtime(password: String, isRegistration: Boolean = false): ValidationResult = 
        validatePassword(password, isRegistration)
    fun signOut() {
        viewModelScope.launch {
            try {
                // Выход из Firebase
                auth.signOut()

                // Полная очистка Google Sign-In
                googleSignInClient.signOut().await()
                googleSignInClient.revokeAccess().await()

                // Очистка локальных данных
                _email.value = ""
                _password.value = ""
                _errorMessage.value = ""

                Log.d("MyLog", "User signed out successfully")
            } catch (e: Exception) {
                Log.e("MyLog", "Error during sign out", e)
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }

    // Оптимизированный метод удаления аккаунта
    suspend fun deleteAccount(): AuthResult {
        _authState.value = AuthResult.Loading
        _isLoading.value = true
        
        return try {
            val user = auth.currentUser
            if (user == null) {
                val errorMessage = "Пользователь не авторизован"
                _authState.value = AuthResult.Error(errorMessage)
                _errorMessage.value = errorMessage
                _isLoading.value = false
                return AuthResult.Error(errorMessage)
            }
            
            // Валидация данных для реавторизации
            val validation = validateCredentials(_email.value, _password.value, false)
            if (validation is ValidationResult.Invalid) {
                _authState.value = AuthResult.Error(validation.message)
                _errorMessage.value = validation.message
                _isLoading.value = false
                return AuthResult.Error(validation.message)
            }
            
            // Реавторизация
            val credential = EmailAuthProvider.getCredential(_email.value, _password.value)
            user.reauthenticate(credential).await()
            
            // Удаление аккаунта
            user.delete().await()
            
            Log.d("MyLog", "Account deleted successfully")
            _authState.value = AuthResult.Success
            _errorMessage.value = ""
            _isLoading.value = false
            AuthResult.Success
            
        } catch (e: FirebaseAuthException) {
            val errorMessage = getUserFriendlyError(e.errorCode)
            Log.d("MyLog", "Account deletion failed", e)
            _authState.value = AuthResult.Error(errorMessage)
            _errorMessage.value = errorMessage
            _isLoading.value = false
            AuthResult.Error(errorMessage)
        } catch (e: Exception) {
            val errorMessage = "Ошибка при удалении аккаунта"
            Log.d("MyLog", "Account deletion failed", e)
            _authState.value = AuthResult.Error(errorMessage)
            _errorMessage.value = errorMessage
            _isLoading.value = false
            AuthResult.Error(errorMessage)
        }
    }
    
    // Обратная совместимость с callback'ами
    fun signDelete(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = deleteAccount()
            onResult(result is AuthResult.Success)
        }
    }
}