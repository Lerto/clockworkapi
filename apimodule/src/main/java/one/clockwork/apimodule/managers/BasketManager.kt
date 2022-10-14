package one.clockwork.apimodule.managers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import one.clockwork.apimodule.api.ApiService
import one.clockwork.apimodule.models.Model
import one.clockwork.apimodule.models.ReturnStatus

class BasketManager {
    private val basket: MutableLiveData<ArrayList<Model.ProductBasket>> =
        MutableLiveData(ArrayList())
    val basketLive: LiveData<ArrayList<Model.ProductBasket>> = basket

    private var lastProduct: Model.Product? = null

    fun removeProduct(product: Model.Product) {
        basket.value?.let { basketValue ->
            var removeSome: Model.ProductBasket? = null
            basketValue.forEach {
                if (it.product.name == product.name) {
                    if (it.amount > 1) {
                        it.amount--
                    } else {
                        removeSome = it
                        return@forEach
                    }
                }
            }
            removeSome?.let {
                basketValue.remove(it)
            }
            basket.postValue(basketValue)
        }
    }

    fun addProduct(product: Model.Product) {
        var flagAmount = true
        lastProduct = product
        basket.value?.let { basketValue ->
            basketValue.forEach {
                if (it.product.name == product.name) {
                    it.amount++
                    flagAmount = false
                }
            }
            if (flagAmount) {
                basketValue.add(Model.ProductBasket(product, 1))
            }
            basket.postValue(basketValue)
        }
    }

    fun getTotal(): Double {
        basket.value?.let { basketValue ->
            var total = 0.0
            basketValue.forEach {
                total += it.product.price * it.amount
            }
            return total
        }
        return 0.0
    }

    fun eraseBasket() {
        basket.value = ArrayList()
    }

    fun getSendBasket(): ArrayList<Model.ProductSend> {
        val listSend = ArrayList<Model.ProductSend>()
        basket.value?.forEach { product ->
            listSend.add(Model.ProductSend(product.product.code, product.amount, ArrayList()))
        }
        return listSend
    }

    fun lastProduct(): Model.Product? {
        return lastProduct
    }

    suspend fun sendOrder(order: Model.SendOrder): ReturnStatus {
        val req = ApiService.apiCustomer().sendOrder(order)
        Log.d("LOGSendOrder", req.toString())
        Log.d("LOGSendOrder", req.body().toString())
        Log.d("LOGSendOrder", req.errorBody()?.string().toString())
        return if (req.isSuccessful)
            ReturnStatus.OK
        else {
            ReturnStatus.FAIL
        }
    }
}