package com.example.testapplication.feature.model

import com.example.testapplication.base_component.base_interface.BaseModel
import com.example.testapplication.domain.models.response.PeopleItemResponse
import com.example.testapplication.domain.models.response.SearchPeopleResponse
import java.io.Serializable

/**
 * relevant local feature data parser
 * in order for the data to be specifically relevant to feature
 * new parser for feture relevant data was added here
 * may be in feature you'll need to use dynamic feature modular
 */
data class PeopleListModel(
    var count : Int = 0,
    var results : List<PeopleItemModel>? = emptyList(),
) : Serializable, BaseModel {
    constructor(resp : SearchPeopleResponse) : this() {
        this.count = resp.count
        this.results = resp.results?.map {
            it.run {
                PeopleItemModel(
                    name,
                    height, mass, hairColor, skinColor,
                    eyeColor, birthYear, gender
                )
            }
        }
    }
}

data class PeopleItemModel(
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
