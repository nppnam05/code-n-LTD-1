package com.example.doan_nhom9_sang6.Kiet_Activity

import Nam_Database.Table.Category
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doan_nhom9_sang6.R
import Nam_Database.Table.Table_ThongKe.CategoryStatistic

class CategoryAdapter(
    private val context: Context,
    private var categoryList: List<CategoryStatistic>,
    private var categoryList2: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryBtn: LinearLayout = view.findViewById(R.id.btnDanhMuc)
        val categoryImg: ImageView = view.findViewById(R.id.ivAnhDanhMuc)
        val categoryName: TextView = view.findViewById(R.id.tvTenDanhMuc)
        val categoryPercentage: TextView = view.findViewById(R.id.tvTiTrongDanhMuc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_danhmuc, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]

        val categoryName = categoryList2.find { it.id == category.categoryId }?.categoryName ?: "Unknown"
        holder.categoryName.text = categoryName

        val imageResId = when (category.categoryId) {
            1 -> R.drawable.icon_category_cloth
            2 -> R.drawable.icon_category_cloth
            3 -> R.drawable.icon_category_health
            4 -> R.drawable.icon_category_phone
            5 -> R.drawable.icon_category_cloth
            6 -> R.drawable.icon_category_cloth
            7 -> R.drawable.icon_category_cloth
            8 -> R.drawable.icon_category_health
            9 -> R.drawable.icon_category_electricity
            10 -> R.drawable.baseline_music_note_24
            else -> R.drawable.blank
        }
        holder.categoryImg.setImageResource(imageResId)

        val percentage = String.format("%.2f%%", category.revenueShare)
        holder.categoryPercentage.text = percentage

        holder.categoryBtn.setOnClickListener {
            val intent = Intent(context, ThongKe_SanPham_DanhMuc::class.java).apply {
                putExtra("CATEGORY_ID", category.categoryId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}

