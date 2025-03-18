package Nam_Database.Table.Table_ThongKe

import Nam_Database.Table.Category
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "CategoryStatistic",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,     // Liên kết với bảng Categories
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategoryStatistic(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val categoryId: Int,
    val categoryName: String,
    val totalSalesAmount: Int,      // số lượng sản phẩm đã bán được
    val totalRevenue: Double,       // doanh thu các sản phẩm trong danh mục
    val revenueShare: Double,       // tỷ lệ phần trăm doanh thu trong tổng doanh thu
    val numberOfProducts: Int       // Số lượng sản phẩm trong danh mục
)
