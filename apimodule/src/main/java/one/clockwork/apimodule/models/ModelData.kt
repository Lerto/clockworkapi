package one.clockwork.apimodule.models

import one.clockwork.apimodule.models.Model
import java.io.Serializable

object ModelData {
    data class ConceptData(
        val data: ArrayList<Model.Concept>
    )

    data class StoryData(
        val data: ArrayList<Model.Story>
    )

    data class ProductData(
        val data: ArrayList<Model.Product>
    )

    data class
    ProductCodeData(
        val product: Model.Product
    )

    data class CategoryData(
        val data: ArrayList<Model.Category>
    ) : Serializable

    data class ContentData(
        val data: ArrayList<Model.Content>
    )

    data class OrderData(
        val data: ArrayList<Model.Order>
    )

    data class FeedbackData(
        val data: ArrayList<Model.Feedback>
    )

    data class NotificationData(
        val data: ArrayList<Model.Notification>
    )

    data class Featured(
        val data: ArrayList<Model.FeaturedProducts>
    )

    data class TerminalData(
        val data: ArrayList<Model.Terminal>
    )

    data class SmartProductsData(
        val data: ArrayList<Model.SmartProducts>
    )

    data class SmartCategoriesData(
        val data: ArrayList<Model.SmartCategories>
    )
}