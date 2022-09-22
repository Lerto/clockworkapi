package one.clockwork.apimodule.api

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import one.clockwork.apimodule.models.Model
import one.clockwork.apimodule.models.ModelData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("concepts/v1")
    suspend fun getConcepts(
    ): Response<ModelData.ConceptData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/contents")
    suspend fun getContents(
    ): Response<ModelData.ContentData>

    @Headers("Content-Type: application/json")
    @GET("products/v1/?limit=500")
    suspend fun getProducts(
    ): Response<ModelData.ProductData>

    @Headers("Content-Type: application/json")
    @GET("categories/v1")
    suspend fun getCategories(): Response<ModelData.CategoryData>

    @Headers("Content-Type: application/json")
    @POST("auth/v1/signup")
    suspend fun registrationUser(
        @Body body: Model.UserRegistration
    ): Response<Model.ApiAnswer>

    @Headers("Content-Type: application/json")
    @POST("orders/v1/order")
    suspend fun sendOrder(
        @Body body: Model.SendOrder
    ): Response<Model.Message>

    @Headers("Content-Type: application/json")
    @GET("me/v1/profile")
    suspend fun getProfile(): Response<Model.User>

    @Headers("Content-Type: application/json")
    @GET("me/v1/balance")
    suspend fun getBalance(): Response<Model.Balance>

    @Headers("Content-Type: application/json")
    @POST("auth/v1/token")
    suspend fun login(
        @Body body: Model.UserLogin
    ): Response<Model.LoginAnswer>

    @Headers("Content-Type: application/json")
    @POST("auth/v1/code")
    suspend fun getCode(
        @Body body: Model.Phone
    ): Response<Model.CodeAnswer>

    @Headers("Content-Type: application/json")
    @GET("stories/v1")
    suspend fun getStories(): Response<ModelData.StoryData>

    @Headers("Content-Type: application/json")
    @GET("favorite/v1")
    suspend fun getFavorite(): Response<ModelData.ProductData>

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "favorite/v1", hasBody = true)
    suspend fun deleteFavorite(
        @Body body: Model.FavoriteCode
    ): Response<ModelData.ProductData>

    @Headers("Content-Type: application/json")
    @GET("notifications/v1")
    suspend fun getNotif(): Response<ModelData.NotificationData>

    @Headers("Content-Type: application/json")
    @POST("favorite/v1")
    suspend fun sendFavorite(
        @Body body: Model.FavoriteCode
    ): Response<Model.Product>


    @Headers("Content-Type: application/json")
    @PUT("me/v1/fcm")
    suspend fun sendToken(
        @Query("push_token") token: String
    ): Response<Model.Message>

    companion object {
        private const val MARKETING_URL = "https://marketing.api.cw.marketing/"
        private const val ADMIN_URL = "https://admin.api.cw.marketing/"
        private const val CUSTOMER_URL = "https://customer.api.cw.marketing/"
        private var apiService: ApiService? = null
        private var apiServiceAdmin: ApiService? = null
        private var apiServiceCustomer: ApiService? = null
        var accessToken = ""
        var loyalty = "62f4dace2b68515813090299"
        var companyKey = "lA8YqeCP6nxog80g15nKGlSz4VmZeWoThppQGOvvfrwEnVJm7X64lqnqjvlwcVz0"


//        fun apiMarketing(): ApiService {
//            if (apiService == null) {
//                apiService = Retrofit.Builder()
//                    .baseUrl(MARKETING_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
//                    .client(httpOK())
//                    .build()
//                    .create(ApiService::class.java)
//            }
//            return apiService!!
//        }

        fun apiCustomer(): ApiService {
            if (apiServiceCustomer == null) {
                apiServiceCustomer = Retrofit.Builder().baseUrl(CUSTOMER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory()).client(httpOK()).build()
                    .create(ApiService::class.java)
            }
            return apiServiceCustomer!!
        }

        fun apiAdmin(context: Context): ApiService {
            if (apiServiceAdmin == null) {
                apiServiceAdmin = Retrofit.Builder().baseUrl(ADMIN_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory()).client(httpOK()).build()
                    .create(ApiService::class.java)
            }
            return apiServiceAdmin!!
        }

        private fun httpOK() =
            OkHttpClient.Builder().addInterceptor(MyInterceptor()).addInterceptor {
                    val request = it.request()
                    val response = it.proceed(request)
                    if (response.code == 307) {
                        val url =
                            HttpUrl.Builder().scheme(request.url.scheme).host(request.url.host)
                                .addPathSegment(
                                    response.header(
                                        "location",
                                        request.url.toUrl().path
                                    ) ?: ""
                                ).build()


                        val newRequest =
                            Request.Builder().method(request.method, request.body).url(url)
                                .headers(request.headers).build()

                        return@addInterceptor it.proceed(newRequest)
                    }

                    response
                }.sslSocketFactory(TLSSocketFactory(), getTrustManager()!!)
                .callTimeout(1, TimeUnit.MINUTES).connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()

        private fun getTrustManager(): X509TrustManager? {
            var trustManagerFactory: TrustManagerFactory?
            return try {
                trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                    //throw new IllegalStateException(
                    //    "Unexpected default trust managers:" + Arrays.toString(trustManagers));
                    null
                } else trustManagers[0] as X509TrustManager
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                null
            } catch (e: KeyStoreException) {
                e.printStackTrace()
                null
            }
        }
    }
}

class MyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val builder = chain.request().newBuilder()
        builder.addHeader(
            "Authorization", "Bearer ${ApiService.accessToken}"
        )
        builder.addHeader(
            "Company-Access-Key", ApiService.companyKey
        )
        builder.addHeader("Loyalaty-Id", ApiService.loyalty)

        return chain.proceed(builder.build())
    }
}