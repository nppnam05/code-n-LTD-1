package com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang

import Nam_Database.DAO.*
import Nam_Database.DatabaseHelper
import Nam_Database.Table.*
import android.content.Context
import com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap.DangNhapActivity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import com.example.doan_nhom9_sang6.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class GioHang : AppCompatActivity() {
    private lateinit var imgBack: ImageView
    private lateinit var imgHome: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var cartItemDAO: CartItemDAO
    private lateinit var cartDAO: CartDAO
    private lateinit var cart: Cart
    private lateinit var rclviewCart: RecyclerView
    private val cartItemWithProducts = mutableListOf<CartItemWithProduct>()
    private lateinit var btnCapNhat: Button
    private lateinit var btnBuyAll: Button
    private lateinit var adapter: CartAdapter
    private lateinit var orderItemDAO: OrderItemDAO
    private lateinit var orderDAO: OrderDAO
    private lateinit var userDAO: UserDao
    private lateinit var user: User



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gio_hang)

        initDatabase()
        loadDataCart()
        setControl()
        setEvent()
    }

    private fun initDatabase() {
        cartItemDAO = DatabaseHelper.getDatabase(this).CartItemDAO()
        cartDAO = DatabaseHelper.getDatabase(this).cartDao()
        orderDAO = DatabaseHelper.getDatabase(this).orderDao()
        orderItemDAO = DatabaseHelper.getDatabase(this).orderItemDao()
        userDAO = DatabaseHelper.getDatabase(this).userDao()
    }

    private fun loadDataCart() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                user = userDAO.getUserById(DangNhapActivity.id)!!
                cart = cartDAO.getCartByUserId(DangNhapActivity.id)!!
                val items = cartItemDAO.getCartItemsWithProducts(cart.id)
                withContext(Dispatchers.Main) {
                    cartItemWithProducts.clear()
                    cartItemWithProducts.addAll(items)
                    setupRecyclerView()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Lỗi khi tải dữ liệu: ${e.message}")
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(
            items = cartItemWithProducts,
            onItemChanged = { itemId, quantity ->
                val item = cartItemWithProducts.find { it.cartItem.id == itemId }
                item?.cartItem?.quantity = quantity
            },
            onItemRemoved = { itemId ->
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        cartItemDAO.deleteCartItemById(itemId)
                        withContext(Dispatchers.Main) {
                            val position =
                                cartItemWithProducts.indexOfFirst { it.cartItem.id == itemId }
                            if (position >= 0) {
                                cartItemWithProducts.removeAt(position)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            println("Lỗi khi xóa sản phẩm: ${e.message}")
                        }
                    }
                }
            },
            onItemBuy = { itemId ->
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val today: LocalDate = LocalDate.now()
                        val item = cartItemWithProducts.find { it.cartItem.id == itemId }
                        if (item != null) {
                            val totalPrice = item.product.price * item.cartItem.quantity

                            val order = Order(
                                userId = user.id,
                                totalPrice = totalPrice,
                                orderDate = today.toString(),
                                shippingAddress = user.address,
                                paymentMethod = "Tiền Mặt",
                                sellerConfirmed = true,
                                orderStatus = "Pending",
                                updatedAt = today.toString()
                            )
                            val idOrder = orderDAO.insertOrder(order)

                            // Tạo và lưu OrderItem

                            val itemOrder = OrderItem(
                                orderId = idOrder.toInt(), productId = item.product.id,
                                quantity = item.cartItem.quantity, price = item.product.price
                            )
                            orderItemDAO.insertOrderItem(itemOrder)

                            // Xóa sản phẩm khỏi cơ sở dữ liệu
                            cartItemDAO.deleteCartItemById(itemId)

                            // Tải lại danh sách từ cơ sở dữ liệu
                            val updatedItems = cartItemDAO.getCartItemsWithProducts(cart.id)
                            withContext(Dispatchers.Main) {
                                cartItemWithProducts.clear()
                                cartItemWithProducts.addAll(updatedItems)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            println("Lỗi khi mua sản phẩm: ${e.message}")
                        }
                    }
                }
            }
        )
        rclviewCart.adapter = adapter
        rclviewCart.layoutManager = LinearLayoutManager(this)
    }

    private fun buyAll() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val today: LocalDate = LocalDate.now()

                val totalPrice = cartItemWithProducts.sumOf { it.product.price * it.cartItem.quantity }

                val order = Order(
                    userId = user.id,
                    totalPrice = totalPrice,
                    orderDate = today.toString(),
                    shippingAddress = user.address,
                    paymentMethod = "Tiền Mặt",
                    sellerConfirmed = true,
                    orderStatus = "Pending",
                    updatedAt = today.toString()
                )
                val idOrder = orderDAO.insertOrder(order)

                cartItemWithProducts.forEach { item ->
                    val itemOrder = OrderItem(
                        orderId = idOrder.toInt(), productId = item.product.id,
                        quantity = item.cartItem.quantity, price = item.product.price
                    )
                    orderItemDAO.insertOrderItem(itemOrder)
                    cartItemDAO.deleteCartItemById(item.cartItem.id)
                }
                cartItemWithProducts.clear()

                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Lỗi khi mua tất cả: ${e.message}")
                }
            }
        }
    }

    private fun setControl() {
        imgBack = findViewById(R.id.imgBack)
        imgProfile = findViewById(R.id.imgProfile)
        imgHome = findViewById(R.id.imgHome)
        rclviewCart = findViewById(R.id.rclviewCart)
        btnCapNhat = findViewById(R.id.btnCapNhat)
        btnBuyAll = findViewById(R.id.btnBuyAll)
    }

    private fun setEvent() {
        imgBack.setOnClickListener { finish() }
        imgHome.setOnClickListener { finish() }
        imgProfile.setOnClickListener {
            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnCapNhat.setOnClickListener { updateDatabase() }
        btnBuyAll.setOnClickListener { buyAll() }

        rclviewCart.setOnTouchListener { v, event ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val focusedView = currentFocus
            if (focusedView != null) {
                imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                focusedView.clearFocus()  // Mất focus khi click vào RecyclerView
            }
            false
        }

    }

    private fun updateDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                cartItemWithProducts.forEach { item ->
                    if (item.cartItem.quantity <= 0) {
                        cartItemDAO.deleteCartItemById(item.cartItem.id)
                    } else {
                        cartItemDAO.updateCartItem(item.cartItem)
                    }
                }

            } catch (e: Exception) {
                println("Lỗi khi cập nhật cơ sở dữ liệu: ${e.message}")
            }
        }
    }
}
