package com.example.doan_nhom9_sang6.moRong

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.doan_nhom9_sang6.R

class ChoLayHangActivity : AppCompatActivity() {
    private lateinit var imgvBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cho_lay_hang)
        setControl()
        setEvent()
    }
    private fun setControl(){
        imgvBack = findViewById(R.id.imgBack)
    }
    private fun setEvent(){
        imgvBack.setOnClickListener{
            finish()
        }
    }
}