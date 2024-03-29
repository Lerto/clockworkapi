package one.clockwork.apimodule.models

import java.io.Serializable

object Model {
    data class Message(
        val message: String
    )

    data class Types(
        val _id: String,
        val name: String,
        val code: String,
        val companyId: String,
        val conceptId: String
    )

    data class DeliveryTypes(
        val _id: String,
        val name: String,
        val code: String,
        val isSelfService: Boolean,
        val companyId: String,
        val conceptId: String
    )

    data class Balance(
        val total: Double,
        val categories: ArrayList<String>
    )

    data class SendOrder(
        val conceptId: String,
        val companyId: String,
        val terminalId: String?,
        val deliveryTypeId: String,
        val personsCount: Int,
        val paymentTypeId: String,
        val deliveryTime: String?,
        val products: ArrayList<ProductSend>,
        val sourceId: String,
        val address: Address?,
        val withdrawBonuses: Int,
        val comment: String,
        val change: Int?
    )

    data class Menu(
        var categories: ArrayList<Category>,
        var products: ArrayList<Product>
    ) : Serializable

    data class Address(
        var city: String,
        val street: String,
        val home: String,
        val floor: Int,
        val flat: Int,
        val entrance: Int
    )

    data class Transactions(
        val _id: String,
        var sum: Double,
        val changedOn: String,
        val source: String,
        val conceptId: String,
        val tpcasId: String
    )

    data class Concept(
        val _id: String?,
        val name: String?,
        val image: Image,
        val additionalData: String,
        val isDeleted: Boolean,
        val isDisabled: Boolean,
        var isSelected: Boolean = false,
        val bonuses: Bonuses
    )

    data class Bonuses(
        val percentValue: Int?,
        val minSumma: Int?,
        val maxSumma: Int?
    )

    data class Image(
        val body: String,
        val hash: String
    ) : Serializable

    data class Restaurant(
        val _id: String = "",
        val name: String = "",
        var isSelected: Boolean = false
    )

    data class Feedback(
        val _id: String,
        val body: String,
        val score: Int,
        val orderId: String
    )

    data class FeedbackSend(
        val body: String,
        val score: Int,
        val orderId: String?
    )

    data class Order(
        val _id: String,
        val number: Int,
        val address: Address,
        val comment: String,
        val conceptId: String,
        val products: ArrayList<OrderProduct>,
        val terminalId: String?,
        val deliveryTime: String,
        val deliveryTypeId: String,
        val createdAt: String
    )

    data class OrderProduct(
        val code: String,
        val name: String,
        val amount: Int,
        val price: Int
    )

    data class Content(
        val _id: String,
        val name: String,
        val type: String,
        val image: String,
        val url: String,
        val text: String,
        val order: Int,
        val conceptId: String?,
        val isDeleted: Boolean,
        val uiSettings: UISetting
    )

    data class Category(
        val _id: String,
        val name: String,
        val parentCategory: String?,
        var imageSize: String?,
        var conceptId: String?,
        val design: Design?,
        val isDeleted: Boolean,
        val isDisabled: Boolean,
        val isHidden: Boolean,
        var isSelected: Boolean = false
    ) : Serializable

    data class Design(
        val type: String,
        val placheolder: String,
        val imageUrl: String
    )

    data class UserRegistration(
        val firstName: String,
        val lastName: String,
        val email: String?,
        val sex: String,
        val dob: String
    )

    data class User(
        val _id: String,
        val firstName: String,
        val lastName: String,
        val phone: Long,
        val email: String,
        val sex: String,
        val place: String,
        val dob: String,
        val card: Long
    )

    data class Modifiers(
        val _id: String,
        val name: String,
        val options: ArrayList<Options>,
        val image: Image?,
        val maxAmount: Int,
        val minAmount: Int,
        val required: Boolean
    ) : Serializable

    data class Options(
        val _id: String,
        val name: String,
        val price: Int,
        val image: Image?,
        val maxAmount: Int,
        val minAmount: Int,
        val required: Boolean
    ) : Serializable


    data class ApiAnswer(
        val token: String
    )

    data class OnlinePaymentsOrder(
        val onlinePayment: OnlinePayment
    )

    data class OnlinePayment(
        val orderId: String,
        val formUrl: String
    )

    data class CategoryProduct(
        val title: String,
        val products: ArrayList<Product>,
        val type: String
    ) : Serializable

    data class Product(
        val _id: String = "",
        val name: String = "",
        val code: String = "",
        val image: Image?,
        val previewImage: Image?,
        var description: String = "",
        var unit: String? = "",
        var price: Int = 0,
        var modifiers: ArrayList<Modifiers>?,
        var weight: Weight,
        var featured: ArrayList<Product>,
        val isHidden: Boolean,
        val isDeleted: Boolean,
        val isDisabled: Boolean,
        val slug: String = "",
        val badges: ArrayList<Badge>,
        var amount: Int = 0,
        var categoryId: String? = "",
        val terminalId: String? = "",
        val nutrition: Nutrition?
    ) : Serializable

    data class Nutrition(
        val energy: Double,
        val fiber: Double,
        val fat: Double,
        val carbohydrate: Double
    ) : Serializable

    data class Weight(
        val full: Double,
        val min: Double
    ) : Serializable

    data class Badge(
        val _id: String,
        val name: String,
        val image: Image
    ) : Serializable

    data class ProductBasket(
        val product: Product,
        var amount: Int,
        val modifiers: ArrayList<Options>
    ) : Serializable

    data class ProductSend(
        val code: String,
        val amount: Int,
        val modifiers: ArrayList<ModifiersSend>
    ) : Serializable

    data class ModifiersSend(
        val id: String,
        val amount: Int
    ) : Serializable

    data class Terminal(
        val _id: String,
        val name: String,
        val terminalId: String,
        val address: String,
        val city: String,
        val emails: ArrayList<String>,
        val timezone: String,
        val delivery: String,
        val phones: ArrayList<Phones>,
        val businessHours: ArrayList<Hours>,
        val conceptId: String,
        val isDeleted: Boolean,
        val isHidden: Boolean,
        val isDisabled: Boolean,
        val geojson: String?
    )

    data class Phones(
        val name: String,
        val phone: String
    )

    data class Hours(
        val days: String,
        val hours: String
    )

    data class FeaturedProducts(
        val _id: String,
        val companyId: String,
        val conceptId: String,
        val products: ArrayList<Product>
    )


    data class Notification(
        val _id: String,
        val title: String,
        val subtitle: String,
        val body: String,
        val image: Image,
        val isDeleted: Boolean,
        val isDisabled: Boolean,
        val createdAt: String?,
        val updatedAt: String?
    ) : Serializable

    data class FavoriteCode(
        val productCode: String,
        val conceptId: String
    )

    data class PromocodesCheck(
        val promocode: String,
        val conceptId: String,
        val products: ArrayList<ProductSend>
    )

    data class CheckDelivery(
        val address: AddressShort,
        val conceptId: String
    )

    data class Support(
        val name: String,
        val phone: String,
        val content: String,
        val os: Os,
        val device: Device,
        val app: App,
        val customerId: String?
    )

    data class Os(
        val version: String,
        val name: String
    )

    data class Device(
        val brand: String,
        val model: String
    )

    data class App(
        val version: String,
        val build: String,
        val id: String
    )

    data class DeliveryAnswer(
        val areaName: String,
        val deliveryPrice: Int,
        val deliveryTime: String,
        val isInDeliveryArea: Boolean,
        val minOrderSum: Int,
        val code: String
    )

    data class PromocodeProduct(
        val product: String?,
        val minSum: Int?,
        val err: String?
    )

    data class AddressShort(
        val city: String,
        val street: String,
        val home: String
    )

    data class Story(
        val _id: String? = "",
        val preview: Image?,
        val type: Int = 0,
        val slides: ArrayList<Image>,
        val isDeleted: Boolean,
        val isDisabled: Boolean,
        val createdAt: String? = "",
        val updatedAt: String? = "",
        val uiSettings: UISetting
    ) : Serializable

    data class UISetting(
        val url: String,
        val text: String,
        val color: String
    )

    data class UserLogin(
        val phone: Long,
        val code: String,
        val tpcasId: String
    )

    data class UserUpdate(
        val firstName: String,
        val lastName: String,
        val sex: String
    )

    data class Phone(
        val phone: Long,
        val tpcasId: String
    )

    data class CodeAnswer(
        val message: String,
        val isRegistered: Boolean
    )

    data class LoginAnswer(
        val access_token: String
    )

    data class SmartCategories(
        val _id: String,
        val name: String?,
        val description: String?,
        val image: Image?,
        val parentCategory: String?,
        val conceptId: String?,
        val terminalId: String?,
        val rrule: Rule
    )

    data class SmartProducts(
        val _id: String,
        val name: String?,
        val description: String?,
        val price: Int,
        val image: Image?,
        val previewImage: Image?,
        val weight: Weight,
        val rrule: Rule
    )

    data class Rule(
        val freq: String,
        val fromTime: FromTime,
        val untilTime: FromTime,
        val weekOfMonth: Int,
        val byWeekdays: ArrayList<Int>
    )

    data class FromTime(
        val hour: Int,
        val minute: Int
    )
}