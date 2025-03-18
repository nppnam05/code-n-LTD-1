package com.example.doan_nhom9_sang6.Kiet_Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import Nam_Database.DAO.DAO_ThongKe.CategoryStatisticDAO
import Nam_Database.AppDatabase
import Nam_Database.DAO.CategoryDAO
import Nam_Database.DAO.DAO_ThongKe.ProductStatisticDAO
import Nam_Database.DAO.ProductDAO
import Nam_Database.DAO.UserDao
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Category
import Nam_Database.Table.Product
import Nam_Database.Table.Table_ThongKe.CategoryStatistic
import Nam_Database.Table.Table_ThongKe.ProductStatistic
import Nam_Database.Table.User
import android.view.View
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.ContextCompat
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import com.example.doan_nhom9_sang6.R
import java.text.DecimalFormat

class ThongKe_SanPham_DanhMuc : AppCompatActivity() {

    private lateinit var btnReturn: ImageView
    private lateinit var btnStore: LinearLayout
    private lateinit var btnProduct: LinearLayout
    private lateinit var btnAccount: LinearLayout
    private lateinit var lvDanhSachSanPham: RecyclerView
    private lateinit var pieChart: PieChart
    private lateinit var tvTenDanhMuc: TextView
    private lateinit var tvTongDoanhThuDanhMuc: TextView
    private lateinit var tvSoSanhTongDoanhThuDanhMuc: TextView
    private lateinit var tvTongSanPham: TextView
    private lateinit var tvSoSanhTongSanPham: TextView
    private lateinit var tvTiTrongSanPham: TextView

    private lateinit var categoryStatisticDAO: CategoryStatisticDAO
    private lateinit var productStatisticDAO: ProductStatisticDAO
    private lateinit var userDAO: UserDao
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var productDAO: ProductDAO
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.thongke_sanpham_danhmuc)
        setControl()
        setEvent()
    }

    private fun setControl() {
        btnReturn = findViewById(R.id.btnReturn)
        btnStore = findViewById(R.id.btnCuaHang)
        btnProduct = findViewById(R.id.btnSanPham)
        btnAccount = findViewById(R.id.btnTaiKhoan)
        lvDanhSachSanPham = findViewById(R.id.lvDanhSachSanPham)
        pieChart = findViewById(R.id.pieChart)

        tvTenDanhMuc = findViewById(R.id.tvTenDanhMuc)
        tvTongDoanhThuDanhMuc = findViewById(R.id.tvTongDoanhThuDanhMuc)
        tvSoSanhTongDoanhThuDanhMuc = findViewById(R.id.tvSoSanhTongDoanhThuDanhMuc)
        tvTongSanPham = findViewById(R.id.tvTongSanPham)
        tvSoSanhTongSanPham = findViewById(R.id.tvSoSanhTongSanPham)
        tvTiTrongSanPham = findViewById(R.id.tvTiTrongSanPham)
    }

    private fun setEvent() {
        db = DatabaseHelper.getDatabase(this)
        categoryStatisticDAO = db.categoryStatisticDao()
        productStatisticDAO = db.productStatisticDao()
        userDAO = db.userDao()
        categoryDAO = db.categoryDao()
        productDAO = db.productDao()

        // Get the category ID from the Intent
        val categoryID = intent.getIntExtra("CATEGORY_ID", 1)

        lifecycleScope.launch(Dispatchers.IO) {
            categoryStatisticDAO.deleteAllCategoryStatistic()
            productStatisticDAO.deleteAllProductStatistics()

            var store = userDAO.getAllUsers()
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
                userDAO.insertUser(user)
            }

            val existingCategories = categoryDAO.getAllCategories()
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
            val category = categoryDAO.getCategoryById(categoryID)

            val existingProducts = productDAO.getAllProducts()
            if (existingProducts.isEmpty()) {
                //Table is empty, so insert the predefined list
                val products = listOf(
                    Product(
                        1,
                        "Điện thoại Smartphone X10",
                        18799000.0,
                        "Điện thoại cao cấp hỗ trợ 5G.",
                        "/images/smartphone_x10.png",
                        1,
                        1,
                        50,
                        "2024-01-01"
                    ),
                    Product(
                        2,
                        "Laptop Gaming G5",
                        30499000.0,
                        "Laptop chơi game hiệu năng cao.",
                        "/images/gaming_laptop_g5.png",
                        2,
                        1,
                        25,
                        "2024-01-02"
                    ),
                    Product(
                        3,
                        "Loa Bluetooth Mini",
                        1149000.0,
                        "Loa nhỏ gọn, dễ mang theo.",
                        "/images/bluetooth_speaker_mini.png",
                        3,
                        1,
                        150,
                        "2024-01-03"
                    ),
                    Product(
                        4,
                        "Tai nghe không dây Pro",
                        4599000.0,
                        "Tai nghe chống ồn cao cấp.",
                        "/images/wireless_earbuds_pro.png",
                        3,
                        1,
                        100,
                        "2024-01-04"
                    ),
                    Product(
                        5,
                        "TV LED 4K",
                        20699000.0,
                        "Màn hình hiển thị 4K rõ nét.",
                        "/images/4k_led_tv.png",
                        1,
                        1,
                        10,
                        "2024-01-05"
                    ),
                    Product(
                        6,
                        "Bàn phím cơ RGB",
                        2299000.0,
                        "Đèn RGB tùy chỉnh.",
                        "/images/mechanical_keyboard_rgb.png",
                        2,
                        1,
                        75,
                        "2024-01-06"
                    ),
                    Product(
                        7,
                        "Ghế văn phòng Ergo",
                        6899000.0,
                        "Ghế văn phòng công thái học.",
                        "/images/office_chair_ergo.png",
                        4,
                        1,
                        40,
                        "2024-01-07"
                    ),
                    Product(
                        8,
                        "Camera hành trình 4K",
                        8049900.0,
                        "Ghi lại mọi hành trình với chất lượng 4K.",
                        "/images/action_camera_4k.png",
                        3,
                        1,
                        30,
                        "2024-01-08"
                    ),
                    Product(
                        9,
                        "Đồng hồ thông minh Z",
                        3449000.0,
                        "Theo dõi mục tiêu sức khỏe của bạn.",
                        "/images/smartwatch_z.png",
                        1,
                        1,
                        120,
                        "2024-01-09"
                    ),
                    Product(
                        10,
                        "Tai nghe chống ồn",
                        5749000.0,
                        "Trải nghiệm âm thanh đỉnh cao.",
                        "/images/noise_cancelling_headphones.png",
                        3,
                        1,
                        85,
                        "2024-01-10"
                    ),
                    Product(
                        11,
                        "Xe điện Scooter",
                        11499000.0,
                        "Phương tiện giao thông thân thiện với môi trường.",
                        "/images/electric_scooter.png",
                        5,
                        1,
                        20,
                        "2024-01-11"
                    ),
                    Product(
                        12,
                        "Máy trộn bột chuyên nghiệp",
                        6899000.0,
                        "Lý tưởng cho việc làm bánh.",
                        "/images/kitchen_mixer_pro.png",
                        6,
                        1,
                        60,
                        "2024-01-12"
                    ),
                    Product(
                        13,
                        "Nhiệt kế thông minh",
                        4599000.0,
                        "Kiểm soát khí hậu trong nhà bạn.",
                        "/images/smart_thermostat.png",
                        7,
                        1,
                        35,
                        "2024-01-13"
                    ),
                    Product(
                        14,
                        "Giày chạy bộ",
                        1839000.0,
                        "Thoải mái và bền bỉ.",
                        "/images/running_shoes.png",
                        8,
                        1,
                        90,
                        "2024-01-14"
                    ),
                    Product(
                        15,
                        "Router không dây AX",
                        2999000.0,
                        "Internet nhanh và ổn định.",
                        "/images/wireless_router_ax.png",
                        9,
                        1,
                        65,
                        "2024-01-15"
                    ),
                    Product(
                        16,
                        "Chuột chơi game Pro",
                        1599000.0,
                        "Độ chính xác và hiệu năng cao.",
                        "/images/gaming_mouse_pro.png",
                        2,
                        1,
                        110,
                        "2024-01-16"
                    ),
                    Product(
                        17,
                        "Bảng vẽ kỹ thuật số",
                        8049900.0,
                        "Dành cho các chuyên gia sáng tạo.",
                        "/images/digital_drawing_tablet.png",
                        2,
                        1,
                        45,
                        "2024-01-17"
                    ),
                    Product(
                        18,
                        "Vòng tay theo dõi sức khỏe",
                        1149000.0,
                        "Theo dõi sức khỏe của bạn.",
                        "/images/fitness_tracker_band.png",
                        1,
                        1,
                        130,
                        "2024-01-18"
                    ),
                    Product(
                        19,
                        "Bộ guitar điện",
                        11499000.0,
                        "Hoàn hảo cho người mới bắt đầu.",
                        "/images/electric_guitar_set.png",
                        10,
                        1,
                        15,
                        "2024-01-19"
                    ),
                    Product(
                        20,
                        "Bộ cờ bàn cổ điển",
                        689900.0,
                        "Niềm vui cho cả gia đình.",
                        "/images/board_game_classic.png",
                        11,
                        1,
                        200,
                        "2024-01-20"
                    ),
                    Product(
                        21,
                        "Ổ cứng di động 2TB",
                        2069000.0,
                        "Lưu trữ tất cả các tệp của bạn.",
                        "/images/portable_hard_drive.png",
                        9,
                        1,
                        150,
                        "2024-01-21"
                    ),
                    Product(
                        22,
                        "Drone có camera",
                        14049000.0,
                        "Máy bay không người lái độ phân giải cao.",
                        "/images/drone_with_camera.png",
                        3,
                        1,
                        10,
                        "2024-01-22"
                    ),
                    Product(
                        23,
                        "Máy lọc không khí thông minh",
                        6899000.0,
                        "Cải thiện chất lượng không khí.",
                        "/images/smart_air_purifier.png",
                        7,
                        1,
                        25,
                        "2024-01-23"
                    ),
                    Product(
                        24,
                        "Ví da thời trang",
                        919000.0,
                        "Phong cách và tiện dụng.",
                        "/images/leather_wallet.png",
                        12,
                        1,
                        300,
                        "2024-01-24"
                    ),
                    Product(
                        25,
                        "Máy pha cà phê Espresso",
                        5749000.0,
                        "Pha cà phê chất lượng barista.",
                        "/images/espresso_machine.png",
                        6,
                        1,
                        20,
                        "2024-01-25"
                    ),
                    Product(
                        26,
                        "Lều cắm trại 4 người",
                        4599000.0,
                        "Đủ chỗ cho 4 người.",
                        "/images/camping_tent.png",
                        5,
                        1,
                        30,
                        "2024-01-26"
                    ),
                    Product(
                        27,
                        "Ấm đun nước điện",
                        1149000.0,
                        "Sôi nhanh và tiện dụng.",
                        "/images/electric_kettle.png",
                        6,
                        1,
                        80,
                        "2024-01-27"
                    ),
                    Product(
                        28,
                        "Xe đạp leo núi",
                        20699000.0,
                        "Chắc chắn và nhẹ.",
                        "/images/mountain_bike.png",
                        5,
                        1,
                        15,
                        "2024-01-28"
                    ),
                    Product(
                        29,
                        "Xe đẩy em bé",
                        6899000.0,
                        "Mang lại sự thoải mái và an toàn.",
                        "/images/baby_stroller.png",
                        13,
                        1,
                        20,
                        "2024-01-29"
                    ),
                    Product(
                        30,
                        "Máy chơi game Console X",
                        11499000.0,
                        "Trò chơi thế hệ mới.",
                        "/images/gaming_console_x.png",
                        2,
                        1,
                        50,
                        "2024-01-30"
                    ),
                    Product(
                        31,
                        "Quạt điện thông minh",
                        4599000.0,
                        "Quạt đa chức năng hiện đại.",
                        "/images/smart_fan.png",
                        7,
                        1,
                        45,
                        "2024-01-31"
                    ),
                    Product(
                        32,
                        "Balo du lịch",
                        1839000.0,
                        "Rộng rãi và tiện lợi.",
                        "/images/travel_backpack.png",
                        5,
                        1,
                        100,
                        "2024-02-01"
                    ),
                    Product(
                        33,
                        "Máy ảnh DSLR",
                        16049000.0,
                        "Chụp ảnh chuyên nghiệp.",
                        "/images/dslr_camera.png",
                        3,
                        1,
                        20,
                        "2024-02-02"
                    ),
                    Product(
                        34,
                        "Đèn bàn học chống cận",
                        1149000.0,
                        "Bảo vệ đôi mắt của bạn.",
                        "/images/study_lamp.png",
                        4,
                        1,
                        75,
                        "2024-02-03"
                    ),
                    Product(
                        35,
                        "Máy ép trái cây",
                        3449000.0,
                        "Giữ lại mọi chất dinh dưỡng.",
                        "/images/juice_extractor.png",
                        6,
                        1,
                        40,
                        "2024-02-04"
                    ),
                    Product(
                        36,
                        "Tủ lạnh mini",
                        6899000.0,
                        "Tiện ích và tiết kiệm không gian.",
                        "/images/mini_fridge.png",
                        6,
                        1,
                        25,
                        "2024-02-05"
                    ),
                    Product(
                        37,
                        "Máy rửa chén",
                        11499000.0,
                        "Tiết kiệm thời gian và công sức.",
                        "/images/dishwasher.png",
                        6,
                        1,
                        10,
                        "2024-02-06"
                    ),
                    Product(
                        38,
                        "Bộ đồ dùng nấu ăn",
                        4599000.0,
                        "Chất liệu chống dính cao cấp.",
                        "/images/cooking_set.png",
                        6,
                        1,
                        60,
                        "2024-02-07"
                    ),
                    Product(
                        39,
                        "Ghế massage",
                        22999000.0,
                        "Thư giãn sau một ngày dài.",
                        "/images/massage_chair.png",
                        4,
                        1,
                        5,
                        "2024-02-08"
                    ),
                    Product(
                        40,
                        "Máy sưởi dầu",
                        5749000.0,
                        "Giữ ấm căn phòng của bạn.",
                        "/images/oil_heater.png",
                        7,
                        1,
                        15,
                        "2024-02-09"
                    ),
                    Product(
                        41,
                        "Máy cạo râu điện",
                        1599000.0,
                        "Thiết kế tiện dụng, pin lâu dài.",
                        "/images/electric_shaver.png",
                        6,
                        1,
                        120,
                        "2024-02-10"
                    ),
                    Product(
                        42,
                        "Đèn ngủ thông minh",
                        1149000.0,
                        "Điều chỉnh ánh sáng qua ứng dụng.",
                        "/images/smart_night_light.png",
                        7,
                        1,
                        200,
                        "2024-02-11"
                    ),
                    Product(
                        43,
                        "Bàn làm việc gấp gọn",
                        2299000.0,
                        "Tiện lợi cho không gian nhỏ.",
                        "/images/folding_desk.png",
                        4,
                        1,
                        50,
                        "2024-02-12"
                    ),
                    Product(
                        44,
                        "Máy hút bụi cầm tay",
                        3449000.0,
                        "Nhẹ, mạnh mẽ và dễ sử dụng.",
                        "/images/handheld_vacuum.png",
                        6,
                        1,
                        80,
                        "2024-02-13"
                    ),
                    Product(
                        45,
                        "Kính thực tế ảo VR",
                        8049900.0,
                        "Trải nghiệm game thực tế ảo.",
                        "/images/vr_headset.png",
                        3,
                        1,
                        30,
                        "2024-02-14"
                    ),
                    Product(
                        46,
                        "Hộp đựng thực phẩm chân không",
                        4599000.0,
                        "Giữ thực phẩm tươi lâu hơn.",
                        "/images/vacuum_food_container.png",
                        6,
                        1,
                        100,
                        "2024-02-15"
                    ),
                    Product(
                        47,
                        "Đồng hồ treo tường thông minh",
                        1839000.0,
                        "Đồng bộ hóa giờ tự động.",
                        "/images/smart_wall_clock.png",
                        7,
                        1,
                        60,
                        "2024-02-16"
                    ),
                    Product(
                        48,
                        "Túi đựng laptop chống sốc",
                        919000.0,
                        "Bảo vệ laptop của bạn.",
                        "/images/laptop_bag.png",
                        2,
                        1,
                        150,
                        "2024-02-17"
                    ),
                    Product(
                        49,
                        "Máy đo huyết áp",
                        2299000.0,
                        "Dễ sử dụng, đo chính xác.",
                        "/images/blood_pressure_monitor.png",
                        1,
                        1,
                        40,
                        "2024-02-18"
                    ),
                    Product(
                        50,
                        "Dụng cụ xay tiêu tự động",
                        574900.0,
                        "Tiện lợi khi nấu ăn.",
                        "/images/auto_pepper_grinder.png",
                        6,
                        1,
                        200,
                        "2024-02-19"
                    ),
                    Product(
                        51,
                        "Đèn pin siêu sáng",
                        1149000.0,
                        "Độ sáng cao, dùng ngoài trời.",
                        "/images/high_power_flashlight.png",
                        5,
                        1,
                        100,
                        "2024-02-20"
                    ),
                    Product(
                        52,
                        "Tủ giày thông minh",
                        6899000.0,
                        "Giữ giày gọn gàng, sạch sẽ.",
                        "/images/smart_shoe_rack.png",
                        4,
                        1,
                        25,
                        "2024-02-21"
                    ),
                    Product(
                        53,
                        "Máy xay sinh tố cầm tay",
                        1149000.0,
                        "Dễ dàng pha chế mọi nơi.",
                        "/images/portable_blender.png",
                        6,
                        1,
                        150,
                        "2024-02-22"
                    ),
                    Product(
                        54,
                        "Camera an ninh ngoài trời",
                        8049900.0,
                        "Giám sát an ninh 24/7.",
                        "/images/outdoor_security_camera.png",
                        3,
                        1,
                        30,
                        "2024-02-23"
                    ),
                    Product(
                        55,
                        "Balo thể thao đa năng",
                        2299000.0,
                        "Dành cho người yêu thể thao.",
                        "/images/sports_backpack.png",
                        5,
                        1,
                        90,
                        "2024-02-24"
                    ),
                    Product(
                        56,
                        "Khay làm đá nhanh",
                        459900.0,
                        "Đổ đầy nước và xoay là có đá.",
                        "/images/quick_ice_tray.png",
                        6,
                        1,
                        300,
                        "2024-02-25"
                    ),
                    Product(
                        57,
                        "Máy chiếu mini",
                        11499000.0,
                        "Xem phim tại nhà với màn hình lớn.",
                        "/images/mini_projector.png",
                        3,
                        1,
                        20,
                        "2024-02-26"
                    ),
                    Product(
                        58,
                        "Chổi lau nhà thông minh",
                        3449000.0,
                        "Giúp làm sạch nhanh chóng.",
                        "/images/smart_mop.png",
                        6,
                        1,
                        80,
                        "2024-02-27"
                    ),
                    Product(
                        59,
                        "Quần áo tập gym",
                        1839000.0,
                        "Thoải mái và thấm hút mồ hôi.",
                        "/images/gym_clothes.png",
                        8,
                        1,
                        100,
                        "2024-02-28"
                    ),
                    Product(
                        60,
                        "Loa thanh soundbar",
                        6899000.0,
                        "Âm thanh sống động cho TV.",
                        "/images/soundbar.png",
                        3,
                        1,
                        50,
                        "2024-02-29"
                    )
                )
                // Insert each CategoryStatistic individually into the database
                products.forEach { product ->
                    productDAO.insertProduct(product)
                }
            }

            val existingCategoryStatistics = categoryStatisticDAO.getAllCategoryStatistics()
            if (existingCategoryStatistics.isEmpty()) {
                // Table is empty, so insert the predefined list
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

                // Insert each CategoryStatistic individually into the database
                categoryStatistics.forEach { categoryStatistic ->
                    categoryStatisticDAO.insertCategoryStatistic(categoryStatistic)
                }
            }
            val categoryStatistic = categoryStatisticDAO.getCategoryStatisticById(categoryID)

            val existingProductStatistics = productStatisticDAO.getAllProductStatistics()
            if (existingProductStatistics.isEmpty()) {
                val productStatistics = listOf(
                    ProductStatistic(id = 1, productId = 1, productName = "Áo hoodies", amount = 25, price = 17499.75, categoryId = 1),
                    ProductStatistic(id = 2, productId = 2, productName = "Giày Chạy Bộ", amount = 15, price = 14999.85, categoryId = 2),
                    ProductStatistic(id = 3, productId = 3, productName = "Trà Sữa", amount = 200, price = 139998.00, categoryId = 3),
                    ProductStatistic(id = 4, productId = 4, productName = "Lap Top G5", amount = 5, price = 649.95, categoryId = 4),
                    ProductStatistic(id = 5, productId = 5, productName = "Giày", amount = 30, price = 137970.00, categoryId = 2),
                    ProductStatistic(id = 6, productId = 6, productName = "TV LED 4K", amount = 3, price = 62097.00, categoryId = 4),
                    ProductStatistic(id = 7, productId = 7, productName = "Loa", amount = 50, price = 114950.00, categoryId = 4),
                    ProductStatistic(id = 8, productId = 8, productName = "Trà Đào", amount = 20, price = 137980.00, categoryId = 3),
                    ProductStatistic(id = 9, productId = 9, productName = "Bánh tráng trộn", amount = 10, price = 80499.00, categoryId = 3),
                    ProductStatistic(id = 10, productId = 10, productName = "Giày uniset", amount = 40, price = 137960.00, categoryId = 2),
                    ProductStatistic(id = 11, productId = 11, productName = "Áo thun", amount = 45, price = 31045.50, categoryId = 5),
                    ProductStatistic(id = 12, productId = 12, productName = "Áo couple xinh xẻo", amount = 10, price = 114990.00, categoryId = 5),
                    ProductStatistic(id = 13, productId = 13, productName = "Giày Thể Thao", amount = 25, price = 459900.00, categoryId = 6),
                    ProductStatistic(id = 14, productId = 14, productName = "Giày Da", amount = 15, price = 689850.00, categoryId = 6),
                    ProductStatistic(id = 15, productId = 15, productName = "Đồ Ăn Nhanh", amount = 120, price = 120000.00, categoryId = 8),
                    ProductStatistic(id = 16, productId = 16, productName = "Đồ Uống", amount = 50, price = 68950.00, categoryId = 9)
                )

                productStatistics.forEach { productStatistic ->
                    productStatisticDAO.insertProductStatistic(productStatistic)
                }
            }
            val productStatistic = productStatisticDAO.getProductStatisticById(categoryID)


            withContext(Dispatchers.Main) {
                category?.let {
                    tvTenDanhMuc.text = it.categoryName
                }

                categoryStatistic?.let {
                    // Format the data for display
                    val formatter = DecimalFormat("#,###")
                    tvTongDoanhThuDanhMuc.text = formatter.format(it.totalRevenue)

                    tvSoSanhTongDoanhThuDanhMuc.text = if (it.totalRevenue >= 9000000) {
                        "Tăng ${
                            String.format(
                                "%.2f",
                                (it.totalRevenue / 9000000) * 100
                            )
                        }% so với tháng trước."
                    } else {
                        "Giảm ${
                            String.format(
                                "%.2f",
                                (it.totalRevenue / 9000000) * 100
                            )
                        }% so với tháng trước."
                    }

                    tvTongSanPham.text = it.numberOfProducts.toString()
                    tvSoSanhTongSanPham.text = if (it.numberOfProducts >= 12) {
                        "Tăng ${
                            String.format(
                                "%.2f",
                                (it.numberOfProducts / 10.0) * 100
                            )
                        }% so với tháng trước."
                    } else {
                        "Giảm ${
                            String.format(
                                "%.2f",
                                (it.numberOfProducts / 10.0) * 100
                            )
                        }% so với tháng trước."
                    }

                    tvTiTrongSanPham.text = "${String.format("%.2f", it.revenueShare)}%"
                }

                lvDanhSachSanPham.layoutManager = LinearLayoutManager(this@ThongKe_SanPham_DanhMuc)
                lvDanhSachSanPham.adapter = ProductAdapter(this@ThongKe_SanPham_DanhMuc, productStatistic)

                setupPieChart(productStatistic)
            }
        }

        btnReturn.setOnClickListener { finish() }
        btnStore.setOnClickListener { navigateTo(ThongKe_CuaHang::class.java) }
        btnProduct.setOnClickListener { navigateTo(ThongKe_SanPham::class.java) }
        btnAccount.setOnClickListener { navigateTo(ThongTinCaNhanActivity::class.java) }
    }

    private fun setupPieChart(products: List<ProductStatistic>) {
        val totalSum = products.sumOf { it.amount * it.price }
        val pieEntries = products.map {
            PieEntry(((it.amount * it.price / totalSum) * 100).toFloat(), it.productName)
        }

        val pieDataSet = PieDataSet(pieEntries, "Tỷ lệ sản phẩm").apply {
            colors = listOf(
                ContextCompat.getColor(this@ThongKe_SanPham_DanhMuc, R.color.purple_200),
                ContextCompat.getColor(this@ThongKe_SanPham_DanhMuc, R.color.teal_200),
                ContextCompat.getColor(this@ThongKe_SanPham_DanhMuc, R.color.yellow),
                ContextCompat.getColor(this@ThongKe_SanPham_DanhMuc, R.color.red),
                ContextCompat.getColor(this@ThongKe_SanPham_DanhMuc, R.color.teal_700),
                ContextCompat.getColor(this@ThongKe_SanPham_DanhMuc, R.color.black)
            )
            valueTextSize = 12f
            valueTextColor = Color.BLACK
        }

        val pieData = PieData(pieDataSet).apply {
            setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String =
                    "${String.format("%.1f", value)}%"
            })
        }

        pieChart.apply {
            data = pieData
            description.text = "Phần trăm giá trị theo sản phẩm"
            isDrawHoleEnabled = true
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
            animateY(1000)
            invalidate()
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
        finish()
    }
}
