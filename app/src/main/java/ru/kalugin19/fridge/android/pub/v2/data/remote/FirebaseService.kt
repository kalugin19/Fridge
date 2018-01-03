package ru.kalugin19.fridge.android.pub.v2.data.remote


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.kalugin19.fridge.android.pub.v2.data.entity.Member

import java.util.HashMap

import javax.inject.Inject

import ru.kalugin19.fridge.android.pub.v2.data.entity.Product
import ru.kalugin19.fridge.android.pub.v2.data.entity.TypeMember
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.Constants


/**
 * Сервис для работы с Firebase
 */
class FirebaseService @Inject
constructor() {

    //Firebase Instance Variables
    var database: FirebaseDatabase? = null
    var mFirebaseReferenceMembers: DatabaseReference? = null
    var mFirebaseReferenceProducts: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null
    private var mAuth: FirebaseAuth? = null
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null
    private var mFirebaseStorage: FirebaseStorage? = null
    private var mChatPhotosReference: StorageReference? = null
    private var mainReference: DatabaseReference? = null
    init {
        this.database = FirebaseDatabase.getInstance()
        this.mAuth = FirebaseAuth.getInstance()
        this.mFirebaseStorage = FirebaseStorage.getInstance()
        this.mainReference = database?.reference?.child(Constants.FRIDGES)?.child(Constants.FRIDGE + mAuth?.currentUser?.uid)
        this.mFirebaseReferenceMembers = mainReference?.child(Constants.MEMBERS)
        this.mFirebaseReferenceProducts = mainReference?.child(Constants.PRODUCTS)
        this.mChatPhotosReference = mFirebaseStorage?.reference?.child(Constants.PRODUCTS_PHOTOS)
        addMember()
    }


    fun getFridgesReferences(): DatabaseReference? {
        return database?.getReferenceFromUrl(database?.reference?.child(Constants.FRIDGES).toString())
    }

    fun addProduct(product: Product): Task<Void>? {
        return mFirebaseReferenceProducts?.push()?.setValue(product)
    }

    fun deletedProduct(product: Product): Task<Void> {
        return mFirebaseReferenceProducts!!.child(product.key).removeValue()
    }

    fun editProduct(product: Product): Task<Void> {
        val hopperRef = mFirebaseReferenceProducts
        val hopperUpdates = HashMap<String, Any>()
        product.key?.let { hopperUpdates.put(it, product) }
        return hopperRef!!.updateChildren(hopperUpdates)
    }

    fun getmFirebaseReference(): DatabaseReference? {
        return mFirebaseReferenceProducts
    }

    fun setmFirebaseReference(mFirebaseReference: DatabaseReference) {
        this.mFirebaseReferenceProducts = mFirebaseReference
    }

    fun getmChildEventListener(): ChildEventListener? {
        return mChildEventListener
    }

    fun setChildEventListener(childEventListener: ChildEventListener) {
        this.mChildEventListener = childEventListener
    }


    fun setmChildEventListener(mChildEventListener: ChildEventListener) {
        this.mChildEventListener = mChildEventListener
    }

    fun getmAuth(): FirebaseAuth? {
        return mAuth
    }

    fun setmAuth(mAuth: FirebaseAuth) {
        this.mAuth = mAuth
    }

    fun getmAuthStateListener(): FirebaseAuth.AuthStateListener? {
        return mAuthStateListener
    }

    fun setmAuthStateListener(mAuthStateListener: FirebaseAuth.AuthStateListener) {
        this.mAuthStateListener = mAuthStateListener
    }

    fun getmFirebaseStorage(): FirebaseStorage? {
        return mFirebaseStorage
    }

    fun setmFirebaseStorage(mFirebaseStorage: FirebaseStorage) {
        this.mFirebaseStorage = mFirebaseStorage
    }

    fun getmChatPhotosReference(): StorageReference? {
        return mChatPhotosReference
    }

    fun setmChatPhotosReference(mChatPhotosReference: StorageReference) {
        this.mChatPhotosReference = mChatPhotosReference
    }

    private fun addMember() {
        var isExist = false
        val valueListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                loop@ for (item in p0?.children!!) {
                    val member = item.getValue(String::class.java)
                    if (member.equals(mAuth?.currentUser?.uid)) {
                        isExist = true
                        break@loop
                    }
                }
            }
        }
        mFirebaseReferenceMembers?.addValueEventListener(valueListener)
        if (!isExist) {

            mFirebaseReferenceMembers?.removeEventListener(valueListener)
            val members = HashMap<String, Member>()
            mAuth?.currentUser?.let {
                val member = it.email?.let { it1 -> Member(it.displayName.toString(),it1, TypeMember.OWNER.text) }
                member?.let { it1 -> members.put(it.uid, it1) }
                mFirebaseReferenceMembers?.updateChildren(members as Map<String, Any>?)
                val owner = HashMap<String, Member>()
                if (member != null) {
                    owner.put(Constants.OWNER, member)
                    mainReference?.updateChildren(owner as Map<String, Any>?)
                }
            }
        }
    }

}
