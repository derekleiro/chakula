import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.chakula.R


@Composable
fun OrderCard(product: Product) {

    Row {
        ProductImage(
            product = product,
            modifier = Modifier.padding(16.dp)
        )
        Column(
            Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.home_sold),
                style = MaterialTheme.typography.labelMedium
            )
            ProductName(product = product)
            ProductPrice(
                product = product,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}