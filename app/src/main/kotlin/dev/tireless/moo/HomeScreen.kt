package dev.tireless.moo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.tireless.moo.ui.theme.AppTheme

@Composable
fun HomeScreen(
  vm: HomeViewModel,
  modifier: Modifier = Modifier,
) {
  Text(
    text = "Moo~ ${vm.name}!",
    modifier = modifier,
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  AppTheme {
    HomeScreen(HomeViewModel())
  }
}
