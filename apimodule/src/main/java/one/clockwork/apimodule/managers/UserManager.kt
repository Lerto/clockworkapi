package one.clockwork.apimodule.managers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import one.clockwork.apimodule.api.ApiService
import one.clockwork.apimodule.models.Gender
import one.clockwork.apimodule.models.Model
import one.clockwork.apimodule.models.ReturnStatus
import org.json.JSONObject

class UserManager constructor(
    private val context: Context,
    private val apiService: ApiService
) {
    var profile: MutableLiveData<Model.User> =
        MutableLiveData(Model.User("_none", "", "", 0, "", Gender.M.type, "", "", 0))

    private val listStories: MutableLiveData<ArrayList<Model.Story>> =
        MutableLiveData(ArrayList())
    val listStoriesLive: LiveData<ArrayList<Model.Story>> = listStories

    val keysString: MutableLiveData<String> = MutableLiveData()

    private val listFeedback: MutableLiveData<ArrayList<Model.Feedback>> =
        MutableLiveData(ArrayList())
    val listFeedbackLive: LiveData<ArrayList<Model.Feedback>> = listFeedback

    private val checkDelivery: MutableLiveData<Model.DeliveryAnswer> =
        MutableLiveData()
    val checkDeliveryLive: LiveData<Model.DeliveryAnswer> = checkDelivery

    private val listContent: MutableLiveData<ArrayList<Model.Content>> =
        MutableLiveData(ArrayList())
    val listContentLive: LiveData<ArrayList<Model.Content>> = listContent

    private val listTerminals: MutableLiveData<ArrayList<Model.Terminal>> =
        MutableLiveData(ArrayList())
    val listTerminalsLive: LiveData<ArrayList<Model.Terminal>> = listTerminals

    private val listNotification: MutableLiveData<ArrayList<Model.Notification>> =
        MutableLiveData(ArrayList())
    val listNotificationLive: LiveData<ArrayList<Model.Notification>> = listNotification


    val listDeliveryTypes: MutableLiveData<ArrayList<Model.DeliveryTypes>> =
        MutableLiveData(ArrayList())

    val listPaymentTypes: MutableLiveData<ArrayList<Model.Types>> =
        MutableLiveData(ArrayList())

    val promocodeCheckData: MutableLiveData<String> =
        MutableLiveData()
    val promocodeCheckProductData: MutableLiveData<Model.Product> =
        MutableLiveData()

    private val listOrders: MutableLiveData<ArrayList<Model.Order>> =
        MutableLiveData(ArrayList())
    val listOrdersLive: LiveData<ArrayList<Model.Order>> = listOrders

    private val listTransactions: MutableLiveData<ArrayList<Model.Transactions>> =
        MutableLiveData(ArrayList())
    val listTransactionsLive: LiveData<ArrayList<Model.Transactions>> = listTransactions

    private val listFeatured: MutableLiveData<ArrayList<Model.FeaturedProducts>> =
        MutableLiveData()
    val listFeaturedLive: LiveData<ArrayList<Model.FeaturedProducts>> = listFeatured

    var balance: MutableLiveData<Model.Balance> = MutableLiveData(Model.Balance(0.0, ArrayList()))

    var addresses: MutableLiveData<ArrayList<Model.Address>> = MutableLiveData(ArrayList())

    init {
//        setAddress()
    }

    fun exit() {
        profile.postValue(Model.User("_none", "", "", 0, "", Gender.M.type, "", "", 0))
        ApiService.accessToken = ""
    }

    fun deleteAccount() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = apiService.deleteAccount()

            profile.postValue(Model.User("_none", "", "", 0, "", Gender.M.type, "", "", 0))
            ApiService.accessToken = ""
        }
    }

    private fun setAddress() {
        val newAdd = ArrayList<Model.Address>()
        addresses.postValue(newAdd)
    }

    suspend fun registration(user: Model.UserRegistration): ReturnStatus {
        Log.d("LOGUser", user.toString())
        val req = apiService.registrationUser(user)
        Log.d("LOGUser", req.toString())
        Log.d("LOGUser", req.body().toString())
        Log.d("LOGUser", req.errorBody()?.string().toString())
        if (req.isSuccessful) {
            req.body()?.let {
                return ReturnStatus.OK
            }
        }
        return ReturnStatus.FAIL
    }

    suspend fun login(phone: Long, code: String): ReturnStatus {
        Log.d("LOGUser", (Model.UserLogin(phone, code, ApiService.loyalty)).toString())
        val req = apiService.login(Model.UserLogin(phone, code, ApiService.loyalty))
        Log.d("LOGUser", req.toString())
        Log.d("LOGUser", req.errorBody()?.string().toString())
        if (req.isSuccessful) {
            req.body()?.let {
                ApiService.accessToken = it.access_token
                Log.d("LOGUser", ApiService.accessToken.toString())
                return ReturnStatus.OK
            }
        }
        return ReturnStatus.FAIL
    }

    suspend fun getCode(phone: Long): Model.CodeAnswer {
        val req = apiService.getCode(Model.Phone(phone, ApiService.loyalty))
        Log.d("LOGUser", req.toString())
        Log.d("LOGUser", req.body().toString())
        Log.d("LOGUser", req.errorBody()?.string().toString())
        if (req.code() == 403) {
            return Model.CodeAnswer("Forbidden", false)
        }
        if (req.isSuccessful) {
            req.body()?.let {
                return it
            }
        }
        return Model.CodeAnswer("Error", false)
    }

    fun getKeys(keys: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = apiService.getPropertiesOfCompany(keys)
            Log.d("LOGUser", req.toString())
            Log.d("LOGUser", req.body().toString())
            Log.d("LOGUser", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    keysString.postValue(it.string())
                }
            }
        }
    }

    fun getProfile() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = apiService.getProfile()
            Log.d("LOGUser", req.toString())
            Log.d("LOGUser", req.body().toString())
            Log.d("LOGUser", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    profile.postValue(it)
                    getBalance()
                    Log.d("LOGProfile", profile.value!!.toString())
                }
            }
        }
    }

    fun getBalance() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = apiService.getBalance()
            Log.d("LOGUser", req.toString())
            Log.d("LOGUser", req.body().toString())
            Log.d("LOGUser", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    balance.postValue(it)
                    Log.d("LOGProfile", balance.value!!.toString())
                }
            }
        }
    }

    fun getOrders() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getOrders()
            Log.d("LOGOrders", req.toString())
            Log.d("LOGOrders", req.body().toString())
            Log.d("LOGOrders", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listOrders.postValue(it.data)
                }
            }
        }
    }

    fun getTransactions() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getTransactions()
            Log.d("LOGOrders", req.toString())
            Log.d("LOGOrders", req.body().toString())
            Log.d("LOGOrders", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listTransactions.postValue(it.data)
                }
            }
        }
    }

    fun getFeedbacks() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getFeedback()
            Log.d("LOGFeedbacks", req.toString())
            Log.d("LOGFeedbacks", req.body().toString())
            Log.d("LOGFeedbacks", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listFeedback.postValue(it.data)
                }
            }
        }
    }

    fun sendFeedback(feedbackSend: Model.FeedbackSend) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().sendFeedback(feedbackSend)
            Log.d("LOGFeedbackSend", req.toString())
            Log.d("LOGFeedbackSend", req.body().toString())
            Log.d("LOGFeedbackSend", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                getFeedbacks()
            }
        }
    }

    fun getStories() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getStories()
            Log.d("LOGStories", req.toString())
            Log.d("LOGStories", req.body().toString())
            Log.d("LOGStories", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listStories.postValue(it.data)
                }
            }
        }
    }

    fun getDeliveryTypes(conceptId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getDeliveryTypes(conceptId)
            Log.d("LOGStories", req.toString())
            Log.d("LOGStories", req.body().toString())
            Log.d("LOGStories", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listDeliveryTypes.postValue(it)
                }
            }
        }
    }

    fun getPaymentTypes(conceptId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getPaymentTypes(conceptId)
            Log.d("LOGStories", req.toString())
            Log.d("LOGStories", req.body().toString())
            Log.d("LOGStories", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listPaymentTypes.postValue(it)
                }
            }
        }
    }

    fun getPromocode(checkPromo: Model.PromocodesCheck) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().checkPromocode(checkPromo)
            Log.d("LOGPromocode", req.toString())
            Log.d("LOGPromocode", req.body().toString())
            val error = req.errorBody()?.string().toString()
            Log.d("LOGPromocode", error)
            if (req.isSuccessful) {
                req.body()?.let {
                    promocodeCheckData.postValue("OK")
                    promocodeCheckProductData.postValue(it.product)
                }
            } else {
                promocodeCheckData.postValue(error)
            }
        }
    }

    fun getNotifications() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getNotif()
            Log.d("LOGNotification", req.toString())
            Log.d("LOGNotification", req.body().toString())
            Log.d("LOGNotification", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listNotification.postValue(it.data)
                }
            }
        }
    }

    fun sendSupport(support: Model.Support) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().sendSupport(support)
            Log.d("LOGSupport", req.toString())
            Log.d("LOGSupport", req.body().toString())
            Log.d("LOGSupport", req.errorBody()?.string().toString())
        }
    }


    fun getContent() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getContents()
            Log.d("LOGContent", req.toString())
            Log.d("LOGContent", req.body().toString())
            Log.d("LOGContent", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listContent.postValue(it.data)
                }
            }
        }
    }

    fun getTerminals() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().getTerminals()
            Log.d("LOGTerminals", req.toString())
            Log.d("LOGTerminals", req.body().toString())
            Log.d("LOGTerminals", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listTerminals.postValue(it.data)
                }
            }
        }
    }

    fun getFeatured() {
        CoroutineScope(Dispatchers.Main).launch {
            val req = apiService.getFeatured()
            Log.d("LOGFeatured", req.toString())
            Log.d("LOGFeatured", req.body().toString())
            Log.d("LOGFeatured", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    listFeatured.postValue(it.data)
                }
            }
        }
    }

    fun sendToken(token: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().sendToken(token)
            Log.d("LOGSendToken", req.toString())
            Log.d("LOGSendToken", req.body().toString())
            Log.d("LOGSendToken", req.errorBody()?.string().toString())
        }
    }

    fun updateProfile(profile: Model.UserUpdate) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().updateProfile(profile)
            Log.d("LOGSendToken", req.toString())
            Log.d("LOGSendToken", req.body().toString())
            Log.d("LOGSendToken", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                getProfile()
            }
        }
    }

    fun checkDelivery(deliveryAddress: Model.CheckDelivery) {
        CoroutineScope(Dispatchers.Main).launch {
            val req = ApiService.apiCustomer().checkDelivery(deliveryAddress)
            Log.d("LOGDelivery", req.toString())
            Log.d("LOGDelivery", req.body().toString())
            Log.d("LOGDelivery", req.errorBody()?.string().toString())
            if (req.isSuccessful) {
                req.body()?.let {
                    checkDelivery.postValue(it)
                }
            }
        }
    }

    init {
        getProfile()
        getStories()
        getOrders()
        getContent()
        getFeatured()
        getTerminals()
        getNotifications()
        getTransactions()
        getKeys("min_order_sum")
    }
}