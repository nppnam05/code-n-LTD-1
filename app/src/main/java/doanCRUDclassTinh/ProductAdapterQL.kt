package tinh

import Nam_Database.Table.Product
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.doan_nhom9_sang6.R


class ProductAdapterQL(
    private val context: Context,
    private var productlist: MutableList<Product>,
    private val action: ITOnlickActionListener // nó là interface
) : ArrayAdapter<Product>(context, 0, productlist) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            // Sử dụng layout riêng cho item trong danh sách
            view = LayoutInflater.from(context).inflate(R.layout.lvquanlisp, parent, false)
            holder = ViewHolder(view) // Tạo ViewHolder mới và truyền View
            view.tag = holder // Lưu ViewHolder vào tag của View
        } else {
            view = convertView
            holder = view.tag as ViewHolder // Lấy lại ViewHolder từ tag
        }

        // Bind dữ liệu vào View
        val product = productlist[position]
        holder.tvTen.text = product.name // Sử dụng setText() thay vì text
        holder.tvGia.text = product.price.toString() +" VND" // Sử dụng setText()


        // Thiết lập hình ảnh tùy thuộc vào loại sản phẩm
        val resourceId = context.resources.getIdentifier(product.imagePath, "drawable", context.packageName)
        if (resourceId != 0) {
            holder.imgAnh.setImageResource(resourceId)
        } else {
            holder.imgAnh.setImageResource(R.drawable.blank) // Ảnh mặc định nếu không tìm thấy
        }

        // Thiết lập nút chỉnh sửa
        val btnSua = view.findViewById<Button>(R.id.btnSua)
        btnSua.setOnClickListener {
            action.onEditProduct(product, position)
        }

        // Thiết lập nút xóa (nếu cần thiết)
        val btnXoa = view.findViewById<Button>(R.id.btnXoa) // Đảm bảo bạn đã thêm nút xóa trong layout XML
        btnXoa?.setOnClickListener {
            action.onDeleteProduct(position) // Gọi callback để xóa
        }

        return view // Trả về view
    }

    private class ViewHolder(view: View) {
        val imgAnh: ImageView = view.findViewById(R.id.imgAnhSP)
        val tvTen: TextView = view.findViewById(R.id.tvTen)
        val tvGia: TextView = view.findViewById(R.id.tvGia)

    }

    fun updateProducts(newUserList: MutableList<Product>) {
        productlist.clear()   // Xóa toàn bộ dữ liệu cũ
        productlist.addAll(newUserList)  // Thêm dữ liệu mới vào
        notifyDataSetChanged()   // Cập nhật giao diện
    }

}
