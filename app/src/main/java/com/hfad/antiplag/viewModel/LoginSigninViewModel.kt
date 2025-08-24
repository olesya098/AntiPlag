package com.hfad.antiplag.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginSigninViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _dialogMessage = MutableStateFlow("")
    val dialogMessage: StateFlow<String> = _dialogMessage.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun updateEmail(email: String) {
        _email.value = email
        _errorMessage.value = ""
    }

    fun updatePassword(password: String) {
        _password.value = password
        _errorMessage.value = ""
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

    fun logIn(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (_email.value.isEmpty() || _password.value.isEmpty()) {
                setError("Все поля должны быть заполнены")
                onResult(false)
                return@launch
            }

            if (!isValidEmail(_email.value)) {
                setError("Введите корректный email адрес")
                onResult(false)
                return@launch
            }

            auth.signInWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("MyLog", "SignInUserWithEmail: successful")
                        _errorMessage.value = ""
                        onResult(true)
                    } else {
                        Log.d("MyLog", "SignInUserWithEmail: Failed")
                        // Получаем код ошибки из исключения
                        val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                        setError(getUserFriendlyError(errorCode))
                        onResult(false)
                    }
                }
        }
    }

    fun signIn(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (_email.value.isEmpty() || _password.value.isEmpty()) {
                setError("Все поля должны быть заполнены")
                onResult(false)
                return@launch
            }

            if (!isValidEmail(_email.value)) {
                setError("Введите корректный email адрес")
                onResult(false)
                return@launch
            }

            if (_password.value.length < 6) {
                setError("Пароль должен содержать минимум 6 символов")
                onResult(false)
                return@launch
            }

            auth.createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("MyLog", "createUserWithEmailAndPassword: successful")
                        _errorMessage.value = ""
                        onResult(true)
                    } else {
                        Log.d("MyLog", "createUserWithEmailAndPassword: Failed")
                        // Получаем код ошибки из исключения
                        val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                        setError(getUserFriendlyError(errorCode))
                        onResult(false)
                    }
                }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"
        return email.matches(emailRegex.toRegex())
    }

//    private fun isValidEmail(email: String): Boolean {
//        val emailRegex = "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@gmail\\.com\$"
//        return email.matches(emailRegex.toRegex())
//    }

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
            "ERROR_INVALID_CREDENTIAL" -> "Неверный email или пароль" // Добавьте эту строку
            null -> "Произошла неизвестная ошибка"
            else -> "Произошла ошибка: $errorCode"
        }
    }

    fun clearFields() {
        _email.value = ""
        _password.value = ""
        _errorMessage.value = ""
    }

    fun signOut() {
        viewModelScope.launch {
            auth.signOut()
        }
    }

    fun signDelete(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val credential = EmailAuthProvider.getCredential(_email.value, _password.value)
            auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    auth.currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                        if (deleteTask.isSuccessful) {
                            Log.d("MyLog", "Deleted")
                            onResult(true)
                        } else {
                            Log.d("MyLog", "Failed")
                            val errorCode = (deleteTask.exception as? FirebaseAuthException)?.errorCode
                            setError(getUserFriendlyError(errorCode))
                            onResult(false)
                        }
                    }
                } else {
                    val errorCode = (reauthTask.exception as? FirebaseAuthException)?.errorCode
                    setError(getUserFriendlyError(errorCode))
                    onResult(false)
                }
            }
        }
    }
}