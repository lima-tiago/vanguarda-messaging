package br.com.tiago.vanguarda_messaging

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.com.tiago.vanguarda_messaging.data.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor():ViewModel() {

    private var messages = mutableStateOf<List<Message>>(listOf())
    val message: MutableState<List<Message>> = messages

    fun addMessage(message: Message) {
        messages.value = messages.value.plus(message)
    }

}