package Nam_Database.Table.Table_ThongKe
import Nam_Database.Table.Category
import Nam_Database.Table.Product
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ProductStatistic",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductStatistic(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val productId: Int,
    val productName: String,
    val amount: Int, // số lượng sản phẩm đã bán
    val price: Double, // tổng tiền đã bán
    val categoryId: Int
)

