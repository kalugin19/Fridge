package ru.kalugin19.fridge.android.pub.v1.ui.add_edit_product.view.adapters

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout

import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener

import java.io.File
import java.util.ArrayList

import kotlinx.android.synthetic.main.images_adapter_layout_images_item.view.*
import kotlinx.android.synthetic.main.item_button_add_product_layout.view.*
import ru.kalugin19.fridge.android.pub.v1.R
import ru.kalugin19.fridge.android.pub.v1.data.entity.PathImagesProduct
import ru.kalugin19.fridge.android.pub.v1.ui.base.util.Constants
import ru.kalugin19.fridge.android.pub.v1.ui.base.util.Util


class AddEditProductAdapter(private val context: Context, private val clickButton: IClickButton?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_IMAGES = 1
    private val list: ArrayList<PathImagesProduct> = ArrayList()
    private val imageSize: Int

    interface IClickButton {
        fun clickAddButton()

        fun clickDeleteButton(pathImagesProduct: PathImagesProduct)
    }

    init {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay?.getMetrics(metrics)
        this.imageSize = metrics.widthPixels / 5
    }


    override fun getItemViewType(position: Int): Int {
        return if (!list.isEmpty() && list[0].path != null) {
            VIEW_TYPE_IMAGES
        } else {
            VIEW_TYPE_HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_button_add_product_layout, parent, false)
            return HeaderViewHolder(view)
        } else if (viewType == VIEW_TYPE_IMAGES) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.images_adapter_layout_images_item, parent, false)
            return ImagesViewHolder(view)
        }
        return null
    }

    fun addHeader() {
        if (list.isEmpty()) {
            list.add(PathImagesProduct(null))
            notifyItemInserted(0)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pathImagesProduct = list[position]
        if (holder is HeaderViewHolder) {
            holder.addProduct.setOnClickListener {
                clickButton?.clickAddButton()
            }

        } else if (holder is ImagesViewHolder) {
            Util.initImageLoader(context)
            holder.image.layoutParams = RelativeLayout.LayoutParams(imageSize, imageSize)
            var resultPath: String? = pathImagesProduct.path
            if (resultPath != null && !resultPath.startsWith(Constants.HTTP)) {
                val uri = Uri.fromFile(File(pathImagesProduct.path))
                resultPath = Uri.decode(uri.toString())
            }
            if (resultPath != null && !resultPath.isEmpty()) {
                ImageLoader.getInstance().displayImage(resultPath, holder.image, object : ImageLoadingListener {
                    override fun onLoadingStarted(imageUri: String, view: View) {
                        holder.progressBar.visibility = View.VISIBLE
                        holder.image.visibility = View.GONE
                        holder.delete.visibility = View.GONE
                    }

                    override fun onLoadingFailed(imageUri: String, view: View, failReason: FailReason) {
                        holder.progressBar.visibility = View.GONE
                        holder.image.visibility = View.VISIBLE
                        holder.delete.visibility = View.VISIBLE
                        holder.image.setImageResource(R.drawable.ic_about_us)
                    }

                    override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap) {
                        holder.progressBar.visibility = View.GONE
                        holder.image.visibility = View.VISIBLE
                        holder.delete.visibility = View.VISIBLE
                    }

                    override fun onLoadingCancelled(imageUri: String, view: View) {

                    }
                })
            } else {
                holder.image.setImageResource(R.drawable.ic_about_us)
            }
            holder.delete.setOnClickListener {
                clickButton?.clickDeleteButton(pathImagesProduct)
            }

            holder.image.setOnClickListener {
                clickButton?.clickDeleteButton(pathImagesProduct)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun delete(pathImagesProduct: PathImagesProduct) {
        val position = list.indexOf(pathImagesProduct)
        if (position != -1) {
            list.removeAt(position)
            notifyDataSetChanged()
            addHeader()
        }
    }


    fun insert(pathImagesProduct: PathImagesProduct) {
        list.clear()
        list.add(pathImagesProduct)
        notifyDataSetChanged()
    }


    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var addProduct: ImageView = itemView.activity_add_edit_product_adapter_add_image
    }


    class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var image = itemView.image
        internal var delete: ImageView = itemView.delete
        internal var progressBar: ProgressBar = itemView.progress_bar
    }
}
