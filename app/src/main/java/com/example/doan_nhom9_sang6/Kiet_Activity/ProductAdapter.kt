package com.example.doan_nhom9_sang6.Kiet_Activity

import Nam_Database.Table.Table_ThongKe.ProductStatistic
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doan_nhom9_sang6.R
import java.text.DecimalFormat

class ProductAdapter(
    private val context: Context,
    private val productList: List<ProductStatistic>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder class to cache references to views for better performance
    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.tvTenSanPham)
        val productTotalPrice: TextView = view.findViewById(R.id.tvGiaSanPham)
        val productCompare: TextView = view.findViewById(R.id.tvSoSanhSanPham)
    }

    // Inflate the item layout and return the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_sanphamnoibat, parent, false)
        return ProductViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        val total = product.amount * product.price

        // Format the total price
        val formatter = DecimalFormat("#,###")
        val formattedPrice = formatter.format(total)

        holder.productName.text = product.productName
        holder.productTotalPrice.text = "$formattedPrice" + "đ"

        if (total >= 9000000) {
            val percentageIncrease = ((total / 9000000.0) * 100)
            holder.productCompare.text = "Tăng %.2f%% so với tháng trước.".format(percentageIncrease)
            holder.productCompare.setTextColor(Color.GREEN)
        } else {
            val percentageDecrease = ((9000000.0 - total) / 9000000.0 * 100)
            holder.productCompare.text = "Giảm %.2f%% so với tháng trước.".format(percentageDecrease)
            holder.productCompare.setTextColor(Color.RED)
        }
    }

    // Return the number of items
    override fun getItemCount(): Int {
        return productList.size
    }
}
