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
    private val menuUpdate = ArrayList<Model.CategoryProduct>(ArrayList())

    var sizeMenu = 0

    private val menuThis: MutableLiveData<ArrayList<Model.CategoryProduct>> =
        MutableLiveData(ArrayList())
    val menuThisLive: LiveData<ArrayList<Model.CategoryProduct>> = menuThis

    val productByCode: MutableLiveData<Model.Product> =
        MutableLiveData()

    private val featuredData: MutableLiveData<ArrayList<Model.Product>> =
        MutableLiveData(ArrayList())
    val featuredDataLive: LiveData<ArrayList<Model.Product>> = featuredData

    private val conceptData: MutableLiveData<ArrayList<Model.Concept>> =
        MutableLiveData(ArrayList())
    val conceptDataLive: LiveData<ArrayList<Model.Concept>> = conceptData

    private val productData: MutableLiveData<ArrayList<Model.Product>> =
        MutableLiveData(ArrayList())
    val productDataLive: LiveData<ArrayList<Model.Product>> = productData

    private val categoryData: MutableLiveData<ArrayList<Model.Category>> =
        MutableLiveData(ArrayList())
    val categoryDataLive: LiveData<ArrayList<Model.Category>> = categoryData

    private val categorySmartData: MutableLiveData<ArrayList<Model.SmartCategories>> =
        MutableLiveData(ArrayList())
    val categorySmartDataLive: LiveData<ArrayList<Model.SmartCategories>> = categorySmartData

    private val productSmartData: MutableLiveData<ArrayList<Model.SmartProducts>> =
        MutableLiveData(ArrayList())
    val productSmartDataLive: LiveData<ArrayList<Model.SmartProducts>> = productSmartData

    private val favoriteData: MutableLiveData<ArrayList<Model.Product>> =
        MutableLiveData(ArrayList())
    val favoriteDataLive: LiveData<ArrayList<Model.Product>> = favoriteData

    init {
        getFavorite()
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

        if (catParent.isNotEmpty()) {
            splitMenu(catParent[0])
        }
    }

    fun splitMenu(parentCatId: Model.Category) {
        menuThis.postValue(ArrayList())
        menuUpdate.clear()
        Log.d("LogMenuManagerSplit", parentCatId.toString())
        val categoryAll = ArrayList<Model.Category>()
        splitCategory(parentCatId, categoryAll)
        categoryAll.add(parentCatId)
        Log.d("LogMenuManagerSplit", categoryAll.toString())

        sizeMenu = categoryAll.size
        CoroutineScope(Dispatchers.Main).launch {
            categoryAll.forEach { cat ->
                getProductsByCategory(cat)
            }

        }
    }

    private fun splitCategory(
        categoryId: Model.Category, menuCategories: ArrayList<Model.Category>
    ) {
        menu.categories.forEach { cat ->
            if (cat.parentCategory == categoryId._id) {
                menuCategories.add(cat)
                splitCategory(cat, menuCategories)
            }
        }
    }

    private suspend fun getProductsByCategory(cat: Model.Category) {
        val req = ApiService.apiCustomer().getProductByCategory(cat._id)
        Log.d("LogMenuManager", req.toString())
        Log.d("LogMenuManager", req.body().toString())
        if (req.isSuccessful) {
            if (req.body() != null && req.body()!!.data.isNotEmpty()) {
                menuUpdate.add(
                    Model.CategoryProduct(
                        cat.name, req.body()!!.data, cat.imageSize ?: "63032ca4c9cf2abb6cf57df8"
                    )
                )
            } else {
                sizeMenu--
            }
        }else {
            sizeMenu--
        }

        if(menuUpdate.size == sizeMenu){
            menuThis.postValue(menuUpdate)
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
                if (conceptData.value != null) {
                    if (conceptData.value!!.isNotEmpty()) {
                        conceptData.value!![0]._id?.let {
                            if (conceptIdThis.isNotEmpty()) {
                                splitCategoryConcept(conceptIdThis)
                            } else {
                                splitCategoryConcept(it)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getSmartProducts() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getSmartProducts()
            Log.d("LogMenuManager", req.toString())
            Log.d("LogMenuManager", req.body().toString())
            if (req.isSuccessful) {
                if (req.body() != null) {
                    productSmartData.postValue(req.body()!!.data)
                }
            }
        }
    }

    private fun getSmartCategories() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getSmartCategories()
            Log.d("LogMenuManager", req.toString())
            Log.d("LogMenuManager", req.body().toString())
            if (req.isSuccessful) {
                if (req.body() != null) {
                    categorySmartData.postValue(req.body()!!.data)
                }
            }
        }
    }

    fun getProductByCode(code: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getProductByCode(code)
            Log.d("LogMenuManager", req.toString())
            Log.d("LogMenuManager", req.body().toString())
            if (req.isSuccessful) {
                if (req.body() != null) {
                    productByCode.postValue(req.body())
                }
            }
        }
    }

    private suspend fun getCategory() {
        val req = ApiService.apiCustomer().getCategories()
        Log.d("LogMenuManager", req.toString())
        Log.d("LogMenuManager", req.body().toString())
        Log.d("LogMenuManager", req.errorBody()?.string().toString())
        if (req.isSuccessful) {
            if (req.body() != null) {
                menu.categories = req.body()!!.data
                getProducts()
            }
        }
    }

    fun getConcepts() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getConcepts()
            Log.d("LogMenuManager", req.toString())
            Log.d("LogMenuManager", req.body().toString())
            Log.d("LogMenuManager", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                if (req.body() != null) {
                    val concepts = req.body()!!.data
                    conceptData.postValue(concepts)
                    getCategory()
                }
            }
        }
    }

    fun getFavorite() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getFavorite(conceptIdThis)
            Log.d("LogMenuManager", req.toString())
            Log.d("LogMenuManager", req.headers().toString())
            Log.d("LogMenuManager", req.body().toString())
            Log.d("LogMenuManager", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                if (req.body() != null) {
                    val products = req.body()!!.data
                    favoriteData.postValue(products)
                }
            }
        }
    }

    fun sendFavorite(code: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().sendFavorite(Model.FavoriteCode(code, conceptIdThis))
            Log.d("LogMenuManager", req.toString())
            Log.d("LogMenuManager", req.body().toString())
            Log.d("LogMenuManager", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                getFavorite()
            }
        }
    }

    fun deleteFavorite(code: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val req =
                ApiService.apiCustomer().deleteFavorite(Model.FavoriteCode(code, conceptIdThis))
            Log.d("LogMenuManager", req.toString())
            Log.d("LogMenuManager", req.body().toString())
            Log.d("LogMenuManager", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                getFavorite()
            }
        }
    }

    init {
        getSmartCategories()
        getSmartProducts()
    }
}