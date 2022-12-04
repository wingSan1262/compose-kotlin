package vanrrtech.app.ajaib_app_sample.domain.data_model.github.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testapplication.base_component.base_interface.BaseModel
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class SearchPeopleResponse(
    @SerializedName("count")
    var count : Int = 0,
    @SerializedName("results")
    var results : List<PeopleItemResponse>? = emptyList(),
) : Serializable, BaseModel

@Entity(tableName =  "people_list_table")
data class PeopleItemResponse(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("name")
    var name : String = "",
    @SerializedName("height")
    var height : String = "0",
    @SerializedName("mass")
    var mass : String = "0",
    @SerializedName("hair_color")
    var hairColor : String = "",
    @SerializedName("skin_color")
    var skinColor : String = "",
    @SerializedName("eye_color")
    var eyeColor : String = "",
    @SerializedName("birth_year")
    var birthYear : String = "",
    @SerializedName("gender")
    var gender : String = "",
) : Serializable, BaseModel
