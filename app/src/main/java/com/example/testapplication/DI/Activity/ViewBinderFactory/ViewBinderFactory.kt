package com.example.testapplication.DI.Activity.ViewBinderFactory

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity

class ViewBinderFactory() {

    fun <S> bindViewActivity(modelClass : Class<S>,
                             activity : AppCompatActivity,
                             inflater: LayoutInflater) {
        when (modelClass){
//            MainActivity::class.java -> {
//                (activity as MainActivity).viewBinding = ActivityMainBinding.inflate(inflater,
//                    null,
//                    false
//                )
//            }

            else -> { throw Exception("no vb class found")}
        }
    }
}