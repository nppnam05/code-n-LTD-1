package Nam_Database.DAO
import Nam_Database.Table.User
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM Users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM Users")
    suspend fun getAllUsers(): List<User>

    @Query("UPDATE Users SET passWord = :newPassword WHERE email = :Email")
    suspend fun updatePassword(Email: String, newPassword: String)

    @Query("UPDATE Users SET access = :newAccess, storeName = :newStoreName, address = :newAddress, categorie = :newCategorie WHERE id = :userId")
    suspend fun updateUserDetails(
        userId: Int,
        newAccess: String,
        newStoreName: String?,
        newAddress: String?,
        newCategorie: String?
    )
}