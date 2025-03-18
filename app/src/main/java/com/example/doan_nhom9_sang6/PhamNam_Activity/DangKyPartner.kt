package PhamNam_Activity


import Nam_Database.DAO.CategoryDAO
import Nam_Database.DAO.UserDao
import Nam_Database.DatabaseHelper
import Nam_Database.Table.Category
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doan_nhom9_sang6.PhamNam_Activity.DangKyDangNhap.DangNhapActivity
import com.example.doan_nhom9_sang6.R
import com.example.doan_nhom9_sang6.PhamNam_Activity.ThongTinCaNhanActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DangKyPartner : AppCompatActivity() {
    private lateinit var edtTenCuaHang : EditText
    private lateinit var edtDiaChi : EditText
    private lateinit var spnDanhMucCha : Spinner
    private lateinit var spnDanhMucCon : Spinner
    private lateinit var btnThoat : Button
    private lateinit var btnDangKy : Button


    private lateinit var userDao: UserDao
    private lateinit var categoryDao: CategoryDAO
    var listCategories = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_ky_partner)


        userDao = DatabaseHelper.getDatabase(this).userDao()
        categoryDao = DatabaseHelper.getDatabase(this).categoryDao()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val categoriesFromDB = categoryDao.getAllCategories()
                withContext(Dispatchers.Main) {
                    listCategories.clear()
                    listCategories.addAll(categoriesFromDB)
                    setControl()
                    setEvent(listCategories)
                }
            } catch (e: Exception) {
                println("Lỗi read dữ liệu categories: ${e.message}")
            }
        }


    }

    fun setEvent(categories:MutableList<Category> ){
        var categoryName = ""
        val categoriesParent = categories.filter { it.parentId == null }.map { it.categoryName }

        val adapterCha = ArrayAdapter(this,android.R.layout.simple_spinner_item,categoriesParent)
        spnDanhMucCha.adapter = adapterCha

        spnDanhMucCha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selecterParentName = categoriesParent[position]
                val selecterParentID = categories.find{ it.categoryName == selecterParentName }?.id
                val subCategories = categories.filter { it.parentId == selecterParentID }.map {it.categoryName}

                val adapterCon = ArrayAdapter(this@DangKyPartner,android.R.layout.simple_spinner_item,subCategories)
                spnDanhMucCon.adapter = adapterCon

                spnDanhMucCon.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        categoryName = subCategories[position]
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Toast.makeText(this@DangKyPartner, "Bạn chưa chọn danh mục con", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@DangKyPartner, "Bạn chưa chọn danh mục cha", Toast.LENGTH_SHORT).show()
            }
        }
        btnDangKy.setOnClickListener {
            var arrCheck = BooleanArray(4)
            if(edtTenCuaHang.text.isNullOrEmpty()){
                edtTenCuaHang.error = "Vui lòng nhập tên cửa hàng"
            }
            else arrCheck[0] = true
            if(edtDiaChi.text.isNullOrEmpty()){
                edtDiaChi.error = "Vui lòng nhập địa chỉ"
            }
            else arrCheck[1] = true
            if(spnDanhMucCha.selectedItemPosition > 0){
                arrCheck[2] = true
            }
            if(spnDanhMucCon.selectedItemPosition > 0){
                arrCheck[3] = true
            }
            for(i in 0..3){
                if(!arrCheck[i]) return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    userDao.updateUserDetails(
                        DangNhapActivity.id,"Partner",edtTenCuaHang.text.toString(),edtDiaChi.text.toString(),
                        categoryName)
                } catch (e: Exception) {
                    println("Lỗi insert dữ liệu User: ${e.message}")
                }
            }

            val intent = Intent(this, ThongTinCaNhanActivity::class.java)
            startActivity(intent)
        }

        btnThoat.setOnClickListener {
            finish()
        }
    }
    fun setControl(){
        edtTenCuaHang = findViewById(R.id.edtTenCuaHang)
        edtDiaChi = findViewById(R.id.edtDiaChi)
        spnDanhMucCha = findViewById(R.id.spnCha)
        spnDanhMucCon = findViewById(R.id.spnCon)
        btnDangKy = findViewById(R.id.btnDangKy)
        btnThoat = findViewById(R.id.btnThoat)
    }
}