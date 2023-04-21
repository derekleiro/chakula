
package com.example.chakula

import android.app.Application
import android.content.Context
import com.example.chakula.data.AppContainer
import com.example.chakula.data.AppContainerImpl
import java.lang.ref.WeakReference

class ChakulaApplication : Application() {

    // AppContainer instance used by the rest of classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
        contextRef = WeakReference(this.applicationContext)
    }

    companion object {
        private lateinit var contextRef: WeakReference<Context>

        fun getContext(): Context? = contextRef.get()
    }
}
