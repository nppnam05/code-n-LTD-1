package com.example.doan_nhom9_sang6.PhamNam_Activity

import Nam_Database.DAO.UserDao
import Nam_Database.DatabaseHelper
import Nam_Database.Table.User
import PhamNam_Activity.DangKyPartner
import com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap.DangNhapActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doan_nhom9_sang6.QuanLySanPham
import com.example.doan_nhom9_sang6.moRong.ChoGiaoHangActivity
import com.example.doan_nhom9_sang6.moRong.ChoLayHangActivity
import com.example.doan_nhom9_sang6.moRong.ChoXacNhanActivity
import com.example.doan_nhom9_sang6.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThongTinCaNhanActivity : AppCompatActivity() {
    private lateinit var btnCuahang: Button
    private lateinit var btnDangXuat: Button
    private lateinit var imgvBack:ImageView
    private lateinit var lloutChoXacNhan:LinearLayout
    private lateinit var lloutChoLayHang:LinearLayout
    private lateinit var lloutChoGiaoHang:LinearLayout
    private lateinit var UserDao: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_thong_tin_ca_nhan)
        UserDao = DatabaseHelper.getDatabase(this).userDao()
        setControl()
        setEvent()
    }
    private fun setControl(){
        btnCuahang = findViewById(R.id.btnCuaHang)
        btnDangXuat = findViewById(R.id.btnDangXuat)
        imgvBack = findViewById(R.id.imgBack)
        lloutChoXacNhan = findViewById(R.id.lloutChoXacNhan)
        lloutChoLayHang = findViewById(R.id.lloutChoLayHang)
        lloutChoGiaoHang = findViewById(R.id.lloutChoGiaoHang)
    }
    private fun setEvent(){
        btnDangXuat.setOnClickListener {
            val intent = Intent(this, DangNhapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        btnCuahang.setOnClickListener {
            val id = DangNhapActivity.id
            var user: User? = null
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    withContext(Dispatchers.Main) {
                        user = UserDao.getUserById(id)
                        println(user)
                        if(user?.access == "Partner") {
                            val intent = Intent(this@ThongTinCaNhanActivity, QuanLySanPham::class.java)
                            startActivity(intent)
                        }
                        else{
                            val intent = Intent(this@ThongTinCaNhanActivity, DangKyPartner::class.java)
                            startActivity(intent)
                        }
                    }
                } catch (e: Exception) {
                    println("Lỗi read dữ liệu products: ${e.message}")
                }
            }
        }
        imgvBack.setOnClickListener{
            finish()
        }
        lloutChoXacNhan.setOnClickListener{
            val intent = Intent(this, ChoXacNhanActivity::class.java)
            startActivity(intent)
        }
        lloutChoLayHang.setOnClickListener{
            val intent = Intent(this, ChoLayHangActivity::class.java)
            startActivity(intent)
        }
        lloutChoGiaoHang.setOnClickListener{
            val intent = Intent(this, ChoGiaoHangActivity::class.java)
            startActivity(intent)
        }
    }
}