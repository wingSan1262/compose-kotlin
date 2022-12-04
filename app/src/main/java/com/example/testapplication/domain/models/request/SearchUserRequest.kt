package vanrrtech.app.ajaib_app_sample.domain.data_model.github.request

import com.example.testapplication.base_component.base_interface.BaseModel
import java.io.Serializable

data class QueryPeopleRequest(
    val page : Int
) : Serializable, BaseModel

data class SearchPeopleRequest(
    val query : String,
    val page : Int
) : Serializable, BaseModel
