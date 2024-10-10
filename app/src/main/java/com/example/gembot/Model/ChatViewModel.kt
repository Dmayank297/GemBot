package com.example.gembot.Model


import androidx.compose.runtime.mutableStateListOf
import com.google.ai.client.generativeai.GenerativeModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gembot.Constant
import com.example.gembot.Data.MessageData
import com.google.ai.client.generativeai.type.content

import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    // Creating and storing a list of messages that we have send to model in data class
    val messageList by lazy {
        mutableStateListOf<MessageData>()
    }

    // It is used for model to implement in our project or viewmodel
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constant.apiKey
    )


    fun onMessageSend(message: String) {
        viewModelScope.launch {

            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) {text(it.message)}
                    }.toList()
                )

                // Adding message that we have ask to model in message List
                messageList.add(MessageData(message, "User"))
                // Adding message Typing such that user can know that model is working
                messageList.add(MessageData("Typing...", "Model"))

                val response = chat.sendMessage(message)
                // Removing the message Typing such that model response doesn't affect the user experience
                messageList.removeAt(messageList.lastIndex)
                // Getting the response of our message and storing it in message List
                messageList.add(MessageData(response.text.toString(), "Model"))
            } catch (e: Exception) {
                messageList.removeAt(messageList.lastIndex)
                messageList.add(MessageData("Something went wrong ${e.message.toString()}", "Model"))
            }


        }
    }
}