package Nam_Database
// cái này là lớp kết nối
import android.content.Context
import androidx.room.Room

object DatabaseHelper {

    // Khai báo biến INSTANCE, sẽ chứa instance của AppDatabase
    @Volatile
    private var INSTANCE: AppDatabase? = null

    // Hàm để lấy instance của AppDatabase
    fun getDatabase(context: Context): AppDatabase {
        // Kiểm tra nếu INSTANCE chưa được khởi tạo
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,   // Lớp AppDatabase là lớp cơ sở dữ liệu
                "app_database"  // Tên cơ sở dữ liệu
            ).fallbackToDestructiveMigration()  // Thêm vào đây để xóa cơ sở dữ liệu cũ
             .build()

            INSTANCE = instance  // Gán instance sau khi tạo
            instance
        }
    }
}
