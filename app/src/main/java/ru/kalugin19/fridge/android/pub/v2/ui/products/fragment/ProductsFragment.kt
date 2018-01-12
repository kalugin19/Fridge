package ru.kalugin19.fridge.android.pub.v2.ui.products.fragment


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import kotlinx.android.synthetic.main.activity_authorization.*
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.android.synthetic.main.fragment_products.view.*
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.data.entity.Product
import ru.kalugin19.fridge.android.pub.v2.data.entity.TypeProducts
import ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.view.activity.AddEditProductActivity
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.Constants
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.ProgressDialogCustom
import ru.kalugin19.fridge.android.pub.v2.ui.products.activity.ProductsActivity
import ru.kalugin19.fridge.android.pub.v2.ui.products.adapter.MultiSelectProductAdapter
import ru.kalugin19.fridge.android.pub.v2.ui.products.fragment.ProductsFragment.Type.ALL
import ru.kalugin19.fridge.android.pub.v2.ui.products.fragment.ProductsFragment.Type.FRESH
import ru.kalugin19.fridge.android.pub.v2.ui.products.fragment.ProductsFragment.Type.SOON
import ru.kalugin19.fridge.android.pub.v2.ui.products.fragment.ProductsFragment.Type.SPOILED
import ru.kalugin19.fridge.android.pub.v2.ui.products.presenter.ProductsListPresenter
import ru.kalugin19.fridge.android.pub.v2.ui.products.utils.RecyclerItemClickListener
import java.util.*
import javax.inject.Inject


class ProductsFragment : BaseFragment(), IProductsListView {

    private var searchView: SearchView? = null
    private var mActionMode: ActionMode? = null
    private var adapter: MultiSelectProductAdapter? = null
    private lateinit var controlPanel: ProductsActivity.IControlPanel
    private var scrolledDistance = 0
    private var controlsVisible = true

    private var productsList = ArrayList<Product>()
    private var multiselectList = ArrayList<Product>()
    private var isMultiSelect = false

    object Type{
        internal const val ALL = 0
        internal const val SPOILED = 1
        const val FRESH = 2
        const val SOON = 3
    }
    var productsListPresenter: ProductsListPresenter? = null
        @Inject set

    private lateinit var progressDialogDeleteProducts: ProgressDialogCustom

    private var typeProducts: TypeProducts? = null


    private val mActionModeCallback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.products_multiselection_action_bar_menu, menu)
            controlPanel.showPanel(false)
            controlPanel.gonePanel(true)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_delete -> {
                    areYouSure(multiselectList)
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            controlPanel.showPanel(true)
            mActionMode = null
            isMultiSelect = false
            multiselectList = ArrayList()
            refreshAdapter()
            controlPanel.gonePanel(false)
        }
    }

    fun setIControlPanel(iControlPanel: ProductsActivity.IControlPanel) {
        this.controlPanel = iControlPanel
    }

    override fun onResume() {
        super.onResume()
        typeProducts?.let { productsListPresenter?.attachDatabaseReadListener(it) }
        if (searchView != null) {
            searchView!!.clearFocus()
        }
        typeProducts?.let { productsListPresenter?.getProducts(it, "") }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        productsListPresenter?.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

        fragmentComponent.inject(this)
        if (activity != null && arguments != null) {
            val a = arguments!!.getInt(PRODUCT_TYPE)
            when (a) {
                ALL -> {
                    typeProducts = TypeProducts.ALL
                    (activity as ProductsActivity).showFam()
                }
                FRESH -> {
                    typeProducts = TypeProducts.FRESH
                    (activity as ProductsActivity).showFam()
                }
                SOON -> {
                    typeProducts = TypeProducts.SOON
                    (activity as ProductsActivity).showFam()
                }
                SPOILED -> {
                    typeProducts = TypeProducts.SPOILED
                    (activity as ProductsActivity).showFam()
                }
            }
        }
        progressDialogDeleteProducts = ProgressDialogCustom(activity, getString(R.string.activity_products_progress_dialog_delete_products))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_products, container, false)
        fragmentComponent.inject(this)
        productsListPresenter?.attachView(this)
        v.productsRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    controlPanel.showPanel(false)
                    controlsVisible = false
                    scrolledDistance = 0
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    controlPanel.showPanel(true)
                    controlsVisible = true
                    scrolledDistance = 0
                }

                if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
                    scrolledDistance += dy
                }
            }
        })

        v.productsRecycler?.addOnItemTouchListener(RecyclerItemClickListener(activity, v.productsRecycler, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (isMultiSelect)
                    if (position != productsList.size - 1) {
                        multiSelect(position)
                    }
            }

            override fun onItemLongClick(view: View, position: Int) {
                if (!isMultiSelect) {
                    multiselectList = ArrayList()
                    isMultiSelect = true
                    if (activity != null && mActionMode == null) {
                        mActionMode = activity!!.startActionMode(mActionModeCallback)
                    }
                }
                multiSelect(position)
            }
        }))
        if (activity != null) {
            v.productsRecycler?.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        return v
    }

    fun multiSelect(position: Int) {
        if (multiselectList.contains(productsList[position]))
            multiselectList.remove(productsList[position])
        else
            multiselectList.add(productsList[position])

        if (multiselectList.size > 0)
            mActionMode?.title = "" + multiselectList.size
        else
            mActionMode?.title = ""
        refreshAdapter()
    }

    fun refreshAdapter() {
        adapter?.apply {
            setSelectedProducts(multiselectList)
            notifyDataSetChanged()
        }
    }

    override fun startProgressDialogGettingProducts() {
        progress_bar_loading.visibility = View.VISIBLE
        productsRecycler.visibility = View.GONE
        emptyState.visibility = View.GONE
    }

    override fun stopProgressDialogGettingProducts() {
        progress_bar_loading.visibility = View.GONE
        productsRecycler.visibility = View.VISIBLE
    }

    override fun startProgressDeleting() {
        progressDialogDeleteProducts.showProgress(true)
    }

    override fun stopProgressDeleting() {
        progressDialogDeleteProducts.showProgress(false)
    }

    override fun showErrorDeleting() {
        Snackbar.make(globalView, R.string.activity_products_progress_deleting_error, Snackbar.LENGTH_LONG).show()
    }

    override fun showNoInternet() {
        if (activity != null) {
            (activity as ProductsActivity).showNoInternetConnection()
        }
    }

    override fun setData(products: List<Product>, typeProducts: TypeProducts) {
        multiselectList = ArrayList()
        productsRecycler.layoutManager = LinearLayoutManager(context)
        adapter?.clean()

        if (mActionMode != null) {
            mActionMode!!.finish()
        }
        productsList = products as ArrayList<Product>
        if (products.isNotEmpty()) {
            productsRecycler.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
            adapter = activity?.let {
                MultiSelectProductAdapter(it, productsList, multiselectList, object : MultiSelectProductAdapter.IMultiSelectProductAdapter {
                    override fun clickByProduct(product: Product) {
                        goToEditInfoProduct(product)
                    }
                })
            }
            productsRecycler.adapter = adapter
        } else {
            productsRecycler.visibility = View.GONE
            when (typeProducts.ordinal) {
                ALL -> emptyState.text = getString(R.string.fragment_products_emptyState_all)
                FRESH -> emptyState.text = getString(R.string.fragment_products_emptyState_fresh)
                SOON -> emptyState.text = getString(R.string.fragment_products_emptyState_soon)
                SPOILED -> emptyState.text = getString(R.string.fragment_products_emptyState_spoiled)
            }
            emptyState.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        productsListPresenter?.detachDatabaseReadListener()
        productsListPresenter?.onStop()
    }

    override fun onPause() {
        super.onPause()
        productsListPresenter?.detachDatabaseReadListener()
        productsListPresenter?.onPause()
    }

    private fun goToEditInfoProduct(product: Product) {
        val intent = Intent(activity, AddEditProductActivity::class.java)
        intent.putExtra(Constants.INFO_PRODUCT, product)
        startActivity(intent)
    }

    private fun areYouSure(listProducts: List<Product>) {
        if (activity != null) {
            val builder = AlertDialog.Builder(activity!!)
            builder
                    .setTitle(R.string.fragment_products_dialog_delete_products_title)
                    .setMessage(R.string.fragment_products_dialog_delete_products_description)
                    .setCancelable(false)
                    .setPositiveButton(activity!!.getString(R.string.fragment_products_dialog_delete_products_positive)) { _, _ -> productsListPresenter?.deleteProducts(listProducts as MutableList<Product>) }
                    // добавляем одну кнопку для закрытия диалога
                    .setNegativeButton(activity!!.getString(R.string.fragment_products_dialog_delete_products_negative)
                    ) { dialog, _ -> dialog.cancel() }
                    // ok
                    .create()
            val alert = builder.create()
            alert.show()
            alert.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener { alert.dismiss() }
        }
    }

    companion object {
        private val HIDE_THRESHOLD = 20
        private val PRODUCT_TYPE = "product_type"

        fun newInstance(typeProducts: TypeProducts): ProductsFragment {
            val fragment = ProductsFragment()
            val args = Bundle()
            args.putInt(PRODUCT_TYPE, typeProducts.ordinal)
            fragment.arguments = args
            return fragment
        }
    }
}
