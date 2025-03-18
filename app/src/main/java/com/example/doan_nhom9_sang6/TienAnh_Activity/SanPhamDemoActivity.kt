package com.example.doan_nhom9_sang6.TienAnh_Activity

import Nam_Database.DAO.CartDAO
import Nam_Database.DAO.CartItemDAO
import Nam_Database.DAO.DAO_ThongKe.ProductStatisticDAO
import Nam_Database.DAO.ProductDAO
import Nam_Database.DatabaseHelper
import Nam_Database.Table.CartItem
import Nam_Database.Table.Product
import com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap.DangNhapActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang.GioHang
import com.example.doan_nhom9_sang6.R
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SanPhamDemoActivity : AppCompatActivity() {
    private lateinit var imgvBack: ImageView
    private lateinit var imgHome: ImageView
    private lateinit var imgCart:ImageView
    private lateinit var imgProfile:ImageView
    private lateinit var txtProductName: TextView
    private lateinit var txtProductDescription: TextView
    private lateinit var txtProductPrice: TextView
    private lateinit var txtProductSold: TextView
    private lateinit var imgProduct: ImageView
    private lateinit var btnAddToCart: Button
    private lateinit var productDAO: ProductDAO
    private lateinit var productStaticDao: ProductStatisticDAO
    private lateinit var product: Product
    private lateinit var cartDAO: CartDAO
    private lateinit var cartItemDAO: CartItemDAO
    var productId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sanphamdemo1)

        productDAO = DatabaseHelper.getDatabase(this).productDao()
        cartDAO = DatabaseHelper.getDatabase(this).cartDao()
        cartItemDAO = DatabaseHelper.getDatabase(this).CartItemDAO()
        productId = intent.getIntExtra("PRODUCT_ID", 0)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    product = productDAO.getProductById(productId)!!
                    loadDataProduct(product)
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu products: ${e.message}")
            }
        }
        setControl()
        setEvent()
    }

    private fun setEvent() {
        imgvBack.setOnClickListener {
            finish()
        }
        imgHome.setOnClickListener {
            // Trở về trang chủ
            val intent = Intent(this, TrangChuActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        imgCart.setOnClickListener {
            // Trở về trang chủ
            val intent = Intent(this, GioHang::class.java)
            startActivity(intent)
            this.finish()
        }
        imgProfile.setOnClickListener {
            // Trở về trang chủ
            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        btnAddToCart.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val cart = cartDAO.getCartByUserId(DangNhapActivity.id)
                    if (cart != null) {
                        val item = cartItemDAO.getCartItemByProductId(cart.id, productId)
                        if (item == null) {
                            // Thêm mới sản phẩm
                            product = productDAO.getProductById(productId)!!
                            val itemCart = CartItem(cartId =  cart.id, productId =  productId, quantity =  1, price = product.price)
                            cartItemDAO.insertCartItem(itemCart)
                        } else {
                            // Cập nhật số lượng sản phẩm đã tồn tại
                            item.quantity += 1
                            cartItemDAO.updateCartItem(item)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        println("Lỗi khi xử lý giỏ hàng: ${e.message}")
                    }
                }
            }

        }

    }

    private fun loadDataProduct(product: Product) {
        var productStaticAmount = 0
        productStaticDao = DatabaseHelper.getDatabase(this).productStatisticDao()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val productTemp = productStaticDao.getOneProductStatisticById(product.id)
                withContext(Dispatchers.Main) {
                    productStaticAmount = productTemp.amount
                    txtProductSold.text = "Đã bán:${productStaticAmount}"
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu User: ${e.message}")
            }
        }

        txtProductName.text = product.name
        txtProductDescription.text = product.description
        txtProductPrice.text = "${product.price} VND"
        val resId = applicationContext.resources.getIdentifier(product.imagePath, "drawable", applicationContext.packageName)// chuyển tên thành kiểu int trong drawable
        if (resId != 0) {
            imgProduct?.setImageResource(resId)
        } else {
            imgProduct?.setImageResource(R.drawable.dt10x)
        }
    }

    private fun setControl() {
        imgvBack = findViewById(R.id.imgvBack)
        imgHome = findViewById(R.id.imgHome)
        imgCart = findViewById(R.id.imgCart)
        imgProfile = findViewById(R.id.imgProfile)
        txtProductName = findViewById(R.id.txtDemo2)
        txtProductDescription = findViewById(R.id.txtThongTin)
        txtProductPrice = findViewById(R.id.txtGia)
        txtProductSold = findViewById(R.id.txtLuotBan)
        imgProduct = findViewById(R.id.imgvDemo)
        btnAddToCart = findViewById(R.id.btnThem)
    }
}
