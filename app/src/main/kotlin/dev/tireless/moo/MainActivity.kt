package dev.tireless.moo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.tireless.moo.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val app = application as MooApp
    val factory =
      object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
              eventStream = app.eventStream,
              inMessageStream = app.inMessageStream,
              outMessageStream = app.outMessageStream,
            ) as T
          }
          throw IllegalArgumentException("Unknown ViewModel class")
        }
      }
    setContent {
      AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          HomeScreen(
            vm = ViewModelProvider(this, factory)[HomeViewModel::class.java],
            modifier = Modifier.padding(innerPadding),
          )
        }
      }
    }
  }
}
