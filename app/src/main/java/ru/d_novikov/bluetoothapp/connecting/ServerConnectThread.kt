package ru.d_novikov.bluetoothapp.connecting

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class ServerConnectThread : Thread() {
    private var bTSocket: BluetoothSocket? = null

    fun acceptConnect(bTAdapter: BluetoothAdapter, mUUID: UUID) {
        var temp: BluetoothServerSocket? = null
        try {
            temp = bTAdapter.listenUsingRfcommWithServiceRecord("Service_Name", mUUID)
        } catch (e: IOException) {
            Log.d("javaClass", "Could not get a BluetoothServerSocket:" + e.toString())
        }

        while (true) {
            try {
                bTSocket = temp?.accept()
            } catch (e: IOException) {
                Log.d("javaClass", "Could not accept an incoming connection.")
                break
            }

            if (bTSocket != null) {
                try {
                    temp?.close()
                } catch (e: IOException) {
                    Log.d("javaClass", "Could not close ServerSocket:" + e.toString())
                }

                break
            }
        }
    }

    fun closeConnect() {
        try {
            bTSocket?.close()
        } catch (e: IOException) {
            Log.d("javaClass", "Could not close connection:" + e.toString())
        }

    }
}