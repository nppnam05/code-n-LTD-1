package Nam_Database.DAO
import Nam_Database.Table.Category
import androidx.room.*

@Dao
interface CategoryDAO {
    @Insert
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM Categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): Category?

    @Query("SELECT * FROM Categories")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT id FROM Categories WHERE categoryName = :name")
    suspend fun getIdCategoryByName(name: String): Int
}