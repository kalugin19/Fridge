package ru.kalugin19.fridge.android.pub.v1.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_confirm.view.*
import ru.kalugin19.fridge.android.pub.v1.R


class DialogOk : DialogFragment() {

    companion object {
        val TITLE_KEY = "titleDialog"
    }

    private var onDismissListener: DialogInterface.OnDismissListener? = null


    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_confirm, container, false)
        view.titleDialog.text = arguments?.get(TITLE_KEY).toString()
        view.confirmDialog.setOnClickListener {
            this.dismiss()
        }
        return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (onDismissListener != null) {
            onDismissListener?.onDismiss(dialog)
        }
    }
}