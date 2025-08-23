package com.hfad.antiplag.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
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


    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(password: String) {
        _password.value = password
    }


    fun showStatusDialog(message: String) {
        _dialogMessage.value = message
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun logIn(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    // Обработка результата операции входа
                    if (task.isSuccessful) {
                        Log.d("MyLog", "SignInUserWithEmail: successful")
                        onResult(true)
                    } else {
                        Log.d("MyLog", "SignInUserWithEmail: Failed")
                    }

                }
        }
    }

    fun clearFields() {
        _email.value = ""
        _password.value = ""
    }


    fun signIn(onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener {  task ->
                    if (task.isSuccessful){
                        Log.d("MyLog", "createUserWithEmailAndPassword: successful")
                        onResult(true)
                    }else{
                        Log.d("MyLog", "createUserWithEmailAndPassword: Failed")
                        onResult(false)
                    }

                }
        }
    }

    fun signOut(){
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
                            onResult(false)
                        }
                    }
                } else {
                    onResult(false)
                }
            }
        }
    }


}