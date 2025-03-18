package com.example.doan_nhom9_sang6.PhamNam_Activity.GioHang

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doan_nhom9_sang6.R

class CartAdapter(
    private val items: MutableList<CartItemWithProduct>, // Dữ liệu từ Activity/Fragment
    private val onItemChanged: (Int, Int) -> Unit,       // Callback khi thay đổi số lượng
    private val onItemRemoved: (Int) -> Unit,            // Callback khi xóa sản phẩm
    private val onItemBuy: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImage)
        val nameTextView: TextView = itemView.findViewById(R.id.itemName)
        val priceTextView: TextView = itemView.findViewById(R.id.itemPrice)
        val quantityEditText: EditText = itemView.findViewById(R.id.itemQuantity)
        val decreaseButton: Button = itemView.findViewById(R.id.decreaseButton)
        val increaseButton: Button = itemView.findViewById(R.id.increaseButton)
        val removeButton: Button = itemView.findViewById(R.id.removeButton)
        val buyButton: Button = itemView.findViewById(R.id.buyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        // Hiển thị thông tin sản phẩm
        val resId = holder.itemView.context.resources.getIdentifier(
            item.product.imagePath, // Tên ảnh từ database
            "drawable",
            holder.itemView.context.packageName
        )
        if (resId != 0) {
            holder.imageView.setImageResource(resId) // Hiển thị ảnh nếu có trong drawable
        } else {
            holder.imageView.setImageResource(R.drawable.dt10x) // Ảnh mặc định nếu không tìm thấy
        }
        holder.nameTextView.text = item.product.name
        holder.priceTextView.text = "${item.cartItem.price} VND"
        holder.quantityEditText.setText(item.cartItem.quantity.toString())

        // Xử lý nút giảm số lượng
        holder.decreaseButton.setOnClickListener {
            if (item.cartItem.quantity > 1) {
                item.cartItem.quantity--
                notifyItemChanged(position)
                onItemChanged(item.cartItem.id, item.cartItem.quantity)
            }
            else{
                onItemRemoved(item.cartItem.id)
            }
        }

        // Xử lý nút tăng số lượng
        holder.increaseButton.setOnClickListener {
            item.cartItem.quantity++
            notifyItemChanged(position)
            onItemChanged(item.cartItem.id, item.cartItem.quantity)
        }
         //Xử lý thay đổi trực tiếp trong EditText
        holder.quantityEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val newQuantity = holder.quantityEditText.text.toString().toIntOrNull() ?: 1
                if (newQuantity > 0) {
                    item.cartItem.quantity = newQuantity
                    onItemChanged(item.cartItem.id, newQuantity)
                    notifyItemChanged(position)
                } else {
                    holder.quantityEditText.setText("1")
                }
            }
        }


        // Xử lý nút xóa
        holder.removeButton.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = items[position]

                // Gọi callback để xử lý xóa trong Activity
                onItemRemoved(item.cartItem.id)

                notifyItemRemoved(position)
            }
        }

        // Xử lý nút mua
        holder.buyButton.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = items[position]

                // Gọi callback xử lý logic mua
                onItemBuy(item.cartItem.id)

                // Đồng bộ Adapter từ Activity
                notifyItemRemoved(position)
            }
        }

    }

    override fun getItemCount(): Int = items.size
}
