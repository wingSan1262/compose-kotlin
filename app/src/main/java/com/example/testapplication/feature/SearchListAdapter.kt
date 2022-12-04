package com.example.testapplication.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.testapplication.base_component.base_classes.BaseAdapter
import com.example.testapplication.base_component.base_classes.BaseViewHolder
import com.example.testapplication.base_component.base_interface.BaseModel
import com.example.testapplication.databinding.ListItemPeopleBinding
import com.example.testapplication.domain.models.response.PeopleItemResponse

/**
 * Search list adapter
 * currently only support people searching right now
 *
 * TODO add new type for other API feed
 * TODO for film, people, and other content from the api provider
 */
class SearchListAdapter
    : BaseAdapter<BaseViewHolder<BaseModel>>() {
    override fun bindVH(holder: BaseViewHolder<BaseModel>, position: Int) {
        holder.bindData(listItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseModel> {
        return PeopleViewHolder(
            ListItemPeopleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return when(listItems.get(position)){
            is PeopleItemResponse -> ViewType.PEOPLE_VIEW_HOLDER.ordinal
            else -> ViewType.PEOPLE_VIEW_HOLDER.ordinal // TODO if other VH need edit here
        }
    }

}

enum class ViewType {
    PEOPLE_VIEW_HOLDER,
    FIL_VIEW_HOLDER
}