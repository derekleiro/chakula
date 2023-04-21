import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chakula.ui.ChakulaDestinations
import com.example.chakula.ui.home.HomeUiState

@Composable
fun SuccessScreen(
    uiState: HomeUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier,)
{
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Success",
                style = MaterialTheme.typography.displayLarge,
                color = Color.Black,
                modifier = Modifier.padding(16.dp),
            )
            Text(
                text = "You have successfully made an order!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier.padding(16.dp),
            )
            Button(
                onClick = { navController.navigate(ChakulaDestinations.HOME_ROUTE) },
                Modifier
                    .fillMaxWidth().padding(horizontal = 25.dp)
            ) {
                Text(text = "Continue")
            }
        }
    }
}
