package com.littlebig.testapp.dependencyinjection

import com.littlebig.testapp.viewmodels.DetailedViewViewModel
import com.littlebig.testapp.viewmodels.MainPageViewModel
import com.littlebig.testapp.views.MainActivity
import com.littlebig.testapp.views.RatesListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, MainModule::class, ViewModelModule::class))
interface MainComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainPageViewModel: MainPageViewModel)
    fun inject(ratesFragment: RatesListFragment)
    fun inject(detailedViewViewModel: DetailedViewViewModel)
}