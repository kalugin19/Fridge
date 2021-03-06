package ru.kalugin19.fridge.android.pub.v2.ui.products.adapter

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_authorization.view.*
import kotlinx.android.synthetic.main.item_product.view.*
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.data.entity.Product
import ru.kalugin19.fridge.android.pub.v2.data.entity.TypeMember
import ru.kalugin19.fridge.android.pub.v2.ui.layoutInflater
import ru.kalugin19.fridge.android.pub.v2.ui.products.adapter.MultiSelectProductAdapter.DateObj.UTC
import java.util.*
import java.util.concurrent.TimeUnit


class MultiSelectProductAdapter(private val context: Context, private var dataSet: ArrayList<Product>, selectedList: ArrayList<Product>, private val iMultiSelectProductAdapter: IMultiSelectProductAdapter?) : RecyclerView.Adapter<ProductViewHolder>() {
    override fun getItemCount(): Int {
        return dataSet.size
    }

    private var selectedProducts: List<Product> = ArrayList()


    object DateObj {
        val UTC = "UTC"
    }


    interface IMultiSelectProductAdapter {
        fun clickByProduct(product: Product)
    }

    init {
        this.selectedProducts = selectedList
    }

    fun setSelectedProducts(selectedProducts: List<Product>) {
        this.selectedProducts = selectedProducts
    }

    fun clean() {
        dataSet = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder? {
        return ProductViewHolder(context.layoutInflater.inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = dataSet[position]
        val count = millisecondsToDays(product.spoiledDate, Calendar.getInstance(TimeZone.getTimeZone(UTC)).timeInMillis)
        val countDays = context.resources.getQuantityString(R.plurals.plurals_for_days, count, count)
        holder.apply {
            this.countDays?.text = countDays
            titleTxt?.text = product.name
            if (product.typeMember == TypeMember.NOT_OWNER.text){
                txtDate.visibility = View.VISIBLE
                txtDate.text = product.ownerEmail
            } else{
                txtDate.visibility = View.GONE
            }

            itemView?.setOnClickListener {
                iMultiSelectProductAdapter?.clickByProduct(product)
            }

            photoImg?.let {
                Glide.with(context).load(product.photo)
                    .into(it)
            }

            if (selectedProducts.contains(dataSet[position])) {
                globalView?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelectedItem))
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    globalView?.background = null
                }
            }
        }

    }
}

private fun millisecondsToDays(spoiledDate: Long, currentDate: Long): Int {
    val currentCalendar = Calendar.getInstance()
    currentCalendar.timeInMillis = currentDate
    currentCalendar.set(
            currentCalendar.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH),
            currentCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
    currentCalendar.clear(Calendar.MILLISECOND)
    val spoiledCalendar = Calendar.getInstance()
    spoiledCalendar.timeInMillis = spoiledDate
    spoiledCalendar.set(
            spoiledCalendar.get(Calendar.YEAR),
            spoiledCalendar.get(Calendar.MONTH),
            spoiledCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
    spoiledCalendar.clear(Calendar.MILLISECOND)
    val milliseconds = spoiledCalendar.timeInMillis - currentCalendar.timeInMillis
    return if (milliseconds >= 0) {
        TimeUnit.MILLISECONDS.toDays(milliseconds).toInt()
    } else {
        0
    }
}


class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var globalView: View? = itemView.globalView
    var txtDate: TextView = itemView.item_product_date
    var photoImg: ImageView? = itemView.itemProductImgViewPhoto
    var countDays: TextView? = itemView.txtViewCountDays
    var titleTxt: TextView? = itemView.item_product_textView_title
}


