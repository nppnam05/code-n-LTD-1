package com.example.doan_nhom9_sang6.Kiet_Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import Nam_Database.DAO.DAO_ThongKe.CategoryStatisticDAO
import Nam_Database.Table.Table_ThongKe.CategoryStatistic
import Nam_Database.* // Replace with your actual database class
import Nam_Database.DAO.CategoryDAO
import Nam_Database.Table.Category
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.doan_nhom9_sang6.R
import kotlinx.coroutines.withContext

class ThongKe_SanPham : AppCompatActivity() {
    private lateinit var btnReturn: ImageView
    private lateinit var btnStore: LinearLayout
    private lateinit var btnProduct: LinearLayout
    private lateinit var btnAccount: LinearLayout
    private lateinit var pieChart: PieChart
    private lateinit var recyclerViewDanhMuc: RecyclerView

    private lateinit var categoryStatisticDAO: CategoryStatisticDAO
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.thongke_sanpham)
        setControl()
        setEvent()
    }

    suspend fun prepareCategoryStatistics() {
        var existingCategoryStatistics = categoryStatisticDAO.getAllCategoryStatistics()

        val existingCategories = categoryDAO.getAllCategories()
        categoryStatisticDAO.deleteAllCategoryStatistic()
        existingCategoryStatistics = categoryStatisticDAO.getAllCategoryStatistics()

        if (existingCategories.isEmpty()) {
            // Table is empty, so insert the predefined list
            val categories = listOf(
                Category(
                    id = 1,
                    categoryName = "Quần Áo",
                    categoryDesc = "Danh mục bao gồm các loại quần, áo thời trang và phong cách.",
                    parentId = null,
                    isActive = true
                ),
                Category(
                    id = 2,
                    categoryName = "Giày", // Dành cho các sản phẩm liên quan đến giày
                    categoryDesc = "Danh mục bao gồm giày thể thao và thời trang.",
                    parentId = null,
                    isActive = true
                ),
                Category(
                    id = 3,
                    categoryName = "Đồ Ăn", // Dành cho các sản phẩm đồ ăn
                    categoryDesc = "Danh mục các loại thực phẩm và đồ uống.",
                    parentId = null,
                    isActive = true
                ),
                Category(
                    id = 4,
                    categoryName = "Điện Tử", // Dành cho các sản phẩm điện tử
                    categoryDesc = "Danh mục các sản phẩm công nghệ và thiết bị điện tử.",
                    parentId = null,
                    isActive = true
                ),
                Category(
                    id = 5,
                    categoryName = "Áo",
                    categoryDesc = "Danh mục các loại áo thun thời trang.",
                    parentId = 1, // "Áo" là cha
                    isActive = true
                ),

                // Con của "Giày"
                Category(
                    id = 6,
                    categoryName = "Giày Thể Thao", // Danh mục con của Giày
                    categoryDesc = "Giày thể thao dành cho hoạt động ngoài trời.",
                    parentId = 2, // "Giày" là cha
                    isActive = true
                ),
                Category(
                    id = 7,
                    categoryName = "Giày Da", // Danh mục con của Giày
                    categoryDesc = "Giày da dành cho các dịp trang trọng.",
                    parentId = 2, // "Giày" là cha
                    isActive = true
                ),

                // Con của "Đồ Ăn"
                Category(
                    id = 8,
                    categoryName = "Đồ Ăn Nhanh", // Danh mục con của Đồ Ăn
                    categoryDesc = "Thức ăn nhanh cho những ngày bận rộn.",
                    parentId = 3, // "Đồ Ăn" là cha
                    isActive = true
                ),
                Category(
                    id = 9,
                    categoryName = "Đồ Uống", // Danh mục con của Đồ Ăn
                    categoryDesc = "Các loại thức uống giải khát.",
                    parentId = 3, // "Đồ Ăn" là cha
                    isActive = true
                ),

                // Con của "Điện Tử"
                Category(
                    id = 10,
                    categoryName = "Điện Thoại", // Danh mục con của Điện Tử
                    categoryDesc = "Danh mục các loại điện thoại thông minh.",
                    parentId = 4, // "Điện Tử" là cha
                    isActive = true
                ),
                Category(
                    id = 11,
                    categoryName = "Laptop", // Danh mục con của Điện Tử
                    categoryDesc = "Danh mục các loại máy tính xách tay.",
                    parentId = 4, // "Điện Tử" là cha
                    isActive = true
                )
            )

            // Insert each Category individually into the database
            categories.forEach { category ->
                categoryDAO.insertCategory(category)
            }
        }

        if (existingCategoryStatistics.isNullOrEmpty()) {
            val categoryStatistics = listOf(
                CategoryStatistic(
                    id = 1, categoryId = 1, categoryName = "Quần Áo",
                    totalSalesAmount = 40, totalRevenue = 36290.75, revenueShare = 18.2, numberOfProducts = 5
                ),
                CategoryStatistic(
                    id = 2, categoryId = 2, categoryName = "Giày",
                    totalSalesAmount = 85, totalRevenue = 425929.85, revenueShare = 21.3, numberOfProducts = 4
                ),
                CategoryStatistic(
                    id = 3, categoryId = 3, categoryName = "Đồ Ăn",
                    totalSalesAmount = 230, totalRevenue = 358477.00, revenueShare = 17.9, numberOfProducts = 5
                ),
                CategoryStatistic(
                    id = 4, categoryId = 4, categoryName = "Điện Tử",
                    totalSalesAmount = 58, totalRevenue = 846596.95, revenueShare = 42.6, numberOfProducts = 6
                ),
                CategoryStatistic(
                    id = 5, categoryId = 5, categoryName = "Áo",
                    totalSalesAmount = 55, totalRevenue = 146035.50, revenueShare = 7.3, numberOfProducts = 2
                ),
                CategoryStatistic(
                    id = 6, categoryId = 6, categoryName = "Giày Thể Thao",
                    totalSalesAmount = 40, totalRevenue = 1149750.00, revenueShare = 15.8, numberOfProducts = 2
                ),
                CategoryStatistic(
                    id = 7, categoryId = 7, categoryName = "Giày Da",
                    totalSalesAmount = 15, totalRevenue = 689850.00, revenueShare = 9.2, numberOfProducts = 1
                ),
                CategoryStatistic(
                    id = 8, categoryId = 8, categoryName = "Đồ Ăn Nhanh",
                    totalSalesAmount = 120, totalRevenue = 120000.00, revenueShare = 4.2, numberOfProducts = 1
                )
            )


            categoryStatistics.forEach { categoryStatistic ->
                categoryStatisticDAO.insertCategoryStatistic(categoryStatistic)
            }
        }

        val categoryStatistics = categoryStatisticDAO.getAllCategoryStatistics()
        val categories = categoryDAO.getAllCategories()

        withContext(Dispatchers.Main) {
            recyclerViewDanhMuc.layoutManager = LinearLayoutManager(this@ThongKe_SanPham)
            recyclerViewDanhMuc.adapter = CategoryAdapter(this@ThongKe_SanPham, categoryStatistics, categories)

            configurePieChart(categoryStatistics)
        }
    }



    private fun setControl() {
        recyclerViewDanhMuc = findViewById(R.id.lvDanhSachDanhMuc)
        btnReturn = findViewById(R.id.btnReturn)
        btnStore = findViewById(R.id.btnCuaHang)
        btnProduct = findViewById(R.id.btnSanPham)
        btnAccount = findViewById(R.id.btnTaiKhoan)
        pieChart = findViewById(R.id.pieChart)
    }

    private fun setEvent() {
        db = DatabaseHelper.getDatabase(this)
        categoryStatisticDAO = db.categoryStatisticDao()
        categoryDAO = db.categoryDao()
        lifecycleScope.launch(Dispatchers.IO) {
            prepareCategoryStatistics()
        }

        // Navigation buttons
        btnReturn.setOnClickListener { finish() }
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

    private fun configurePieChart(categoryStatistics: List<CategoryStatistic>) {
        val pieColors = listOf(
            ContextCompat.getColor(this, R.color.purple_200),
            ContextCompat.getColor(this, R.color.teal_200),
            ContextCompat.getColor(this, R.color.yellow),
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this, R.color.teal_700),
            ContextCompat.getColor(this, R.color.black)
        )

        // Create PieChart entries based on the updated statistics
        val pieEntries = categoryStatistics.map {
            PieEntry(it.revenueShare.toFloat() * 100, it.categoryName) // Use categoryName or fallback
        }

        val pieDataSet = PieDataSet(pieEntries, "Tỷ lệ danh mục").apply {
            colors = pieColors
            valueTextSize = 12f
            valueTextColor = Color.BLACK
        }

        val percentageFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${String.format("%.1f", value)}%"
            }
        }

        val pieData = PieData(pieDataSet).apply {
            setValueFormatter(percentageFormatter)
        }

        pieChart.apply {
            data = pieData
            description.text = "Tỷ lệ doanh thu theo danh mục"
            setEntryLabelTextSize(10f)
            setEntryLabelColor(Color.BLACK)
            animateY(1700)
            setUsePercentValues(true)
            legend.isEnabled = false
            invalidate()
        }
    }
}
