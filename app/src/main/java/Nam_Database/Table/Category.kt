package Nam_Database.Table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryName: String,
    val categoryDesc: String?,
    val parentId: Int?,  // Chỉ là trường tham chiếu mà không cần ForeignKey
    val isActive: Boolean
)

