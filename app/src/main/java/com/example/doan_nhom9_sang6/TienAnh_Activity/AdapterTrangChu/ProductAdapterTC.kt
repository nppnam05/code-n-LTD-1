package com.example.doan_nhom9_sang6.TienAnh_Activity.AdapterTrangChu

import Nam_Database.DAO.DAO_ThongKe.ProductStatisticDAO
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Product
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.doan_nhom9_sang6.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private lateinit var productStaticDao: ProductStatisticDAO

class ProductAdapterTC(
    private val context: Context,
    var products: List<Product>
) : BaseAdapter() {

    override fun getCount(): Int {
        return products.size
    }

    override fun getItem(position: Int): Product {
        return products[position]
    }

    override fun getItemId(position: Int): Long {
        return products[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_product1, parent, false)
        }

        val product = products[position]

        val productName = view?.findViewById<TextView>(R.id.txtProductName)
        val productPrice = view?.findViewById<TextView>(R.id.txtProductPrice)
        val productDesc = view?.findViewById<TextView>(R.id.txtThongTin)
        val productsold = view?.findViewById<TextView>(R.id.txtLuotBan)
        val productImage = view?.findViewById<ImageView>(R.id.ivProduct)

        productName?.text = product.name
        productDesc?.text = product.description
        var productStaticAmount = 0

        productStaticDao = DatabaseHelper.getDatabase(context).productStatisticDao()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val product = productStaticDao.getOneProductStatisticById(product.id)
                withContext(Dispatchers.Main) {
                    productStaticAmount = product!!.amount
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu User: ${e.message}")
            }
        }
        productsold?.text = "${productStaticAmount}"
        productPrice?.text = "${product.price}₫"
        val resId = context.resources.getIdentifier(product.imagePath, "drawable", context.packageName)// chuyển tên thành kiểu int trong drawable
        if (resId != 0) {
            productImage?.setImageResource(resId)
        } else {
            productImage?.setImageResource(R.drawable.dt10x)
        }

        return view!!
    }

}
