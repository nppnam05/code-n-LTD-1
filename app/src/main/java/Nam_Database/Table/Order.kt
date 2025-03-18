package Nam_Database.Table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "Orders",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val totalPrice: Double,
    val orderDate: String = "",
    val shippingAddress: String?,
    val paymentMethod: String?,
    val sellerConfirmed: Boolean = false,
    val orderStatus: String = "Pending",
    val updatedAt: String
)
