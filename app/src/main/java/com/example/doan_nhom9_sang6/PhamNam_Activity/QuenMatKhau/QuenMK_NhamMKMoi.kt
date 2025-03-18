package com.example.doan_nhom9_sang6.PhamNam_Activity.QuenMatKhau

import Nam_Database.DAO.UserDao
import Nam_Database.DatabaseHelper
import Nam_Database.Table.User
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap.DangNhapActivity
import com.example.doan_nhom9_sang6.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuenMK_NhamMKMoi : AppCompatActivity() {
    private lateinit var btnThoat : Button
    private lateinit var btnXacNhan : Button
    private lateinit var edtMatKhau : EditText
    private lateinit var edtXacThucMatKhau : EditText
    private lateinit var userDao: UserDao

    var listUser = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quen_mk_nham_mkmoi)


        userDao = DatabaseHelper.getDatabase(this).userDao()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    listUser.clear()
                    listUser.addAll( userDao.getAllUsers())
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu User: ${e.message}")
            }
        }

        setControl()
        setEvent()
    }
    fun kiemTra():Boolean{
        var arrCheck = BooleanArray(2)
        if(edtMatKhau.text.isNullOrEmpty()){
            edtMatKhau.error = "Vui lòng nhập mật khẩu mới"
        }
        else arrCheck[0] = true
        if(edtXacThucMatKhau.text.isNullOrEmpty()){
            edtXacThucMatKhau.error = "Vui lòng nhập mật khẩu mới lần 2"
        }
        else arrCheck[1] = true
        if(edtMatKhau.text.toString() != edtXacThucMatKhau.text.toString()) {
            edtXacThucMatKhau.error = "mật khẩu chưa trùng"
            return false;
        }
        for(i in 0..1){
            if(!arrCheck[i])
                return false
        }
        return true
    }
    fun setEvent(){
        btnXacNhan.setOnClickListener {
            if(kiemTra()){
                var matKhauMoi = edtXacThucMatKhau.text.toString()
                val email = intent.getStringExtra("Email")
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        if(email != null){
                            userDao.updatePassword(email,matKhauMoi)
                        }
                    } catch (e: Exception) {
                        println("Lỗi update 1 dữ liệu User: ${e.message}")
                    }
                }

                val intent = Intent(this, DangNhapActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        btnThoat.setOnClickListener {
            this.finish()
        }
    }
    fun setControl(){
        btnThoat = findViewById(R.id.btnThoat)
        btnXacNhan = findViewById(R.id.btnXacNhan)
        edtMatKhau = findViewById(R.id.edtMatKhau)
        edtXacThucMatKhau = findViewById(R.id.edtXacThucMatKhau)
    }
}