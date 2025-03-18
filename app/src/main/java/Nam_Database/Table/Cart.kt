package Nam_Database.Table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Cart",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Cart(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,              // ID của người dùng sở hữu giỏ hàng
    val createdAt: String,        // Ngày tạo giỏ hàng
    val status: String            // Trạng thái của giỏ hàng (e.g., "Pending", "Completed")
)
