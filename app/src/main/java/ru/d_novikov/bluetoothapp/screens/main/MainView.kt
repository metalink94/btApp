package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import ru.d_novikov.bluetoothapp.mvp.IView

interface MainView: IView {
    fun showAlertDialog()
    fun initPager(bluetoothAdapter: BluetoothAdapter)

}
