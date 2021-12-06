package com.littlebig.testapp.dependencyinjection

import com.littlebig.testapp.network.ApiClient
import com.littlebig.testapp.network.ApiClientBuilder
import com.littlebig.testapp.utils.FilteringTools
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun providesApiClient(): ApiClient {
        return ApiClientBuilder.apiClient()
    }

    @Provides
    @Singleton
    fun providesFilteringTools(): FilteringTools {
        return FilteringTools()
    }
}