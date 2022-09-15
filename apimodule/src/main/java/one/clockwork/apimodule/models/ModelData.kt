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

    data class CategoryData(
        val data: ArrayList<Model.Category>
    ) : Serializable

    data class ContentData(
        val data: ArrayList<Model.Content>
    )
}