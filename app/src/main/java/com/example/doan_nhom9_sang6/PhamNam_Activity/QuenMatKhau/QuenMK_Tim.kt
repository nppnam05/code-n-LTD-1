package com.example.doan_nhom9_sang6.PhamNam_Activity.QuenMatKhau

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.doan_nhom9_sang6.R

class QuenMK_Tim : AppCompatActivity() {
    private lateinit var edtMa : EditText
    private lateinit var btnThoat: Button
    private lateinit var btnXacNhan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quen_mk_tim)
        setControl()
        setEvent()
    }

    private fun tinKiem() {
        if (edtMa.text.isNullOrEmpty()) {
            edtMa.error = "Vui lòng nhập mã PIN"
            return
        }

        val number = intent.getStringExtra("Ma")
        val email = intent.getStringExtra("Email")
        val intent = Intent(this, QuenMK_NhamMKMoi::class.java)
        intent.putExtra("Email",email)

        if (number == edtMa.text.toString()) {
            if (email != null) {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn cần đổi mật khẩu mới")
                    .setCancelable(false)
                    .setPositiveButton("OK") { _, _ ->
                        startActivity(intent)
                        this.finish()
                    }
                    .create()
                dialog.show()
            } else {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Mã PIN không chính xác", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setEvent() {
        btnXacNhan.setOnClickListener {
            tinKiem()
        }
        btnThoat.setOnClickListener {
            this.finish()
        }
    }

    private fun setControl() {
        edtMa = findViewById(R.id.edtMa)
        btnThoat = findViewById(R.id.btnThoat)
        btnXacNhan = findViewById(R.id.btnXacNhan)
    }
}
