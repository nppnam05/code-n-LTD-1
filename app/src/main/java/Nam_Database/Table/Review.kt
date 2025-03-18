package Nam_Database.Table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "Reviews",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val userId: Int,
    val rating: Int,
    val reviewText: String?,
    val createdAt: String
)
