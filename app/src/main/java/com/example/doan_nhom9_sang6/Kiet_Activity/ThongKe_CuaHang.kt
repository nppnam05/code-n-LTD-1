package com.example.doan_nhom9_sang6.Kiet_Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import Nam_Database.*
import Nam_Database.DAO.DAO_ThongKe.StoreStatisticDAO
import Nam_Database.AppDatabase
import Nam_Database.DAO.UserDao
import Nam_Database.Table.Table_ThongKe.StoreStatistic
import Nam_Database.Table.User
import android.util.Log
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import com.example.doan_nhom9_sang6.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ThongKe_CuaHang : AppCompatActivity() {
    private lateinit var btnReturn : ImageView
    private lateinit var btnStore : LinearLayout
    private lateinit var btnProduct : LinearLayout
    private lateinit var btnAccount : LinearLayout

    private lateinit var tvTongDoanhThuCuaHang: TextView
    private lateinit var tvSoSanhTongDoanhThuCuaHang: TextView
    private lateinit var tvTongSanPhamCuaHang: TextView
    private lateinit var tvSoSanhTongSanPhamCuaHang: TextView
    private lateinit var tvLuotKhachCuaHang: TextView
    private lateinit var tvSoSanhLuotKhachCuaHang: TextView
    private lateinit var tvKhachMoi: TextView
    private lateinit var tvSoSanhKhachMoi: TextView
    private lateinit var tvTiLeMuaHang: TextView
    private lateinit var tvSoSanhTiLeMuaHang: TextView
    private lateinit var tvTiLePhanHoi: TextView
    private lateinit var tvSoSanhTiLePhanHoi: TextView
    private lateinit var db: AppDatabase
    private lateinit var storeStatisticDAO: StoreStatisticDAO
    private lateinit var userDAO: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.thongke_cuahang)
        setControl()
        setEvent()
    }

    private fun setControl() {
        btnReturn = findViewById(R.id.btnReturn)
        btnStore = findViewById(R.id.btnCuaHang)
        btnProduct = findViewById(R.id.btnSanPham)
        btnAccount = findViewById(R.id.btnTaiKhoan)

        tvTongDoanhThuCuaHang = findViewById(R.id.tvTongDoanhThuCuaHang)
        tvSoSanhTongDoanhThuCuaHang = findViewById(R.id.tvSoSanhTongDoanhThuCuaHang)
        tvTongSanPhamCuaHang = findViewById(R.id.tvTongSanPhamCuaHang)
        tvSoSanhTongSanPhamCuaHang = findViewById(R.id.tvSoSanhTongSanPhamCuaHang)
        tvLuotKhachCuaHang = findViewById(R.id.tvLuotKhachCuaHang)
        tvSoSanhLuotKhachCuaHang = findViewById(R.id.tvSoSanhLuotKhachCuaHang)
        tvKhachMoi = findViewById(R.id.tvKhachMoi)
        tvSoSanhKhachMoi = findViewById(R.id.tvSoSanhKhachMoi)
        tvTiLeMuaHang = findViewById(R.id.tvTiLeMuaHang)
        tvSoSanhTiLeMuaHang = findViewById(R.id.tvSoSanhTiLeMuaHang)
        tvTiLePhanHoi = findViewById(R.id.tvTiLePhanHoi)
        tvSoSanhTiLePhanHoi = findViewById(R.id.tvSoSanhTiLePhanHoi)
    }

    private fun setEvent() {
        db = DatabaseHelper.getDatabase(this)
        storeStatisticDAO = db.storeStatisticDao() // Initialize the DAO
        userDAO = db.userDao() // Initialize the DAO
        lifecycleScope.launch(Dispatchers.IO) {
            var storeStats = storeStatisticDAO.getAllStoreStatistics()
            var store = userDAO.getAllUsers()
            withContext(Dispatchers.Main) {
                if (store.isEmpty()) {
                    // Insert the user first and get the generated ID
                    val user = User(
                        firstName = "John",
                        lastName = "Doe",
                        email = "john.doe@example.com",
                        phoneNumber = "123-456-7890",
                        userName = "johndoe",
                        passWord = "password123",
                        access = "admin",
                        created = "2024-12-19T15:30:00",
                        storeName = "Doe's Store",
                        address = "123 Main St, City, Country",
                        categorie = "Electronics"
                    )
                    val userId = userDAO.insertUser(user) // Assuming insertUser returns the generated user ID
                    Log.e("StoreStatisticCheck", "User added successfully.")
                    if (userId != null) {
                        if (storeStats.isEmpty()) {
                            val defaultData1 = StoreStatistic(
                                storeId = 1,  // Use the generated user ID as storeId
                                storeName = "Doe's Store",
                                totalRevenue = 500000.0,
                                totalVisits = 1500.0,
                                newVisits = 800.0,
                                purchaseRate = 0.55,
                                totalProducts = 100.0,
                                responseRate = 0.85,
                                recordedAt = "2024-11-01"
                            )

                            val defaultData2 = StoreStatistic(
                                storeId = 1,  // Use the generated user ID as storeId
                                storeName = "Doe's Store",
                                totalRevenue = 600000.0,
                                totalVisits = 1700.0,
                                newVisits = 900.0,
                                purchaseRate = 0.60,
                                totalProducts = 120.0,
                                responseRate = 0.90,
                                recordedAt = "2024-12-02"
                            )

                            // Insert the default data into the database
                            storeStatisticDAO.insertStoreStatistic(defaultData1)
                            storeStatisticDAO.insertStoreStatistic(defaultData2)
                            Log.e("StoreStatisticCheck", "Stats added successfully.")
                        }
                    }
                } else {
                    if (storeStats.isEmpty()) {
                        val defaultData1 = StoreStatistic(
                            storeId = 1,  // Use the generated user ID as storeId
                            storeName = "Doe's Store",
                            totalRevenue = 500000.0,
                            totalVisits = 1500.0,
                            newVisits = 800.0,
                            purchaseRate = 0.55,
                            totalProducts = 100.0,
                            responseRate = 0.85,
                            recordedAt = "2024-11-01"
                        )

                        val defaultData2 = StoreStatistic(
                            storeId = 1,  // Use the generated user ID as storeId
                            storeName = "Doe's Store",
                            totalRevenue = 600000.0,
                            totalVisits = 1700.0,
                            newVisits = 900.0,
                            purchaseRate = 0.60,
                            totalProducts = 120.0,
                            responseRate = 0.90,
                            recordedAt = "2024-12-02"
                        )

                        // Insert the default data into the database
                        storeStatisticDAO.insertStoreStatistic(defaultData1)
                        storeStatisticDAO.insertStoreStatistic(defaultData2)
                        Log.e("StoreStatisticCheck", "Stats added successfully.")
                    }
                }
                storeStats = storeStatisticDAO.getAllStoreStatistics()
                store = userDAO.getAllUsers()
                Log.e("DatabaseCheck", "Users found: $store")
                Log.e("DatabaseCheck", "Stats found: $storeStats")
                val lastTwoStats = storeStatisticDAO.getLastTwoStoreStatistics()

                if (!lastTwoStats.isNullOrEmpty() && lastTwoStats.size == 2) {
                    val secondLast = lastTwoStats[1]  // The second-to-last statistic
                    val last = lastTwoStats[0]         // The most recent statistic

                    tvTongDoanhThuCuaHang.text =
                        DecimalFormat("#,###").format(last.totalRevenue).toString() + "đ"
                    tvTongSanPhamCuaHang.text =
                        DecimalFormat("#,###").format(last.totalProducts).toString()
                    tvLuotKhachCuaHang.text =
                        DecimalFormat("#,###").format(last.totalVisits).toString() + " lượt"
                    tvKhachMoi.text =
                        DecimalFormat("#,###").format(last.newVisits).toString() + " lượt"
                    tvTiLeMuaHang.text = (last.purchaseRate * 100).toString() + "%"
                    tvTiLePhanHoi.text = (last.responseRate * 100).toString() + "%"

                    // Comparison logic
                    updateComparison(last, secondLast)
                } else {
                    // Set default values if there is no data in the database
                    tvTongDoanhThuCuaHang.text = "0đ"
                    tvTongSanPhamCuaHang.text = "0"
                    tvLuotKhachCuaHang.text = "0 lượt"
                    tvKhachMoi.text = "0 lượt"
                    tvTiLeMuaHang.text = "0%"
                    tvTiLePhanHoi.text = "0%"

                    tvSoSanhTongDoanhThuCuaHang.text = "Không có dữ liệu tháng trước"
                    tvSoSanhTongSanPhamCuaHang.text = "Không có dữ liệu tháng trước"
                    tvSoSanhLuotKhachCuaHang.text = "Không có dữ liệu tháng trước"
                    tvSoSanhKhachMoi.text = "Không có dữ liệu tháng trước"
                    tvSoSanhTiLeMuaHang.text = "Không có dữ liệu tháng trước"
                    tvSoSanhTiLePhanHoi.text = "Không có dữ liệu tháng trước"

                    // Optionally, you can hide comparison text views or set them to a neutral color
                    tvSoSanhTongDoanhThuCuaHang.setTextColor(Color.GRAY)
                    tvSoSanhTongSanPhamCuaHang.setTextColor(Color.GRAY)
                    tvSoSanhLuotKhachCuaHang.setTextColor(Color.GRAY)
                    tvSoSanhKhachMoi.setTextColor(Color.GRAY)
                    tvSoSanhTiLeMuaHang.setTextColor(Color.GRAY)
                    tvSoSanhTiLePhanHoi.setTextColor(Color.GRAY)
                }
            }
        }

        btnReturn.setOnClickListener {
            finish()
        }
        btnStore.setOnClickListener {
            val intent = Intent(this, ThongKe_CuaHang::class.java)
            startActivity(intent)
            finish()
        }
        btnProduct.setOnClickListener {
            val intent = Intent(this, ThongKe_SanPham::class.java)
            startActivity(intent)
            finish()
        }
        btnAccount.setOnClickListener {
            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateComparison(last: Nam_Database.Table.Table_ThongKe.StoreStatistic,
                                 secondLast: Nam_Database.Table.Table_ThongKe.StoreStatistic) {

        //////////////
        if (last.totalRevenue - secondLast.totalRevenue >= 0.0) {
            tvSoSanhTongDoanhThuCuaHang.text = "Tăng ${String.format("%.2f", ((last.totalRevenue - secondLast.totalRevenue) / secondLast.totalRevenue) * 100)}% so với tháng trước."
            tvSoSanhTongDoanhThuCuaHang.setTextColor(Color.parseColor("#00FF00"))
        } else {
            tvSoSanhTongDoanhThuCuaHang.text = "Giảm ${String.format("%.2f", ((last.totalRevenue - secondLast.totalRevenue) / secondLast.totalRevenue) * 100)}% so với tháng trước."
            tvSoSanhTongDoanhThuCuaHang.setTextColor(Color.parseColor("#FF0000"))
        }

        //////////////
        if (last.totalProducts - secondLast.totalProducts >= 0.0) {
            tvSoSanhTongSanPhamCuaHang.text = "Tăng ${String.format("%.2f", ((last.totalProducts - secondLast.totalProducts) / secondLast.totalProducts) * 100)}% so với tháng trước."
            tvSoSanhTongSanPhamCuaHang.setTextColor(Color.parseColor("#00FF00"))
        } else {
            tvSoSanhTongSanPhamCuaHang.text = "Giảm ${String.format("%.2f", ((last.totalProducts - secondLast.totalProducts) / secondLast.totalProducts) * 100)}% so với tháng trước."
            tvSoSanhTongSanPhamCuaHang.setTextColor(Color.parseColor("#FF0000"))
        }

        //////////////
        if (last.totalVisits - secondLast.totalVisits >= 0.0) {
            tvSoSanhLuotKhachCuaHang.text = "Tăng ${String.format("%.2f", ((last.totalVisits - secondLast.totalVisits) / secondLast.totalVisits) * 100)}% so với tháng trước."
            tvSoSanhLuotKhachCuaHang.setTextColor(Color.parseColor("#00FF00"))
        } else {
            tvSoSanhLuotKhachCuaHang.text = "Giảm ${String.format("%.2f", ((last.totalVisits - secondLast.totalVisits) / secondLast.totalVisits) * 100)}% so với tháng trước."
            tvSoSanhLuotKhachCuaHang.setTextColor(Color.parseColor("#FF0000"))
        }

        //////////////
        if (last.newVisits - secondLast.newVisits >= 0.0) {
            tvSoSanhKhachMoi.text = "Tăng ${String.format("%.2f", ((last.newVisits - secondLast.newVisits) / secondLast.newVisits) * 100)}% so với tháng trước."
            tvSoSanhKhachMoi.setTextColor(Color.parseColor("#00FF00"))
        } else {
            tvSoSanhKhachMoi.text = "Giảm ${String.format("%.2f", ((last.newVisits - secondLast.newVisits) / secondLast.newVisits) * 100)}% so với tháng trước."
            tvSoSanhKhachMoi.setTextColor(Color.parseColor("#FF0000"))
        }

        //////////////
        if (last.purchaseRate - secondLast.purchaseRate >= 0.0) {
            tvSoSanhTiLeMuaHang.text = "Tăng ${String.format("%.2f", ((last.purchaseRate - secondLast.purchaseRate) / secondLast.purchaseRate) * 100)}% so với tháng trước."
            tvSoSanhTiLeMuaHang.setTextColor(Color.parseColor("#00FF00"))
        } else {
            tvSoSanhTiLeMuaHang.text = "Giảm ${String.format("%.2f", ((last.purchaseRate - secondLast.purchaseRate) / secondLast.purchaseRate) * 100)}% so với tháng trước."
            tvSoSanhTiLeMuaHang.setTextColor(Color.parseColor("#FF0000"))
        }

        //////////////
        if (last.responseRate - secondLast.responseRate >= 0.0) {
            tvSoSanhTiLePhanHoi.text = "Tăng ${String.format("%.2f", ((last.responseRate - secondLast.responseRate) / secondLast.responseRate) * 100)}% so với tháng trước."
            tvSoSanhTiLePhanHoi.setTextColor(Color.parseColor("#00FF00"))
        } else {
            tvSoSanhTiLePhanHoi.text = "Giảm ${String.format("%.2f", ((last.responseRate - secondLast.responseRate) / secondLast.responseRate) * 100)}% so với tháng trước."
            tvSoSanhTiLePhanHoi.setTextColor(Color.parseColor("#FF0000"))
        }

    }
}
