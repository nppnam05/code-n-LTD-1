package com.example.doan_nhom9_sang6.PhamNam_Activity.QuenMatKhau

import Nam_Database.DAO.UserDao
import Nam_Database.DatabaseHelper
import Nam_Database.Table.User
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doan_nhom9_sang6.R
import com.example.doan_nhom9_sang6.PhamNam_Activity.QuenMatKhau.Email.EmailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class QuenMatKhauActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var btnThoat: Button
    private lateinit var btnGuiMa: Button
    var randomNumber = 0;


    private lateinit var userDao: UserDao
    var listUser = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.quenmatkhau)

        userDao = DatabaseHelper.getDatabase(this).userDao()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    listUser.clear()
                    listUser.addAll(userDao.getAllUsers())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Lỗi read dữ liệu User : ${e.message}")
                }
            }
        }

        setControl()
        setEvent()
    }
    private fun setEvent() {
        btnGuiMa.setOnClickListener {
            val email = edtEmail.text.toString()
            var check = false
            for(user in listUser){
                if(user.email == email){
                    check = true
                    break
                }
            }
            if(check){
                guiMa()
                val intent = Intent(this, QuenMK_Tim::class.java)
                intent.putExtra("Ma",randomNumber.toString())
                intent.putExtra("Email",email)
                startActivity(intent)
                this.finish()
            }
            else{
                Toast.makeText(this,"Email này chưa từng được đăng ký trước đó",Toast.LENGTH_SHORT).show()
            }
        }
        btnThoat.setOnClickListener {
            finish()
        }
    }
    private fun guiMa() {
        if (edtEmail.text.isNullOrEmpty()) {
            edtEmail.error = "Vui lòng nhập Email"
            return
        }
        randomNumber = Random.nextInt(100000, 999999)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.IO) {
                    val emailSender = EmailSender("23211tt1909@mail.tdc.edu.vn", "ixal ctqg kvht zgur") // mk ứng dụng
                    val recipientEmail = edtEmail.text.toString() // email được gửi tới
                    val subject = "Khôi phục mật khẩu của bạn" // tiêu đề email
                    val body = "Xin chào, mã xác thực của bạn là: $randomNumber"
                    emailSender.sendEmail(recipientEmail, subject, body)
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@QuenMatKhauActivity, "Đã gửi mã khôi phục, vui lòng kiểm tra email.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@QuenMatKhauActivity, "Gửi email thất bại: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setControl() {
        edtEmail = findViewById(R.id.edtEmail)
        btnThoat = findViewById(R.id.btnThoat)
        btnGuiMa = findViewById(R.id.btnGuiMa)
    }
}
