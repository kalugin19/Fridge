package ru.kalugin19.fridge.android.pub.v1.ui.base.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.MenuItem

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType

import ru.kalugin19.fridge.android.pub.v1.BuildConfig
import ru.kalugin19.fridge.android.pub.v1.R
import ru.kalugin19.fridge.android.pub.v1.ui.common_fridge.CommonFridgeActivity
import ru.kalugin19.fridge.android.pub.v1.ui.common_fridge.CommonFridgeMemberAdapter

/**
 * Для часто повторяющихся методов
 *
 * @author Kalugin Valerij
 */
object Util {
    /**
     * Метод инициализирующий ImageLoader
     */
    fun initImageLoader(activity: Context) {
        val displayImageOptions = DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build()

        val config = ImageLoaderConfiguration.Builder(activity)
                .memoryCache(LruMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(displayImageOptions)
                .diskCacheExtraOptions(480, 320, null)
                .build()
        ImageLoader.getInstance().init(config)
    }

    /**
     * Метод определения наличия подключения к интернету
     */
    fun hasConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiInfo: NetworkInfo?
        wifiInfo = cm.activeNetworkInfo
        if (wifiInfo != null && wifiInfo.isConnected) {
            return false
        }
        if (wifiInfo != null && wifiInfo.isConnected) {
            return false
        }
        return wifiInfo == null || !wifiInfo.isConnected
    }


    /**
     * Метод, вызывающий события по нажатию на вкладки меню
     */
    fun clickMenuItem(context: Context?, item: MenuItem): Boolean {
        val id = item.itemId
        if (context != null) {
            if (id == R.id.nav_info) {
//                val intent = Intent(context, IntoActivity::class.java)
//                intent.putExtra(IntoActivity.INFO, "")
//                context.startActivity(intent)
                return true
            }
            if (id == R.id.nav_common_fridge) {
                val intent = Intent(context, CommonFridgeActivity::class.java)
                context.startActivity(intent)
                return true
            }
            if (id == R.id.nav_share) {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(context.resources.getString(R.string.share_app_link), BuildConfig.APP_LINK))
                shareIntent.type = "text/plain"
                context.startActivity(shareIntent)
                return true
            }
        }
        return false
    }

}
