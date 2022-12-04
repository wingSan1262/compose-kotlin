package com.example.testapplication.feature

import android.view.View
import com.example.testapplication.R
import com.example.testapplication.base_component.base_classes.BaseViewHolder
import com.example.testapplication.base_component.base_interface.BaseModel
import com.example.testapplication.databinding.ListItemPeopleBinding
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.response.PeopleItemModel

class PeopleViewHolder(
    val vhBinding : ListItemPeopleBinding,
    val root : View = vhBinding.root
) : BaseViewHolder<BaseModel>(root) {
    override fun bindData(model: BaseModel) {
        (model as? PeopleItemModel)?.let { data ->
            with(vhBinding){
                nameTv.text = data.name
                birthTv.text = data.birthYear
                heightTv.text = "${data.height} Cm"
                massTv.text = "${data.mass} Kg"
                genderTv.text = data.gender
                eyeTv.text = "${data.eyeColor} Eye"
                skinTv.text = "${data.skinColor} Skin"
                hairTv.text = "${data.hairColor} Hair"
            }
        }
    }

}