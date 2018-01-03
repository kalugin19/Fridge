package ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.Constants

/**
 * Класс звпуска камеры для распознования штрих-кода
 */
class BarcodeScannerActivity : Activity(), ZXingScannerView.ResultHandler {
    private lateinit var mScannerView: ZXingScannerView

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        val intent = Intent()
        intent.putExtra(Constants.CODE, rawResult.text)
        setResult(Activity.RESULT_OK, intent)
        finish()
        mScannerView.resumeCameraPreview(this)
    }
}
