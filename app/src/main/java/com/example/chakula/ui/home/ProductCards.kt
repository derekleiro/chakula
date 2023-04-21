import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.chakula.R


@Composable
fun ProductPrice(
    product: Product,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = StringBuilder().append("KES ").append(product.price).toString(),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ProductImage(product: Product, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(product.imageUrl),
        contentDescription = null, // decorative
        modifier = modifier
            .size(40.dp, 40.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun ProductName(product: Product) {
    Text(
        text = product.name,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
fun ProductCard(product: Product, addToCart: (Product) -> Unit) {

    Row(
        Modifier
            .clickable(onClick = { addToCart(product) })
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
        IconButton(onClick = { addToCart(product) }) {
            Icon(
                imageVector = Icons.Filled.AddShoppingCart,
                contentDescription = stringResource(R.string.cd_more_actions)
            )
        }
    }
}
