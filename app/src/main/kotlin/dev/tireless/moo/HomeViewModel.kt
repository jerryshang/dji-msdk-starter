package dev.tireless.moo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

class HomeViewModel(
  eventStream: SharedFlow<String>,
  inMessageStream: SharedFlow<String>,
  private val outMessageStream: MutableSharedFlow<String>,
) : ViewModel() {
  val name = "MSDK"

  val events: Flow<List<String>> =
    eventStream.scan(listOf()) { acc, value -> acc + value }
  val inMessages: Flow<List<String>> =
    inMessageStream.scan(listOf()) { acc, value -> acc + value }
  val outMessages: Flow<List<String>> =
    outMessageStream.scan(listOf()) { acc, value -> acc + value }

  fun sendMessage(message: String) =
    viewModelScope.launch {
      outMessageStream.emit(message)
    }
}
