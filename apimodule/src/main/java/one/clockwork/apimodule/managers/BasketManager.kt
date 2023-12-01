package one.clockwork.apimodule.managers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import one.clockwork.apimodule.api.ApiService
import one.clockwork.apimodule.models.Model
import one.clockwork.apimodule.models.ReturnStatus

class BasketManager {
    private val basket: MutableLiveData<ArrayList<Model.ProductBasket>> =
        MutableLiveData(ArrayList())
    val basketLive: LiveData<ArrayList<Model.ProductBasket>> = basket

    private var lastProduct: Model.Product? = null

    fun removeProduct(product: Model.Product, modifiers: ArrayList<Model.Options>) {
        basket.value?.let { basketValue ->
            var removeSome: Model.ProductBasket? = null
            basketValue.forEach {
                if (it.product.name == product.name) {
                    var flag = true
                    product.modifiers?.forEach { pModi ->
                        var flag2 = false
                        modifiers.forEach { mModi ->
                            if (pModi._id == mModi._id) {
                                flag2 = true
                            }
                        }
                        if (!flag2) {
                            flag = false
                        }
                    }
                    if (flag) {
                        if (it.amount > 1) {
                            it.amount--
                        } else {
                            removeSome = it
                            return@forEach
                        }
                    }
                }
            }
            removeSome?.let {
                basketValue.remove(it)
            }
            basket.postValue(basketValue)
        }
    }

    fun addProduct(product: Model.Product, modifiers: ArrayList<Model.Options>) {
        var flagAmount = true
        lastProduct = product
        basket.value?.let { basketValue ->
            basketValue.forEach {
                if (it.product.name == product.name) {
                    var flag = true
                    product.modifiers?.forEach { pModi ->
                        var flag2 = false
                        modifiers.forEach { mModi ->
                            if (pModi._id == mModi._id) {
                                flag2 = true
                            }
                        }
                        if (!flag2) {
                            flag = false
                        }
                    }
                    if (flag) {
                        it.amount++
                        flagAmount = false
                    }
                }
            }
            if (flagAmount) {
                basketValue.add(Model.ProductBasket(product, 1, modifiers))
            }
            basket.postValue(basketValue)
        }
    }

    fun getTotal(): Double {
        basket.value?.let { basketValue ->
            var total = 0.0
            basketValue.forEach {
                total += it.product.price * it.amount
                it.modifiers.forEach {
                    total += it.price
                }
            }
            return total
        }
        return 0.0
    }

    fun eraseBasket() {
        basket.postValue(ArrayList())
    }

    fun getSendBasket(): ArrayList<Model.ProductSend> {
        val listSend = ArrayList<Model.ProductSend>()
        basket.value?.forEach { product ->
            val modifiers = ArrayList<Model.ModifiersSend>()
            product.modifiers.forEach {
                modifiers.add(Model.ModifiersSend(it._id, 1))
            }
            listSend.add(Model.ProductSend(product.product.code, product.amount, modifiers))
        }
        return listSend
    }

    fun lastProduct(): Model.Product? {
        return lastProduct
    }

    suspend fun sendOrder(order: Model.SendOrder): String {
        val req = ApiService.apiCustomer().sendOrder(order)
        Log.d("LOGSendOrder", req.toString())
        Log.d("LOGSendOrder", req.body().toString())
        Log.d("LOGSendOrder", req.errorBody()?.string().toString())
        return if (req.isSuccessful)
            req.body()?.message ?: "Fail"
        else {
            "Fail"
        }
    }

    suspend fun createOrder(id: String): String {
        val req = ApiService.apiCreate().createOrder(id)
        Log.d("LOGCreateOrder", req.toString())
        Log.d("LOGCreateOrder", req.body().toString())
        Log.d("LOGCreateOrder", req.errorBody()?.string().toString())
        return if (req.isSuccessful) {
            req.body()?.onlinePayment?.formUrl ?: "Fail"
        } else {
            "Fail"
        }
    }
}