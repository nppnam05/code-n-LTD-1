package com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap

import Nam_Database.DAO.CartDAO
import Nam_Database.DAO.UserDao
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Cart
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import Nam_Database.Table.User
import androidx.lifecycle.lifecycleScope
import com.example.doan_nhom9_sang6.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class TaoTaiKhoanActivity : AppCompatActivity() {
    private lateinit var edtTen:EditText
    private lateinit var edtHo:EditText
    private lateinit var edtEmail:EditText
    private lateinit var edtSDT:EditText
    private lateinit var edtTenDangNhap:EditText
    private lateinit var edtMatKhau:EditText
    private lateinit var edtXacThucMatKhau:EditText
    private lateinit var cbChapNhan:CheckBox
    private lateinit var tvDocDieuKhoan:TextView
    private lateinit var edtDiaChi: EditText
    private lateinit var btnDangKy:Button
    private lateinit var tvDangNhap:TextView
    private lateinit var cartDAO: CartDAO


    private lateinit var userDao: UserDao
    var listUser = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dangky)

        userDao = DatabaseHelper.getDatabase(this).userDao()
        cartDAO = DatabaseHelper.getDatabase(this).cartDao()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    listUser.clear()
                    listUser.addAll(userDao.getAllUsers())
                    println("Danh sách người dùng: $listUser")
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu User: ${e.message}")
            }
        }

        setControl()
        setEvent()
    }
    private fun kiemTraDangKy():Boolean{
        val matKhau = edtMatKhau.text.toString()
        val xacThucMK = edtXacThucMatKhau.text.toString()
        val arrCheck = BooleanArray(10)
        if(edtTen.text.isNullOrEmpty()){
            edtTen.error = "Vui lòng nhập tên"
        }
        else arrCheck[0] = true
        if(edtHo.text.isNullOrEmpty()){
            edtHo.error = "Vui lòng nhập họ"
        }
        else arrCheck[1] = true
        if(edtEmail.text.isNullOrEmpty()){
            edtEmail.error = "Vui lòng nhập Email"
        }
        else arrCheck[2] = true
        if(edtSDT.text.isNullOrEmpty()){
            edtSDT.error = "Vui lòng nhập SDT"
        }
        else arrCheck[3] = true
        if(edtTenDangNhap.text.isNullOrEmpty()){
            edtTenDangNhap.error = "Vui lòng nhập Tên đăng nhập"
        }
        else arrCheck[4] = true
        if(edtMatKhau.text.isNullOrEmpty()){
            edtMatKhau.error = "Vui lòng nhập mật khẩu"
        }
        else arrCheck[5] = true
        if(edtXacThucMatKhau.text.isNullOrEmpty()){
            edtXacThucMatKhau.error = "Vui lòng nhập lại mật khẩu"
        }
        else arrCheck[6] = true
        if(matKhau != xacThucMK){
            edtXacThucMatKhau.error = "Xác thực mật khẩu chưa đúng"
        }
        else arrCheck[7] = true
        if(edtDiaChi.text.isNullOrEmpty()){
            edtDiaChi.error = "Vui long nhap dia chi"
        }
        else arrCheck[8] = true
        if( cbChapNhan.isChecked == false){
            Toast.makeText(this, "Vui lòng chọn Tôi chấp nhận các điều khoản.", Toast.LENGTH_SHORT).show()
        }
        else arrCheck[9] = true
        for(i in 0..9 ){
            if(!arrCheck[i])
                return false
        }
        return true
    }
    private fun dangKy(){

        for(item in listUser){
            if(item.userName == edtTenDangNhap.text.toString() && item.passWord == edtMatKhau.text.toString()){
                Toast.makeText(this,"Nick đã có người dùng",Toast.LENGTH_SHORT).show()
                return
            }
        }
        val today: LocalDate = LocalDate.now()
        val user = User(
            firstName = edtTen.text.toString(),
            lastName = edtHo.text.toString(),
            email = edtEmail.text.toString(),
            phoneNumber = edtSDT.text.toString(),
            userName = edtTenDangNhap.text.toString(),
            passWord = edtMatKhau.text.toString(),
            access = "User",
            created = today.toString(),
            storeName = null,
            address = edtDiaChi.text.toString()
        )


        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val idUser = userDao.insertUser(user)
                val cart = Cart(userId = idUser.toInt(), createdAt = today.toString(), status = "Pending")
                cartDAO.insertCart(cart)
            } catch (e: Exception) {
                println("Lỗi insert dữ liệu User: ${e.message}")
            }
        }

        val inten = Intent(this, DangNhapActivity::class.java)
        startActivity(inten)
        this.finish()
    }
    private fun setEvent(){
        btnDangKy.setOnClickListener{
            if(kiemTraDangKy()){
                dangKy()
            }
        }
        tvDocDieuKhoan.setOnClickListener{
            val inten = Intent(this, DieuKhoanActivity::class.java)
            startActivity(inten)
        }
        tvDangNhap.setOnClickListener{
            val inten = Intent(this, DangNhapActivity::class.java)
            startActivity(inten)
            this.finish()
        }
    }
    private fun setControl(){
        edtTen = findViewById(R.id.edtTen)
        edtHo = findViewById(R.id.edtTenDem)
        edtEmail = findViewById(R.id.edtEmail)
        edtSDT = findViewById(R.id.edtSDT)
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap)
        edtMatKhau = findViewById(R.id.edtMatKhau)
        edtXacThucMatKhau = findViewById(R.id.edtXacThucMatKhau)
        edtDiaChi = findViewById(R.id.edtDiaChi)
        cbChapNhan = findViewById(R.id.cbChapNhan)
        tvDocDieuKhoan = findViewById(R.id.tvDocDieuKhoan)
        btnDangKy = findViewById(R.id.btnDangKy)
        tvDangNhap = findViewById(R.id.tvDangNhap)
    }
}