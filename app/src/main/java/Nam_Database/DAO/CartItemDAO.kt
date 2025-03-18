package Nam_Database.DAO

import Nam_Database.Table.CartItem
import androidx.room.*
import com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang.CartItemWithProduct

@Dao
interface CartItemDAO {
    @Insert
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Query("DELETE FROM cartitem WHERE id = :cartItemId")
    suspend fun deleteCartItemById(cartItemId: Int): Int


    @Query("SELECT * FROM CartItem WHERE cartId = :cartId")
    suspend fun getItemsByCartId(cartId: Int): MutableList<CartItem>

    @Query("SELECT * FROM CartItem")
    suspend fun getAllCartItem():MutableList<CartItem>

    @Query("SELECT * FROM CartItem WHERE cartId = :cartId AND productId = :productId LIMIT 1")
    fun getCartItemByProductId(cartId: Int, productId: Int): CartItem?

    // **Thêm phương thức lấy CartItemWithProduct**
    @Transaction
    @Query("SELECT * FROM CartItem WHERE cartId = :cartId")
    suspend fun getCartItemsWithProducts(cartId: Int):MutableList<CartItemWithProduct>
}
