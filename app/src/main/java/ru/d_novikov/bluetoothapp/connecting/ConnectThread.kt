package ru.d_novikov.bluetoothapp.connecting

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log

import java.io.IOException
import java.util.UUID

class ConnectThread : Thread() {

    private val bTSocket: BluetoothSocket? = null

    fun connect(bTDevice: BluetoothDevice, mUUID: UUID): Boolean {
        var temp: BluetoothSocket? = null
        try {
            temp = bTDevice.createRfcommSocketToServiceRecord(mUUID)
        } catch (e: IOException) {
            Log.d("javaClass", "Could not create RFCOMM socket:" + e.toString())
            return false
        }

        try {
            bTSocket?.connect()
        } catch (e: IOException) {
            Log.d("javaClass", "Could not connect: " + e.toString())
            try {
                bTSocket?.close()
            } catch (close: IOException) {
                Log.d("javaClass", "Could not close connection:" + e.toString())
                return false
            }

        }

        return true
    }

    fun cancel(): Boolean {
        try {
            bTSocket?.close()
        } catch (e: IOException) {
            Log.d("javaClass", "Could not close connection:" + e.toString())
            return false
        }

        return true
    }

}
