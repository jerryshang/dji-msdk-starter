package dev.tireless.moo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.tireless.moo.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
  vm: HomeViewModel,
  modifier: Modifier = Modifier,
) {
  val events by vm.events.collectAsState(initial = emptyList())

  val inMessages by vm.inMessages.collectAsState(initial = emptyList())
  val outMessages by vm.outMessages.collectAsState(initial = emptyList())

  var outMessage by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxSize()) {
    Text(
      text = "Moo~ ${vm.name}!",
      modifier = modifier,
    )
    Row(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
      MessagesInPanel(
        messages = inMessages,
        modifier = Modifier.weight(1f),
      )
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Messages OUT",
          modifier = modifier,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
          TextField(value = outMessage, onValueChange = { outMessage = it }, singleLine = true)
          Button(onClick = {
            vm.sendMessage(outMessage)
            outMessage = ""
          }) { Text(text = "Send") }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
          items(outMessages) {
            Text(text = it)
          }
        }
      }
    }
    EventsPanel(events = events, modifier = Modifier.weight(1.0f))
  }
}

@Composable
fun MessagesInPanel(
  messages: List<String>,
  modifier: Modifier = Modifier,
) {
  val listState = rememberLazyListState()

  val coroutineScope = rememberCoroutineScope()
  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      coroutineScope.launch {
        listState.animateScrollToItem(messages.lastIndex)
      }
    }
  }

  Column(modifier = modifier) {
    Text(
      text = "Messages IN",
      modifier = modifier,
    )
    LazyColumn(state = listState) {
      items(messages) {
        Text(text = it)
      }
    }
  }
}

@Composable
fun EventsPanel(
  events: List<String>,
  modifier: Modifier = Modifier,
) {
  val listState = rememberLazyListState()

  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(events.size) {
    if (events.isNotEmpty()) {
      coroutineScope.launch {
        listState.animateScrollToItem(events.lastIndex)
      }
    }
  }

  Column(modifier = modifier.fillMaxWidth()) {
    Text(text = "Events")
    LazyColumn(state = listState, modifier = Modifier.weight(1.0f)) {
      items(events) {
        Text(text = it)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  AppTheme {
    HomeScreen(
      HomeViewModel(
        eventStream = MutableSharedFlow(),
        inMessageStream = MutableSharedFlow(),
        outMessageStream = MutableSharedFlow(),
      ),
    )
  }
}
