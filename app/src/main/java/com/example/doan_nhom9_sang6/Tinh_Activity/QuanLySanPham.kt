package com.example.doan_nhom9_sang6

import Nam_Database.DAO.CategoryDAO
import Nam_Database.DAO.ProductDAO
import Nam_Database.DAO.UserDao
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Product
import com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap.DangNhapActivity
import android.text.Editable
import android.text.TextWatcher
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.doan_nhom9_sang6.Kiet_Activity.ThongKe_CuaHang
import com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang.GioHang
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import com.example.doan_nhom9_sang6.TienAnh_Activity.TrangChuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tinh.ITOnlickActionListener
import tinh.ProductAdapterQL

class QuanLySanPham : AppCompatActivity(), ITOnlickActionListener {
    private lateinit var lvQuanLySanPham: ListView
    private var productList: MutableList<Product> = mutableListOf()
    private var filteredProductList: MutableList<Product> = mutableListOf() // Danh sách sản phẩm đã lọc
    private lateinit var adapter: ProductAdapterQL
    private lateinit var imgRing: ImageView
    private lateinit var imgCart: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var imgHome: ImageView
    private lateinit var imgThongKe: ImageView
    private lateinit var btnThem: Button
    private lateinit var imgvBack: ImageView
    private lateinit var edtTimSanPham: EditText


    private lateinit var productDao: ProductDAO
    private lateinit var userDao: UserDao
    private lateinit var categoryDao: CategoryDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_ly_san_pham)

        productDao = DatabaseHelper.getDatabase(this).productDao()
        categoryDao = DatabaseHelper.getDatabase(this).categoryDao()
        userDao = DatabaseHelper.getDatabase(this).userDao()


        setControl()
        setEvent()
    }

    // Hàm load dữ liệu từ cơ sở dữ liệu
    private fun loadProductsFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            // Đảm bảo bạn đã lấy danh sách sản phẩm từ database
            val idUser = DangNhapActivity.id
            // lấy những sản phảm của người bán
            val allProducts = productDao.getProductsWhoSeller(idUser)

            withContext(Dispatchers.Main) {
                // Làm mới danh sách sản phẩm
                productList.clear()
                productList.addAll(allProducts)
                filteredProductList.clear()
                filteredProductList.addAll(allProducts)

                // Sắp xếp danh sách sản phẩm theo ID giảm dần (hoặc bạn có thể thay đổi theo yêu cầu)
                filteredProductList.sortByDescending { it.id }

                // Cập nhật adapter
                adapter.updateProducts(filteredProductList)
            }
        }
    }

    private val addProductLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        loadProductsFromDatabase()
    }

    private val editProductLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loadProductsFromDatabase()
        }
    }

    // để thực hiện hành động này thì phải cần 1 lớp thực thi nó là QuanLySanPham : AppCompatActivity(), ITOnlickActionListener
    // và trong lớp đó phải triển khai các pthức mà ITOnlickActionListener này có
    // QuanLySanPham ghi đè(override) lên cái onEditProduct
    // cái detele cũng tương tự
    override fun onEditProduct(product: Nam_Database.Table.Product, position: Int) {
        val intent = Intent(this, ThongTinSanPham::class.java) // Tạo Intent để mở Activity chỉnh sửa
        intent.putExtra("product", product) // Truyền đối tượng sản phẩm qua Intent
        editProductLauncher.launch(intent) // Sử dụng launcher để nhận kết quả
    }


    override fun onDeleteProduct(position: Int) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Xóa sản phẩm")
            .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
            .setPositiveButton("OK") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Lấy sản phẩm cần xóa
                    val productToDelete = filteredProductList[position]

                    // Thực hiện xóa trong DB
                    productDao.deleteProduct(productToDelete)

                    // Chuyển về UI thread để cập nhật UI
                    withContext(Dispatchers.Main) {
                        loadProductsFromDatabase()
                        Toast.makeText(this@QuanLySanPham, "Xóa thành công", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Hủy") { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }

    private fun setControl() {
        imgvBack = findViewById(R.id.imgBack)
        imgRing = findViewById(R.id.imgRing)
        imgCart = findViewById(R.id.imgCart)
        imgHome = findViewById(R.id.imgHome)
        imgProfile = findViewById(R.id.imgProfile)
        imgThongKe = findViewById(R.id.imgThongKe)
        btnThem = findViewById(R.id.btnThem)
        lvQuanLySanPham = findViewById(R.id.lvQuanLySanPham)
        edtTimSanPham = findViewById(R.id.edtTimSanPham)
    }

    private fun setEvent() {

        adapter = ProductAdapterQL(this, filteredProductList,this)
        lvQuanLySanPham.adapter = adapter

        lvQuanLySanPham.setOnItemClickListener { _, _, position, _ ->
            val product = productList[position]
            onEditProduct(product, position) // Sử dụng phương thức chung
        }

        CoroutineScope(Dispatchers.Main).launch {
            productList = productDao.getAllProducts().toMutableList()
            filteredProductList = productList.toMutableList()
            runOnUiThread {
                adapter.updateProducts(filteredProductList)
            }
        }

        // Thêm sự kiện cho EditText (TextWatcher)
        edtTimSanPham.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().toLowerCase() // Lấy văn bản tìm kiếm, chuyển sang chữ thường
                filterProducts(query) // Gọi hàm lọc sản phẩm
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        imgvBack.setOnClickListener {
            finish()
        }
        imgProfile.setOnClickListener {
            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
            finish()
        }
        imgHome.setOnClickListener {
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
            finish()
        }
        imgCart.setOnClickListener {
            val intent = Intent(this, GioHang::class.java)
            startActivity(intent)
        }
        imgThongKe.setOnClickListener {
            val intent = Intent(this, ThongKe_CuaHang::class.java)
            startActivity(intent)
        }
        btnThem.setOnClickListener {
            val intent = Intent(this, ThemSanPham::class.java)
            addProductLauncher.launch(intent) // Khởi động activity thêm sản phẩm
        }
    }

    // Hàm lọc sản phẩm theo tên hoặc mô tả
    private fun filterProducts(query: String) {
        filteredProductList.clear() // Xóa danh sách đã lọc trước đó
        if (query.isEmpty()) {
            filteredProductList.addAll(productList) // Nếu không có gì nhập, hiển thị tất cả sản phẩm
        } else {
            filteredProductList.addAll(productList.filter {
                // Lọc sản phẩm dựa trên tên hoặc thể loại (category)
                it.name.toLowerCase().contains(query.toLowerCase())
            })
        }
        adapter.updateProducts(filteredProductList) // Cập nhật lại giao diện sau khi lọc
    }
}
