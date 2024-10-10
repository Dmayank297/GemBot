package com.example.gembot.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme


import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Cyan

import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow

import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gembot.ChatViewModel
import com.example.gembot.Data.MessageData
import com.example.gembot.R
import com.example.gembot.ui.theme.ModelColor
import com.example.gembot.ui.theme.UserColor


@Composable
fun ChatBot(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(1f)
            .background(Black)
            ,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

            ChatHead()

            MessageList(modifier = Modifier
                .weight(1f),
                messageList = viewModel.messageList)

            MessageBox(onMessageSend = {
                // Send the message to viewModel
                viewModel.onMessageSend(it)},

            )


    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messageList: List<MessageData>
) {
    if (messageList.isEmpty()) {
        Column (
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        // TODO: Open message box
                    },
                painter = painterResource(id = R.drawable.baseline_message_24),
                contentDescription = "Icon",
                tint = White
            )
            Text(
                text = "Start a Conversation...",
                color = White,
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()) {
                MessageRow(messageData = it)
            }
        }
    }

    
}

@Composable
fun MessageRow(messageData: MessageData) {
    val isModel = messageData.role == "Model"

    Row {
        Box (
            modifier = Modifier.fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .align(
                        if (isModel) Alignment.BottomStart else Alignment.BottomEnd
                    )
                    .padding(
                        start = if (isModel) 8.dp else 64.dp,
                        end = if (isModel) 64.dp else 8.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    )
                    .clip(RoundedCornerShape(40f))
                    .background(if (isModel) ModelColor else UserColor)
                    .padding(16.dp)
            ) {
                SelectionContainer {
                    Column { // Use a Column to arrange Text and Icon
                        Text(
                            text = AnnotatedString(messageData.message),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                            color = White,
                            fontSize= MaterialTheme.typography.bodyLarge.fontSize,

                        )
                    }
                }

            }
        }

    }
}

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    onMessageSend: (String)-> Unit
) {
    var message by remember {mutableStateOf("")}
    val send = Icons.Default.Send

    Row (
        modifier = Modifier
            .padding(start = 8.dp).fillMaxWidth(1f),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center

    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            colors = OutlinedTextFieldDefaults.colors(White),
            value = message,
            label = {
                Text(text = "Your message here...",
                    color = White)
                    },
            onValueChange = { value ->
            message = value
        }
        )

        IconButton(
            modifier = Modifier.padding(bottom = 12.dp),
            onClick = {
            if (message.isNotEmpty()) {
                onMessageSend(message)
                message = ""
            }
        }
        ) {
            Icon(
                modifier = Modifier.fillMaxWidth(1f)
                    .padding(bottom = 4.dp),
                imageVector = send,
                contentDescription = "Send Message",
                tint = White
            )
        }
    }
}

@Composable
fun ChatHead() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkGray),
        contentAlignment = Alignment.TopStart
    ) {
        Text(modifier = Modifier
            .padding(12.dp)
            .align(Alignment.Center),
            text = "Gemini Bot",
            color = White,
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic
            )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    ChatBot(viewModel = ChatViewModel())
}