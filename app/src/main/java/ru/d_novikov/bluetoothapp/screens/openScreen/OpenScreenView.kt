package ru.d_novikov.bluetoothapp.screens.openScreen

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import ru.d_novikov.bluetoothapp.mvp.IView

interface OpenScreenView: IView {
    fun setButtonStart()
    fun setButtonStop()
    fun onBluetooth()
    fun scanDevices(bluetoothAdapter: BluetoothAdapter)
    fun stopScanDevices()
    fun connect(device: BluetoothDevice)
    fun showToast(message: String)
    fun onDataReceived(value: Int, state: Int)
    fun stopListener()
    fun setTimer()
    fun startTimer()
    fun stopTimer()
    fun startGetData()
    fun setPersonState(personState: Int)

}
