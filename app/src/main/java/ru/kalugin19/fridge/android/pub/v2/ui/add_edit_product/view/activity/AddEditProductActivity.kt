package ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.view.activity

import android.Manifest
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast

import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty

import java.io.File
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.ExecutionException

import javax.inject.Inject

import kotlinx.android.synthetic.main.activity_add_edit_product.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.EasyImageConfig
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.data.entity.PathImagesProduct
import ru.kalugin19.fridge.android.pub.v2.data.entity.Product
import ru.kalugin19.fridge.android.pub.v2.data.remote.FirebaseService
import ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.presenter.IAddEditProductPresenter
import ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.view.adapters.AddEditProductAdapter
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.Constants
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.ConvertBarCodeTask
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.CustomTextWatcher
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.Dialogs
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.ProgressDialogCustom
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.Util
import ru.kalugin19.fridge.android.pub.v2.ui.base.view.activity.BaseActivity
import ru.kalugin19.fridge.android.pub.v2.ui.dialogs.DialogOk
import ru.kalugin19.fridge.android.pub.v2.ui.products.activity.ProductsActivity


/**
 * Активити добавления или редактирования товара
 */
class AddEditProductActivity : BaseActivity(), IAddEditProductView, Validator.ValidationListener {

    var addEditProductPresenter: IAddEditProductPresenter? = null
        @Inject set

    var firebaseService: FirebaseService? = null
        @Inject set

    @NotEmpty(trim = true)
    private lateinit var activityAddEditProductEditTextNameProduct: EditText
    private lateinit var adapter: AddEditProductAdapter
    private lateinit var validator: Validator
    private var product: Product? = null
    private val IMAGE = 1
    private lateinit var progressDialogGetAddEditProduct: ProgressDialogCustom
    private var selectCurrentPhoto = -1
    private var file: Uri? = null
    private var lastImageName: String? = null
    private var currentCalendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)
        activityComponent.inject(this)
        addEditProductPresenter?.attachView(this)
        progressDialogGetAddEditProduct = ProgressDialogCustom(this, getString(R.string.activity_add_edit_product_progress_dialog_text))
        activityAddEditProductEditTextNameProduct = findViewById(R.id.edit_text_name_product)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

//        expiration_date?.state()?.edit()?.setMinimumDate(Calendar.getInstance())?.commit()

        validator = Validator(this)
        validator.setValidationListener(this)
//
//        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recycleImages?.layoutManager = layoutManager
//        recycleImages?.setHasFixedSize(true)
//        adapter = AddEditProductAdapter(this@AddEditProductActivity, object : AddEditProductAdapter.IClickButton {
//            override fun clickAddButton() {
//                selectPhoto(IMAGE)
//            }
//
//            override fun clickDeleteButton(pathImagesProduct: PathImagesProduct) {
//                Dialogs.showAlert(this@AddEditProductActivity, Dialogs.TypeAlert.ALERT_CONFIRM_ACT, R.string.activity_add_edit_product_dialog_delete_image, null, object : Dialogs.IClickButtonDialog {
//                    override fun clickPositiveButton() {
//                        adapter.delete(pathImagesProduct)
//                        product?.photo = null
//                    }
//
//                    override fun clickNegativeButton() {
//
//                    }
//                })
//            }
//        })
//        recycleImages?.adapter = adapter
        product = intent.getParcelableExtra(Constants.INFO_PRODUCT)

        if (product == null) {
            product = Product()
        } else {
            if (product!!.name != null &&
                    !product!!.name?.isEmpty()!!) {
                activityAddEditProductEditTextNameProduct.setText(product!!.name)
            }
            if (!TextUtils.isEmpty(product!!.photo) && !TextUtils.isEmpty(product!!.photoName)) {
                adapter.insert(PathImagesProduct(product!!.photo))
                lastImageName = product!!.photoName
            }
            if (product!!.spoiledDate != 0L) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = product!!.spoiledDate

//                expiration_date.setCurrentDate(calendar)
//                expiration_date.setSelectedDate(calendar)
            }
        }
//        adapter.addHeader()
//        expiration_date?.setOnDateChangedListener { _, date, _ -> product!!.spoiledDate = date.calendar.timeInMillis }
        activityAddEditProductEditTextNameProduct.addTextChangedListener(CustomTextWatcher(activityAddEditProductEditTextNameProduct))

        save_product?.setOnClickListener {
            if (Util.hasConnection(this@AddEditProductActivity)) {
                showErrorNoInternet()
            } else {
                validator.validate()
            }
        }


        showDatePicker()

        expiredDateTextInput.setOnClickListener {
            showDatePicker()
        }
        expiredDateEditTxt.setOnClickListener {
            showDatePicker()
        }
    }

    override fun testData(imagePath: String?, imageName: String?) {
        if (product?.key != null && !product?.key!!.isEmpty()) {
            getInfoSaveProduct(imagePath, imageName)?.let { lastImageName?.let { it1 -> addEditProductPresenter?.editProduct(it, it1) } }
        } else {
            getInfoSaveProduct(imagePath, imageName)?.let { addEditProductPresenter?.addProduct(it) }
        }
    }


    private fun callBarCode() {
        if (Util.hasConnection(this@AddEditProductActivity)) {
            showErrorNoInternet()
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, BarcodeScannerActivity::class.java)
                startActivityForResult(intent, Constants.RESULT_CODE)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), Constants.RESULT_CODE)
            }
        }
    }

    private fun selectPhoto(photoId: Int) {
        selectCurrentPhoto = photoId
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            EasyImage.openChooserWithGallery(this, getString(R.string.activity_add_edit_product_select_picture_with), EasyImageConfig.REQ_TAKE_PICTURE)
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    Constants.GET_REQUEST_PERMISSIONS)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.RESULT_CODE -> if (permissions.size == 1 &&
                    permissions[0].toLowerCase(Locale.ENGLISH) == Manifest.permission.CAMERA.toLowerCase(Locale.ENGLISH) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                global_view.let { callBarCode() }
            } else {
                Snackbar.make(global_view, R.string.activity_add_edit_product_error_permission_granted_to_camera, Snackbar.LENGTH_LONG).show()
            }
            Constants.GET_REQUEST_PERMISSIONS -> if (permissions.size == 2 &&
                    permissions[0].toLowerCase(Locale.getDefault()) == Manifest.permission.READ_EXTERNAL_STORAGE.toLowerCase(Locale.getDefault())
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && permissions[1].toLowerCase(Locale.getDefault()) == Manifest.permission.CAMERA.toLowerCase(Locale.getDefault())
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (selectCurrentPhoto > 0) {
                    selectPhoto(selectCurrentPhoto)
                }
            } else {
                Snackbar.make(global_view, R.string.activity_add_edit_product_error_permission_granted_to_camera, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.RESULT_CODE -> {
                if (data == null) {
                    return
                }
                val code = data.getStringExtra(Constants.CODE)
                if (Util.hasConnection(this@AddEditProductActivity)) {
                    showErrorNoInternet()
                } else {
                    try {
                        val resultName: String
                        val title = HTML_OPEN_TITLE
                        if (code == null || code.isEmpty()) {
                            return
                        }
                        val resultString = ConvertBarCodeTask().execute(code).get()
                        val indexStart: Int
                        if (resultString == null || !resultString.contains(title)) {
                            resultName = code
                        } else {
                            indexStart = resultString.indexOf(title) + title.length
                            val subResult = resultString.substring(indexStart)
                            val indexEnd = subResult.indexOf(HTML_CLOSE_TITLE)
                            resultName = subResult.substring(0, indexEnd).trim { it <= ' ' }
                        }

                        val barcode = resultName.contains(getString(R.string.barcode))
                        val search = resultName.contains(getString(R.string.activity_add_edit_product_barcode_search))
                        when {
                            barcode -> {
                                val index = resultName.indexOf(getString(R.string.barcode))
                                activityAddEditProductEditTextNameProduct.setText(resultName.substring(0, index))
                            }
                            search -> activityAddEditProductEditTextNameProduct.setText(R.string.activity_add_edit_product_error_search_product)
                            else -> activityAddEditProductEditTextNameProduct.setText(resultName)
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    }

                }
            }
            else -> EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
                override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                    selectCurrentPhoto = -1
                }

                override fun onImagePicked(imageFile: File, source: EasyImage.ImageSource, type: Int) {
                    resultSelectedPhoto(imageFile)
                }
            })
        }
    }

    private fun resultSelectedPhoto(image: File?) {
        if (image != null && selectCurrentPhoto != -1)
            adapter.insert(PathImagesProduct(image.absolutePath))
        selectCurrentPhoto = -1
        file = Uri.fromFile(image)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.barCodeScanner -> {
                callBarCode()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onValidationSucceeded() {
        if (getInfoSaveProduct(product!!.photo, product!!.photoName) != null) {
            if (file != null) {
                addEditProductPresenter?.getImageToFirebaseStorage(file!!)
            } else {
                testData(product!!.photo, product!!.photoName)
            }
        }
    }


    private fun getInfoSaveProduct(imagePath: String?, imageName: String?): Product? {
        if (product!!.spoiledDate == 0L) {
            Snackbar.make(global_view, R.string.activity_add_edit_product_error_validation_view_unfit_before, Snackbar.LENGTH_LONG).show()
            return null
        } else {
            val createdDate = Calendar.getInstance()
            product!!.createdDate = createdDate.timeInMillis
            product!!.name = activityAddEditProductEditTextNameProduct.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(imagePath) && !TextUtils.isEmpty(imageName)) {
                product!!.photo = imagePath
                product!!.photoName = imageName
            } else {
                product!!.photo = null
                product!!.photoName = null
            }
            return product
        }
    }

    override fun onValidationFailed(errors: List<ValidationError>) {
        for (error in errors) {
            val view = error.view
            val message = error.getCollatedErrorMessage(this)
            if (view.parent != null && view.parent.parent != null && view.parent.parent is TextInputLayout) {
                (view.parent.parent as TextInputLayout).isErrorEnabled = true
                (view.parent.parent as TextInputLayout).error = message
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showErrorNoInternet() {
        Snackbar.make(global_view, R.string.activity_add_edit_product_error_no_internet, Snackbar.LENGTH_LONG).show()
    }

    override fun showDialogSuccessAddProduct() {

        val dialogOk = DialogOk()
        val bundle = Bundle()
        bundle.putString(DialogOk.TITLE_KEY, getString(R.string.activity_add_edit_product_dialog_text_success_add_product))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialogOk.setStyle(DialogFragment.STYLE_NO_FRAME,  android.R.style.ThemeOverlay_Material_Dialog_Alert)
        }
        dialogOk.arguments = bundle
        dialogOk.show(supportFragmentManager, "dialog")
        dialogOk.setOnDismissListener(DialogInterface.OnDismissListener { goToProductActivity() })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if(Locale.getDefault().displayLanguage == "русский"){
            val inflater = menuInflater
            inflater.inflate(R.menu.action_menu, menu)
            true
        } else{
            false
        }
    }




    override fun showDialogSuccessEditProduct() {
        Dialogs.showAlert(this, Dialogs.TypeAlert.ALERT_OK_GOOD, R.string.activity_add_edit_product_dialog_title_edit, R.string.activity_add_edit_product_dialog_text_success_edit_product, object : Dialogs.IClickButtonDialog {
            override fun clickPositiveButton() {
                goToProductActivity()
            }

            override fun clickNegativeButton() {

            }
        })
    }

    private fun goToProductActivity() {
        val intent = Intent(this@AddEditProductActivity, ProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun startProgressDialogGetAddEditProduct() {
        progressDialogGetAddEditProduct.showProgress(true)
    }

    override fun stopProgressDialogGetAddEditProduct() {
        progressDialogGetAddEditProduct.showProgress(false)
    }

    override fun checkForWorkProgressDialogGetAddEditProduct(): Boolean {
        return !progressDialogGetAddEditProduct.isShow
    }

    override fun showErrorDownloadImage() {
        Dialogs.showAlert(this, Dialogs.TypeAlert.ALERT_ERROR_DOWNLOAD, R.string.activity_add_edit_product_dialog_title_error_download_image, R.string.activity_add_edit_product_dialog_text_error_download_image, object : Dialogs.IClickButtonDialog {
            override fun clickPositiveButton() {
                testData(null, null)
            }

            override fun clickNegativeButton() {

            }
        })
    }

    override fun showErrorAddOrEditProduct() {
        Snackbar.make(global_view, R.string.activity_add_edit_product_error_add_or_edit_product, Snackbar.LENGTH_LONG).show()
    }


    private fun showDatePicker(){
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val spoiledCalendar = Calendar.getInstance()
            spoiledCalendar.set(year, monthOfYear, dayOfMonth)
            product?.spoiledDate = spoiledCalendar.timeInMillis
            expiredDateEditTxt.setText(product?.getFormattedSpoiledDate())
            currentCalendar = spoiledCalendar
        }

       val datePickerDialog = DatePickerDialog(this@AddEditProductActivity,
                dateSetListener,
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH),
                currentCalendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }


    companion object {
        private val HTML_OPEN_TITLE = "<title>"
        private val HTML_CLOSE_TITLE = "</title>"
    }
}
