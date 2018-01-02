package ru.kalugin19.fridge.android.pub.v1.ui.base.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface

import ru.kalugin19.fridge.android.pub.v1.R


/**
 * Класс для отображения прогресс диалога
 */
class ProgressDialogCustom(private val activity: Activity?, private val title: String) {
    private val progressDialog: ProgressDialog = ProgressDialog(activity)

    val isShow: Boolean
        get() = progressDialog.isShowing

    fun setNegativeButton(clickListener: DialogInterface.OnClickListener) {
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity?.getString(R.string.custom_progress_dialog_button_cancel_title), clickListener)
    }

    /**
     * Показать/Скрыть ProgressBar с сообщением на экране
     *
     * @param show показывать(true)/скрыть(false) прогрессДиалог
     */
    fun showProgress(show: Boolean) {
        if (show) {
            progressDialog.setMessage(title)
            progressDialog.isIndeterminate = true
            progressDialog.setCancelable(false)
            progressDialog.show()
        } else {
                progressDialog.dismiss()
        }
    }
}
