package ru.kalugin19.fridge.android.pub.v1.ui.products.fragment


import ru.kalugin19.fridge.android.pub.v1.data.entity.Product
import ru.kalugin19.fridge.android.pub.v1.data.entity.TypeProducts
import ru.kalugin19.fridge.android.pub.v1.ui.base.ViewContract

/**
 * Контракт для фрагмента ProductsFragment
 */
interface IProductsListView : ViewContract {
    /**
     * Установка данных
     */
    fun setData(products: List<Product>, typeProducts: TypeProducts)

    /**
     * Показать прогресс получения продуктов
     */
    fun startProgressDialogGettingProducts()

    /**
     * Скрыть прогресс получения продуктов
     */
    fun stopProgressDialogGettingProducts()

    /**
     * Показать прогресс удаления
     */
    fun startProgressDeleting()

    /**
     * Показать прогресс удаления
     */
    fun stopProgressDeleting()

    /**
     * Показать ошибку удаления
     */
    fun showErrorDeleting()

    /**
     * Нет интернета
     */
    fun showNoInternet()
}
