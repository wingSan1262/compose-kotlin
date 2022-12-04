package com.example.testapplication.feature

import android.view.View
import com.example.testapplication.base_component.base_classes.BaseViewHolder
import com.example.testapplication.base_component.base_interface.BaseModel
import com.example.testapplication.databinding.ListItemPeopleBinding
import com.example.testapplication.feature.model.PeopleItemModel

/**
 * ViewHolder extends baseViewHolder and BaseModel
 * @see BaseViewHolder
 * @See BaseModel
 *
 * merit : you can easily create dynamic recycleview :)
 *
 */
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