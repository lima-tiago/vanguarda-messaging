package br.com.tiago.vanguarda_messaging.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tiago.vanguarda_messaging.MessageViewModel
import br.com.tiago.vanguarda_messaging.chatScreen.ChatActivity
import br.com.tiago.vanguarda_messaging.chatScreen.SignsViewModel
import br.com.tiago.vanguarda_messaging.data.Message
import br.com.tiago.vanguarda_messaging.network.request.TranslateBody
import br.com.tiago.vanguarda_messaging.ui.theme.pink
import br.com.tiago.vanguarda_messaging.ui.theme.yellow
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.launch

@Composable
fun VChatView(
    activity: ChatActivity,
    signsViewModel: SignsViewModel = hiltViewModel(),
    messagesViewModel: MessageViewModel = hiltViewModel()
) {
//    val signs by remember { signsViewModel.translated }
    val loadError by remember { signsViewModel.loadError }
    val isLoading by remember { signsViewModel.isLoading }

    val messages by remember { messagesViewModel.message }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            MyTopAppBar()
        },
        content = {
            Column() {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f),
                ) {
                    items(items = messages) { message ->
                        Spacer(modifier = Modifier.height(8.dp))
                        if (message.isSent) {
                            TextBubble(message)
                        } else {
                            ImagesBubble(message)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (messages.lastIndex == messages.indexOf(message))
                            coroutineScope.launch {

                                listState.animateScrollToItem(messages.lastIndex, 0)
                            }
                    }
                }

                MessageBox(
                    onAddItem = {
                        messagesViewModel.addMessage(it)
                    },
                    signsViewModel,
                    activity,
                    isLoading
                )
            }
        }
    )
}

@Composable
fun MyTopAppBar() {
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text("VanguardaMessaging") },
        elevation = 0.dp,
        actions = {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(onClick = {}) {
                    Text(text = "Alfabeto")
                }
                DropdownMenuItem(onClick = {}) {
                    Text(text = "Settings")
                }
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }

            IconButton(onClick = { showMenu = showMenu != true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }
    )
}

@Composable
fun MessageBox(
    onAddItem: (Message) -> Unit,
    viewModel: SignsViewModel,
    activity: ChatActivity,
    isLoading: Boolean
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val textStyleBody1 = MaterialTheme.typography.body1
    var isfirstclick = true
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            maxLines = 3,
            textStyle = textStyleBody1,
            value = textState.value,
            modifier = Modifier
                .height(60.dp)
                .weight(1f)
                .padding(start = 5.dp),
            shape = RoundedCornerShape(30.dp),
            onValueChange = {
                textState.value = it
            })
        Spacer(modifier = Modifier.size(12.dp))

        FloatingActionButton(
            onClick = {
                if (textState.value.text.isNotEmpty()) {
                    onAddItem(Message(textState.value.text, true))
                    var gamb = 0
                    viewModel.translateAddMessage(TranslateBody(textState.value.text))
                    if (!isLoading) {
                        viewModel.translated.observe(
                            activity
                        ) {
                            if (isfirstclick) isfirstclick = false
                            else {
                                if (gamb == 0) {
                                    onAddItem(Message("", false, it))
                                    gamb++
                                }
                            }
                        }
                    }

                    textState.value = TextFieldValue("")
                }
            },
            modifier = Modifier.size(60.dp)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
        }
    }
}

@Composable
fun TextBubble(message: Message) {
    Box(
        modifier = Modifier
            .padding(start=10.dp)
            .background(color = pink)
            .clip(RoundedCornerShape(8.dp)),
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column() {
                Text(
                    text = message.message,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ImagesBubble(
    message: Message
) {
    Box(
        modifier = Modifier
            .padding(end=10.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Column() {
                message.signs?.forEach { word ->
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        word.forEach { sign ->
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                            ) {
                                Image(
                                    modifier = Modifier.size(50.dp),
                                    contentScale = ContentScale.Fit,
                                    painter = rememberCoilPainter(request = sign.url),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}