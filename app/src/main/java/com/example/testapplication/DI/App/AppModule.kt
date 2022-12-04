package com.example.testapplication.DI.App

import android.app.Application
import dagger.Module
import dagger.Provides
import com.example.testapplication.DI.Activity.ViewBinderFactory.ViewBinderFactory
import com.example.testapplication.DI.Activity.ViewModelProducer.VmFactory
import com.example.testapplication.data.remote_repository.SwapiApiImpl
import com.example.testapplication.data.remote_repository.SwapiApiInterface
import com.example.testapplication.domain.usecases.FetchOfflinePeopleListUseCase
import com.example.testapplication.domain.usecases.FetchPeopleUseCase
import com.example.testapplication.domain.usecases.SearchPeopleUseCase
import com.example.testapplication.domain.usecases.UpdateOfflinePeopleListUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.testapplication.data.local_db.LocalDb
import com.example.testapplication.data.local_db.PeopleItemDao
import com.example.testapplication.data.remote_repository.RemoteApiRetrofitClient

@Module
class AppModule(val application: Application) {

    @Provides
    @AppScope
    fun getClientLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @AppScope
    fun getOkHttpClientBuilder(logger : HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(logger)
    }

    @Provides
    @AppScope
    fun getRetrofitClient(
        okHttpBuilder: OkHttpClient.Builder
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RemoteApiRetrofitClient.BASE_URL_SWAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.build())
            .build()
    }

    @Provides
    @AppScope
    fun getClient(retrofit: Retrofit): RemoteApiRetrofitClient {
        return RemoteApiRetrofitClient(retrofit)
    }

    @Provides
    @AppScope
    fun getSwapiApi(
        retrofit: RemoteApiRetrofitClient, peopleItemDao: PeopleItemDao
    ): SwapiApiInterface {
        return SwapiApiImpl(retrofit.getSwapiInterface(), peopleItemDao)
    }


//
    @Provides
    @AppScope
    fun getUserListDataDB(application: Application): LocalDb {
        return LocalDb.getInstance(application.applicationContext)
    }

    @Provides
    @AppScope
    fun getUserListDataDBDao(db : LocalDb): PeopleItemDao {
        return db.peopleItemDao()
    }

    @Provides
    fun application() = application


    @Provides
    @AppScope
    fun getVmFactory(
        mApplication: Application,
        fetchPeopleUseCase: FetchPeopleUseCase,
        searchPeopleUseCase: SearchPeopleUseCase,
        fetchOfflinePeopleListUseCase: FetchOfflinePeopleListUseCase,
        updateOfflinePeopleListUseCase: UpdateOfflinePeopleListUseCase
    ) : VmFactory =
        VmFactory(
            mApplication,
            fetchPeopleUseCase,
            searchPeopleUseCase,
            fetchOfflinePeopleListUseCase,
            updateOfflinePeopleListUseCase
        )


    @Provides
    @AppScope
    fun getViewBinderFactory() : ViewBinderFactory = ViewBinderFactory()



}