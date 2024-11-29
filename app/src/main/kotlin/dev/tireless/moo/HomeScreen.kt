package dev.tireless.moo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.tireless.moo.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableSharedFlow

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
    Row(
      modifier =
        Modifier
          .weight(1.0f)
          .fillMaxWidth(),
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Messages IN",
          modifier = modifier,
        )
        LazyColumn {
          items(inMessages) {
            Text(text = it)
          }
        }
      }
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
    Column(
      modifier =
        Modifier
          .weight(1.0f)
          .fillMaxWidth(),
    ) {
      Text(
        text = "Events",
        modifier = modifier,
      )
      LazyColumn {
        items(events) {
          Text(text = it)
        }
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
