package com.example.doan_nhom9_sang6.TienAnh_Activity

import Nam_Database.DAO.CategoryDAO
import Nam_Database.DAO.ProductDAO
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Product
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang.GioHang
import com.example.doan_nhom9_sang6.R
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import com.example.doan_nhom9_sang6.TienAnh_Activity.AdapterTrangChu.ProductAdapterTC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopBannerActivity : AppCompatActivity() {
    private lateinit var lvDanhSach: ListView
    private lateinit var edtSearch: EditText
    private lateinit var imgClothes: ImageView
    private lateinit var imgShoes: ImageView
    private lateinit var imgFoods: ImageView
    private lateinit var imgAcces: ImageView
    private lateinit var imgCategory: ImageView
    private lateinit var imgCart: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var imgvBack: ImageView
    private lateinit var imgHome: ImageView
    private lateinit var productAdapter: ProductAdapterTC
    private lateinit var products: ProductDAO
    private lateinit var categories: CategoryDAO
    private var listProducts : MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topbanner_main)

        setControl()
        loadDataTopBanner()
        setEvent()
    }
    private fun loadDataTopBanner(){
        products = DatabaseHelper.getDatabase(this).productDao()
        categories = DatabaseHelper.getDatabase(this).categoryDao()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    listProducts.clear()
                    listProducts.addAll(products.getAllProducts())
                    productAdapter.notifyDataSetChanged() // Cập nhật giao diện
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu products: ${e.message}")
            }
        }
        productAdapter = ProductAdapterTC(this, listProducts)
        lvDanhSach.adapter = productAdapter
    }

    private fun setControl() {
        lvDanhSach = findViewById(R.id.lvDanhSach)
        edtSearch = findViewById(R.id.edtSearch)
        imgClothes = findViewById(R.id.imgvClothes)
        imgShoes = findViewById(R.id.imgvShoes)
        imgFoods = findViewById(R.id.imgvFoods)
        imgAcces = findViewById(R.id.imgvAcces)
        imgHome = findViewById(R.id.imgHome)
        imgvBack = findViewById(R.id.imgvBack)
        imgCart = findViewById(R.id.imgCart)
        imgCategory = findViewById(R.id.imgCategory)
        imgProfile = findViewById(R.id.imgProfile)

    }

    private fun setEvent() {
        imgHome.setOnClickListener {
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
        }
        imgCart.setOnClickListener {
            val intent = Intent(this, GioHang::class.java)
            startActivity(intent)
        }

        imgProfile.setOnClickListener {
            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
        }

        imgvBack.setOnClickListener {
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
            finish()
        }

        lvDanhSach.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, SanPhamDemoActivity::class.java)
            intent.putExtra("PRODUCT_ID", listProducts[position].id)
            startActivity(intent)
        }

        imgClothes.setOnClickListener { filterByCategory("Quần Áo") }
        imgShoes.setOnClickListener { filterByCategory("Giày") }
        imgFoods.setOnClickListener { filterByCategory("Đồ Ăn") }
        imgAcces.setOnClickListener { filterByCategory("Điện Tử") }

    }
    private fun filterByCategory(categoryName: String) {
        var id = 0
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    id = categories.getIdCategoryByName(categoryName)
                    listProducts.clear()
                    listProducts.addAll(products.getProductsByCategoryId(id))
                    productAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu products: ${e.message}")
            }
        }
    }
}
