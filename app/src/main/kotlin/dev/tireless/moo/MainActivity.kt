package dev.tireless.moo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import dev.tireless.moo.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          HomeScreen(
            vm = ViewModelProvider(this)[HomeViewModel::class.java],
            modifier = Modifier.padding(innerPadding),
          )
        }
      }
    }
  }
}
