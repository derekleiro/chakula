data class Product(val id: Int, val name: String, val imageUrl: String, val price: Double)
data class Payment(val id: Int, val name: String)

data class ProductResponse(val status: String, val description: String, val data: List<Product>)
data class PaymentResponse(val status: String, val description: String, val data: List<Payment>)
data class Order(val date: String, val total: Double, val data: List<Product>, val payment: Payment)
