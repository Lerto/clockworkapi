package one.clockwork.apimodule.models

import java.io.Serializable

object Model {
    data class Message(
        val message: String
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
        val comment: String
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

    data class Concept(
        val _id: String?,
        val name: String?,
        val image: Image,
        val additionalData: String,
        val isDeleted: Boolean,
        val isDisabled: Boolean,
        var isSelected: Boolean = false
    )

    data class Image(
        val body: String,
        val hash: String
    )

    data class Restaurant(
        val _id: String = "",
        val name: String = "",
        var isSelected: Boolean = false
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
        val isDeleted: Boolean,
        val isDisabled: Boolean,
        val isHidden: Boolean,
        var isSelected: Boolean = false
    ) : Serializable

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
        val sex: Gender,
        val place: String,
        val dob: String,
        val card: Long
    )

    data class Modifiers(
        val _id: String,
        val name: String,
        val options: ArrayList<Options>
    ) : Serializable

    data class Options(
        val _id: String,
        val name: String,
        val price: Int
    )


    data class ApiAnswer(
        val token: String
    )

    data class CategoryProduct(
        val title: String,
        val products: ArrayList<Product>,
        val type: String
    )

    data class Product(
        val _id: String = "",
        val name: String = "",
        val code: String = "",
        val image: Image,
        val previewImage: Image,
        var description: String = "",
        var unit: String = "",
        var price: Int = 0,
        var modifiers: ArrayList<Modifiers>,
        var weight: Weight,
        var featured: ArrayList<Product>,
        val isHidden: Boolean,
        val isDeleted: Boolean,
        val isDisabled: Boolean,
        val slug: String = "",
        val badges: ArrayList<Badge>,
        var amount: Int = 0,
        var categoryId: String = "",
        val terminalId: String = ""
    )

    data class Weight(
        val full: Double,
        val min: Double
    )

    data class Badge(
        val _id: String,
        val name: String,
        val image: Image
    )

    data class ProductBasket(
        val product: Product,
        var amount: Int
    )

    data class ProductSend(
        val code: String,
        val amount: Int,
        val modifiers: ArrayList<ModifiersSend>
    )

    data class ModifiersSend(
        val id: String,
        val amount: Int
    )

    data class Terminal(
        val _id: String,
        val name: String,
        val terminalId: String,
        val address: String,
        val conceptId: String,
        val isDeleted: Boolean,
        val isHidden: Boolean,
        val isDisabled: Boolean
    )


    data class Notification(
        val _id: String,
        val title: String,
        val subtitle: String,
        val body: String,
        val image: Image,
        val isDeleted: Boolean,
        val isDisabled: Boolean
    )

    data class FavoriteCode(
        val productCode: String,
        val conceptId: String
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
}