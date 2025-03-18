package com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.doan_nhom9_sang6.R


class DieuKhoanActivity : AppCompatActivity() {
    private lateinit var btnThoat:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dieukhoan)
        setControl()
        btnThoat.setOnClickListener(){
            this.finish()
        }
    }
    private fun setControl(){
        btnThoat = findViewById(R.id.btnThoat)
    }
}