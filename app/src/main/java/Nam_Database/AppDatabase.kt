package Nam_Database

import Nam_Database.DAO.*
import Nam_Database.DAO.DAO_ThongKe.*
import Nam_Database.Table.*
import Nam_Database.Table.Table_ThongKe.*
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        User::class,
        Category::class,
        Product::class,
        Order::class,
        OrderItem::class,
        Review::class,
        StoreStatistic::class,
        CategoryStatistic::class,
        ProductStatistic::class,
        Cart::class,
        CartItem::class
    ],
    version = 10, // Tăng version nếu có thêm/sửa bảng
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDAO
    abstract fun productDao(): ProductDAO
    abstract fun orderDao(): OrderDAO
    abstract fun orderItemDao(): OrderItemDAO
    abstract fun reviewDao(): ReviewDAO


    abstract fun categoryStatisticDao(): CategoryStatisticDAO
    abstract fun productStatisticDao(): ProductStatisticDAO
    abstract fun storeStatisticDao(): StoreStatisticDAO

    // cart
    abstract fun cartDao(): CartDAO
    abstract fun CartItemDAO(): CartItemDAO
}
