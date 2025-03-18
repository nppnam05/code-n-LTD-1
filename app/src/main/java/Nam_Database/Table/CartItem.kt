package Nam_Database.Table
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "CartItem",
    foreignKeys = [
        ForeignKey(
            entity = Cart::class,
            parentColumns = ["id"],
            childColumns = ["cartId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cartId: Int,              // ID của giỏ hàng mà sản phẩm thuộc về
    val productId: Int,           // ID của sản phẩm
    var quantity: Int,            // Số lượng sản phẩm
    val price: Double             // Giá của sản phẩm (có thể lưu giá tại thời điểm thêm vào giỏ hàng)
)
