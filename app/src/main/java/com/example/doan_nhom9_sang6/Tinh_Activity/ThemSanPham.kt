package com.example.doan_nhom9_sang6

import Nam_Database.DAO.CategoryDAO
import Nam_Database.DAO.ProductDAO
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Product
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

class ThemSanPham : AppCompatActivity() {
    private lateinit var imgBack: ImageView
    private lateinit var imgCart: ImageView
    private lateinit var imgAnh: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var imgHome: ImageView
    private lateinit var edtTen: EditText
    private lateinit var edtGia: EditText
    private lateinit var edtTonKho: EditText
    private lateinit var edtMoTa: EditText
    private lateinit var btnLuuSanPham: Button
    private lateinit var spPhanLoai: Spinner
    private lateinit var spAnh: Spinner
    private lateinit var productDAO : ProductDAO
    private lateinit var categoryDAO : CategoryDAO
    private var selectedImageName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_them_san_pham)
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
        spPhanLoai = findViewById(R.id.spPhanLoai)
        spAnh = findViewById(R.id.spnAnh)
        btnLuuSanPham = findViewById(R.id.btnLuuSanPham)
    }

    private fun setEvent() {
        // Khởi tạo cơ sở dữ liệu
        productDAO = DatabaseHelper.getDatabase(this).productDao()
        categoryDAO = DatabaseHelper.getDatabase(this).categoryDao()

        // Tải dữ liệu danh mục vào Spinner
        setupSpinner()
        // Tải tên ảnh vào spinner
        setupAnh()

        // Xử lý các nút điều hướng
        imgBack.setOnClickListener { finish() }
        imgCart.setOnClickListener {
            val intent = Intent(this, GioHang::class.java)
            startActivity(intent)
        }
        imgProfile.setOnClickListener {
            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
        }
        imgHome.setOnClickListener {
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện lưu sản phẩm
        btnLuuSanPham.setOnClickListener {
            val name = edtTen.text.toString()
            val desc = edtMoTa.text.toString()
            val stockQuantityString = edtTonKho.text.toString()
            val stockQuantity: Int = stockQuantityString.toInt() // Chuyển thành Int
            val price = edtGia.text.toString().toDoubleOrNull()
            val categoryID = spPhanLoai.selectedItemPosition + 1

            if (name.isNotEmpty() && price != null && selectedImageName != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val sellerId = DangNhapActivity.id // ID mặc định của người bán
                    val newProduct = Product(
                        name = name,
                        price = price,
                        description = desc,
                        imagePath = selectedImageName,
                        categoryId = categoryID,
                        sellerId = sellerId,
                        stockQuantity = stockQuantity,
                        createdAt = LocalDate.now().toString()
                    )
                    productDAO.insertProduct(newProduct)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ThemSanPham,
                            "Sản phẩm đã được thêm thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setupAnh(){
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
        spAnh.adapter = adapter

        spAnh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Lấy tên ảnh được chọn
                selectedImageName = parent.getItemAtPosition(position).toString()
                // Lấy ID của ảnh trong drawable
                val imageResId = resources.getIdentifier(selectedImageName, "drawable", packageName)
                // Hiển thị ảnh vào ImageView
                imgAnh.setImageResource(imageResId)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                imgAnh.setImageDrawable(null) // Không chọn gì thì xóa ảnh
            }
        }

    }

    private fun setupSpinner() {
        CoroutineScope(Dispatchers.IO).launch {
            val categories = categoryDAO.getAllCategories()
            Log.d("Categories", "Loaded categories: $categories") // Kiểm tra danh mục

            withContext(Dispatchers.Main) {
                val spinnerAdapter = ArrayAdapter(
                    this@ThemSanPham,
                    android.R.layout.simple_spinner_dropdown_item,
                    categories.map { it.categoryName } // Hiển thị tên danh mục trong Spinner
                )
                spPhanLoai.adapter = spinnerAdapter
                spPhanLoai.tag = categories.map { it.id } // Lưu danh sách ID để sử dụng khi lưu sản phẩm
            }
        }
    }
}
