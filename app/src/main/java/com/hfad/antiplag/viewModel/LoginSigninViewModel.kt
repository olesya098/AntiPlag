package com.hfad.antiplag.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
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

class LoginSigninViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    // Основные поля
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isGoogleUser = MutableStateFlow(false)
    val isGoogleUser: StateFlow<Boolean> = _isGoogleUser.asStateFlow()

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

    // Обработка результата входа через Google (ИСПРАВЛЕННАЯ СИГНАТУРА)
    fun handleGoogleSignInResult(data: Intent?, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account, onResult)
                } else {
                    _errorMessage.value = "Не удалось получить данные аккаунта Google"
                    _isLoading.value = false
                    onResult(false)
                }
            } catch (e: ApiException) {
                Log.w("MyLog", "Google sign in failed", e)
                _errorMessage.value = "Ошибка входа через Google: ${e.statusCode}"
                _isLoading.value = false
                onResult(false)
            }
        }
    }

    // Аутентификация в Firebase с учетными данными Google
    private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount, onResult: (Boolean) -> Unit) {
        try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()

            if (result.user != null) {
                Log.d("MyLog", "signInWithGoogle: successful")
                _email.value = account.email ?: ""
                _errorMessage.value = ""
                _isLoading.value = false
                _isGoogleUser.value = true
                onResult(true)
            } else {
                _errorMessage.value = "Ошибка аутентификации через Google"
                _isLoading.value = false
                onResult(false)
            }
        } catch (e: Exception) {
            Log.d("MyLog", "signInWithGoogle: Failed", e)
            _errorMessage.value = "Ошибка аутентификации через Google"
            _isLoading.value = false
            onResult(false)
        }
    }

    // Методы обновления полей
    fun updateEmail(email: String) {
        _email.value = email
        _errorMessage.value = ""
    }

    fun updatePassword(password: String) {
        _password.value = password
        _errorMessage.value = ""
    }

    // Метод входа (ИСПРАВЛЕННАЯ СИГНАТУРА)
    fun logIn(context: Context, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            // Валидация данных
            if (_email.value.isBlank() || _password.value.isBlank()) {
                _errorMessage.value = "Заполните все поля"
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                _isLoading.value = false
                onResult(false)
                return@launch
            }

            try {
                val result = auth.signInWithEmailAndPassword(_email.value, _password.value).await()
                if (result.user != null) {
                    Log.d("MyLog", "SignInUserWithEmail: successful")
                    _errorMessage.value = ""
                    _isLoading.value = false
                    _isGoogleUser.value = false
                    Toast.makeText(context, "Успешный вход", Toast.LENGTH_SHORT).show()
                    onResult(true)
                } else {
                    _errorMessage.value = "Ошибка входа"
                    Toast.makeText(context, "Ошибка входа", Toast.LENGTH_SHORT).show()
                    _isLoading.value = false
                    onResult(false)
                }
            } catch (e: FirebaseAuthException) {
                val errorMessage = getUserFriendlyError(e.errorCode)
                Log.d("MyLog", "SignInUserWithEmail: Failed", e)
                _errorMessage.value = errorMessage
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                _isLoading.value = false
                onResult(false)
            }
        }
    }

    // Метод регистрации (ИСПРАВЛЕННАЯ СИГНАТУРА)
    fun signIn(context: Context, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            // Валидация данных
            if (_email.value.isBlank() || _password.value.isBlank()) {
                _errorMessage.value = "Заполните все поля"
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                _isLoading.value = false
                onResult(false)
                return@launch
            }

            if (_password.value.length < 6) {
                _errorMessage.value = "Пароль должен содержать минимум 6 символов"
                Toast.makeText(context, "Пароль должен содержать минимум 6 символов", Toast.LENGTH_SHORT).show()
                _isLoading.value = false
                onResult(false)
                return@launch
            }

            try {
                val result = auth.createUserWithEmailAndPassword(_email.value, _password.value).await()
                if (result.user != null) {
                    Log.d("MyLog", "createUserWithEmailAndPassword: successful")
                    _errorMessage.value = ""
                    _isLoading.value = false
                    Toast.makeText(context, "Пользователь зарегистрирован", Toast.LENGTH_SHORT).show()
                    onResult(true)
                } else {
                    _errorMessage.value = "Ошибка регистрации"
                    Toast.makeText(context, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                    _isLoading.value = false
                    onResult(false)
                }
            } catch (e: FirebaseAuthException) {
                val errorMessage = getUserFriendlyError(e.errorCode)
                Log.d("MyLog", "createUserWithEmailAndPassword: Failed", e)
                _errorMessage.value = errorMessage
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                _isLoading.value = false
                onResult(false)
            }
        }
    }

    private fun getUserFriendlyError(errorCode: String?): String {
        return when (errorCode) {
            "ERROR_INVALID_EMAIL" -> "Неверный формат email адреса"
            "ERROR_USER_NOT_FOUND" -> "Пользователь с таким email не найден"
            "ERROR_WRONG_PASSWORD" -> "Неверный пароль"
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Пользователь с таким email уже существует"
            "ERROR_WEAK_PASSWORD" -> "Пароль слишком слабый"
            "ERROR_NETWORK_REQUEST_FAILED" -> "Ошибка сети"
            "ERROR_TOO_MANY_REQUESTS" -> "Слишком много попыток"
            "ERROR_USER_DISABLED" -> "Аккаунт заблокирован"
            "ERROR_OPERATION_NOT_ALLOWED" -> "Операция не разрешена"
            "ERROR_INVALID_CREDENTIAL" -> "Неверный email или пароль"
            null -> "Произошла неизвестная ошибка"
            else -> "Произошла ошибка: $errorCode"
        }
    }

    // Методы очистки и сброса состояния
    fun clearFields() {
        _email.value = ""
        _password.value = ""
        _errorMessage.value = ""
    }
    fun signOut(context: Context, onResult: () -> Unit) {
        viewModelScope.launch {
            try {
                // Выход из Firebase
                auth.signOut()

                // Очистка локальных данных
                _email.value = ""
                _password.value = ""
                _errorMessage.value = ""
                _isGoogleUser.value = false

                // Безопасный выход из Google Sign-In с обработкой исключений
                try {
                    googleSignInClient.signOut().await()
                    Log.d("MyLog", "Google sign out successful")
                } catch (e: Exception) {
                    Log.w("MyLog", "Google sign out failed (may be already signed out)", e)
                    // Игнорируем ошибку выхода, так как пользователь может быть уже вышел
                }

                try {
                    googleSignInClient.revokeAccess().await()
                    Log.d("MyLog", "Google revoke access successful")
                } catch (e: Exception) {
                    Log.w("MyLog", "Google revoke access failed", e)
                    // Игнорируем ошибку отзыва доступа
                }

                Toast.makeText(context, "Вы вышли из системы", Toast.LENGTH_SHORT).show()
                Log.d("MyLog", "User signed out successfully")
                onResult()
            } catch (e: Exception) {
                Log.e("MyLog", "Error during sign out", e)
                // Даже при ошибке очищаем локальные данные и вызываем callback
                _email.value = ""
                _password.value = ""
                _errorMessage.value = ""
                _isGoogleUser.value = false
                Toast.makeText(context, "Вы вышли из системы", Toast.LENGTH_SHORT).show()
                onResult()
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

    // Метод удаления аккаунта (ИСПРАВЛЕННАЯ СИГНАТУРА)
    fun signDelete(context: Context, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val user = auth.currentUser
                if (user == null) {
                    _errorMessage.value = "Пользователь не авторизован"
                    Toast.makeText(context, "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
                    _isLoading.value = false
                    onResult(false)
                    return@launch
                }

                // Реавторизация
                val credential = EmailAuthProvider.getCredential(_email.value, _password.value)
                user.reauthenticate(credential).await()

                // Удаление аккаунта
                user.delete().await()

                Log.d("MyLog", "Account deleted successfully")
                _errorMessage.value = ""
                _isLoading.value = false
                Toast.makeText(context, "Аккаунт удален", Toast.LENGTH_SHORT).show()
                onResult(true)

            } catch (e: FirebaseAuthException) {
                val errorMessage = getUserFriendlyError(e.errorCode)
                Log.d("MyLog", "Account deletion failed", e)
                _errorMessage.value = errorMessage
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                _isLoading.value = false
                onResult(false)
            } catch (e: Exception) {
                val errorMessage = "Ошибка при удалении аккаунта"
                Log.d("MyLog", "Account deletion failed", e)
                _errorMessage.value = errorMessage
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                _isLoading.value = false
                onResult(false)
            }
        }
    }
}