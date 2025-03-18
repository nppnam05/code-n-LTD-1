package Nam_Database.Table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val userName: String,
    val passWord: String,
    val access: String,
    val created: String,
    val storeName: String? = null,
    val address: String? = null,
    val categorie: String? = null
)
