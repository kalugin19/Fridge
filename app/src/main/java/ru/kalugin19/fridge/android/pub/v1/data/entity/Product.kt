package ru.kalugin19.fridge.android.pub.v1.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Сущность - продукт
 *
 * @author Kalugin Valerij
 */
class Product : Parcelable {
    var createdDate: Long = 0
    var name: String? = null
        set(value) {
            field = value
            if (name != null) {
                this.upperName = value?.toUpperCase()
            }
        }

    var upperName: String? = null
        private set
    var photo: String? = null
    var spoiledDate: Long = 0
    var key: String? = null
    var photoName: String? = null
    var date: String? = null

    val formattedSpoiledDate: String
        get() {
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            return formatter.format(Date(spoiledDate))
        }

    @Exclude
    var typeMember: String? = null

    @Exclude
    var ownerEmail: String? = null

    constructor() {}

    constructor(name: String?, createdDate: Long, spoiledDate: Long, photo: String) {
        this.name = name
        if (name != null) {
            this.upperName = name.toUpperCase()
        }
        this.createdDate = createdDate
        this.spoiledDate = spoiledDate
        this.photo = photo
    }

    constructor(name: String, date: String) {
        this.name = name
        this.upperName = name.toUpperCase()
        this.date = date

    }

    protected constructor(`in`: Parcel) {
        createdDate = `in`.readLong()
        name = `in`.readString()
        photo = `in`.readString()
        spoiledDate = `in`.readLong()
        key = `in`.readString()
        photoName = `in`.readString()
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(createdDate)
        dest.writeString(name)
        dest.writeString(photo)
        dest.writeLong(spoiledDate)
        dest.writeString(key)
        dest.writeString(photoName)
    }

    companion object {

        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(`in`: Parcel): Product {
                return Product(`in`)
            }

            override fun newArray(size: Int): Array<Product?> {
                return arrayOfNulls(size)
            }
        }
    }
}
