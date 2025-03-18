package com.example.doan_nhom9_sang6.TienAnh_Activity

import Nam_Database.DAO.ProductDAO
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Product
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang.GioHang
import com.example.doan_nhom9_sang6.R
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import com.example.doan_nhom9_sang6.TienAnh_Activity.AdapterTrangChu.ProductAdapterTC
import com.example.doan_nhom9_sang6.TienAnh_Activity.AdapterTrangChu.BannerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TrangChuActivity : AppCompatActivity() {
    private lateinit var edtSearch: EditText
    private lateinit var imgProfile: ImageView
    private lateinit var imgHome: ImageView
    private lateinit var imgCart: ImageView
    private lateinit var gvProducts: GridView
    private lateinit var viewBanner: ViewPager
    private lateinit var productAdapter: ProductAdapterTC
    private lateinit var products: ProductDAO
    private var listProducts: MutableList<Product> = mutableListOf()
    private var productsFromDB: List<Product> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trangchu)
        products = DatabaseHelper.getDatabase(this).productDao()
        setControl()
        loadDataTrangChu()
        setEvent()
    }
    private fun loadDataProduct(text: String, productsFromDB: List<Product>){
        if(text.isEmpty()){
            listProducts.clear()
            listProducts.addAll(productsFromDB) // Gán vào listProducts
            productAdapter = ProductAdapterTC(this@TrangChuActivity, listProducts)
            gvProducts.adapter = productAdapter
        }
        else{
            listProducts.clear()
            listProducts.addAll(productsFromDB.filter { it.name.lowercase().contains(text) })
            productAdapter.notifyDataSetChanged()
        }
    }
    private fun loadDataTrangChu(){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                productsFromDB = products.getAllProducts()
                withContext(Dispatchers.Main) {
                    loadDataProduct("",productsFromDB)
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu products: ${e.message}")
            }
        }

        // Cài đặt adapter cho banner với click listener
        val bannerAdapter = BannerAdapter(this, listOf(
            R.drawable.banner,
            R.drawable.newbanner,
            R.drawable.flsale,
            R.drawable.flashsale
        )) { position ->
            val intent = Intent(this, TopBannerActivity::class.java)
            intent.putExtra("CURRENT_BANNER", position)
            startActivity(intent)
        }
        viewBanner.adapter = bannerAdapter
    }
    private fun setEvent() {
        imgProfile.setOnClickListener {
            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
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
        // Xử lý click sản phẩm
        gvProducts.setOnItemClickListener { _, _, position, _ ->
            val product = listProducts[position]
            val intent = Intent(this, SanPhamDemoActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }

        // Tự động chuyển banner
        val handler = android.os.Handler()
        var currentPage = 0
        val runnable = object : Runnable {
            override fun run() {
                if (currentPage == 3) currentPage = 0
                viewBanner.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)

        // Sự kiện tìm kiếm
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = edtSearch.text.toString().trim().lowercase()
                loadDataProduct(text,productsFromDB)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}
        }
        )
    }

    private fun setControl() {
        gvProducts = findViewById(R.id.gvProducts)
        edtSearch = findViewById(R.id.edtSearch)
        viewBanner = findViewById(R.id.imgBanner)
        imgProfile = findViewById(R.id.imgProfile)
        imgHome = findViewById(R.id.imgHome)
        imgCart = findViewById(R.id.imgCart)
    }
}
