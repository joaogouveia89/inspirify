package io.github.joaogouveia89.inspirify

import android.app.Application
import io.github.joaogouveia89.inspirify.di.DaggerInspirifyComponent
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.di.modules.DataModule
import io.github.joaogouveia89.inspirify.di.modules.RepositoriesModule
import io.github.joaogouveia89.inspirify.di.modules.UseCasesModule

class InspirifyApplication: Application() {
    val inspirifyComponent: InspirifyComponent by lazy {
        DaggerInspirifyComponent.builder().apply {
            dataModule(DataModule(context = baseContext))
            repositoriesModule(RepositoriesModule())
            useCasesModule(UseCasesModule())
        }.build()
    }
}