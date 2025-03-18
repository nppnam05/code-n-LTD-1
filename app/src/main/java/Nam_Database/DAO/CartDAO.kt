package Nam_Database.DAO

import Nam_Database.Table.Cart
import androidx.room.*


@Dao
interface CartDAO {
    @Insert
    suspend fun insertCart(cart: Cart)

    @Update
    suspend fun updateCart(cart: Cart)

    @Delete
    suspend fun deleteCart(cart: Cart)

    @Query("DELETE FROM Cart")
    suspend fun deleteAllCarts()

    @Query("SELECT * FROM Cart WHERE userId = :userId")
    suspend fun getCartByUserId(userId: Int): Cart?

    @Query("SELECT * FROM Cart")
    suspend fun getAllCart() : List<Cart>
}

