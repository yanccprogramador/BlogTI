package br.com.yanccprogramador.blogti.br.com.yanccprogramador.blogti.multidex

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication

class MyApplication : MultiDexApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
