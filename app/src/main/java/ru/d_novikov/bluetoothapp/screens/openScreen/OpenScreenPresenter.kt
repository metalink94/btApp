package ru.d_novikov.bluetoothapp.screens.openScreen

import android.bluetooth.BluetoothAdapter
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import android.bluetooth.BluetoothDevice
import android.util.Log


class OpenScreenPresenter: ViewPresenter<OpenScreenView>() {

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun onCreate() {
        if (bluetoothAdapter == null) return
        if (!bluetoothAdapter.isEnabled) {
            getView()?.onBluetooth()
        }
        getPairedDevices()

    }

    private fun getPairedDevices() {
        val pairedDevices = bluetoothAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                Log.d("PAIRED", "pairedDevice = " + device.name)
            }
        } else {
            startScan()
        }
    }

    private fun startScan() {
        getView()?.scanDevices()
        bluetoothAdapter.startDiscovery()
    }

    fun onButtonClick() {
        if (!bluetoothAdapter.isEnabled) {
            getView()?.setButtonStart()
        } else {
            getView()?.setButtonStop()
        }
    }

    private fun stopScan() {
        getView()?.stopScanDevices()
        bluetoothAdapter.cancelDiscovery()
    }
}