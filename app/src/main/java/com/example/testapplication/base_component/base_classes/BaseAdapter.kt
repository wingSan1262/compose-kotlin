package com.example.testapplication.base_component.base_classes

import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.base_component.base_interface.BaseModel

/** Standard BaseAdapter Handling BaseModel List and  BaseViewHolder**/
abstract class BaseAdapter<T : BaseViewHolder<BaseModel>> : RecyclerView.Adapter<T>() {
    open var listItems: MutableList<BaseModel> = mutableListOf()

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun removeItem(item : BaseModel){
        listItems.remove(item)
        notifyItemRangeRemoved(0, listItems.size)
    }

    fun clearList(){
        listItems.clear()
        notifyDataSetChanged()
    }

    fun insertAtTop(item: BaseModel){
        listItems.add(0, item)
        notifyItemInserted(0)
    }

    fun insertAll(item: List<BaseModel>){
        listItems.addAll(listItems.size, item)
        notifyItemInserted(listItems.size)
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        bindVH(holder, position)
    }

    abstract fun bindVH(holder: T, position: Int)

}