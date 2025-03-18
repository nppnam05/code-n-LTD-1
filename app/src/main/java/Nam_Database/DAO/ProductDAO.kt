package Nam_Database.DAO

import Nam_Database.Table.Category
import Nam_Database.Table.Product
import androidx.room.*

@Dao
interface ProductDAO {
    @Insert
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM Products") // Xóa tất cả sản phẩm
    suspend fun deleteAllProducts()

    @Query("DELETE FROM Categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Int)

    @Query("SELECT * FROM Products WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product?

    @Query("SELECT * FROM Products WHERE categoryId = :categoryId")
    suspend fun getProductsByCategoryId(categoryId: Int): List<Product>

    @Query("SELECT * FROM Products")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM Products WHERE sellerId = :idUser")
    suspend fun getProductsWhoSeller(idUser: Int): List<Product>

    @Query("DELETE FROM Categories")
    suspend fun deleteAllCategories()
}