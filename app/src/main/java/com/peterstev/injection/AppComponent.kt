package com.peterstev.injection

import android.app.Activity
import androidx.fragment.app.Fragment
import com.peterstev.data.injection.ApiModule
import com.peterstev.data.injection.DataModule
import com.peterstev.database.injection.DatabaseModule
import com.peterstev.database.injection.DatabaseRepoModule
import com.peterstev.domain.injection.DomainModule
import com.peterstev.fragment.DetailFragment
import com.peterstev.fragment.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DomainModule::class,
        DatabaseModule::class,
        DatabaseRepoModule::class,
        DataModule::class,
        ApiModule::class,
        AppModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity): AppComponent
    }

    fun inject(mainFragment: MainFragment)
    fun inject(detailFragment: DetailFragment)

    companion object {
        fun create(fragment: Fragment): AppComponent {
            return DaggerAppComponent.factory().create(fragment.requireActivity())
        }
    }
}
