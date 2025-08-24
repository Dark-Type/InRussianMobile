package com.example.inrussian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.inrussian.root.root.RootComponentUi
import com.example.inrussian.di.SharedDI
import com.example.inrussian.platformInterfaces.UserConfigurationStorage
import com.example.inrussian.platformInterfaces.UserConfigurationStorageImpl
import org.koin.dsl.module

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val platformModule = module {
            single<UserConfigurationStorage> { UserConfigurationStorageImpl(this@MainActivity) }
        }

        SharedDI.start(platformModule)

        val componentContext = defaultComponentContext()
        val rootComponent = SharedDI.createRoot(componentContext)

        setContent {
            RootComponentUi(rootComponent)
        }
    }
}