package com.example.doan_nhom9_sang6

import Nam_Database.DAO.CategoryDAO
import Nam_Database.DAO.ProductDAO
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Product
import Nam_Database.Table.Category
import com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap.DangNhapActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang.GioHang
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import com.example.doan_nhom9_sang6.TienAnh_Activity.TrangChuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ThongTinSanPham : AppCompatActivity() {

    private lateinit var imgAnh: ImageView
    private lateinit var imgCart: ImageView
    private lateinit var imgBack: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var imgHome: ImageView
    private lateinit var edtTen: EditText
    private lateinit var edtGia: EditText
    private lateinit var edtMoTa: EditText
    private lateinit var edtTonKho: EditText
    private lateinit var spnPhanLoai: Spinner
    private lateinit var spnAnh: Spinner
    private lateinit var btnLuuSanPham: Button
    private lateinit var categoryList: List<Category>
    private lateinit var productDAO: ProductDAO
    private lateinit var categoryDAO: CategoryDAO
    private var selectedImageName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thong_tin_san_pham)
        productDAO = DatabaseHelper.getDatabase(this).productDao()
        categoryDAO = DatabaseHelper.getDatabase(this).categoryDao()
        setControl()
        setEvent()
    }

    private fun setControl() {
        imgBack = findViewById(R.id.imgBack)
        imgCart = findViewById(R.id.imgCart)
        imgHome = findViewById(R.id.imgHome)
        imgProfile = findViewById(R.id.imgProfile)
        imgAnh = findViewById(R.id.imgAnh)
        edtTen = findViewById(R.id.edtTen)
        edtGia = findViewById(R.id.edtGia)
        edtMoTa = findViewById(R.id.edtMoTa)
        edtTonKho = findViewById(R.id.edtTonKho)
        spnPhanLoai = findViewById(R.id.spnPhanLoai)
        spnAnh = findViewById(R.id.spnAnh)
        btnLuuSanPham = findViewById(R.id.btnLuuSanPham)
    }

    private fun setEvent() {

        // Lấy danh sách ảnh
        setupAnh()
        // Lấy thông tin sản phẩm từ Intent
        val product = intent.getSerializableExtra("product") as? Product

        // Lấy danh sách danh mục từ cơ sở dữ liệu
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Lấy danh sách danh mục từ database
                categoryList = categoryDAO.getAllCategories()

                // Chạy trên main thread để cập nhật Spinner
                withContext(Dispatchers.Main) {
                    // Lấy tên danh mục để hiển thị trong Spinner
                    val categoryNames = categoryList.map { it.categoryName }

                    // Tạo adapter cho Spinner
                    val adapter = ArrayAdapter(
                        this@ThongTinSanPham,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnPhanLoai.adapter = adapter

                    // Thiết lập lựa chọn trong Spinner theo categoryId của sản phẩm
                    val selectedCategoryIndex = categoryList.indexOfFirst { it.id == product?.categoryId }
                    if (selectedCategoryIndex >= 0) {
                        spnPhanLoai.setSelection(selectedCategoryIndex)
                    }

                    // Hiển thị thông tin sản phẩm lên UI
                    edtTen.setText(product?.name ?: "")
                    edtGia.setText(product?.price?.toString() ?: "")
                    edtMoTa.setText(product?.description ?: "")
                    edtTonKho.setText(product?.stockQuantity?.toString() ?: "")

                    // Hiển thị ảnh sản phẩm
                    val resourceId = resources.getIdentifier(product?.imagePath ?: "", "drawable", packageName)
                    imgAnh.setImageResource(resourceId)
                }
            } catch (e: Exception) {
                // Xử lý lỗi nếu có
                Log.e("DatabaseError", "Error fetching categories: ${e.message}", e)
            }
        }

        imgBack.setOnClickListener {
            finish()
        }

        imgProfile.setOnClickListener {
            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
        }

        imgHome.setOnClickListener {
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
        }

        imgCart.setOnClickListener {
            val intent = Intent(this, GioHang::class.java)
            startActivity(intent)
        }

        btnLuuSanPham.setOnClickListener {
            if (edtTen.text.isEmpty() || edtGia.text.isEmpty() || edtMoTa.text.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val categoryIndex = spnPhanLoai.selectedItem.toString()
            val categoryId = categoryList.firstOrNull { it.categoryName == categoryIndex }?.id ?: 0

            // cái này là ể kiểm tra xem người dùng có chọn ảnh trong spn k nếu không chọn thì ấy ảnh mặc định đã chuyển qua
            val imagePath = if (selectedImageName.isNotEmpty()) {
                // Nếu người dùng chọn ảnh từ Spinner
                selectedImageName
            } else {
                // Nếu người dùng không chọn Spinner, dùng ảnh mặc định đã truyền qua
                product?.imagePath ?: ""
            }


            val sellerId = DangNhapActivity.id // ID mặc định của người bán

            val stockQuantityString = edtTonKho.text.toString()
            val stockQuantity: Int = stockQuantityString.toInt() // Chuyển thành Int

            val updatedProduct = Product(
                id = product?.id ?: 0,
                name = edtTen.text.toString(),
                price = edtGia.text.toString().toDouble(),
                description = edtMoTa.text.toString(),
                imagePath = imagePath,
                categoryId = categoryId,
                sellerId = sellerId,
                stockQuantity = stockQuantity,
                createdAt = LocalDate.now().toString()
            )

            CoroutineScope(Dispatchers.IO).launch {
                productDAO.updateProduct(updatedProduct)

                val new = productDAO.getProductById(updatedProduct.id)?.name?.toString() ?: "Không có tên sản phẩm"

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ThongTinSanPham, "Sửa thành công $new", Toast.LENGTH_SHORT).show()

                    // Trả kết quả về Activity trước đó
                    val resultIntent = Intent()
                    setResult(RESULT_OK, resultIntent)  // Trả kết quả thành công
                    finish()  // Đóng màn sửa sản phẩm
                }
            }
        }

    }

    private fun setupAnh() {
        val drawableFields = R.drawable::class.java.fields
        val imageNames = drawableFields.map { it.name }
            .filter { name ->
                // Lấy ID của resource
                val resId = resources.getIdentifier(name, "drawable", packageName)
                // Kiểm tra ID có hợp lệ và đảm bảo resource là hình ảnh
                try {
                    val drawable = resources.getDrawable(resId, null)
                    drawable != null
                } catch (e: Exception) {
                    false
                }
            }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, imageNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnAnh.adapter = adapter

        // Biến để kiểm tra hành động người dùng
        var isUserAction = false

        spnAnh.setOnTouchListener { _, _ ->
            // Đánh dấu rằng người dùng đã tương tác với Spinner
            isUserAction = true
            false // Trả về false để sự kiện vẫn tiếp tục
        }

        spnAnh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Chỉ xử lý khi là hành động của người dùng
                if (isUserAction) {
                    // Lấy tên ảnh được chọn
                    selectedImageName = parent.getItemAtPosition(position).toString()
                    // Lấy ID của ảnh trong drawable
                    val imageResId = resources.getIdentifier(selectedImageName, "drawable", packageName)
                    // Hiển thị ảnh vào ImageView
                    if (imageResId != 0) {
                        imgAnh.setImageResource(imageResId)
                    } else {
                        imgAnh.setImageDrawable(null) // Nếu không tìm thấy, xóa ảnh cũ
                    }
                    isUserAction = false // Reset biến cờ
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                imgAnh.setImageDrawable(null) // Không chọn gì thì xóa ảnh
            }
        }
    }

}
