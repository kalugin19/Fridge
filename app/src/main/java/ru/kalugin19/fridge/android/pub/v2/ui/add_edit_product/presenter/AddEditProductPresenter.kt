package ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.presenter

import android.net.Uri
import android.text.TextUtils

import com.google.firebase.storage.FirebaseStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import java.lang.ref.WeakReference

import javax.inject.Inject

import ru.kalugin19.fridge.android.pub.v2.data.entity.Product
import ru.kalugin19.fridge.android.pub.v2.data.model.ProductModel
import ru.kalugin19.fridge.android.pub.v2.data.remote.FirebaseService
import ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.view.activity.IAddEditProductView


/**
 * Презентер для активити добавления/редактирования продукта
 */
class AddEditProductPresenter @Inject
constructor(private val firebaseService: FirebaseService, private val productModel: ProductModel) : IAddEditProductPresenter {
    private var addEditProductView: WeakReference<IAddEditProductView>? = null
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    override fun getImageToFirebaseStorage(file: Uri) {
        addEditProductView?.get()?.startProgressDialogGetAddEditProduct()
        val riversRef = storageReference.child(DIRECTORY + file.lastPathSegment)
        val uploadTask = riversRef.putFile(file)
        uploadTask.addOnFailureListener {
            addEditProductView?.get()?.stopProgressDialogGetAddEditProduct()
            addEditProductView?.get()?.showErrorDownloadImage()
        }
//                .addOnSuccessListener { taskSnapshot ->
//            if (taskSnapshot != null && taskSnapshot.downloadUrl != null) {
//                val path = taskSnapshot.downloadUrl.toString()
//                addEditProductView?.get()?.testData(path, file.lastPathSegment)
//            }
//        }
    }


    override fun addProduct(product: Product) {
        if (addEditProductView?.get()?.checkForWorkProgressDialogGetAddEditProduct()!!) {
            addEditProductView?.get()?.startProgressDialogGetAddEditProduct()
        }
        productModel.addProduct(product)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Boolean>() {
                    override fun onNext(t: Boolean) {
                        addEditProductView?.get()?.stopProgressDialogGetAddEditProduct()
                        addEditProductView?.get()?.showDialogSuccessAddProduct()
                    }

                    override fun onComplete() {
                        this.dispose()
                    }

                    override fun onError(e: Throwable) {
                        addEditProductView?.get()?.stopProgressDialogGetAddEditProduct()
                        addEditProductView?.get()?.showErrorAddOrEditProduct()
                    }
                })
    }

    override fun editProduct(product: Product, lastImageName: String) {
        addEditProductView?.get()?.startProgressDialogGetAddEditProduct()
        productModel.editProduct(product)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Boolean>() {
                    override fun onNext(t: Boolean) {
                        if (!TextUtils.isEmpty(lastImageName)) {
                            if (lastImageName == product.photoName) {
                                addEditProductView?.get()?.stopProgressDialogGetAddEditProduct()
                                addEditProductView?.get()?.showDialogSuccessEditProduct()
                            } else {
                                deleteImageToFirebaseStorage(lastImageName)
                            }
                        } else {
                            addEditProductView?.get()?.stopProgressDialogGetAddEditProduct()
                            addEditProductView?.get()?.showDialogSuccessEditProduct()
                        }
                    }

                    override fun onComplete() {
                        this.dispose()
                    }

                    override fun onError(e: Throwable) {
                        addEditProductView?.get()?.stopProgressDialogGetAddEditProduct()
                        addEditProductView?.get()?.showErrorAddOrEditProduct()
                    }
                })
    }

    /**
     * Удалить фото из firebase
     */
    private fun deleteImageToFirebaseStorage(lastImageName: String) {
        val desertRef = storageReference.child(DIRECTORY + lastImageName)
        desertRef.delete().addOnSuccessListener {
            addEditProductView?.get()?.stopProgressDialogGetAddEditProduct()
            addEditProductView?.get()?.showDialogSuccessEditProduct()
        }.addOnFailureListener {
            addEditProductView?.get()?.stopProgressDialogGetAddEditProduct()
            addEditProductView?.get()?.showDialogSuccessEditProduct()
        }
    }

    override fun attachView(view: IAddEditProductView) {
        addEditProductView = WeakReference(view)
    }

    override fun detachView() {
        addEditProductView = null
        firebaseService.getmChildEventListener()?.let { firebaseService.getmFirebaseReference()?.removeEventListener(it) }
    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    companion object {
        private val DIRECTORY = "images/"
    }
}
