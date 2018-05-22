package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import android.bluetooth.BluetoothDevice
import android.util.Log


class MainPresenter: ViewPresenter<MainView>() {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun onCreate() {
        if (bluetoothAdapter == null) {
            getView()?.showAlertDialog()
        }
        getView()?.initPager()
    }
}