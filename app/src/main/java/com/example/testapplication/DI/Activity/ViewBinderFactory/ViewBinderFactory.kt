package com.example.testapplication.DI.Activity.ViewBinderFactory

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.testapplication.feature.MainActivity
import com.example.testapplication.databinding.ActivityMainBinding


/**
 * standard ViewBinding factory
 * TODO remove this to dagger ViewModel bindkey feature
 */

class ViewBinderFactory() {

    fun <S> bindViewActivity(modelClass : Class<S>,
                             activity : AppCompatActivity,
                             inflater: LayoutInflater) {
        when (modelClass){
            MainActivity::class.java -> {
                (activity as MainActivity).viewBinding = ActivityMainBinding.inflate(inflater,
                    null,
                    false
                )
            }

            else -> { throw Exception("no vb class found")}
        }
    }
}