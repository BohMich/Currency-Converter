package com.littlebig.testapp.dependencyinjection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.littlebig.testapp.viewmodels.DetailedViewViewModel
import com.littlebig.testapp.viewmodels.MainPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainPageViewModel::class)
    internal abstract fun bindMainPageViewModel(mainPageViewModel: MainPageViewModel)
            : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailedViewViewModel::class)
    internal abstract fun detailedViewFeedViewModel(detailedViewViewModel: DetailedViewViewModel)
            : ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory)
              : ViewModelProvider.Factory
}