package ru.kalugin19.fridge.android.pub.v1.ui.products.presenter

import android.text.TextUtils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver

import java.util.ArrayList

import javax.inject.Inject

import ru.kalugin19.fridge.android.pub.v1.data.entity.Product
import ru.kalugin19.fridge.android.pub.v1.data.entity.TypeProducts
import ru.kalugin19.fridge.android.pub.v1.data.model.NoInternetException
import ru.kalugin19.fridge.android.pub.v1.data.model.ProductModel
import ru.kalugin19.fridge.android.pub.v1.data.remote.FirebaseService
import ru.kalugin19.fridge.android.pub.v1.ui.products.fragment.IProductsListView


/**
 * Реализация презентера для фрагмента ProductsFragment
 *
 * @author Kalugin Valerij
 */
class ProductsListPresenter @Inject
internal constructor(private val firebaseService: FirebaseService?, private val productModel: ProductModel) : IProductsListPresenter {

    private lateinit var gettingProductsSubscriber: DisposableObserver<List<Product>>
    private var addProductSubscriber: DisposableObserver<Boolean>? = null
    private var deleteProductSubscriber: DisposableObserver<String>? = null
    private var mChildEventListener: ChildEventListener? = null
    private var productsListView: IProductsListView? = null
    private var countProduct = 0
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    override fun attachView(view: IProductsListView) {
        productsListView = view
    }

    override fun detachView() {
        productsListView = null
        if (firebaseService?.getmFirebaseReference() != null && firebaseService.getmChildEventListener() != null) {
            firebaseService.getmFirebaseReference()!!.removeEventListener(firebaseService.getmChildEventListener())
        }
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onStop() {
        cancelSubscription()
    }

    override fun onPause() {
        cancelSubscription()
    }

    fun attachDatabaseReadListener(typeProducts: TypeProducts) {

        if (mChildEventListener == null) {
            mChildEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {}

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    getProducts(typeProducts, "")
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                    getProducts(typeProducts, "")
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot?, s: String?) {}

                override fun onCancelled(databaseError: DatabaseError?) {}
            }

            firebaseService?.getmFirebaseReference()!!.addChildEventListener(mChildEventListener)
        }
    }

    fun detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            firebaseService?.getmFirebaseReference()?.removeEventListener(mChildEventListener)
            mChildEventListener = null
        }
    }


    override fun getProducts(typeProducts: TypeProducts, searchText: String) {
        productsListView?.startProgressDialogGettingProducts()
        gettingProductsSubscriber = object : DisposableObserver<List<Product>>() {
            override fun onComplete() {
                this.dispose()
            }

            override fun onError(e: Throwable) {
                productsListView?.setData(ArrayList(), typeProducts)
                productsListView?.stopProgressDialogGettingProducts()
                if (e is NoInternetException) {
                    productsListView?.showNoInternet()
                }
            }

            override fun onNext(products: List<Product>) {
                productsListView?.stopProgressDialogGettingProducts()
                productsListView?.setData(products, typeProducts)
            }
        }

        productModel.getProducts(typeProducts, searchText).observeOn(AndroidSchedulers.mainThread())
                .subscribe(gettingProductsSubscriber)
    }

    override fun addProduct(product: Product) {
        addProductSubscriber = object : DisposableObserver<Boolean>() {
            override fun onComplete() {
                this.dispose()
            }

            override fun onNext(t: Boolean) {
                //For next tasks
            }


            override fun onError(e: Throwable) {
                //nothing
            }

        }
        productModel.addProduct(product).observeOn(AndroidSchedulers.mainThread())
                .subscribe(addProductSubscriber as DisposableObserver<Boolean>)
    }

    private fun cancelSubscription() {
        if (!gettingProductsSubscriber.isDisposed) {
            gettingProductsSubscriber.dispose()
        }
        if (addProductSubscriber != null && !addProductSubscriber!!.isDisposed) {
            addProductSubscriber?.dispose()
            addProductSubscriber = null
        }
        if (deleteProductSubscriber != null && !deleteProductSubscriber!!.isDisposed) {
            deleteProductSubscriber?.dispose()
            deleteProductSubscriber = null
        }
    }

    override fun deleteProducts(list: MutableList<Product>) {
        productsListView?.startProgressDeleting()
        deleteProductSubscriber = object : DisposableObserver<String>() {
            override fun onComplete() {
                this.dispose()
            }

            override fun onError(e: Throwable) {
                productsListView?.stopProgressDeleting()
            }

            override fun onNext(photoName: String) {
                countProduct = countProduct + 1
                if (!TextUtils.isEmpty(photoName)) {
                    val desertRef = storageReference.child(DIRECTORY + photoName)
                    desertRef.delete().addOnSuccessListener {
                        if (list.size == countProduct) {
                            productsListView?.stopProgressDeleting()
                        }
                    }.addOnFailureListener {
                        if (list.size == countProduct) {
                            productsListView?.stopProgressDeleting()
                        }
                    }
                } else {
                    if (list.size == countProduct) {
                        productsListView?.stopProgressDeleting()
                    }
                }
            }
        }
        productModel.deleteProducts(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteProductSubscriber as DisposableObserver<String>)
    }

    companion object {
        private val DIRECTORY = "images/"
    }
}
