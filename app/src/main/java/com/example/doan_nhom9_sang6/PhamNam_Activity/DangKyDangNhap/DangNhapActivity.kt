package com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap

import Nam_Database.DAO.CartDAO
import Nam_Database.DAO.CartItemDAO
import Nam_Database.DAO.CategoryDAO
import Nam_Database.DAO.DAO_ThongKe.ProductStatisticDAO
import Nam_Database.DAO.OrderDAO
import Nam_Database.DAO.OrderItemDAO
import Nam_Database.DAO.ProductDAO
import Nam_Database.DAO.UserDao
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Cart
import Nam_Database.Table.CartItem
import Nam_Database.Table.Category
import Nam_Database.Table.Product
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import Nam_Database.Table.User
import com.example.doan_nhom9_sang6.PhamNam_Activity.QuenMatKhau.QuenMatKhauActivity
import androidx.lifecycle.lifecycleScope
import com.example.doan_nhom9_sang6.R
import com.example.doan_nhom9_sang6.TienAnh_Activity.TrangChuActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DangNhapActivity : AppCompatActivity() {
    private lateinit var edtTenDangNhap:EditText
    private lateinit var edtMatKhau:EditText
    private lateinit var btnDangNhap:Button
    private lateinit var tvQuenMK:TextView
    private lateinit var tvTaoTK:TextView
    private lateinit var imgvfb:ImageView
    private lateinit var imgvinsta:ImageView
    private lateinit var imgvtwi:ImageView
    private lateinit var imgvmail:ImageView

    private lateinit var userDao: UserDao
    private lateinit var categoryDao:CategoryDAO
    private lateinit var productDao:ProductDAO
    private lateinit var cartDAO: CartDAO
    private lateinit var cartItemDAO: CartItemDAO
    private lateinit var orderDAO: OrderDAO
    private lateinit var orderItemDAO: OrderItemDAO
    private lateinit var productStatisticDAO: ProductStatisticDAO
    companion object {
        var id = 2 // lay id user cho = 2 vì 2 là Partner
    }
    var listUser = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dangnhap)

        userDao = DatabaseHelper.getDatabase(this).userDao()
        categoryDao = DatabaseHelper.getDatabase(this).categoryDao()
        productDao = DatabaseHelper.getDatabase(this).productDao()
        cartDAO = DatabaseHelper.getDatabase(this).cartDao()
        cartItemDAO = DatabaseHelper.getDatabase(this).CartItemDAO()
        orderDAO = DatabaseHelper.getDatabase(this).orderDao()
        orderItemDAO = DatabaseHelper.getDatabase(this).orderItemDao()
        productStatisticDAO = DatabaseHelper.getDatabase(this).productStatisticDao()


        addUser()
        addCategory()
        addProduct()
        addItemCartForCartID1()
        addCart()
        lifecycleScope.launch(Dispatchers.IO){
            val temp = userDao.getAllUsers()
            try {
                withContext(Dispatchers.Main) {
                    listUser.clear()
                    listUser.addAll(temp)
                    println("Danh sach user: "+userDao.getAllUsers().size)
                    println("Danh sách Product "+productDao.getAllProducts().size)
                    println("Danh sách category: "+categoryDao.getAllCategories().size)
                    println("Danh sach Cart: "+cartDAO.getAllCart().size)
                    println("Danh sách CartItem: "+ cartItemDAO.getAllCartItem().size)
                    println("Danh sach Order: "+orderDAO.getAllOrders().size)
                    println("Danh sach OrderItem: "+orderItemDAO.getAllOrderItem().size)
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu User: ${e.message}")
            }
        }



        setControl();
        setEvent();
    }
    fun addCart(){
        val carts = listOf<Cart>(
            Cart(1,1, "2024-12-19","Pending"),
            Cart(2,2,"2024-12-19","Pending"),
            Cart(3,3,"2024-12-19","Pending")
        )
        lifecycleScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main) {
                    carts.forEach { item ->
                        cartDAO.insertCart(item)
                    }
                }
            } catch (e: Exception) {
                println("Lỗi add dữ liệu User: ${e.message}")
            }
        }
    }
    fun addUser(){
        val users = listOf<User>(
            User(
                id = 1,
                firstName = "John",
                lastName = "Doe",
                email = "nppnam05@gmail.com",
                phoneNumber = "123456789",
                userName = "a",
                passWord = "a",
                access = "User",
                created = "2024-12-19",
                storeName = null,
                address = "sara Street",
                categorie = null
            ),
            User(
                id = 2,
                firstName = "Alice",
                lastName = "Johnson",
                email = "nppnam05@gmail.com",
                phoneNumber = "555666777",
                userName = "b",
                passWord = "b",
                access = "Partner",
                created = "2024-12-19",
                storeName = "123 Main Street",
                address = "789 Oak Avenue",
                categorie = "Electronics"
            ),
            User(
                id = 3,
                firstName = "Jane",
                lastName = "Smith",
                email = "jane.smith@example.com",
                phoneNumber = "987654321",
                userName = "c",
                passWord = "c",
                access = "User",
                created = "2024-12-19",
                storeName = null,
                address = "456 Elm Street",
                categorie = null
            )
        )
        lifecycleScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main) {
                    users.forEach { item ->
                        userDao.insertUser(item)
                    }
                }
            } catch (e: Exception) {
                println("Lỗi add dữ liệu User: ${e.message}")
            }
        }
    }
    fun addProduct(){
        val products = listOf(
            Product(
                id = 1,
                name = "Áo hoodies",
                price = 699.99,
                description = "A high-end smartphone with 128GB storage.",
                imagePath = "hoodies",
                categoryId = 1,
                sellerId = 2, // Alice is the seller (Partner)
                stockQuantity = 50,
                createdAt = "2024-12-19"
            ),
            Product(
                id = 2,
                name = "Giày Chạy Bộ",
                price = 999.99,
                description = "A powerful laptop with 16GB RAM and 512GB SSD.",
                imagePath = "giaychaybo",
                categoryId = 2,
                sellerId = 2, // Alice is the seller (Partner)
                stockQuantity = 30,
                createdAt = "2024-12-19"
            ),
            Product(
                id = 3,
                name = "Trà Sữa",
                price = 699.99,
                description = "A high-end smartphone with 128GB storage.",
                imagePath = "trasua",
                categoryId = 3,
                sellerId = 2, // Alice is the seller (Partner)
                stockQuantity = 500,
                createdAt = "2024-12-19"
            ),
            Product(
                id = 4,
                name = "Lap Top G5",
                price = 129.99,
                description = "A stylish and warm jacket for winter.",
                imagePath = "g5",
                categoryId = 4,
                sellerId = 2, // Alice is the seller (Partner)
                stockQuantity = 100,
                createdAt = "2024-12-19"
            ),
            Product(
                5,
                "Giày",
                4599000.0,
                "Giày chạy bộ nhức nách.",
                "giay",
                2,
                2,
                100,
                "2024-01-04"
            ),
            Product(
                6,
                "TV LED 4K",
                20699000.0,
                "Màn hình hiển thị 4K rõ nét.",
                "tv",
                4,
                2,
                10,
                "2024-01-05"
            ),
            Product(
                7,
                "Loa",
                2299000.0,
                "Loa RGB tùy chỉnh.",
                "loa",
                4,
                2,
                75,
                "2024-01-06"
            ),
            Product(
                8,
                "Trà Đào",
                6899000.0,
                "Đậm đà ",
                "tradao",
                3,
                2,
                40,
                "2024-01-07"
            ),
            Product(
                9,
                "Bánh tráng trộn",
                8049900.0,
                "Phần đặc biệt 2 trứng.",
                "banhtrangtron",
                3,
                2,
                30,
                "2024-01-08"
            ),
            Product(
                10,
                "Giày uniset",
                3449000.0,
                "Phù hợp cho cả nam và nữ",
                "giayuniset",
                2,
                2,
                120,
                "2024-01-09"
            ),
            Product(
                11,
                "Iphone 16",
                5749000.0,
                "Trải nghiệm đỉnh cao.",
                "iphone16",
                4,
                2,
                85,
                "2024-01-10"
            ),
            Product(
                12,
                "Xe điện Scooter",
                11499000.0,
                "Phương tiện giao thông thân thiện với môi trường.",
                "/images/electric_scooter.png",
                5,
                2,
                20,
                "2024-01-11"
            ),
            Product(
                13,
                "Áo thun",
                6899000.0,
                "Coton mát mẻ.",
                "aothun",
                1,
                2,
                60,
                "2024-01-12"
            ),
            Product(
                14,
                "Áo thun Gấu ",
                4599000.0,
                "Phù hợp vs cặp đôi.",
                "aothungau",
                1,
                2,
                35,
                "2024-01-13"
            ),
            Product(
                15,
                "Giày chạy bộ",
                1839000.0,
                "Thoải mái và bền bỉ.",
                "giay",
                2,
                2,
                90,
                "2024-01-14"
            ),
            Product(
                16,
                "Trà đào size L",
                2999000.0,
                "Full topping .",
                "tradao",
                3,
                2,
                65,
                "2024-01-15"
            ),
            Product(
                17,
                "Sữa tươi chân châu đường đen",
                1599000.0,
                "Ăn bon miệng.",
                "trasua",
                3,
                2,
                110,
                "2024-01-16"
            ),
            Product(
                18,
                "Điện thoại SamSung",
                8049900.0,
                "Dành cho các chuyên gia sáng tạo.",
                "x10",
                4,
                2,
                45,
                "2024-01-17"
            ),
            Product(
                19,
                "Loa LJB 1000",
                1149000.0,
                "Âm thanh 10/10",
                "loa",
                4,
                2,
                130,
                "2024-01-18"
            ),
            Product(
                20,
                "Áo couple xinh xẻo",
                11499000.0,
                "Hoàn hảo cho người mới bắt đầu.",
                "aothungau",
                1,
                2,
                15,
                "2024-01-19"
            ),
            Product(
                21,
                "Giày Cat&Sofa",
                689900.0,
                "Siêu bền, sang, đẹp.",
                "giayuniset",
                2,
                2,
                200,
                "2024-01-20"
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
                10,
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
                10,
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
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                   products.forEach { it ->
                       productDao.insertProduct(it)
                   }
                }
            } catch (e: Exception) {
                println("Lỗi ko add ợc product: ${e.message}")
            }
        }

    }
    fun addCategory(){
        val categories = listOf<Category>(
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
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    categories.forEach { item ->
                        categoryDao.insertCategory(item)
                    }
                }
            } catch (e: Exception) {
                println("Lỗi add dữ liệu User: ${e.message}")
            }
        }
    }
    fun addItemCartForCartID1(){
        val itemCart = listOf<CartItem>(
            CartItem(1,1,1,1,100.0),
            CartItem(2,1,3,3,300.0),
            CartItem(3,1,2,2,200.0)
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    itemCart.forEach { it ->
                        cartItemDAO.insertCartItem(it)
                    }
                }
            }
            catch (e: Exception){
                println("Loi :" + e.message)
            }
        }
    }
    fun dangNhap(){
        var arrCheck = BooleanArray(2)
        if(edtTenDangNhap.text.isNullOrEmpty()){
            edtTenDangNhap.error = "Vui lòng nhập tên đăng nhập"
        }
        else arrCheck[0] = true
        if(edtMatKhau.text.isNullOrEmpty()){
            edtMatKhau.error = "Vui lòng nhập mật khẩu"
        }
        else arrCheck[1] = true
        for(i in 0..1){
            if(!arrCheck[i]){
                return
            }
        }
        var login = false
        for(item in listUser){
            if(item.userName == edtTenDangNhap.text.toString() && item.passWord == edtMatKhau.text.toString()){
                id = item.id
                val intent = Intent(this, TrangChuActivity::class.java)
                startActivity(intent)
                this.finish()
                login = true
            }
        }
        if(!login){
            Toast.makeText(this,"Sai Username hoặc Password",Toast.LENGTH_SHORT).show()
        }
    }
    private fun setEvent(){
        btnDangNhap.setOnClickListener(){
            dangNhap()
        }
        tvQuenMK.setOnClickListener(){
            val intent = Intent(this, QuenMatKhauActivity::class.java)
            startActivity(intent)
        }
        tvTaoTK.setOnClickListener(){
            val intent = Intent(this, TaoTaiKhoanActivity::class.java)
            startActivity(intent)
        }
        imgvfb.setOnClickListener(){
            // code lệnh chuyển qua trang home
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        imgvinsta.setOnClickListener(){
            // code lệnh chuyển qua trang home
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        imgvtwi.setOnClickListener(){
            // code lệnh chuyển qua trang home
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        imgvmail.setOnClickListener(){
            // code lệnh chuyển qua trang home
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
    private fun setControl(){
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap)
        edtMatKhau = findViewById(R.id.edtMatKhau)
        btnDangNhap = findViewById(R.id.btnDangNhap)
        tvQuenMK = findViewById(R.id.tvQuenMatKhau)
        tvTaoTK = findViewById(R.id.tvTaoTaiKhoan)
        imgvfb = findViewById(R.id.ivfb)
        imgvinsta = findViewById(R.id.ivinsta)
        imgvtwi = findViewById(R.id.ivtwi)
        imgvmail = findViewById(R.id.ivmail)
    }
}