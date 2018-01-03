package ru.kalugin19.fridge.android.pub.v2.ui.base.util;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * класс для проверки валидации
 */
public class CustomTextWatcher implements TextWatcher {
    private final WeakReference<EditText> editText;

    public CustomTextWatcher(EditText editText) {
        this.editText = new WeakReference<>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(editText.get()!=null && editText.get().getParent()!=null && editText.get().getParent().getParent()!=null && editText.get().getParent().getParent() instanceof TextInputLayout) {
            ((TextInputLayout)editText.get().getParent().getParent()).setError(null);
            ((TextInputLayout)editText.get().getParent().getParent()).setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
