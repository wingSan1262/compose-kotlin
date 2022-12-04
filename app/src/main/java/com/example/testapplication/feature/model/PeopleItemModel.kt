package vanrrtech.app.ajaib_app_sample.domain.data_model.github.response

import com.example.testapplication.base_component.base_interface.BaseModel
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PeopleListModel(
    var count : Int = 0,
    var results : List<PeopleItemModel>? = emptyList(),
) : Serializable, BaseModel {
    constructor(resp : SearchPeopleResponse) : this() {
        this.count = resp.count
        this.results = resp.results?.map {
            it.run {
                PeopleItemModel(
                    id,
                    name,
                    height, mass, hairColor, skinColor,
                    eyeColor, birthYear, gender
                )
            }
        }
    }
}

data class PeopleItemModel(
    var id : Int = 0,
    var name : String = "",
    var height : String = "0",
    var mass : String = "0",
    var hairColor : String = "",
    var skinColor : String = "",
    var eyeColor : String = "",
    var birthYear : String = "",
    var gender : String = "",
) : Serializable, BaseModel {
    constructor(resp : PeopleItemResponse) : this() {
        resp.let {
            id = it.id
            name = it.name
            height = it.height
            mass = it.mass
            hairColor = it.hairColor
            skinColor = it.skinColor
            eyeColor = it.eyeColor
            birthYear = it.birthYear
            gender = it.gender
        }
    }
}
