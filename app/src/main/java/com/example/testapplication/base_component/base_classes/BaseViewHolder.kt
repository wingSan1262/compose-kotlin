package com.example.testapplication.base_component.base_classes

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/** RV ViewHolder Base **/
abstract class BaseViewHolder<in BaseModel>(rootView: View) : RecyclerView.ViewHolder(rootView) {
    abstract fun bindData(model: BaseModel)
}