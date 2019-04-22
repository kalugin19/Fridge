package ru.kalugin19.fridge.android.pub.v2.ui.products.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast

import com.nostra13.universalimageloader.core.ImageLoader

import javax.inject.Inject

import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.app_bar_products.*
import kotlinx.android.synthetic.main.content_products.*
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.data.entity.TypeProducts
import ru.kalugin19.fridge.android.pub.v2.data.local.db.UserSharedPreferences
import ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.view.activity.AddEditProductActivity
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.Util
import ru.kalugin19.fridge.android.pub.v2.ui.base.view.activity.BaseActivity
import ru.kalugin19.fridge.android.pub.v2.ui.products.fragment.ProductsFragment

/**
 * Экран Список продуктов
 */
class ProductsActivity : BaseActivity(), IProductsView, NavigationView.OnNavigationItemSelectedListener {

    var userSharedPreferences: UserSharedPreferences? = null
        @Inject set

    private var controlPanel: IControlPanel? = null

    private lateinit var email: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        setSupportActionBar(toolbar)
        controlPanel = object : IControlPanel {
            override fun showPanel(flag: Boolean) {
                if (flag) {
                    showViews()
                } else {
                    hideViews()
                }
            }

            override fun gonePanel(flag: Boolean) {
                goneBottomElements(flag)
            }
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        selectFragment(bottomNavigationView.menu.getItem(0))
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            selectFragment(item)
            true
        }

//        val toggle = object : ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
//
//            override fun onDrawerStateChanged(newState: Int) {
//                if (newState == DrawerLayout.STATE_SETTLING) {
//                    closeKeyBoard()
//                }
//                super.onDrawerStateChanged(newState)
//            }
//        }

//        drawer.addDrawerListener(toggle)
//        toggle.syncState()
//        navView.setNavigationItemSelectedListener { item ->
//            Util.clickMenuItem(this@ProductsActivity, item)
//            drawer.closeDrawers()
//            true
//        }
//        email = navView.getHeaderView(0).findViewById(R.id.email)
//        email.text = userSharedPreferences?.email
        val photoImageView = navView.getHeaderView(0).findViewById<CircleImageView>(R.id.navDrawerImageView)
        Util.initImageLoader(this)
        ImageLoader.getInstance().displayImage(userSharedPreferences?.photoUrl, photoImageView)

        fab_add_edit_product.setOnClickListener {
            val intent = Intent(this@ProductsActivity, AddEditProductActivity::class.java)
            startActivity(intent)
        }
        disableShiftMode(bottomNavigationView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    /**
     * скрыть панель с кнопками при скроллинге вниз
     */
    fun hideViews() {
        val lp = bottomNavigationView.layoutParams as CoordinatorLayout.LayoutParams
        val bottomMargin = lp.bottomMargin
        bottomNavigationView.animate().translationY((bottomNavigationView.height + bottomMargin).toFloat()).setInterpolator(AccelerateInterpolator(2f)).start()
        val lpFam = fab_add_edit_product.layoutParams as CoordinatorLayout.LayoutParams
        val famMargin = lpFam.bottomMargin
        fab_add_edit_product.animate().translationY((fab_add_edit_product.height + bottomNavigationView.height + bottomMargin + famMargin).toFloat()).setInterpolator(AccelerateInterpolator(2f)).start()
    }

    /**
     * показать панель c кнопками при скроллинге вверх
     */
    fun showViews() {
        bottomNavigationView.animate().translationY(0f).setInterpolator(DecelerateInterpolator(2f)).start()
        fab_add_edit_product.animate().translationY(0f).setInterpolator(DecelerateInterpolator(2f)).start()
    }


    private fun selectFragment(item: MenuItem) {
        var fragment: ProductsFragment? = null
        // init corresponding fragment
        when (item.itemId) {
            R.id.bottom_navigation_menu_all -> fragment = ProductsFragment.newInstance(TypeProducts.ALL)
            R.id.bottom_navigation_menu_fresh -> fragment = ProductsFragment.newInstance(TypeProducts.FRESH)
            R.id.bottom_navigation_menu_soon -> fragment = ProductsFragment.newInstance(TypeProducts.SOON)
            R.id.bottom_navigation_menu_spoiled -> fragment = ProductsFragment.newInstance(TypeProducts.SPOILED)
        }

        updateToolbarText(item.title)

        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            controlPanel?.let { fragment.setIControlPanel(it) }
            ft.replace(R.id.activity_products_container, fragment, fragment.tag)
            ft.commit()
        }
    }

    private fun updateToolbarText(text: CharSequence) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = text
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun showFam() {
        if (fab_add_edit_product.visibility != View.VISIBLE) {
            val animShow = AnimationUtils.loadAnimation(this, R.anim.scale_show)
            fab_add_edit_product.startAnimation(animShow)
            fab_add_edit_product.visibility = View.VISIBLE
        }
    }

    fun showNoInternetConnection() {
        val toast = Toast.makeText(applicationContext,
                getString(R.string.activity_products_error_no_internet), Toast.LENGTH_SHORT)
        toast.show()
    }

    fun goneBottomElements(flag: Boolean) {
        fab_add_edit_product.visibility = if (flag) View.GONE else View.VISIBLE
        bottomNavigationView.visibility = if (flag) View.GONE else View.VISIBLE
    }

    fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView

//                item.setShiftingMode(false)
                // set once again checked value, so view will be updated

                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("BNVHelper", "Unable to get shift mode field", e)
        } catch (e: IllegalAccessException) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e)
        }

    }
    interface IControlPanel {
        fun showPanel(flag: Boolean)

        fun gonePanel(flag: Boolean)
    }

}
