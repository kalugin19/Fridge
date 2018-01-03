package ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.view.activity


import ru.kalugin19.fridge.android.pub.v2.ui.base.ViewContract

/**
 * контракт для активити добавления или редактирования продукта
 *
 * @author Natochij Alexander
 */
interface IAddEditProductView : ViewContract {

    /**
     *  показать диалог об успешном добавлении продукта
     */
    fun showDialogSuccessAddProduct()

    /**
     *  показать диалог об успешном редактировании продукта
     */
    fun showDialogSuccessEditProduct()

    /**
     * Показать прогресс добавления или редактирования продукта
     */
    fun startProgressDialogGetAddEditProduct()

    /**
     * Скрыть прогресс добавления или редактирования продукта
     */
    fun stopProgressDialogGetAddEditProduct()

    /**
     * Проверка работает ли прогресс
     */
    fun checkForWorkProgressDialogGetAddEditProduct(): Boolean

    /**
     * проверка на использование метода добавления продукта или редактирования
     */
    fun testData(imagePath: String?, imageName: String?)

    /**
     * показать ошибку при загрузке изображения
     */
    fun showErrorDownloadImage()

    /**
     * показать ошибку при добавлении или редактировании товара
     */
    fun showErrorAddOrEditProduct()


}
