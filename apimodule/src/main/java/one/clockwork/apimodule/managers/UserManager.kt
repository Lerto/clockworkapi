package one.clockwork.apimodule.managers

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import one.clockwork.apimodule.api.ApiService
import one.clockwork.apimodule.models.Gender
import one.clockwork.apimodule.models.Model
import one.clockwork.apimodule.models.ReturnStatus

class UserManager constructor(
    private val context: Context,
    private val apiService: ApiService
) {
    var profile: MutableLiveData<Model.User> =
        MutableLiveData(Model.User("_none", "", "", 0, "", Gender.M, "", "", 0))

    var balance: MutableLiveData<Model.Balance> = MutableLiveData(Model.Balance(0.0, ArrayList()))

    var addresses: MutableLiveData<ArrayList<Model.Address>> = MutableLiveData(ArrayList())

    init {
        setAddress()
    }

    fun exit() {
        profile.postValue(Model.User("_none", "", "", 0, "", Gender.M, "", "", 0))
        ApiService.accessToken = ""
    }

    fun addAddress(address: Model.Address) {
        val valueAd = addresses.value!!
        valueAd.add(address)
        addresses.postValue(valueAd)
    }

    private fun setAddress() {
        val newAdd = ArrayList<Model.Address>()
        newAdd.add(
            Model.Address(
                city = "Красноярск",
                street = "Щорса",
                home = "40",
                flat = 1,
                floor = 2,
                entrance = 0
            )
        )
        newAdd.add(
            Model.Address(
                city = "Красноярск",
                street = "Щорса",
                home = "40",
                flat = 1,
                floor = 2,
                entrance = 0
            )
        )
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

    init {
        getProfile()
    }
}