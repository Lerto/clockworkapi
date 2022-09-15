package one.clockwork.apimodule.managers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import one.clockwork.apimodule.api.ApiService
import one.clockwork.apimodule.models.Model

class MenuManager constructor(
    private val context: Context
) {
    var menu = Model.Menu(ArrayList(), ArrayList())
    var conceptIdThis = ""
    private val menuUpdate = ArrayList<Model.Menu>()

    private val menuThis: MutableLiveData<ArrayList<Model.CategoryProduct>> =
        MutableLiveData(ArrayList())
    val menuThisLive: LiveData<ArrayList<Model.CategoryProduct>> = menuThis

    private val conceptData: MutableLiveData<ArrayList<Model.Concept>> =
        MutableLiveData(ArrayList())
    val conceptDataLive: LiveData<ArrayList<Model.Concept>> = conceptData

    private val productData: MutableLiveData<ArrayList<Model.Product>> =
        MutableLiveData(ArrayList())
    val productDataLive: LiveData<ArrayList<Model.Product>> = productData

    private val categoryData: MutableLiveData<ArrayList<Model.Category>> =
        MutableLiveData(ArrayList())
    val categoryDataLive: LiveData<ArrayList<Model.Category>> = categoryData

    init {
        getMenu()
    }

    private fun getMenu() {
//        val db = MenuDB.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
//            val menuData = db?.menuDao()?.getToken()
//            menu = menuData?.menu ?: ArrayList()
            getConcepts()
        }
    }

    private fun saveMenu() {
//        val db = MenuDB.getInstance(context)
//        menu = menuNew
//        CoroutineScope(Dispatchers.IO).launch {
//            val menuSave = MenuDatabase()
//            menuSave.id = 1
//            menuSave.menu = menu
//            menuSave.delivery = terminalConcepts.value
//
//            db?.menuDao()?.addUser(menuSave)
//        }
    }

    fun splitCategoryConcept(conceptId: String) {
        conceptIdThis = conceptId
        val catParent = ArrayList<Model.Category>()
        menu.categories.forEach { category ->
            if (category.parentCategory == null && category.conceptId == conceptId) {
                catParent.add(category)
                if (catParent.size == 1) {
                    catParent[0].isSelected = true
                }
            }
        }
        categoryData.postValue(catParent)
        splitMenu(catParent[0])
    }

    fun splitMenu(parentCatId: Model.Category) {
        Log.d("LogMenuManagerSplit", parentCatId.toString())
        val menuSplit = ArrayList<Model.CategoryProduct>()
        val categoryAll = ArrayList<Model.Category>()
        splitCategory(parentCatId, categoryAll)
        Log.d("LogMenuManagerSplit", categoryAll.toString())

        categoryAll.forEach { cat ->
            val productSplit = ArrayList<Model.Product>()
            menu.products.forEach { product ->
                if (product.categoryId == cat._id) {
                    productSplit.add(product)
                }
            }
            if (productSplit.isNotEmpty()) {
                menuSplit.add(
                    Model.CategoryProduct(
                        cat.name,
                        productSplit,
                        cat.imageSize ?: "63032ca4c9cf2abb6cf57df8"
                    )
                )
            }
        }
        menuThis.postValue(menuSplit)
        Log.d("LogMenuManager", menuThis.value.toString())
    }

    private fun splitCategory(
        categoryId: Model.Category,
        menuCategories: ArrayList<Model.Category>
    ) {
        menu.categories.forEach { cat ->
            if (cat.parentCategory == categoryId._id) {
                menuCategories.add(cat)
                splitCategory(cat, menuCategories)
            }
        }
    }

    private suspend fun getProducts() {
        val req = ApiService.apiCustomer().getProducts()
        Log.d("LogMenuManager", req.toString())
        Log.d("LogMenuManager", req.body().toString())
        if (req.isSuccessful) {
            if (req.body() != null) {
                menu.products = req.body()!!.data

                productData.postValue(menu.products)
                conceptData.value?.get(0)?._id?.let { splitCategoryConcept(it) }
            }
        }
    }

    private suspend fun getCategory() {
        val req = ApiService.apiCustomer().getCategories()
        Log.d("LogMenuManager", req.toString())
        Log.d("LogMenuManager", req.body().toString())
        if (req.isSuccessful) {
            if (req.body() != null) {
                menu.categories = req.body()!!.data
                getProducts()
            }
        }
    }

    private fun getConcepts() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getConcepts()
            Log.d("LogMenuManager", req.toString())
            Log.d("LogMenuManager", req.body().toString())
            if (req.isSuccessful) {
                if (req.body() != null) {
                    val concepts = req.body()!!.data
                    conceptData.postValue(concepts)
                    getCategory()
                }
            }
        }
    }
}