package ru.kalugin19.fridge.android.pub.v2.ui.base.util


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.MenuItem
import ru.kalugin19.fridge.android.pub.v2.BuildConfig
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.ui.common_fridge.CommonFridgeActivity

/**
 * Для часто повторяющихся методов
 *
 * @author Kalugin Valerij
 */
object Util {


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
