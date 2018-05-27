package ru.d_novikov.bluetoothapp

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration



class BluetoothApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("bluetooth.realm").build()
        Realm.setDefaultConfiguration(config)
    }
}