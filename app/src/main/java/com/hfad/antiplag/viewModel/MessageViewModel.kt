package com.hfad.antiplag.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.hfad.antiplag.presentation.components.message.Message
import com.hfad.antiplag.presentation.components.message.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class MessageViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private var messagesListener: ListenerRegistration? = null

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun initChat(userId: String) {
        messagesListener?.remove()
        messagesListener = db.collection("users")
            .document(userId)
            .collection("chat")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                snapshot?.documents?.let { documents ->
                    val messagesList = documents.mapNotNull { doc ->
                        doc.toObject(Message::class.java)
                    }
                    _messages.value = messagesList
                }
            }
    }

    fun addMessage(userId: String, text: String, type: MessageType = MessageType.SYSTEM) {
        viewModelScope.launch {
            try {
                val message = Message(
                    text = text,
                    timestamp = Date(),
                    type = type
                )

                db.collection("users")
                    .document(userId)
                    .collection("chat")
                    .document(message.id)
                    .set(message)
                    .addOnSuccessListener {
                        Log.d("MessageViewModel", "Message saved to Firestore: $text")
                    }
                    .addOnFailureListener { e ->
                        Log.e("MessageViewModel", "Error saving message: ${e.message}")
                    }
            } catch (e: Exception) {
                Log.e("MessageViewModel", "Exception saving message: ${e.message}")
            }
        }
    }

    // Локальное добавление сообщения (без Firestore)
    fun addLocalMessage(text: String, type: MessageType = MessageType.SYSTEM) {
        val message = Message(
            text = text,
            timestamp = Date(),
            type = type
        )
        _messages.value = _messages.value + message
    }

    override fun onCleared() {
        super.onCleared()
        messagesListener?.remove()
    }
}