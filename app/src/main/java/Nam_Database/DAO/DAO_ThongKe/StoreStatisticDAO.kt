package Nam_Database.DAO.DAO_ThongKe

import Nam_Database.Table.Table_ThongKe.StoreStatistic
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface StoreStatisticDAO {

    @Insert
    suspend fun insertStoreStatistic(storeStatistic: StoreStatistic)

    @Update
    suspend fun updateStoreStatistic(storeStatistic: StoreStatistic)

    @Query("SELECT * FROM StoreStatistic")
    suspend fun getAllStoreStatistics(): List<StoreStatistic>

    @Query("SELECT * FROM StoreStatistic WHERE storeId = :storeId")
    suspend fun getStoreStatisticById(storeId: Int): StoreStatistic?

    @Query("SELECT * FROM StoreStatistic ORDER BY recordedAt DESC LIMIT 2")
    suspend fun getLastTwoStoreStatistics(): List<StoreStatistic>?
}
