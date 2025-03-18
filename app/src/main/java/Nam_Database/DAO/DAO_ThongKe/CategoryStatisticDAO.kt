package Nam_Database.DAO.DAO_ThongKe

import Nam_Database.Table.Table_ThongKe.CategoryStatistic
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryStatisticDAO {
    @Insert
    suspend fun insertCategoryStatistic(categoryStatistic: CategoryStatistic)

    @Query("DELETE FROM CategoryStatistic")
    suspend fun deleteAllCategoryStatistic()

    @Update
    suspend fun updateCategoryStatistic(categoryStatistic: CategoryStatistic)

    @Query("SELECT * FROM CategoryStatistic")
    suspend fun getAllCategoryStatistics(): List<CategoryStatistic>

    @Query("SELECT * FROM CategoryStatistic WHERE categoryId = :categoryId")
    suspend fun getCategoryStatisticById(categoryId: Int): CategoryStatistic?

    @Query("UPDATE CategoryStatistic SET revenueShare = :revenueShare WHERE id = :id")
    suspend fun updateRevenueShare(id: Long, revenueShare: Double)
}
