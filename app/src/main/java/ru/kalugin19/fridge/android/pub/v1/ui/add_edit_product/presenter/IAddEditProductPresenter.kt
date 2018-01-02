package ru.kalugin19.fridge.android.pub.v1.ui.add_edit_product.presenter

import android.net.Uri

import ru.kalugin19.fridge.android.pub.v1.data.entity.Product
import ru.kalugin19.fridge.android.pub.v1.ui.add_edit_product.view.activity.IAddEditProductView
import ru.kalugin19.fridge.android.pub.v1.ui.base.Presenter

/**
 * инерфейс презентера добавления и редактирования продукта
 *
 * @author Natochij Alexander
 */
interface IAddEditProductPresenter : Presenter<IAddEditProductView> {

    /**
     * загрузить изображение в firebase
     */
    fun getImageToFirebaseStorage(file: Uri)

    /**
     * Добавить продукт
     */
    fun addProduct(product: Product)

    /*
     * изменить данные в продукте
     */
    fun editProduct(product: Product, lastImageName: String)
}
