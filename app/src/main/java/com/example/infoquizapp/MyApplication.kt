package com.example.infoquizapp

import android.app.Application
import com.example.infoquizapp.di.appModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

class MyApplication : Application(), DIAware {
    override val di = DI.lazy {
        import(androidXModule(this@MyApplication))
        import(appModule)
    }
}