package Nam_Database.DAO
import Nam_Database.Table.OrderItem
import androidx.room.*

@Dao
interface OrderItemDAO {
    @Insert
    suspend fun insertOrderItem(orderItem: OrderItem)

    @Insert
    suspend fun insertOrderItems(orderItems: List<OrderItem>): List<Long>

    @Update
    suspend fun updateOrderItem(orderItem: OrderItem)

    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem)

    @Query("SELECT * FROM Order_Items")
    suspend fun getAllOrderItem(): MutableList<OrderItem>

    @Query("SELECT * FROM Order_Items WHERE orderId = :orderId")
    suspend fun getOrderItemsByOrderId(orderId: Int): List<OrderItem>

    @Query("SELECT * FROM Order_Items WHERE productId = :productId")
    suspend fun getOrderItemsByProductId(productId: Int): List<OrderItem>
}