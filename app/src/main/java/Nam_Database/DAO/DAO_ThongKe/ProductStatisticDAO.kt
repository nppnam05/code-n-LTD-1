package Nam_Database.DAO.DAO_ThongKe

import Nam_Database.Table.Table_ThongKe.CategoryStatistic
import Nam_Database.Table.Table_ThongKe.ProductStatistic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductStatisticDAO {

    @Insert
    suspend fun insertProductStatistic(productStatistic: ProductStatistic)

    @Update
    suspend fun updateProductStatistic(productStatistic: ProductStatistic)

    @Query("DELETE FROM ProductStatistic")
    suspend fun deleteAllProductStatistics()

    @Query("SELECT * FROM ProductStatistic")
    suspend fun getAllProductStatistics(): List<ProductStatistic>

    @Query("SELECT * FROM ProductStatistic WHERE categoryId = :productId")
    suspend fun getProductStatisticById(productId: Int): List<ProductStatistic>

    @Query("SELECT * FROM CategoryStatistic WHERE categoryId = :categoryId")
    suspend fun getCategoryStatisticById(categoryId: Int): CategoryStatistic?

    @Query("SELECT * FROM ProductStatistic WHERE productId = :productId")
    suspend fun getOneProductStatisticById(productId: Int): ProductStatistic

    @Query("SELECT * FROM CategoryStatistic WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryStatistic?
}
