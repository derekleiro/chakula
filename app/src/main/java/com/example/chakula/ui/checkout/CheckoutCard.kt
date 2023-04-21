import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.chakula.ChakulaApplication
import com.example.chakula.R


@Composable
fun CheckoutCard(product: Product, removeFromCart: (Product) -> Unit) {

    Row(
        Modifier
            .clickable(onClick = { removeFromCart(product) })
    ) {
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
                text = stringResource(id = R.string.home_sale),
                style = MaterialTheme.typography.labelMedium
            )
            ProductName(product = product)
            ProductPrice(
                product = product,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        IconButton(onClick = {
            removeFromCart(product)
            Toast.makeText(ChakulaApplication.getContext(), "I have not yet implemented this feature", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = Icons.Filled.RemoveShoppingCart,
                contentDescription = stringResource(R.string.cd_more_actions)
            )
        }
    }
}
