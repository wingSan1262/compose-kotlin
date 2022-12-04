package com.example.testapplication.DI.Activity

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.testapplication.DI.Activity.ViewModelProducer.VmFactory
import com.example.testapplication.feature.SearchViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule (val activity: AppCompatActivity, val context : Context) {

    @Provides
    fun activity() = activity

    @Provides
    fun context(): Context = context

    @Provides
    fun layoutInflater() = LayoutInflater.from(activity)

    @Provides
    fun fragmentManager() : FragmentManager = activity.supportFragmentManager

    @Provides
    fun searchViewModel (activity: AppCompatActivity, vmFactory: VmFactory) : SearchViewModel =
        ViewModelProvider(activity, vmFactory).get(SearchViewModel::class.java)


}