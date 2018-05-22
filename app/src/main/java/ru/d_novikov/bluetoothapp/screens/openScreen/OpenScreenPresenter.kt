package ru.d_novikov.bluetoothapp.screens.openScreen

import android.bluetooth.BluetoothAdapter
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter

class OpenScreenPresenter: ViewPresenter<OpenScreenView>() {

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun onCreate() {
        if (!bluetoothAdapter.isEnabled) {
            getView()?.onBluetooth()
        }
    }

    fun onButtonClick() {
        if (!bluetoothAdapter.isEnabled) {
            getView()?.setButtonStart()
        } else {
            getView()?.setButtonStop()
        }
    }
}