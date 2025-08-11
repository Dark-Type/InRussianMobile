package com.example.inrussian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.inrussian.root.root.RootComponentUi
import com.example.inrussian.di.SharedDI

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SharedDI.start()

        val componentContext = defaultComponentContext()
        val rootComponent = SharedDI.createRoot(componentContext)

        setContent {
            RootComponentUi(rootComponent)
        }
    }
}