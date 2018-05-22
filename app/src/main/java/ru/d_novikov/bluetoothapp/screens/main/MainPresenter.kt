package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import android.bluetooth.BluetoothDevice
import android.util.Log


class MainPresenter: ViewPresenter<MainView>() {

    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var pairedDevices: Set<BluetoothDevice> = mutableSetOf()

    fun onCreate() {
        if (bluetoothAdapter == null) {
            getView()?.showAlertDialog()
        }
        getView()?.initPager(bluetoothAdapter)
    }
}