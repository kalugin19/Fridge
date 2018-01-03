package ru.kalugin19.fridge.android.pub.v2.ui.base.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ru.kalugin19.fridge.android.pub.v2.R;

/**
 * Класс для работы с алертами
 */
public class Dialogs {

    public interface IClickButtonDialog {
        void clickPositiveButton();

        @SuppressWarnings("EmptyMethod")
        void clickNegativeButton();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AlertDialog showAlert(final Context context, final TypeAlert typeAlert, @android.support.annotation.StringRes int titleId, @android.support.annotation.StringRes int messageId, final IClickButtonDialog clickButton) {
        return showAlert(context, typeAlert, titleId, context.getString(messageId), clickButton);
    }

    public static AlertDialog showAlert(final Context context, final TypeAlert typeAlert, @android.support.annotation.StringRes int title, String message, final IClickButtonDialog clickButton) {
        AlertDialog alert = createAlert(context, context.getString(title), message);
        alert.setCanceledOnTouchOutside(false);
        switch (typeAlert) {
            case ALERT_OK_ERROR:
                alert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), (dialog, which) -> {
                    dialog.cancel();
                    if (clickButton != null) {
                        clickButton.clickPositiveButton();
                    }
                });
                break;
            case ALERT_OK_GOOD:
                alert.setCancelable(false);
                alert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), (dialog, which) -> {
                    dialog.cancel();
                    if (clickButton != null) {
                        clickButton.clickPositiveButton();
                    }
                });
                break;
            case ALERT_CONFIRM_ACT:
                alert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.deleted_button), (dialog, which) -> {
                    dialog.cancel();
                    if (clickButton != null) {
                        clickButton.clickPositiveButton();
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel_button), (dialog, which) -> {
                    dialog.cancel();
                    if (clickButton != null) {
                        clickButton.clickNegativeButton();
                    }
                });
                break;
            case ALERT_ERROR_DOWNLOAD:
                alert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.continue_button), (dialog, which) -> {
                    dialog.cancel();
                    if (clickButton != null) {
                        clickButton.clickPositiveButton();
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel_button), (dialog, which) -> {
                    dialog.cancel();
                    if (clickButton != null) {
                        clickButton.clickNegativeButton();
                    }
                });
                break;
        }

        alert.show();
        return alert;

    }

    private static AlertDialog createAlert(Context context, String title, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        return ad.setTitle(title)
                .setMessage(message)
                .create();
    }

    /**
     * Тип алерт диалога
     */
    public enum TypeAlert {
        /**
         * Алерт, только с одной кнопкой "ОК". Вызывать в случае ошибки.
         */
        ALERT_OK_ERROR,
        /**
         * Алерт, только с одной кнопкой "ОК". Вызывать, когда действие завершилось успехом.
         */
        ALERT_OK_GOOD,
        /*
         * Алерт, с двумя кнопками "Да" и "Отмена". Вызывать, когда требуется подтверждение или опровержение какого-либо действия
         */
        ALERT_CONFIRM_ACT,
        /*
         * Алерт с двумя кнопками "Продолжить" и "Отмена". Вызывать, когда произойдет ошибка загрузки фото
         */
        ALERT_ERROR_DOWNLOAD
    }

}
