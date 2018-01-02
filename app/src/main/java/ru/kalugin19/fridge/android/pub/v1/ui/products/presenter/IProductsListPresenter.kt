package ru.kalugin19.fridge.android.pub.v1.ui.products.presenter


import ru.kalugin19.fridge.android.pub.v1.data.entity.Product
import ru.kalugin19.fridge.android.pub.v1.data.entity.TypeProducts
import ru.kalugin19.fridge.android.pub.v1.ui.base.Presenter
import ru.kalugin19.fridge.android.pub.v1.ui.products.fragment.IProductsListView

/**
 * Презентер для фрагмента ProductsFragment
 */
internal interface IProductsListPresenter : Presenter<IProductsListView> {
    /**
     * Получить продукты
     */
    fun getProducts(typeProducts: TypeProducts, searchText: String)

    /**
     * Добавить продукт
     */
    fun addProduct(product: Product)

    /**
     * Удалить список продуктов
     */
    fun deleteProducts(list: MutableList<Product>)
}
