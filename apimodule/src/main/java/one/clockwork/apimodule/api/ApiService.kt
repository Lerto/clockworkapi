package one.clockwork.apimodule.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.*
import one.clockwork.apimodule.models.Model
import one.clockwork.apimodule.models.ModelData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

interface ApiService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/concepts")
    suspend fun getConcepts(
    ): Response<ModelData.ConceptData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/contents")
    suspend fun getContents(
    ): Response<ModelData.ContentData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/orders/")
    suspend fun getOrders(
    ): Response<ModelData.OrderData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/transactions/")
    suspend fun getTransactions(
    ): Response<ModelData.TransactionData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/feedbacks/")
    suspend fun getFeedback(
    ): Response<ModelData.FeedbackData>

    @Headers("Content-Type: application/json")
    @POST("api/v1/feedbacks/")
    suspend fun sendFeedback(
        @Body body: Model.FeedbackSend
    ): Response<ModelData.FeedbackData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/terminals")
    suspend fun getTerminals(
    ): Response<ModelData.TerminalData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/products/?limit=500")
    suspend fun getProducts(
    ): Response<ModelData.ProductData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/products/?limit=500")
    suspend fun getProductByCategory(
        @Query("categoryId") categoryId: String
    ): Response<ModelData.ProductData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/products/code/{code_id}")
    suspend fun getProductByCode(
        @Path("code_id") id: String
    ): Response<Model.Product>

    @Headers("Content-Type: application/json")
    @GET("api/v1/properties_of_companies/bulk/{keys}")
    suspend fun getPropertiesOfCompany(
        @Path("keys") keys: String
    ): Response<String>

    @Headers("Content-Type: application/json")
    @GET("api/v1/categories/?limit=500")
    suspend fun getCategories(): Response<ModelData.CategoryData>

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/signup")
    suspend fun registrationUser(
        @Body body: Model.UserRegistration
    ): Response<Model.ApiAnswer>

    @Headers("Content-Type: application/json")
    @POST("api/v1/orders/order")
    suspend fun sendOrder(
        @Body body: Model.SendOrder
    ): Response<Model.Message>

    @Headers("Content-Type: application/json")
    @POST("v1/create")
    suspend fun createOrder(
        @Query("id") id: String
    ): Response<Model.OnlinePaymentsOrder>

    @Headers("Content-Type: application/json")
    @GET("api/v1/smart_products")
    suspend fun getSmartProducts(): Response<ModelData.SmartProductsData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/smart_categories")
    suspend fun getSmartCategories(): Response<ModelData.SmartCategoriesData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/me/profile")
    suspend fun getProfile(): Response<Model.User>

    @Headers("Content-Type: application/json")
    @PUT("api/v1/me/profile")
    suspend fun updateProfile(
        @Body body: Model.UserUpdate
    ): Response<Model.User>

    @Headers("Content-Type: application/json")
    @GET("api/v1/me/balance")
    suspend fun getBalance(): Response<Model.Balance>

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/token")
    suspend fun login(
        @Body body: Model.UserLogin
    ): Response<Model.LoginAnswer>

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/code")
    suspend fun getCode(
        @Body body: Model.Phone
    ): Response<Model.CodeAnswer>

    @Headers("Content-Type: application/json")
    @GET("api/v1/stories")
    suspend fun getStories(): Response<ModelData.StoryData>

    @Headers("Content-Type: application/json")
    @GET("/api/v1/favorite/")
    suspend fun getFavorite(
        @Query("conceptId") conceptId: String
    ): Response<ModelData.ProductData>

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "/api/v1/favorite/", hasBody = true)
    suspend fun deleteFavorite(
        @Body body: Model.FavoriteCode
    ): Response<ModelData.ProductData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/notifications/")
    suspend fun getNotif(): Response<ModelData.NotificationData>

    @Headers("Content-Type: application/json")
    @GET("api/v1/featured_products/")
    suspend fun getFeatured(): Response<ModelData.Featured>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/favorite/")
    suspend fun sendFavorite(
        @Body body: Model.FavoriteCode
    ): Response<Model.Product>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/promocodes/")
    suspend fun checkPromocode(
        @Body body: Model.PromocodesCheck
    ): Response<ModelData.ProductCodeData>

    @Headers("Content-Type: application/json")
    @GET("/api/v1/payments_types/")
    suspend fun getPaymentTypes(
        @Query("conceptId") conceptId: String
    ): Response<ArrayList<Model.Types>>

    @Headers("Content-Type: application/json")
    @GET("/api/v1/delivery_types/")
    suspend fun getDeliveryTypes(
        @Query("conceptId") conceptId: String
    ): Response<ArrayList<Model.Types>>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/delivery/check/")
    suspend fun checkDelivery(
        @Body body: Model.CheckDelivery
    ): Response<Model.DeliveryAnswer>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/supports/")
    suspend fun sendSupport(
        @Body body: Model.Support
    ): Response<Model.Support>

    @Headers("Content-Type: application/json")
    @DELETE("/api/v1/me/profile")
    suspend fun deleteAccount(): Response<Model.Message>

    @Headers("Content-Type: application/json")
    @PUT("api/v1/me/fcm")
    suspend fun sendToken(
        @Query("push_token") token: String
    ): Response<Model.Message>

    companion object {
        private const val CUSTOMER_URL = "https://customer.api.cw.marketing/"
        private const val CREATE_URL = "https://payments.cw.marketing/"
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
            return Retrofit.Builder().baseUrl(CUSTOMER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory()).client(httpOK()).build()
                .create(ApiService::class.java)
        }

        fun apiCreate(): ApiService {
            return Retrofit.Builder().baseUrl(CREATE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory()).client(httpOK()).build()
                .create(ApiService::class.java)
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