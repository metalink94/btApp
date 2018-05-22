package ru.d_novikov.bluetoothapp.screens.main

import ru.d_novikov.bluetoothapp.mvp.IView

interface MainView: IView {
    fun onBluetooth()
    fun searchVisibleDevices()

}
