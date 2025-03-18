package Nam_Database.DAO
import Nam_Database.Table.Order
import androidx.room.*

@Dao
interface OrderDAO {
    @Insert
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("DELETE FROM Orders")
    suspend fun deleteAllOrders()

    @Query("SELECT * FROM Orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Int): Order?

    @Query("SELECT * FROM Orders WHERE userId = :userId")
    suspend fun getOrdersByUserId(userId: Int): List<Order>

    @Query("SELECT * FROM Orders")
    suspend fun getAllOrders(): List<Order>
}