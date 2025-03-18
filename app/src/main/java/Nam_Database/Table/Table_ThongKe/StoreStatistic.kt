package Nam_Database.Table.Table_ThongKe

import Nam_Database.Table.User
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "StoreStatistic",
    foreignKeys = [
        ForeignKey(
            entity = User::class,        // Liên kết với bảng Users (người bán)
            parentColumns = ["id"],
            childColumns = ["storeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StoreStatistic(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val storeId: Int,
    val storeName: String,
    val totalRevenue: Double, // tổng doanh thu
    val totalVisits: Double, // tổng lượt kh ghé thăm
    val newVisits: Double, // lượt kh ghé thăm mới
    val purchaseRate: Double, // tỉ lệ mua hàng
    val totalProducts: Double, // tổng sản phẩm
    val responseRate: Double, // tỉ lệ phản hồi
    val recordedAt: String // ngày lưu
)
