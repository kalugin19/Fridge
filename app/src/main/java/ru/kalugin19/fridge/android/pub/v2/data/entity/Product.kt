package ru.kalugin19.fridge.android.pub.v2.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Сущность - продукт
 *
 * @author Kalugin Valerij
 */
class Product() : Parcelable {

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeLong(createdDate)
        p0?.writeString(name)
        p0?.writeString(upperName)
        p0?.writeString(photo)
        p0?.writeLong(spoiledDate)
        p0?.writeString(key)
        p0?.writeString(photoName)
        p0?.writeString(date)
        p0?.writeString(typeMember)
        p0?.writeString(ownerEmail)
    }

    override fun describeContents(): Int {
        return 0
    }


    var createdDate: Long = 0
    var name: String? = null
//        set(value) {
//            field = value
//            if (name != null) {
//                this.upperName = value?.toUpperCase()
//            }
//        }

    private var upperName: String? = null
    var photo: String? = null
    var spoiledDate: Long = 0
    var key: String? = null
    var photoName: String? = null
    var date: String? = null

    @Exclude
    var typeMember: String? = null

    @Exclude
    var ownerEmail: String? = null

    constructor(parcel: Parcel) : this() {
        createdDate = parcel.readLong()
        name = parcel.readString()
        upperName = parcel.readString()
        photo = parcel.readString()
        spoiledDate = parcel.readLong()
        key = parcel.readString()
        photoName = parcel.readString()
        date = parcel.readString()
        typeMember = parcel.readString()
        ownerEmail = parcel.readString()
    }



    constructor(name: String?, createdDate: Long, spoiledDate: Long, photo: String) : this() {
        this.name = name
        if (name != null) {
            this.upperName = name.toUpperCase()
        }
        this.createdDate = createdDate
        this.spoiledDate = spoiledDate
        this.photo = photo
    }

    constructor(name: String, date: String) : this() {
        this.name = name
        this.upperName = name.toUpperCase()
        this.date = date

    }

    fun getFormattedSpoiledDate(): String{
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(Date(spoiledDate))
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }


}
