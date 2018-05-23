package ru.d_novikov.bluetoothapp.screens.openScreen

import ru.d_novikov.bluetoothapp.mvp.IView

interface OpenScreenView: IView {
    fun setButtonStart()
    fun setButtonStop()
    fun onBluetooth()
    fun scanDevices()
    fun stopScanDevices()

}
