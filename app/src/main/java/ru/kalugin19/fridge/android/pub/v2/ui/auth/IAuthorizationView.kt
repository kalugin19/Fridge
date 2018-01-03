package ru.kalugin19.fridge.android.pub.v2.ui.auth


import ru.kalugin19.fridge.android.pub.v2.ui.base.ViewContract

/**
 * Контракт для экрана авторизации
 *
 * @author Kalugin Valerij
 */

interface IAuthorizationView : ViewContract {
    /**
     * Переход к главному экрану
     */
    fun goToMainActivity()

    /**
     * Показать ошибку авторизации
     */
    fun showAuthorizationError()

    /**
     * Старт прогресса "Авторизация"
     */
    fun startAuthorizationProgressDialog()

    /**
     * Стоп прогресса "Авторизация"
     */
    fun stopAuthorizationProgressDialog()
}
