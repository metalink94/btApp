package ru.d_novikov.bluetoothapp.screens.openScreen

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import android.util.Log


class OpenScreenPresenter: ViewPresenter<OpenScreenView>() {

    lateinit var bluetoothAdapter: BluetoothAdapter
    val BLUETOOTH_DEVICE_NAME = "H-C-2010-06-01"

    fun onCreate(bluetoothAdapter: BluetoothAdapter?) {
        if (bluetoothAdapter == null) return
        this.bluetoothAdapter = bluetoothAdapter
        if (!bluetoothAdapter.isEnabled) {
            getView()?.onBluetooth()
        } else {
            getPairedDevices()
        }
    }

    private fun getPairedDevices() {
        Log.d("getPairedDevices", " SCAN MODE " + bluetoothAdapter.scanMode)
        val pairedDevices = bluetoothAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                onReceive(device)
            }
        } else {
            startScan()
        }
    }

    private fun startScan() {
        getView()?.scanDevices(bluetoothAdapter)
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

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            getPairedDevices()
        }
    }

    fun onReceive(device: BluetoothDevice) {
        if (device.name != null && device.name == BLUETOOTH_DEVICE_NAME) {
            getView()?.connect(device)
            getView()?.stopScanDevices()
        }
    }
}