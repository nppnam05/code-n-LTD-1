package tinh

import Nam_Database.Table.Product

interface ITOnlickActionListener {
    fun onEditProduct(product: Product, position: Int)
    fun onDeleteProduct(position: Int)
}